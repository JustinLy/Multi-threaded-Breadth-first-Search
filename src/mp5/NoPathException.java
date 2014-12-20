package mp5;

public class NoPathException extends RuntimeException {

	private static final long serialVersionUID = 5773936035045823922L;
	
	public NoPathException(String start, String end )
	{
		System.out.println( "Error: No path could be found between " + start + " and " + end + "\n");
	}

}
