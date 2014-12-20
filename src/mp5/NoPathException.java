package mp5;

public class NoPathException extends RuntimeException {

	private static final long serialVersionUID = 5773936035045823922L;
	
	/**
	 * Creates a NoPathException when no path can be found between the
	 * "start" and "end" vertices.
	 * @param start - The start vertex you wanted to find a path from
	 * @param end	- The end vertex you wanted to find a path to
	 */
	public NoPathException(String start, String end )
	{
		System.out.println( "No path could be found between " + start + " and " + end + "\n");
	}

}
