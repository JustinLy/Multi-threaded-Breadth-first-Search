package mp5;

import java.io.FileNotFoundException;

public class MP5 {

	/**This class takes arguments from the command line to build a graph from the given data
	 * and conduct a breadth-first search on it. 
	 * @param args - Of the form: <filename> <vertex1> <vertex2> <numThreads>
	 * 		-filename - the file you want to create the graph from
	 * 		-vertex1  - the start vertex of the search
	 * 		-vertex2  - The end vertex of the search
	 * 		-numThreads - The number of threads to use in the search
	 */
	public static void main(String[] args) {
		SimpleGraph graph = null;
		if( args.length != 4)
		{
			throw new IllegalArgumentException( "Error: Must supply 4 arguments\n");
		}
		else
		{	
			String filename = args[0];
			String startVertex = args[1];
			String endVertex = args[2];
			int threads = Integer.parseInt(args[3]);
			
			try
			{
				 graph = MarvelGenerator.generateGraph(filename);
			} 
			catch (FileNotFoundException e) 
			{
				System.out.println( "Error: Reading file\n");
			}
			try
			{
			graph.breadthFirstSearch(startVertex, endVertex, threads);
			}
			catch(NoPathException e)
			{
				//No need to do anything, NoPathException prints out an error message.
			}
		}
	}

}
