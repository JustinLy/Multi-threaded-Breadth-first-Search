package tests;


import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;
import  mp5.*;
public class GraphBuildingTests {

	/**Simple test to add vertex to graph and see if it worked
	 * Expected: Vertex added successfully with empty set of neighbours **/
	@Test
	public void addVertex() {
		SimpleGraph graph = new SimpleGraph();
		graph.addVertex( "Daler Mehndi" );
		try{
		assertEquals(  new HashSet<String>(), graph.getNeighbours("Daler Mehndi"));}
		catch(Exception e)
		{
			fail(); //If getNeighbours throws exception then it didn't work.
		}
	}
	
	/**Test if adding duplicate vertices is prevented
	 * Expected: Duplicate vertex not ended
	 */
	@Test
	public void addVertexDuplicate() {
		SimpleGraph graph = new SimpleGraph();
		graph.addVertex( "Daler Mehndi" );
		assertEquals( false, graph.addVertex("Daler Mehndi"));
	}
	
	/**Simple test to add edge between 2 EXISTING vertices in the graph
	 * Expected: Edges are added successfully.
	 */
	@Test
	public void addEdgeExisting() {
		SimpleGraph graph = new SimpleGraph();
		String v1 = "Daler Mehndi";
		String v2 = "Relad Idnhem";
		Set<String> pair = new HashSet<String>();
		pair.add(v1);
		pair.add(v2);
		Edge e1 = new Edge( pair, "Heaven vs Hell" );
		
		graph.addVertex(v1 );
		graph.addVertex( v2 );
		graph.addEdge(v1, v2, "Heaven vs Hell" );
		assertEquals(true,  graph.getEdges(pair).contains(e1));	
	}
	
	/**Test to add edge between 2 vertices that don't exist on the graph
	 * Expected: Edge added successfuly AND 2 vertices added to adjacencyList
	 */
	@Test
	public void addEdgeNonexisting() {
		SimpleGraph graph = new SimpleGraph();
		String v1 = "Daler Mehndi";
		String v2 = "Relad Idnhem";
		Set<String> pair = new HashSet<String>();
		pair.add(v1);
		pair.add(v2);
		Edge e1 = new Edge( pair, "Heaven vs Hell" );
		
		graph.addEdge(v1, v2, "Heaven vs Hell" );
		boolean edgeAdded = graph.getEdges(pair).contains(e1);
		boolean adjacency1 = graph.getNeighbours(v1).contains(v2);
		boolean adjacency2 = graph.getNeighbours(v2).contains(v1);
		assertEquals( true, edgeAdded && adjacency1 && adjacency2);
	}
	
	/**Test to add a DUPLICATE EDGE
	 * Expected: The duplicate edge should not be added to graph
	 */
	@Test
	public void addEdgeDuplicate() {
		SimpleGraph graph = new SimpleGraph();
		Set<String> pair = new HashSet<String>();
		pair.add(new String("Daler"));
		pair.add(new String("Relad"));
		graph.addEdge( new String("Daler"), new String("Relad"), new String( "Revelation"));
		graph.addEdge( new String("Daler"), new String("Relad"), new String( "Revelation"));
		assertEquals( 1, graph.getEdges(pair).size());
	}
	

	
}
