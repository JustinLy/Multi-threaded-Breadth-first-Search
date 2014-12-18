package mp5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MarvelGraph {

	/*REP INVARIANT: 
	- Cannot contain duplicate vertices or duplicate edges. 
	- A vertex cannot have an edge with itself
	- Vertices and edges cannot be null
	- Each vertex must have an edge in edgeMap (Map< Set{n1, n2}, Edge ) with each of its
		neighbouring vertices in adjacencyList ( Map< String, Set{neighbours}  )>
	*/
	
	/*Abstraction Function
	 - Strings in adjacencyList ( Map< String, Set{neighbours} ) map to all vertices and
	 	Set{neighbours} maps to the neighbouring vertices of a given vertex
	 -  "Edge" objects which contain a set of Strings of the comics both vertices appeared in
	 	are mapped to edges between vertices on the graph. These "edges" are found in 
	 	key-value pairs in edgeMap 
	 	Together these data structures make up the MarvelGraph ADT  
	 */
	
	//adjacency list mapping all vertices to their neighbours
	private final Map< String, Set<String> > adjacencyList = new HashMap< String, Set<String> >();
	//maps a vertex pair to all the edges connecting them 
	private final Map< Set<String>, Set<Edge> > edgeMap = new HashMap< Set<String>, Set<Edge> >();
	
	/**
	 * Creates an empty MarvelGraph
	 * @return a MarvelGraph with no vertices or edges
	 */
	public MarvelGraph() 
	{
		
	}
	
	/**
	 * Add a new vertex to the graph and maps it to an empty list of neighbours.
	 * If the vertex already exists in the graph
	 * then this method will return false. Otherwise this method will add the
	 * vertex to the graph and return true.
	 * 
	 * @param vertex
	 *            the vertex to add to the graph. Requires that vertex != null.
	 * @return true if the vertex was successfully added and false otherwise.
	 * @modifies this by adding the vertex to the graph if the vertex did not
	 *           exist in the graph.
	 */
	public boolean addVertex(String vertex) 
	{
		if (vertex == null || adjacencyList.containsKey(vertex)) 
		{
			return false;
		}
		else 
		{
			adjacencyList.put(vertex, new HashSet<String>());
			return true;
		}
	}
	
	/**
	 * Add a new edge between 2 vertices if the edge doesn't already exist and is not null. Duplicates will
	 * not be added. Returns true if successfully added or edge already existed, false otherwise. If either vertex 
	 * doesn't exist in the graph, they will be added to the graph and the edge will be created.
	 * @param String vertex1 - first vertex 
	 * @param String vertex2 - second vertex
	 * @param String edgeName - label of the edge connecting vertex1 and vertex2
	 * @return true if edge successfully added or already exists, false when either vertex or edgeName is null
	 */
	public boolean addEdge( String vertex1, String vertex2, String edgeName )
	{
		Set<String> newNeighboursSet; 
		Set<Edge> newEdgesSet;

		if (vertex1 == null || vertex2 == null || edgeName == null) 
			return false;
		else
		{
			//Create the new edge
			Set<String> vertexPair = new HashSet<String>();
			vertexPair.add(vertex1);
			vertexPair.add(vertex2);
			Edge newEdge = new Edge( vertexPair, edgeName );
			
			for( String vertex : vertexPair ) //Update adjacencyList  for both vertices
			{
				newNeighboursSet = adjacencyList.get(vertex);
				if( newNeighboursSet != null ) //Check if vertex exists on the graph
				{
					newNeighboursSet.add(newEdge.getPartner(vertex)); //Add vertex 2 to vertex1's set of neighbours
				}
				else //vertex does not exist on graph. Add it to the graph 
				{
					newNeighboursSet = new HashSet<String>();
					newNeighboursSet.add( newEdge.getPartner(vertex) );
				}
				adjacencyList.put(vertex, newNeighboursSet); //update adjacencyList for current "vertex"
			}
			
			//Add newEdge to set of edges for vertexPair on edgeMap if it exists
			newEdgesSet = edgeMap.get(vertexPair);
			if( newEdgesSet != null )
				newEdgesSet.add(newEdge);
			else //this vertex pair has no existing edges. Make new set of edges and add newEdge
			{
				newEdgesSet = new HashSet<Edge>();
				newEdgesSet.add(newEdge);
			}	
			edgeMap.put(vertexPair, newEdgesSet); //Update edgeMap
			return true;
		}
	}
	
	/**
	 * Conducts a breadth first search for the shortest and alphabetically lowest path between 2 vertices, 
	 * using one or more threads to conduct the search
	 * @param startVertex - starting vertex for the path
	 * @param endVertex - ending vertex for the path
	 * @param threads - number of threads to use when searching
	 * @return the list of vertices on the shortest path from "startVertex" to "endVertex"
	 * 
	 * Thread Safety Argument:
	 * 1. pathMap, currentVertices, and nextVertices are thread-safe data types:
	 * 	- All "search threads" will remove vertices from currentVertices and add vertices
	 * 	 	to nextVertices. The methods for these operations are atomic. 
	 * 2. Since all threads can only work on the same "level" of vertices from startVertex
	 * 		there is no possibility of a thread finding endVertex first on a further level and
	 * 		causing a longer path to be returned
	 * 3. Since threads can only work on the same "level" of vertices and no paths
	 * 4. Within Searcher's run() method:
	 * 		- Checking if empty and polling is done together atomically in queues
	 * 		- All accesses to a "neighbour" vertex and its current path is synchronized and made atomic
	 */
	List<String> breadthFirstSearch( String startVertex, String endVertex, int threads )
	{
		//Maps each visited vertex to shortest path to get to it from startVertex. These paths are optimized
		Map<String, List<String>> completePaths = new ConcurrentHashMap<String, List<String>>();
		//Temporary path map for the threads to work on. Final results are transferred to pathMap at end of each level of search.
		Map<String, List<String>> tempPaths = new ConcurrentHashMap<String, List<String>>();
		
		//add startVertex to completePaths 
		ArrayList<String> startPath = new ArrayList<String>();
		startPath.add(startVertex);
		completePaths.put(startVertex, startPath);
		
		if( endVertex.equals(startVertex) ) //start and end are same just return startPath
		{
			System.out.println( "Path from " + startVertex + "to" + endVertex + "is" + startVertex);
			return startPath; 
		}
		
		//Create the Search Task. Will be run by all threads sharing the same parameters
		Search searchTask = new Search(completePaths, tempPaths, startVertex );
		List<Callable<Void> > taskList = new ArrayList<Callable<Void> >();
		for( int index = 0; index < threads; index++ ) //Add Search Task "threads" times to taskList
		{
			taskList.add(searchTask);
		}
	
		final ExecutorService executor = Executors.newFixedThreadPool(threads); //pool of "threads" size
		//TODO: While( nextQ.notempty) submit n tasks to threadpool
		do
		{
			//Execute the search task using the specified number of threads. 
			try
			{
			executor.invokeAll(taskList); //This thread is blocked till all search threads are done
			}
			catch(Exception e)
			{
				System.out.println( "Error when invoking search tasks\n");
			}
			
			List<String> finalPath = tempPaths.get(endVertex);
			if( finalPath != null ) //Optimal path to endVertex found
			{
				//printPath( finalPath ); //TODO: implement this
				return finalPath;
			}
			else
			{
				completePaths.putAll(tempPaths); //Add finished paths to pathMap
				tempPaths.clear(); //Clear tempMap for next round of searching
			}
		}
		while( searchTask.updateSearch() ); //Updates search task and continues if there is more work to be done.
		
		throw new NoPathException(startVertex, endVertex ); //No path between the vertices found
	}
	
	private class Search implements Callable<Void> 
	{
		/**This is a helper class to find the optimal path to all neighbours of a "level" of vertices
		 * (ie vertices in currentVertices are a "level" as they are all equidistant from the start vertex) 
		 * and add these neighbours to the next level of vertices (nextVertices) in preparation of the next search
		 */
		
		//Maps each visited vertex to shortest and alphabetically correct path to get to it from startVertex
		private Map<String, List<String>> completePaths;
		
		//temporary pathMap to hold tentative paths to vertices that may change due to alphabetical order
		private Map<String, List<String>> tempPaths;
		
		//currentVertices for current "level" of vertices the threads are working on
		//nextVertices for neighbours of the vertices in currentQueue 
		private Queue<String> currentVertices = new ConcurrentLinkedQueue<String>();
		private Queue<String> nextVertices = new ConcurrentLinkedQueue<String>();
		
		/**
		 * Creates a search task with the given current queue, next queue, temporary path map and permanent pathMap.
		 * @param completePaths - Map containing shortest and alphabetically lowest path for visited vertices
		 * @param tempPaths - Empty map for temporarily saving paths to vertices being visited during the search.
		 * @param root - vertex to start the search at
		 */
		private Search( Map<String, List<String>> completePaths, Map<String, List<String>> tempPaths, String root)
		{
			this.completePaths = completePaths;
			this.tempPaths = tempPaths;
			currentVertices.offer(root); //Add startVertex to the current queue to start the search.
		}
		
		/**
		 * Updates the work queues for next round of searcing by switching currentQueue with nextQueue and
		 * clearing nextQueue. Also returns true or false indicating if there are any vertices left to visit in search.
		 * @modifies currentVertices - by assigning it to nextVertices. 
		 * 			 nextVertices -    by making it empty
		 * @return true if currentQueue is not empty, false otherwise
		 */
		private boolean updateSearch( )
		{
			Queue<String> tempQueue = currentVertices;
			currentVertices = nextVertices; 
			nextVertices = tempQueue; 
			nextVertices.clear();	
			return currentVertices.isEmpty();
		}
		
		/**
		 * Populates tempMap with the shortest and alphabetically lowest paths to the neighbours of
		 * the vertices in currentVertices, and also adds these neighbours to nextVertices. 
		 * @return null - Return is irrelevant for the search
		 * @modifies completePaths, tempPaths, currentVertices, and nextVertices 
		 */
		public Void call()
		{
			String currentVertex;
			
			//Process vertices in currentVertices till it's empty. Empty-Checks and dequeues are atomic
			while( ( currentVertex = currentVertices.poll() ) != null )
			{
				//Process each neighbour of currentVertex 
				for( String neighbour : adjacencyList.get(currentVertex))
				{
					if (completePaths.get(neighbour) == null ) //Haven't found optimal path for "neighbour" yet
					{
						synchronized( neighbour ) //Lock so accesses to "neighbour" and its path are atomic
						{
							List<String> currentPath = tempPaths.get(neighbour);
							List<String> newPath;
							
							//"neighbour" has not been visited. Create path for it
							if( currentPath == null ) 
							{
								newPath = new ArrayList<String>( completePaths.get(currentVertex) );
								newPath.add( neighbour ); 
								tempPaths.put(neighbour, newPath); //Update optimal path for "neighbour"
								nextVertices.offer( neighbour ); //Add to nextVertices queue
							}
							else //"neighbour" has already been visited 
							{	
								//the penultimate vertex on currentPath . Use to compare alphabetical order
								String lastVertexOnPath = currentPath.get( currentPath.size()-2 );
								
								//Update to this new path if new path is alphabetically lower
								if( currentVertex.compareToIgnoreCase( lastVertexOnPath ) < 0 )
								{
									newPath = new ArrayList<String>( completePaths.get(currentVertex) );
									newPath.add( neighbour ); 
									tempPaths.put(neighbour, newPath); //Update optimal path for "neighbour"
								}
							}	
						}
					}
				}
			}
			return null;
		}
	}
	
	
}
