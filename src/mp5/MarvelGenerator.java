package mp5;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public final class MarvelGenerator {
/**
 * This class creates a SimpleGraph using a datafile with the same format as
 * the Marvel Dataset labeled_edges.tsv.
 */
	/**Instances of this class should not be made **/
	private MarvelGenerator()
	{}
	
	/**
	 * Creates a SimpleGraph using a tab-separated value file of 2 fields (like the Marvel Dataset)
	 * @param data - file with the data you want to create a SimpleGraph from
	 * @requires data is a tsv file with exactly 2 fields and same format as Marvel Dataset
	 * @return a SimpleGraph of the data in "data"
	 * @throws FileNotFoundException
	 *  
	 * Thread Safety Arguments:
	 * 	1. The shared datatype, "tasks" is a thread-safe type accesses to it are atomic.
	 * 	2. The producer thread and consumer thread do different things with "tasks" that don't
	 * 		affect each other.
	 * 	3. Message-passing: A poison pill is placed in "tasks" by the producer when file is read
	 * 		so consumer knows when to stop so no deadlock occurs.
	 */
	public static SimpleGraph generateGraph( String data ) throws FileNotFoundException
	{
		SimpleGraph graph = new SimpleGraph();
		final String[] POISON_PILL = new String[1];
		final BlockingQueue<String[]> tasks = new LinkedBlockingQueue<String[]>();
		String[] nextEntry;
		String currentComic; 
		Set<String> characterGroup = new HashSet<String>(); //Characters to create edges between
		final BufferedReader producer = new BufferedReader( new FileReader( new File(data)) );
		
		//Producer thread reads and tokenizes data from file into "tasks" queue
		new Thread() 
		{
			public void run()
			{
				String next;
				try 
				{	
					int count = 0;
					while( (next = producer.readLine() ) != null )
					{
						System.out.println( "Building Graph: " + ++count + " " + next);
						String[] nextData = next.split("\\t"); //Split char and comic
						tasks.put( nextData ); //Place into task queue for consumer
					}
					tasks.put(POISON_PILL); //End of file, place poison pill to stop consumer 
				}
				catch(Exception e )
				{
					System.out.println( "Error building graph: File reading" );
				}
				
			}	
		}.start();
		
		//Consumer runs in this thread. Takes tokens from queue and builds graph
		try
		{
			//Get first task and initialize currentComic or end if empty data
			nextEntry = tasks.take(); 
			if( nextEntry != POISON_PILL )
				currentComic = nextEntry[1];
			else
				return graph;
			
			do
			{	//if not the last entry and character from same comic as previous entry
				if( nextEntry != POISON_PILL && nextEntry[1].equals(currentComic) )
					characterGroup.add(nextEntry[0]); 
				else 
				{	
					if( characterGroup.size() == 1 ) //Special case: only 1 character in comic
					{
						graph.addVertex( (String) characterGroup.iterator().next() );
						characterGroup.clear();
					}
					else
					{
						//Create edges between all characters in this characterGroup, add to graph
						Iterator<String> outter = characterGroup.iterator();
						while( outter.hasNext() ) 
						{
							String currentChar = outter.next();		
							outter.remove(); //Remove currentChar from characterGroup
							
							Iterator<String> inner = characterGroup.iterator(); //one less than outter now
							while( inner.hasNext() ) //Creates edges between currentChar and others in group
								graph.addEdge(currentChar, (String) inner.next(), currentComic);	
						}
					}
					
					if( nextEntry != POISON_PILL) //If nextEntry isn't the poison pill, start new group
					{
						currentComic = nextEntry[1]; //Change currentComic to next comic in data
						characterGroup.add( nextEntry[0] ); //Add character from next comic to now-empty set 
					}
					else
						break; //Stop processing. We've built the last edges and there's no more tasks	
				}
				nextEntry = tasks.take(); //Get next task
			}
			while(  characterGroup.size() !=0 ); //Keep processing till producer ends
			producer.close(); //Close the producer thread reading from file
		}
		catch(Exception e)
		{
			System.out.println( "Error building graph: Processing\n");
		}
		return graph;
	}
}
