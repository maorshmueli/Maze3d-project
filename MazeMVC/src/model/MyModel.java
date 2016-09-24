package model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import algorithms.demo.MazeAdapter;
import algorithms.mazeGenarators.ByLastCell;
import algorithms.mazeGenarators.ByRandomCell;
import algorithms.mazeGenarators.GrowingTreeGenerator;
import algorithms.mazeGenarators.Maze3d;
import algorithms.mazeGenarators.Maze3dGenerator;
import algorithms.mazeGenarators.Maze3dGenerator;
import algorithms.mazeGenarators.Position;
import algorithms.mazeGenarators.SimpleMaze3dGenerator;
import algorithms.search.BFS;
import algorithms.search.DFS;
import algorithms.search.Searchable;
import algorithms.search.Searcher;
import algorithms.search.Solution;
//import algorithms.demo.Maze3dSearchable;
import controller.Controller;
import io.MyCompressorOutputStream;
import io.MyDecompressorInputStream;
/**
 * generating the code behind each command
 * @author Maor Shmueli
 *
 */

public class MyModel extends CommonModel {
	private HashMap<String, Maze3d> mazeCollection;//hash map that storing mazes with key name
	private ExecutorService threadPool;
	private HashMap<String, String> mazeToFile;//hash map with key-name of maze,value-the file name where the maze is saved
	private HashMap<Maze3d, Solution<Position>> mazeSolutions;//hash map that hold the solution for mazes

	/**
	 * {@inheritDoc}
	 */
	public MyModel(Controller c) {
		super(c);
		mazeCollection = new HashMap<String, Maze3d>();
		threadPool = Executors.newFixedThreadPool(10);
		
		mazeToFile=new HashMap<String,String>();
		mazeSolutions=new HashMap<Maze3d, Solution<Position>>();
	}

	/**
	 * {@inheritDoc}
	 */
	public void HandleDirPath(String[] paramArray) {
		if (paramArray.length != 1) // path did not supplied
		{
			c.passError("Invalid path");
			return;
		}
		File f = new File(paramArray[0].toString());

		if ((f.list() != null) && (f.list().length > 0)) {
			c.passDirPath(f.list());
		} 
		else if (f.list() == null) // invalid path
		{
			c.passError("Invalid path");
			return;
		} 
		else // if there is nothing in the list
		{
			c.passError("Empty folder");
			return;
		}

	}

	/**
	 * {@inheritDoc}
	 */
	public void handleGenerate3dMaze(String[] paramArray) {
		if ((paramArray.length != 5) && (paramArray.length != 6)) {
			c.passError("Invalid number of parameters");
			return;
		}
		try {
			if ((Integer.parseInt(paramArray[1]) <= 0) || (Integer.parseInt(paramArray[2]) <= 0)|| (Integer.parseInt(paramArray[3]) <= 0)) 
			{// checking if the sizes of the maze passed valid
				c.passError("Invalid parameters");
				return;
			}
		} 
		//catch if non numbers values entered
		catch (NumberFormatException e) {
			c.passError("Invalid parameters");
			return;
		}

		//execute in a new thread
		threadPool.execute(new Runnable() {
			public void run() 
			{
				Maze3dGenerator mg;
				if (paramArray[4].intern() == "simple") {
					mg = new SimpleMaze3dGenerator();
				} 
				else if (paramArray[4].intern() == "growing") {
					if (paramArray.length != 6) {//if the growing tree type did not mentioned
						c.passError("Growing type did not mentioned, please provide ByLastCell\\ByRandom");
						return;
					}
					
					if(paramArray[5].intern() == "ByLastCell"){
						mg = new GrowingTreeGenerator(new ByLastCell());
					}
					else if (paramArray[5].intern() == "ByRandom") {
						mg = new GrowingTreeGenerator(new ByRandomCell());
					}
					else {
						c.passError("Invalid algorithm type name");
						return;
					}
					
				
				}
				else {
					c.passError("Invalid algorithm name");
					return;
				}

				//check if maze key name already exists it the collection
				if (mazeCollection.containsKey(paramArray[0].toString())) {
					c.passError("This name already exists,choose another one.");
					return;
				}

				// generate maze with the size from the input
				mazeCollection.put(paramArray[0].toString(), mg.generate(Integer.parseInt(paramArray[1]),
						Integer.parseInt(paramArray[2]), Integer.parseInt(paramArray[3])));
				

				c.passGenerate3dMaze("maze " + paramArray[0].toString() + " is ready");
				
			}
		});
		
	}

	/**
	 * {@inheritDoc}
	 */
	public void handleDisplayName(String[] paramArray) {
		if (paramArray.length != 1) {
			c.passError("Invalid number of parameters");
			return;
		}

		
		if (!mazeCollection.containsKey(paramArray[0].toString())) {
			c.passError("Maze doesn't exists");
			return;
		}
		//getting the maze from maze collection, pass it by byte array
		c.passDisplayName(mazeCollection.get(paramArray[0].toString()).toByteArray());
		
		

	}

	/**
	 * {@inheritDoc}
	 */
	public void handleDisplayCrossSectionBy(String[] paramArray) 
	{
		if ((paramArray.length != 4) || (paramArray[2].intern() != "for")) 
		{
			c.passError("Invalid number of parameters");
			return;
		}
	

		if (!mazeCollection.containsKey(paramArray[3].toString())) // checking if maze is in the collection
		{
			c.passError("This maze doesn't exists");
			return;
		}

		
		if (paramArray[0].intern() == "x") {
			Maze3d maze = mazeCollection.get(paramArray[3].toString());
			try {
				int[][] crossSection = maze.getCrossSectionByX(Integer.parseInt(paramArray[1]));
				c.passDisplayCrossSectionBy(maze.print2DMaze(crossSection));
				return;
			} 
			catch (NumberFormatException e) {
				c.passError("Invalid parameters");
				return;
			} 
			catch (IndexOutOfBoundsException e) {
				c.passError("Invalid x coordinate");
				return;
			}

		} else if (paramArray[0].intern() == "y") 
		{
			Maze3d maze = mazeCollection.get(paramArray[3].toString());
			try 
			{
				int[][] crossSection = maze.getCrossSectionByY(Integer.parseInt(paramArray[1]));
				c.passDisplayCrossSectionBy(maze.print2DMaze(crossSection));
				return;
			} 
			catch (NumberFormatException e) 
			{
				c.passError("Invalid parameters");
				return;
			} 
			catch (IndexOutOfBoundsException e) 
			{
				c.passError("Invalid y coordinate");
				return;
			}
		} 
		else if (paramArray[0].intern() == "z") 
		{
			Maze3d maze = mazeCollection.get(paramArray[3].toString());
			try {
				int[][] crossSection = maze.getCrossSectionByZ(Integer.parseInt(paramArray[1]));
				c.passDisplayCrossSectionBy(maze.print2DMaze(crossSection));
				return;
			} 
			catch (NumberFormatException e) {
				c.passError("Invalid parameters");
				return;
			} 
			catch (IndexOutOfBoundsException e) {
				c.passError("Invalid z coordinate");
				return;
			}
		} else {
			c.passError("Invalid parameters");
			return;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void handleSaveMaze(String[] paramArray) 
	{

		if (paramArray.length != 2) 
		{
			c.passError("Invalid amount of parameters");
			return;
		}

		
		//check if the maze exits
		if (!(mazeCollection.containsKey(paramArray[0]))) 
		{
			c.passError("maze doesn't exists");
			return;
		}
		
		//select maze from collection
		Maze3d maze = mazeCollection.get(paramArray[0].toString());
		
		try{
			OutputStream out = new MyCompressorOutputStream(new FileOutputStream(paramArray[1].toString()));
			
			// writing the size of the maze
			out.write(ByteBuffer.allocate(4).putInt(maze.toByteArray().length).array());
			
			//writing the maze by byte array
			out.write(maze.toByteArray());
			out.flush();
			out.close();
			c.passLoadMaze(paramArray[0] + " maze has been saved from file: " + paramArray[1]);
			return;
		}
		catch (FileNotFoundException e){
		    System.err.println(e);
		    return;
		}
		catch (IOException e) {
			e.printStackTrace();
			return;
		}

	}

	/**
	 * {@inheritDoc}
	 */
	public void handleLoadMaze(String[] paramArray)
	{
		if (paramArray.length != 2) 
		{
			c.passError("Invalid amount of parameters");
			return;
		}
		
		//checking if there are already maze with the same name
		if (mazeCollection.containsKey(paramArray[1])) 
		{
			c.passError("Invalid name,this name is taken");
			return;
		}
		
		
		try{		
			InputStream in = new MyDecompressorInputStream(new FileInputStream(paramArray[0].toString()));
			ByteArrayOutputStream outByte = new ByteArrayOutputStream();
			
			outByte.write(in.read());
			outByte.write(in.read());
			outByte.write(in.read());
			outByte.write(in.read());
			

			ByteArrayInputStream inByte = new ByteArrayInputStream(outByte.toByteArray());
			DataInputStream dis = new DataInputStream(inByte);
			
			
			byte b[] = new byte[dis.readInt()];
			in.read(b);
			in.close();
			
			Maze3d loaded = new Maze3d(b);
			mazeCollection.put(paramArray[1], loaded);
			c.passLoadMaze(paramArray[1] + " maze has been loaded from file: " + paramArray[0]);
			
		}
		catch (FileNotFoundException e){
		    System.err.println(e);
		    return;
		} 
		catch (IOException e) {
			System.err.println(e);
			return;
		}
		
		
		

	}
	
	/**
	 * {@inheritDoc}
	 */
	public void handleSolve(String[] paramArray)
	{
			if(paramArray.length!=2)
			{
				c.passError("invalid number of parameters");
				return;
			}

		
		threadPool.execute(new Runnable() {
			
			@Override
			public void run() 
			{
				//check if  maze exists
				if(!(mazeCollection.containsKey(paramArray[0])))
				{
					c.passError("Maze doesn't exist");
					return;
				}
				/*
				//if  solution for this maze exists
				if(mazeSolutions.containsKey(mazeCollection.get(paramArray[0])))
				{
					c.passSolve("Solution for "+paramArray[0]+ " is ready");
					return;
				}
				
				*/
				
				if(paramArray[1].toString().equals("bfs"))
				{
					MazeAdapter ms=new MazeAdapter(mazeCollection.get(paramArray[0]));
					
					Searcher<Position> bfs=new BFS<Position>();
					Solution<Position> sol=bfs.search(ms);
					
					//check if solution for the maze already exists, if so, replace it with the new one
					if(mazeSolutions.containsKey(mazeCollection.get(paramArray[0]))) {
						mazeSolutions.remove(mazeCollection.get(paramArray[0]));
						mazeSolutions.put(mazeCollection.get(paramArray[0]), sol);
					}
					else {
						mazeSolutions.put(mazeCollection.get(paramArray[0]), sol);
					}
					
					c.passSolve("Solution for "+paramArray[0]+ " is ready");
					return;
				}
				else if(paramArray[1].toString().equals("dfs"))
				{
					MazeAdapter ms=new MazeAdapter(mazeCollection.get(paramArray[0]));
					
					Searcher<Position> dfs=new DFS<Position>();
					Solution<Position> sol=dfs.search(ms);
					
					//check if solution for the maze already exists, if so, replace it with the new one
					if(mazeSolutions.containsKey(mazeCollection.get(paramArray[0]))) {
						mazeSolutions.remove(mazeCollection.get(paramArray[0]));
						mazeSolutions.put(mazeCollection.get(paramArray[0]), sol);
					}
					else {
						mazeSolutions.put(mazeCollection.get(paramArray[0]), sol);
					}
					
					c.passSolve("Solution for "+paramArray[0]+ " is ready");
					return;
				}
				else
				{
					c.passError("Invalid algorithm");
					return;
				}
				
			}
		});
	}
	/**
	 * {@inheritDoc}
	 */
	public void handleDisplaySolution(String[] paramarray)
	{
		if(paramarray.length!=1)
		{
			c.passError("Invalid amount of parameters");
			return;
		}
		
		if(!(mazeCollection.containsKey(paramarray[0])))
		{
			c.passError("Maze with this name,doesn't exists");
			return;
		}
		
		if(mazeSolutions.containsKey(mazeCollection.get(paramarray[0])))
		{
			//take the object of maze from maze 3d,and pass it to the maze Solution,and it return the solution
			c.passDisplaySolution(mazeSolutions.get(mazeCollection.get(paramarray[0])));
			return;
		}
		else
		{
			c.passError("Solution doesn't exists(use solve command first)");
			return;
		}
	}
	/**
	 * {@inheritDoc}
	 */
	public void handleExitCommand(String[] emptyArr)
	{
		//close all threads
		threadPool.shutdown();
		try 
		{
			while(!(threadPool.awaitTermination(10, TimeUnit.SECONDS)));
		} 
		catch (InterruptedException e) {

			e.printStackTrace();
		}
		
	}
	
}