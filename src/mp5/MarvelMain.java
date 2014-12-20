package mp5;

public class MarvelMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try{
		SimpleGraph marvelGraph = MarvelGenerator.generateGraph("labeled_edges.tsv");
		System.out.println( "Marvel Graph built successfully!\n");
		
		//Try a few searches to see if it works:
marvelGraph.breadthFirstSearch("\"FROST, CARMILLA\"", "\"24-HOUR MAN/EMMANUEL\"", 1);
marvelGraph.breadthFirstSearch("\"FROST, CARMILLA\"", "\"WOLVERINE/LOGAN \"", 1);
marvelGraph.breadthFirstSearch("\"FROST, CARMILLA\"", "\"WOLVERINE/LOGAN \"", 3);

marvelGraph.breadthFirstSearch("\"OLD SKULL\"", "\"MACTAGGERT, MOIRA KI\"", 1);
marvelGraph.breadthFirstSearch("\"OLD SKULL\"", "\"MACTAGGERT, MOIRA KI\"", 2);

marvelGraph.breadthFirstSearch("\"VOICE\"", "\"KENT, CLARK\"", 1);
		
		}
		catch(NoPathException e )
		{
			
		}
		catch(Exception e ) {
			System.out.println( "Error reading the file" );
		}
	}

}
