package test;

import static org.junit.Assert.*;

import org.junit.Test;

import algorithms.demo.MazeAdapter;
import algorithms.mazeGenarators.ByLastCell;
import algorithms.mazeGenarators.GrowingTreeGenerator;
import algorithms.mazeGenarators.Maze3d;
import algorithms.mazeGenarators.Position;
import algorithms.search.BFS;
import algorithms.search.Searcher;
import algorithms.search.Solution;

public class BFSTest {

	private Solution<Position> solution;
	private Searcher<Position> mySearcher;
	Maze3d maze;
	MazeAdapter myMaze;
	
	
	
	public BFSTest(){
		// Set growing maze generator
		GrowingTreeGenerator mg = new GrowingTreeGenerator(new ByLastCell());
		// generate another 3d maze
		this.maze = mg.generate(10, 10, 10);

		// Declare search algorithm - BFS
		this.myMaze = new MazeAdapter(maze);
		this.mySearcher = new BFS<Position>();
		this.solution = mySearcher.search(myMaze);
	}
	
	
	@Test
	public void mySearcherIsNotNullTest() {
		assertNotNull(mySearcher);
	}


	@Test
	public void validNumberOfEvalutedNodes() {
		assertEquals(true, mySearcher.getNumberOfNodesEvaluated() > 0);
	}

	@Test
	public void numOfEvalutedNodesVsNumOfStates() {
		assertEquals(true, mySearcher.getNumberOfNodesEvaluated() >= solution.getStates().size());
	}
	
	
	@Test
	public void searcherIsNullWhenMazeIsNullTest() {
		assertEquals(null, mySearcher.search(null));
	}
	

}
