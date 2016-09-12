package algorithms.demo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.sql.rowset.spi.TransactionalWriter;

import algorithms.mazeGenarators.ByLastCell;
import algorithms.mazeGenarators.GrowingTreeGenerator;
import algorithms.mazeGenarators.Maze3d;
import algorithms.mazeGenarators.Maze3dGenerator;
import algorithms.mazeGenarators.Position;
import algorithms.search.BFS;
import algorithms.search.DFS;
import algorithms.search.Solution;
import io.MyCompressorOutputStream;
import io.MyDecompressorInputStream;

public class Demo {

	
	public static void main(String[] args) throws IOException {	
		
		run();
	}
	private static void run() throws IOException {
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
		
		//Compression
		
		try{
			OutputStream out = new MyCompressorOutputStream(new FileOutputStream("1.maz"));
			out.write(maze.toByteArray());
			out.flush();
			out.close();
			
			InputStream in = new MyDecompressorInputStream(new FileInputStream("1.maz"));
			byte b[] = new byte[maze.toByteArray().length];
			in.read(b);
			in.close();
			
			Maze3d loaded = new Maze3d(b);
			System.out.println(loaded);
			
			System.out.println(loaded.equals(maze));
			
		}
		catch (FileNotFoundException e){
		    System.err.println(e);
		}
		
		
		
		
	}
}
