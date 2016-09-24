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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import algorithms.demo.MazeAdapter;
import algorithms.mazeGenarators.ByLastCell;
import algorithms.mazeGenarators.ByRandomCell;
import algorithms.mazeGenarators.GrowingTreeGenerator;
import algorithms.mazeGenarators.Maze3d;
import algorithms.mazeGenarators.Maze3dGenerator;
import algorithms.mazeGenarators.Position;
import algorithms.mazeGenarators.SimpleMaze3dGenerator;
import algorithms.search.BFS;
import algorithms.search.DFS;
import algorithms.search.Searcher;
import algorithms.search.Solution;
import io.MyCompressorOutputStream;
import io.MyDecompressorInputStream;
/**
 * generating the code behind each command
 * @author Maor Shmueli
 *
 */

public class MyModel extends Observable implements Model {
	private HashMap<String, Maze3d> mazeCollection;//hash map that storing mazes with key name
	private ExecutorService threadPool;
	private HashMap<String, String> mazeToFile;//hash map with key-name of maze,value-the file name where the maze is saved
	private HashMap<Maze3d, Solution<Position>> mazeSolutions;//hash map that hold the solution for mazes
	
	
	private String  errorCode;//the error that pop when there is an error will be sent to the observers
	private String message;//the message that will be sent to the observers
	private String[] dirPath; //dirpath string for moving it to the observers
	private String crossSection;//the 2d array of crossed maze, will be sent to the observers
	private Solution<Position> solution;//the solution ouput for the maze, will be sent to the observers
	

	/**
	 * {@inheritDoc}
	 */
	public MyModel() {
		super();
		mazeCollection = new HashMap<String, Maze3d>();
		threadPool = Executors.newFixedThreadPool(10);
		
		mazeToFile=new HashMap<String,String>();
		mazeSolutions=new HashMap<Maze3d, Solution<Position>>();
		
		//load mazes and solutions from previous runs
		loadSolutions();
	}

	/**
	 * {@inheritDoc}
	 */
	public void HandleDirPath(String[] paramArray) {
		if (paramArray.length != 1) // path did not supplied
		{
			errorCode = "Invalid path";
			
			//notify the observers that error occurred
			String[] s = new String[1];
			s[0] = "error";
			
			setChanged();
			notifyObservers(s);
			
			return;
		}
		File f = new File(paramArray[0].toString());

		if ((f.list() != null) && (f.list().length > 0)) {
			//update dirPath data member for future select
			dirPath = f.list();
			
			//update hasCahnged status
			setChanged();
			
			//for send specific notification
			String[] s = new String[1];
			s[0] = "dir";
			notifyObservers(s);
			
		} 
		else if (f.list() == null) // invalid path
		{
			errorCode = "Invalid path";
			
			//notify the observers that error occurred
			String[] s = new String[1];
			s[0] = "error";
			
			setChanged();
			notifyObservers(s);
			
			return;
		} 
		else // if there is nothing in the list
		{
			errorCode = "Empty folder";
			
			//notify the observers that error occurred
			String[] s = new String[1];
			s[0] = "error";
			
			setChanged();
			notifyObservers(s);
			
			return;
		}

	}

	/**
	 * {@inheritDoc}
	 */
	public void handleGenerate3dMaze(String[] paramArray) {
		if ((paramArray.length != 5) && (paramArray.length != 6)) {
			errorCode = "Invalid number of parameters";
			
			//notify the observers that error occurred
			String[] s = new String[1];
			s[0] = "error";
			
			setChanged();
			notifyObservers(s);
			
			return;
		}
		try {
			if ((Integer.parseInt(paramArray[1]) <= 0) || (Integer.parseInt(paramArray[2]) <= 0)|| (Integer.parseInt(paramArray[3]) <= 0)) 
			{// checking if the sizes of the maze passed valid
				errorCode = "Invalid parameters";
				
				//notify the observers that error occurred
				String[] s = new String[1];
				s[0] = "error";
				
				setChanged();
				notifyObservers(s);
				
				return;
			}
		} 
		//catch if non numbers values entered
		catch (NumberFormatException e) {
			errorCode = "Invalid parameters";
			
			//notify the observers that error occurred
			String[] s = new String[1];
			s[0] = "error";
			
			setChanged();
			notifyObservers(s);
			
			return;
		}


		Future<String> future = threadPool.submit(new Callable<String>() {

			@Override
			public String call() throws Exception 
			{
				
				Maze3dGenerator mg;
				if (paramArray[4].intern() == "simple") {
					mg = new SimpleMaze3dGenerator();
				} 
				else if (paramArray[4].intern() == "growing") {
					if (paramArray.length != 6) {//if the growing tree type did not mentioned
						errorCode = "Growing type did not mentioned, please provide ByLastCell\\ByRandom";

						return "error";
					}
					
					if(paramArray[5].intern() == "ByLastCell"){
						mg = new GrowingTreeGenerator(new ByLastCell());
					}
					else if (paramArray[5].intern() == "ByRandom") {
						mg = new GrowingTreeGenerator(new ByRandomCell());
					}
					else {
						errorCode = "Invalid algorithm type name";
						
						return "error";
					}
					
				
				}
				else {
					errorCode = "Invalid algorithm name";

					return "error";
				}

				//check if maze key name already exists it the collection
				if (mazeCollection.containsKey(paramArray[0].toString())) {
					errorCode = "This name already exists,choose another one.";

					return "error";
				}

				// generate maze with the size from the input
				mazeCollection.put(paramArray[0].toString(), mg.generate(Integer.parseInt(paramArray[1]),
						Integer.parseInt(paramArray[2]), Integer.parseInt(paramArray[3])));
				
				
				message = "maze " + paramArray[0].toString() + " is ready";
	
				//thread returning the maze name
				return paramArray[0].toString();
			}
		});
		
		
				try {
					if (future.get() == "error") { //if error occurred at maze creation thread
						//notify the observers that error occurred
						String[] s = new String[1];
						s[0] = "error";
						
						setChanged();
						notifyObservers(s);
						
						return;
					}
					
					else { //if thread succeeded to create the maze
						String[] s = new String[1];
						s[0] = "message";
						
						setChanged();
						notifyObservers(s);
						
						return;
						
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}

		
	

	}

	/**
	 * {@inheritDoc}
	 */
	public void handleDisplayName(String[] paramArray) {
		if (paramArray.length != 1) {
			errorCode = "Invalid number of parameters";
			
			//notify the observers that error occurred
			String[] s = new String[1];
			s[0] = "error";
			
			setChanged();
			notifyObservers(s);
			
			return;
		}

		
		if (!mazeCollection.containsKey(paramArray[0].toString())) {
			
			errorCode = "Maze doesn't exists";
			
			//notify the observers that error occurred
			String[] s = new String[1];
			s[0] = "error";
			
			setChanged();
			notifyObservers(s);
			
			return;
		}
		
		//sending notification to the observers to display the maze by its name
		String[] arg = new String[2];
		arg[0] = "display";
		arg[1] = paramArray[0];
		
		setChanged();
		notifyObservers(arg);

	}

	/**
	 * {@inheritDoc}
	 */
	public void handleDisplayCrossSectionBy(String[] paramArray) 
	{
		if (paramArray.length != 3) 
		{
			errorCode = "Invalid number of parameters";
			
			//notify the observers that error occurred
			String[] s = new String[1];
			s[0] = "error";
			
			setChanged();
			notifyObservers(s);
			
			return;
		}
	

		if (!mazeCollection.containsKey(paramArray[2].toString())) // checking if maze is in the collection
		{
			errorCode = "This maze doesn't exists";
			
			//notify the observers that error occurred
			String[] s = new String[1];
			s[0] = "error";
			
			setChanged();
			notifyObservers(s);
			
			return;
		}

		
		if (paramArray[0].intern() == "x") {
			Maze3d maze = mazeCollection.get(paramArray[2].toString());
			try {
				int[][] crossSectionarray = maze.getCrossSectionByX(Integer.parseInt(paramArray[1]));
				crossSection = maze.print2DMaze(crossSectionarray);
				
				//update hasCahnged status
				setChanged();
				
				//for send specific notification
				String[] s = new String[1];
				s[0] = "cross";
				notifyObservers(s);
				
				return;
			} 
			catch (NumberFormatException e) {
				errorCode = "Invalid parameters";
				
				//notify the observers that error occurred
				String[] s = new String[1];
				s[0] = "error";
				
				setChanged();
				notifyObservers(s);
				
				return;
			} 
			catch (IndexOutOfBoundsException e) {
				
				errorCode = "Invalid x coordinate";
				
				//notify the observers that error occurred
				String[] s = new String[1];
				s[0] = "error";
				
				setChanged();
				notifyObservers(s);
				
				return;
			}

		} else if (paramArray[0].intern() == "y") 
		{
			Maze3d maze = mazeCollection.get(paramArray[2].toString());
			try 
			{
				int[][] crossSectionarray = maze.getCrossSectionByY(Integer.parseInt(paramArray[1]));
				crossSection = maze.print2DMaze(crossSectionarray);
				
				//update hasCahnged status
				setChanged();
				
				//for send specific notification
				String[] s = new String[1];
				s[0] = "cross";
				notifyObservers(s);
				
				return;

			} 
			catch (NumberFormatException e) {
				errorCode = "Invalid parameters";
				
				//notify the observers that error occurred
				String[] s = new String[1];
				s[0] = "error";
				
				setChanged();
				notifyObservers(s);
				
				return;
			} 
			catch (IndexOutOfBoundsException e) {
				
				errorCode = "Invalid x coordinate";
				
				//notify the observers that error occurred
				String[] s = new String[1];
				s[0] = "error";
				
				setChanged();
				notifyObservers(s);
				
				return;
			}
		} 
		else if (paramArray[0].intern() == "z") 
		{
			Maze3d maze = mazeCollection.get(paramArray[2].toString());
			try {
				int[][] crossSectionarray = maze.getCrossSectionByZ(Integer.parseInt(paramArray[1]));
				crossSection = maze.print2DMaze(crossSectionarray);
				
				//update hasCahnged status
				setChanged();
				
				//for send specific notification
				String[] s = new String[1];
				s[0] = "cross";
				notifyObservers(s);
				
				return;
				
			} 
			catch (NumberFormatException e) {
				errorCode = "Invalid parameters";
				
				//notify the observers that error occurred
				String[] s = new String[1];
				s[0] = "error";
				
				setChanged();
				
				return;
			} 
			catch (IndexOutOfBoundsException e) {
				
				errorCode = "Invalid x coordinate";
				
				//notify the observers that error occurred
				String[] s = new String[1];
				s[0] = "error";
				
				setChanged();
				notifyObservers(s);
				
				return;
			}
		} else {
			errorCode = "Invalid parameters";
			
			//notify the observers that error occurred
			String[] s = new String[1];
			s[0] = "error";
			
			setChanged();
			notifyObservers(s);
			
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
			errorCode = "Invalid amount of parameters";
			
			//notify the observers that error occurred
			String[] s = new String[1];
			s[0] = "error";
			
			setChanged();
			notifyObservers(s);
			
			return;
			

		}

		
		//check if the maze exits
		if (!(mazeCollection.containsKey(paramArray[0]))) 
		{
			errorCode = "maze doesn't exists";
			
			//notify the observers that error occurred
			String[] s = new String[1];
			s[0] = "error";
			
			setChanged();
			notifyObservers(s);
			
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
			
			//message to the observers
			message = paramArray[0] + " maze has been saved to file: " + paramArray[1];
			
			//sending a message notification to the observers
			String[] s = new String[1];
			s[0] = "message";
			
			setChanged();
			notifyObservers(s);
			
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
			errorCode = "Invalid amount of parameters";
			
			//notify the observers that error occurred
			String[] s = new String[1];
			s[0] = "error";
			
			setChanged();
			notifyObservers(s);
			
			return;
		}
		
		//checking if there are already maze with the same name
		if (mazeCollection.containsKey(paramArray[1])) 
		{
			errorCode = "Invalid name,this name is taken";
			
			//notify the observers that error occurred
			String[] s = new String[1];
			s[0] = "error";
			
			setChanged();
			notifyObservers(s);
			
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
			
			//message to the observers
			message = paramArray[1] + " maze has been loaded from file: " + paramArray[0];
			
			//sending a message notification to the observers
			String[] s = new String[1];
			s[0] = "message";
			
			setChanged();
			notifyObservers(s);
			
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
				errorCode = "invalid number of parameters";
				
				//notify the observers that error occurred
				String[] s = new String[1];
				s[0] = "error";
				
				setChanged();
				notifyObservers(s);
				
				return;
				
			}
			//check if  maze exists
			if(!(mazeCollection.containsKey(paramArray[0])))
			{
				errorCode = "Maze doesn't exist";

				//notify the observers that error occurred
				String[] s = new String[1];
				s[0] = "error";
				
				setChanged();
				notifyObservers(s);
				
				return;
				
			}
			
			//if  solution for this maze already exists
			if(mazeSolutions.containsKey(mazeCollection.get(paramArray[0])))
			{
				message = "Solution for "+paramArray[0]+ " is ready";
				
				//sending a message notification to the observers
				String[] s = new String[1];
				s[0] = "message";
				
				setChanged();
				notifyObservers(s);
			}
			

			Future<String> future = threadPool.submit(new Callable<String>() {

				@Override
				public String call() throws Exception {

					
					
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
						
						//message to the observers
						message = "Solution for "+paramArray[0]+ " is ready";
						
						//thread returns maze name if solve command succeeded
						return paramArray[0];
						
						
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
						
						//message to the observers
						message = "Solution for "+paramArray[0]+ " is ready";
						
						//thread returns maze name if solve command succeeded
						return paramArray[0];
					}
					else
					{	
						errorCode = "Invalid algorithm";
						
						return "error";
						
					}
					
				}
			});
		
			try {
				if (future.get() == "error")
				{
					//notify the observers that error occurred
					String[] s = new String[1];
					s[0] = "error";
					
					setChanged();
					notifyObservers(s);
				}
				else 
				{
					//sending a message notification to the observers
					String[] s = new String[1];
					s[0] = "message";
					
					setChanged();
					notifyObservers(s);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}

	}
	/**
	 * {@inheritDoc}
	 */
	public void handleDisplaySolution(String[] paramarray)
	{
		if(paramarray.length!=1)
		{
			errorCode = "Invalid amount of parameters";
			
			//notify the observers that error occurred
			String[] s = new String[1];
			s[0] = "error";
			
			setChanged();
			notifyObservers(s);
			
			return;
		}
		
		if(!(mazeCollection.containsKey(paramarray[0])))
		{
			errorCode = "Maze with this name,doesn't exists";
			
			//notify the observers that error occurred
			String[] s = new String[1];
			s[0] = "error";
			
			setChanged();
			notifyObservers(s);
			
			return;
		}
		
		if(mazeSolutions.containsKey(mazeCollection.get(paramarray[0])))
		{
			//take the object of maze from maze 3d,and pass it to the maze Solution,and it return the solution
			solution = mazeSolutions.get(mazeCollection.get(paramarray[0]));
			
			//update hasCahnged status
			setChanged();
			
			//for send specific notification
			String[] s = new String[1];
			s[0] = "solution";
			notifyObservers(s);
			
			return;
			
		}
		else
		{
			errorCode = "Solution doesn't exists(use solve command first)";
			
			//notify the observers that error occurred
			String[] s = new String[1];
			s[0] = "error";
			
			setChanged();
			notifyObservers(s);
			
			return;
		}
	}
	
	private void saveSolutions() {
		ObjectOutputStream oos = null;
		try {
		    oos = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream("solutions.dat")));
			oos.writeObject(mazeCollection);
			oos.writeObject(mazeSolutions);			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				oos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void loadSolutions() {
		File file = new File("solutions.dat");
		if (!file.exists())
			return;
		
		ObjectInputStream ois = null;
		
		try {
			ois = new ObjectInputStream(new GZIPInputStream(new FileInputStream("solutions.dat")));
			mazeCollection = (HashMap<String, Maze3d>)ois.readObject();
			mazeSolutions = (HashMap<Maze3d, Solution<Position>>)ois.readObject();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			try {
				ois.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}
	
	
	public String getErrorCode() {
		return errorCode;
	}

	public String getMessage() {
		return message;
	}

	public String[] getDirPath() {
		return dirPath;
	}

	public String getCrossSection() {
		return crossSection;
	}

	public Solution<Position> getSolution() {
		return solution;
	}

	/**
	 * {@inheritDoc}
	 */
	public void handleExitCommand(String[] emptyArr)
	{
		//save mazes and solutions in a file for future use
		saveSolutions();
		
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
	
	public byte[] getMazeByName(String name) {
		Maze3d maze = mazeCollection.get(name);
		return maze.toByteArray();
		
	}
	
}

