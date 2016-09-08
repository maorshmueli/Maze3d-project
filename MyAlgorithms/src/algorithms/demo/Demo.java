package algorithms.demo;

import algorithms.mazeGenarators.ByLastCell;
import algorithms.mazeGenarators.GrowingTreeGenerator;
import algorithms.mazeGenarators.Maze3d;
import algorithms.mazeGenarators.Maze3dGenerator;
import algorithms.mazeGenarators.Position;
import algorithms.search.BFS;
import algorithms.search.DFS;
import algorithms.search.Solution;

public class Demo {

	
	public static void main(String[] args) {	
		
		run();
	}
	private static void run() {
		Maze3dGenerator generator = new GrowingTreeGenerator(new ByLastCell());
		Maze3d maze = generator.generate(5, 5, 5);
		
		//System.out.println(maze);
		
		MazeAdapter adapter = new MazeAdapter(maze);
		BFS<Position> bfs = new BFS<Position>();
		DFS<Position> dfs = new DFS<Position>();
		//Solution<Position> solution = bfs.search(adapter);
		Solution<Position> solution = dfs.search(adapter);
		System.out.println("DFS:");
		System.out.println(solution);
		System.out.println("Number of evaluated nodes: " + dfs.getEvaluatedNodes());
		
		System.out.println("BFS:");
		solution = bfs.search(adapter);
		System.out.println(solution);
		System.out.println("Number of evaluated nodes: " + bfs.getEvaluatedNodes());
		
		System.out.println("THE END!");
	}
}
