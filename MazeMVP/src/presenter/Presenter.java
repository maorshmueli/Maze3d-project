package presenter;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import model.Model;
import view.View;

/**
 * Presenter class implements Observer
 * link between the view and the model by listening to observable notification
 * @author Maor Shmueli
 *
 */
public class Presenter implements Observer{

	Model m;
	View v;
	protected HashMap<String, Command> viewStringToCommand;
	protected HashMap<String, Command> modelStringToCommand;
	CommandManager cmgr;
	
	/**
	 * Presenter constructor with values, 
	 * declare new show of CommandManager and link the classes command list
	 * @param m Model
	 * @param v View
	 */
	public Presenter(Model m , View v) {
		this.m = m;
		this.v = v;
		
		cmgr = new CommandManager(m, v);
		viewStringToCommand = cmgr.getViewCommands();
		modelStringToCommand = cmgr.getModelCommands();
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update(Observable ob, Object arg) {

		//if notified by the view
		if(ob == v) {
			String commandLine = (String)arg;
			
			String[] arr = commandLine.split((" "));
			String command = arr[0];
			
			if(!viewStringToCommand.containsKey(command)) {
				v.showError("Command not found!");
			}
			
			else {
				String[] paramArray = null;
				if(arr.length > 1) {
					//insert into paramArray just the arguments
					paramArray = commandLine.substring(commandLine.indexOf(" ") +1).split(" ");
				}
				//declare then execute the command with the parameters
				Command cmd = viewStringToCommand.get(command);
				cmd.doCommand(paramArray);
			}
		}
		
		//if notified by the model
		if(ob == m){
			String[] command = (String[])arg;
			
			if(!modelStringToCommand.containsKey(command[0])) {
				v.showError("Command not found!");
			}
			else {
				if(command.length == 1)
				{
					Command cmd = modelStringToCommand.get(command[0]);
					cmd.doCommand(null);
				}
				else {
					//create parameter array split by spaces
					Command cmd = modelStringToCommand.get(command[0]);
					cmd.doCommand(command);
					

				}
				
				
				
			}
			
			
		}
		
	}
	
	

}
