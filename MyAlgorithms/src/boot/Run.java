package boot;
import algorithms.mazeGenarators.ByLastCell;
import algorithms.mazeGenarators.ByRandomCell;
import algorithms.mazeGenarators.GrowingTreeGenerator;
import algorithms.mazeGenarators.Maze3d;
import algorithms.mazeGenarators.Maze3dGenerator;
import algorithms.mazeGenarators.Position;
import algorithms.mazeGenarators.SimpleMaze3dGenerator;

public class Run {

	public static void main(String[] args) {
		System.out.println("TEST SIMPLE MAZE: ");
		test(new SimpleMaze3dGenerator());
		System.out.println('\n');
		
		System.out.println("TEST GROWING TREE LAST CELL MAZE: ");
		test(new GrowingTreeGenerator(new ByLastCell()));
		System.out.println('\n');
		
		System.out.println("TEST GROWING TREE RANDOM CELL MAZE: ");
		test(new GrowingTreeGenerator(new ByRandomCell()));
		
	}
	
	private static void test (Maze3dGenerator mg)
	{
		// prints the time it takes the algorithm to run
		System.out.print("Time measured for the algorithm: ");
		System.out.println(mg.measureAlgorithmTime(4, 3, 2));
		// generate another 3d maze
		Maze3d maze=mg.generate(4, 3, 2);
		// get the maze entrance
		Position p=maze.getStartPosition();
		// print the position
		System.out.print("Start position: ");
		System.out.println(p); // format "{x,y,z}"
		// get all the possible moves from a position
		String[] moves=maze.getPossibleMovesString(p);
		// print the moves
		System.out.println("Possible moves: ");
		for(String move : moves)
		System.out.println(move);
		// prints the maze exit position
		System.out.print("Exit position: ");
		System.out.println(maze.getGoalPosition());
		
		try{
			// get 2d cross sections of the 3d maze
			System.out.println("The Maze cross by X:");
			int[][] maze2dx=maze.getCrossSectionByX(3);
			System.out.println(maze.print2DMaze(maze2dx));
			
			System.out.println("The Maze cross by Y:");
			int[][] maze2dy=maze.getCrossSectionByY(2);
			System.out.println(maze.print2DMaze(maze2dy));
			
			System.out.println("The Maze cross by Z:");
			int[][] maze2dz=maze.getCrossSectionByZ(0);
			System.out.println(maze.print2DMaze(maze2dz));
	
			//print maze
			System.out.println("The Maze:");
			System.out.println(maze);
			
			// this should throw an exception!
			maze.getCrossSectionByX(-1);
		} catch (IndexOutOfBoundsException e){
			System.out.println("good!");
		}
	}

}
