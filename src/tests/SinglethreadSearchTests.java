package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import mp5.MarvelGenerator;
import mp5.NoPathException;
import mp5.SimpleGraph;

import org.junit.BeforeClass;
import org.junit.Test;

public class SinglethreadSearchTests {
static SimpleGraph dcGraph;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		dcGraph = MarvelGenerator.generateGraph("dc.tsv");
	}

	/**TEST LONG VS SHORT PATH: Batman --> Chloe Sullivan
	 * There's a long and short path:
	 * 	1) Batman -> Joker -> Lex Luthor -> Chloe Sullivan
	 * 	2) Batman -> Green Arrow -> Chloe Sullivan
	 * Expected: Shorter path 2) taken
	 */
	@Test
	public void longPathVsShort() {
		List<String> correct = new ArrayList<String>();
		correct.add("\"Batman\"");
		correct.add("\"Green Arrow\"");
		correct.add("\"Chloe Sullivan\"");
		assertEquals( correct, dcGraph.breadthFirstSearch("\"Batman\"", "\"Chloe Sullivan\"", 1));
	}
	
	/**TEST SIMPLE ALPHABETICAL ORDER of Vertices (at the final level) Captain America --> Deathstroke
	 * At the last level there is a choice between 2 paths of same length, but different
	 * alphabetical order. (There will be a test later to test alphabetical order of EDGES)
	 * 1) Captain America -> Batman -> Flash -> Deathstroke
	 * 2) Captain America -> Batman -> Green Arrow -> Deathstroke
	 * Expected: 1) Taken because Flash is alphabetically before Green Arrow
	 */
	@Test
	public void alphabetOrderVertices() {
		List<String> correct = new ArrayList<String>();
		correct.add("\"Captain America\"");
		correct.add("\"Batman\"");
		correct.add("\"Flash\"");
		correct.add("\"Deathstroke\"");
		assertEquals( correct, dcGraph.breadthFirstSearch("\"Captain America\"", "\"Deathstroke\"", 1));
	}

	/**TEST COMPLICATED PATH (many possible paths, lot's of alphabetical order computations) 
	 * Random Pedestrian --> Darkseid
	 * Expected: Random Pedestrian -> Batman -> Ra's al Ghul -> Darkseid
	 */
	@Test
	public void complicatedPath() {
		List<String> correct = new ArrayList<String>();
		correct.add("\"Random Pedestrian\"");
		correct.add("\"Batman\"");
		correct.add("\"Ra's al Ghul\"");
		correct.add("\"Darkseid\"");
		assertEquals( correct, dcGraph.breadthFirstSearch("\"Random Pedestrian\"", "\"Darkseid\"", 1));
	}
	
	/**Test No path. Batman -> Daler Mehndi
	 * Expected: No path
	 */
	@Test
	public void testNoPath() {
		try{
			dcGraph.breadthFirstSearch("\"Batman\"", "\"Daler Mehndi\"", 1);
			fail();
		}
		catch(NoPathException e)
		{
			assertEquals(true,true);
		}
	}
	
	/**Test start and end same. Batman -> Batman
	 * Expected: Return path with just Batman
	 */
	@Test
	public void testPathToSelf() {
		List<String> correct = new ArrayList<String>();
		correct.add("\"Batman\"");
		assertEquals( correct, dcGraph.breadthFirstSearch("\"Batman\"", "\"Batman\"", 1));
	}
	
	/**
	 * TEST ALPHABETICAL ORDER OF EDGES. Random Pedestrian -> Darkseid
	 * We will add comics to the first 2 edges that are alphabetically lower than the ones
	 * that currently exist. 
	 * Expected: Path will use the newly added edges that are alphabetically lower. Last edge
	 * should be kept the same, because the new comic is alphabetically greater
	 */
	@Test
	public void alphabetOrderEdges() {
		dcGraph.addEdge("\"Batman\"", "\"Random Pedestrian\"", "\"A lower comic\"");
		dcGraph.addEdge("\"Batman\"", "\"Ra's al Ghul\"", "\"Batman Begins\"");
		//This one won't be used in output, because it is alphab. greater than "Darkness"
		dcGraph.addEdge( "\"Ra's al Ghul\"","\"Darkseid\"", "\"Not a lower comic\"" );
		
		String correct = "Random Pedestrian and Batman appear in A lower comic\n"+
						"Batman and Ra's al Ghul appear in Batman Begins\n" +
						"Ra's al Ghul and Darkseid appear in Darkness\n";
		String test = dcGraph.pathToString(dcGraph.breadthFirstSearch("\"Random Pedestrian\"", "\"Darkseid\"", 1));
		assertEquals(correct, test );
	}
	
	/**
	 * TEST NON-EXIST VERTICES
	 * Expected: Throw IllegalArgumentException because vertices not on graph
	 */
	@Test
	public void nonexistentVertices() {
		try{
			dcGraph.breadthFirstSearch("\"Relad\"", "\"Ryu\"", 1);
			fail();
		}
		catch(Exception e )
		{
			
		}
	}
}
