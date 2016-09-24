package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import controller.Command;

/**
 * defines the starting point of command line interface
 * @author Maor Shmueli
 *
 */
public class CLI extends Thread{
	BufferedReader in;
	PrintWriter out;
	HashMap<String,Command> stringToCommand;
	
	/**
	 * CLI constructor
	 * @param in the input source
	 * @param out the output source
	 */
	CLI(BufferedReader in, PrintWriter out) {
		super();
		this.in = in;
		this.out = out;
		
	}
	
	/**
	 * starts the command line interface
	 */
	public void start()
	{
		out.println("Hello,Welcome to my command line interface!\nPlease Enter command:");
		out.flush();
		new Thread(new Runnable() 
		{
			public void run() 
			{
				String line;
				Command command=null;

				try 
				{
					while((line=in.readLine()).intern()!="exit")
					{
						//define parameter list
						ArrayList<String> paramArray=new ArrayList<String>();
						
						//loop while command string is not empty or beak executed
						while(!(line.isEmpty()))
						{
							
							if((command=stringToCommand.get(line)) != null)
							{
								//reverse the order of the elements in the list
								Collections.reverse(paramArray);
								
								
								command.doCommand(paramArray.toArray(new String[paramArray.size()]));
								break;
							}
							if(line.indexOf(" ")==-1)//we ended the line
							{
								break;
							}
							
							paramArray.add(line.substring(line.lastIndexOf(" ")+1));
							line=line.substring(0, line.lastIndexOf(" "));	//cutting till " " (not including)
						}
						
						
						if(command==null)
						{
							out.println("Command not found!");
							out.flush();
						}
						
					}
					//execute the exit command
					command=stringToCommand.get("exit");
					command.doCommand(null);
					out.println("Goodbye :)");
					out.flush();
					
				} 
				catch (IOException e) {

					e.printStackTrace();
				}
			}
		}).start();

	}
	/**
	 * getter of input stream source
	 * @return input stream source
	 */
	public BufferedReader getIn() {
		return in;
	}
	/**
	 * sets the input stream source
	 * @param in input stream source
	 */
	public void setIn(BufferedReader in) {
		this.in = in;
	}
	
	/**
	 * getter of the output stream source
	 * @return output stream source
	 */
	public PrintWriter getOut() {
		return out;
	}
	/**
	 * sets the output stream source
	 * @param out output stream source
	 */
	public void setOut(PrintWriter out) {
		this.out = out;
	}
	/**
	 * gets the hash map with the commands
	 * @return hash map with key-name of command,value-the command
	 */
	public HashMap<String, Command> getStringToCommand() {
		return stringToCommand;
	}
	/**
	 * sets the hash map with the commands
	 * @param stringToCommand hash map with key-name of command,value-the command
	 */
	public void setStringToCommand(HashMap<String, Command> stringToCommand) {
		this.stringToCommand = stringToCommand;
	}
	
	
}