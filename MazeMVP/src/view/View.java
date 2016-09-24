package view;



import java.util.HashMap;

import algorithms.search.Solution;
import algorithms.mazeGenarators.Position;



/**
 * display all the outcome of the commands
 * @author Maor Shmueli
 *
 */
public interface View {
	/**
	 * starting the command line interface
	 */
	public void start();
	
	/**
	 * display the outcome of command:dir <path>
	 * displaying the files and directories of the specified path
	 * @param dirArray string's array with the names of files and directories in the specified path
	 */
	public void showDirPath(String[] dirArray);
	/**
	 * display the error in the commands that the client wrote
	 * @param message string telling what is the error
	 */
	public void showError(String message);
	/**
	 * display the message in the commands that the client wrote
	 * @param message string telling what is the error
	 */
	public void showmessage(String message);
	/**
	 * displaying help,which shows the commands the client can write
	 */
	public void showHelp();
	/**
	 * display the message that the maze is ready
	 * @param message string with the massege:maze is ready
	 */
	public void showGenerate3dMaze(String message);
	/**
	 * displaying the specified maze
	 * @param byteArr byte array representing the maze
	 */
	public void showDisplayName(byte[] byteArr);
	/**
	 * displaying the cross section which the client asked for
	 * @param crossSection 2d array with the cross section asked
	 */
	public void showDisplayCrossSectionBy(String crossString);
	/**
	 * displaying the string:the maze has been saved
	 * @param str string with the word:maze has been saved
	 */
	public void showSaveMaze(String str);
	/**
	 * displaying the string:the maze has been loaded
	 * @param str string with the word:maze has been loaded
	 */
	public void showLoadMaze(String str);
	/**
	 * displaying the string:solution for maze is ready
	 * @param message string with the words:solution for maze is ready
	 */
	public void showSolve(String message);
	/**
	 * displaying the solution of the specified maze
	 * @param sol the solution of the maze
	 */
	public void showDisplaySolution(Solution<Position> sol);
}