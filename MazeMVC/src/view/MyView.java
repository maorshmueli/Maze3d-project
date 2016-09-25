package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import algorithms.mazeGenarators.Maze3d;
import algorithms.mazeGenarators.Position;
import algorithms.search.Solution;
import algorithms.search.State;
import controller.Command;
import controller.Controller;

/**
 * get data from the controler and display the output to the user
 * @author Maor Shmueli
 *
 */
public class MyView extends CommonView
{
	CLI cli;
	HashMap<String,Command> stringToCommand;
	/**
	 * constructor using fields
	 * can direct the output/input to/from different sources
	 * @param c controller of this view
	 * @param in BufferedReader input
	 * @param out PrintWriter output
	 */
	public MyView(Controller c,BufferedReader in,PrintWriter out) 
	{
		super(c);
		cli=new CLI(in, out);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start() {
		cli.start();
		
	}
	
	
	
	/**
	 * setting the hash map that was initialized in the controller to the view
	 */
	public void setStringToCommand(HashMap<String, Command> stringToCommand) 
	{
		this.stringToCommand = stringToCommand;
		cli.setStringToCommand(stringToCommand);
	}
	/**
	 * {@inheritDoc}
	 */
	public void showDirPath(String[] dirArray)
	{
		System.out.println("The files and directories in this path are:");
		for(String s:dirArray)
		{
			System.out.println(s);
		}
	}
	/**
	 * {@inheritDoc}
	 */
	public void showError(String message)
	{
		System.out.println(message);
	}
	/**
	 * {@inheritDoc}
	 */
	public void showGenerate3dMaze(String message)
	{
		System.out.println(message);
	}
	/**
	 * {@inheritDoc}
	 */
	public void showDisplayName(byte[] byteArr)
	{
		try {
			Maze3d maze3d=new Maze3d(byteArr);
			
			System.out.println(maze3d);
			
			System.out.println("The start position: "+maze3d.getStartPosition());
			System.out.println("The goal position:  "+maze3d.getGoalPosition());
			
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
		System.out.println(crossString);
	}
	/**
	 * {@inheritDoc}
	 */
	public void showSaveMaze(String str)
	{
		System.out.println(str);
	}
	/**
	 * {@inheritDoc}
	 */
	public void showLoadMaze(String str)
	{
		System.out.println(str);
	}
	/**
	 * {@inheritDoc}
	 */
	public void showSolve(String message)
	{
		System.out.println(message);
	}

	/**
	 * {@inheritDoc}
	 */
	public void showDisplaySolution(Solution<Position> sol)
	{
		ArrayList<State<Position>> al = (ArrayList<State<Position>>) sol.getStates();

		System.out.println(al.toString());
		
	}
	/**
	 * {@inheritDoc}
	 */
	public void showHelp()
	{
		System.out.println("Help Center:");
		System.out.println("dir <path>                                           				-display the files and directories in this specific path.");
		System.out.println("generate_3d_maze <name> <x> <y> <z> <algorithm> {ByLastCell\\ByRandom}     	-generating maze with the specified name,with xyz dimensions with algorith:simple/growing");
		System.out.println("display <name>                                       				-display the specified maze");
		System.out.println("display_cross_section {x,y,z} <index> for <name>  				-diplaying cross section(x,y or z,chose one) in the index specified for maze with this name");
		System.out.println("save_maze <name> <file name>                         				-save maze in file name specified");
		System.out.println("load_maze <file name> <name>                         				-load maze from file specified");
		System.out.println("solve <name> <algorithm>                             				-solve maze with specified algorithm:bfs/dfs");
		System.out.println("display_solution <name>                             				-solve the maze and show the solution");
		System.out.println("exit                                                 				-exit the program");
		
		System.out.println();
		
	}

}