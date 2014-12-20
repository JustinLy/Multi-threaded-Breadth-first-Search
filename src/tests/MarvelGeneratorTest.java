package tests;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import mp5.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class MarvelGeneratorTest {
	SimpleGraph dcGraph = new SimpleGraph();
	
/** Uses a small DC dataset "DC.txt" (because DC > Marvel :) )
 * Test whether MarvelGenerator correctly builds a graph from a file "DC.txt"
 * Expected: Graph built successfully, and manually built graph equal to one
 * built with MarvelGenerator.
 */
	@Test
	public void testMarvelGenerator() {
		//Comic sets to store characters from each comic
		Set<String> justiceLeague = new HashSet<String>();
		Set<String> smallville = new HashSet<String>();
		Set<String> darkKnight = new HashSet<String>();
		Set<String> theFlash = new HashSet<String>();
		Set<String> arrow = new HashSet<String>();
		Set<String> legionOfDoom = new HashSet<String>();
		Set<String> marvelVsDC = new HashSet<String>();
		Set<String> marvelVsCapcom = new HashSet<String>();
		Set<String> darkness = new HashSet<String>();
		Set<String> theBible = new HashSet<String>();
		
		//justice league
		justiceLeague.add("Batman");
		justiceLeague.add("Green Arrow");
		justiceLeague.add("Flash");
		justiceLeague.add("Random Pedestrian" );
		buildComicGroup( justiceLeague, "Justice League" );
		
		//smallville
		smallville.add("Green Arrow" );
		smallville.add("Lex Luthor" );
		smallville.add("Chloe Sullivan" );
		buildComicGroup( smallville, "Smallville" );
		
		//dark knight
		darkKnight.add("Batman");
		darkKnight.add("Joker");
		darkKnight.add("Ra's al Ghul");
		darkKnight.add("Scarecrow");
		buildComicGroup( darkKnight, "Dark Knight" );
		
		//the flash
		theFlash.add("Flash");
		theFlash.add("Deathstroke");
		buildComicGroup( theFlash, "The Flash" );
		
		//arrow
		arrow.add("Green Arrow");
		arrow.add("Deathstroke");
		buildComicGroup( arrow, "Arrow" );
		
		//legion of doom
		legionOfDoom.add("Lex Luthor");
		legionOfDoom.add("Joker");
		legionOfDoom.add("Deathstroke");
		buildComicGroup( legionOfDoom, "Legion of Doom" );
		
		//marvel vs dc
		marvelVsDC.add("Captain America");
		marvelVsDC.add("Batman");
		buildComicGroup( marvelVsDC, "Marvel vs DC" );
		
		//marvel vs capcom
		marvelVsCapcom.add("Captain America");
		marvelVsCapcom.add("Zero");
		buildComicGroup( marvelVsCapcom, "Marvel vs Capcom" );
		
		//darkness
		darkness.add("Lex Luthor");
		darkness.add("Deathstroke");
		darkness.add("Darkseid");
		darkness.add("Ra's al Ghul");
		darkness.add("Scarecrow");
		buildComicGroup( darkness, "Darkness" );
		
		//bible
		theBible.add("Daler Mehndi" );
		buildComicGroup( theBible, "The Bible" );
		
		SimpleGraph testGraph = null;
		try{
		 testGraph = MarvelGenerator.generateGraph("dc.tsv");
		}
		catch(Exception e )
		{
			System.out.println("Error reading file" );
			fail();
		}
			assertEquals(true,  testGraph.equals(dcGraph));
	}
	
	/**Helper method for testing MarvelGenerator. Creates edges between characters
	 * in the given characterGroup for the given comic and adds these edges and characters
	 * to "dcGraph"
	 * @param characterGroup - Group of characters in a comic 
	 * @param comic			- The comic "characterGroup" belongs to
	 */
	public void buildComicGroup( Set<String> characterGroup, String comic )
	{
		if( characterGroup.size() == 1 ) //Special case: only 1 character in comic
			dcGraph.addVertex( (String) characterGroup.iterator().next() );
		else
		{
			//Create edges between all characters in this characterGroup, add to graph
			Iterator<String> outter = characterGroup.iterator();
			while( outter.hasNext() ) 
			{
				String currentChar = outter.next();
				System.out.println( "Building Graph: " + currentChar + " in " + comic);
				outter.remove(); //Remove currentChar from characterGroup
				
				Iterator<String> inner = characterGroup.iterator(); //one less than outter now
				while( inner.hasNext() ) //Creates edges between currentChar and others in group
					dcGraph.addEdge(currentChar, (String) inner.next(), comic);	
			} 
		}
		
	}
}
