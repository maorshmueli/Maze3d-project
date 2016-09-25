package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import algorithms.mazeGenarators.Maze3d;
import algorithms.mazeGenarators.Position;
import algorithms.search.Solution;
import algorithms.search.State;

/**
 * get data from the controler and display the output to the user
 * @author Maor Shmueli
 *
 */
public class MyView extends Observable implements View, Observer
{
	private BufferedReader in;
	private PrintWriter out;
	CLI cli;
	//HashMap<String,Command> stringToCommand;
	/**
	 * constructor using fields
	 * can direct the output/input to/from different sources
	 * @param c controller of this view
	 * @param in BufferedReader input
	 * @param out PrintWriter output
	 */
	public MyView(BufferedReader in,PrintWriter out) 
	{
		this.in = in;
		this.out = out;
		
		cli=new CLI(in, out);
		cli.addObserver(this);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start() {
		cli.start();
		
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public void showDirPath(String[] dirArray)
	{
		out.println("The files and directories in this path are:");
		for(String s:dirArray)
		{
			out.println(s);
		}
	}
	/**
	 * {@inheritDoc}
	 */
	public void showError(String message)
	{
		out.println(message);
	}
	/**
	 * {@inheritDoc}
	 */
	public void showGenerate3dMaze(String message)
	{
		out.println(message);
	}
	/**
	 * {@inheritDoc}
	 */
	public void showDisplayName(byte[] byteArr)
	{
		try {
			Maze3d maze3d=new Maze3d(byteArr);
			
			out.println(maze3d);
			
			out.println("The start position: "+maze3d.getStartPosition());
			out.println("The goal position:  "+maze3d.getGoalPosition());
			
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	/**
	 * {@inheritDoc}
	 */
	public void showDisplayCrossSectionBy(String crossString)
	{
		out.println(crossString);
	}
	/**
	 * {@inheritDoc}
	 */
	public void showSaveMaze(String str)
	{
		out.println(str);
	}
	/**
	 * {@inheritDoc}
	 */
	public void showLoadMaze(String str)
	{
		out.println(str);
	}
	/**
	 * {@inheritDoc}
	 */
	public void showSolve(String message)
	{
		out.println(message);
	}

	/**
	 * {@inheritDoc}
	 */
	public void showDisplaySolution(Solution<Position> sol)
	{
		ArrayList<State<Position>> al = (ArrayList<State<Position>>) sol.getStates();

		out.println(al.toString());
		
	}
	/**
	 * {@inheritDoc}
	 */
	public void showHelp()
	{
		out.println("Help Center:");
		out.println("dir <path>                                           				-display the files and directories in this specific path.");
		out.println("generate_3d_maze <name> <x> <y> <z> <algorithm> {ByLastCell\\ByRandom}     	-generating maze with the specified name,with xyz dimensions with algorith:simple/growing");
		out.println("display <name>                                       				-display the specified maze");
		out.println("display_cross_section {x,y,z} <index> <name>  					-diplaying cross section(x,y or z,chose one) in the index specified for maze with this name");
		out.println("save_maze <name> <file name>                         				-save maze in file name specified");
		out.println("load_maze <file name> <name>                         				-load maze from file specified");
		out.println("solve <name> <algorithm>                             				-solve maze with specified algorithm:bfs/dfs");
		out.println("display_solution <name>                             				-solve the maze and show the solution");
		out.println("exit                                                 				-exit the program");
		
		out.println();
		
	}

	@Override
	public void update(Observable o, Object arg) {
		if(o == cli)
		{
			setChanged();
			notifyObservers(arg);
		}
		
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showmessage(String message) {
		out.println(message);
		
	}

}