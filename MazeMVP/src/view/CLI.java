package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;


/**
 * defines the starting point of command line interface
 * @author Maor Shmueli
 *
 */
public class CLI extends Observable{
	
	BufferedReader in;
	PrintWriter out;
	
	//HashMap<String,Command> stringToCommand;
	
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
				String commandline;
				try{
					while((commandline=in.readLine()).intern()!="exit")
					{		
								if(!commandline.isEmpty())
								{
									setChanged();
									notifyObservers(commandline);
								}

					}
					
					//issue the exit command that release all resources
					setChanged();
					notifyObservers("exit");
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

	
}