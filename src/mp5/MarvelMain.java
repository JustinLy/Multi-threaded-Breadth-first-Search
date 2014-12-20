package mp5;

public class MarvelMain {

	/** The purpose of this class is to show that the functionality for building the large provided
	 * labeled_edges.tsv data set works, and that searches can be conducted on this set.
	 * To test your own searches, use  MP5.java which can be found in the same package.
	 */
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try{
		SimpleGraph marvelGraph = MarvelGenerator.generateGraph("labeled_edges.tsv");
		System.out.println( "Marvel Graph built successfully!\n");
		
		//Try a few searches to see if it works: To test your own searches, use MP5.java 
		marvelGraph.breadthFirstSearch("FROST, CARMILLA", "24-HOUR MAN/EMMANUEL", 1);
		marvelGraph.breadthFirstSearch("FROST, CARMILLA", "WOLVERINE/LOGAN ", 1);
		marvelGraph.breadthFirstSearch("FROST, CARMILLA", "WOLVERINE/LOGAN ", 3);

		marvelGraph.breadthFirstSearch("OLD SKULL", "MACTAGGERT, MOIRA KI", 1);
		marvelGraph.breadthFirstSearch("OLD SKULL", "MACTAGGERT, MOIRA KI", 2);

		marvelGraph.breadthFirstSearch("VOICE", "KENT, CLARK", 1);
		marvelGraph.breadthFirstSearch("VOICE", "KENT, CLARK", 3);
		
		}
		catch(NoPathException e ) //No need to do anything, since NoPathException already prints
		{
			
		}
		catch(Exception e ) {
			System.out.println( "Error reading the file" );
		}
	}

}
