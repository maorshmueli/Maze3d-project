package presenter;

import java.util.HashMap;

import model.Model;
import view.View;

public class CommandManager {

	private Model m;
	private View v;
	protected HashMap<String, Command> viewStringToCommand;
	protected HashMap<String, Command> modelStringToCommand;
	
	public CommandManager(Model m , View v) {
		this.m = m;
		this.v = v;
		viewStringToCommand = new HashMap<String,Command>();
		modelStringToCommand = new HashMap<String,Command>();
		initCommands();
	}
	
	protected void initCommands() 
	{
		//**************Commands from view***************
		viewStringToCommand.put("dir",new Command()
		{
			@Override
			public void doCommand(String[] args) {
				m.HandleDirPath(args);
			}
		});
		
		viewStringToCommand.put("generate_3d_maze", new Command()
		{

			@Override
			public void doCommand(String[] args) {
				m.handleGenerate3dMaze(args);
				
			}
			
		});
		
		viewStringToCommand.put("help", new Command(){

			@Override
			public void doCommand(String[] args) {
				v.showHelp();
				
			}
			
		});
		viewStringToCommand.put("display", new Command()
		{

			@Override
			public void doCommand(String[] args) {
				m.handleDisplayName(args);
				
			}
			
		});
		
		viewStringToCommand.put("display_cross_section", new Command()
		{

			@Override
			public void doCommand(String[] args) 
			{
				m.handleDisplayCrossSectionBy(args);
				
			}
		
		});
		viewStringToCommand.put("save_maze",new Command() {
			
			@Override
			public void doCommand(String[] args) {
				m.handleSaveMaze(args);
				
			}
		});
		viewStringToCommand.put("load_maze", new Command(){

			@Override
			public void doCommand(String[] args) {
				m.handleLoadMaze(args);
				
			}
			
		});
		viewStringToCommand.put("solve", new Command(){

			@Override
			public void doCommand(String[] args) {
				m.handleSolve(args);
				
			}
			
		});
		viewStringToCommand.put("display_solution", new Command() {
			
			@Override
			public void doCommand(String[] args) {
				m.handleDisplaySolution(args);
				
			}
		});
		viewStringToCommand.put("get_all_mazes", new Command() {
			
			@Override
			public void doCommand(String[] args) {
				m.handleGetMazesList(args);
				
			}
		});
		viewStringToCommand.put("exit", new Command() {
			
			@Override
			public void doCommand(String[] args) {
				m.handleExitCommand(args);
				
			}
		});
	
		
		
		//**************Commands from view***************
		modelStringToCommand.put("dir",new Command()
		{
			@Override
			public void doCommand(String[] args) {
				v.showDirPath(m.getDirPath());
			}
		});
		
		modelStringToCommand.put("cross",new Command()
		{
			@Override
			public void doCommand(String[] args) {
				v.showDisplayCrossSectionBy(m.getCrossSection());
			}
		});
		
		modelStringToCommand.put("solution",new Command()
		{
			@Override
			public void doCommand(String[] args) {
				v.showDisplaySolution(m.getSolution());
			}
		});
		
		modelStringToCommand.put("display",new Command()
		{
			@Override
			public void doCommand(String[] args) {
				v.showDisplayName(m.getMazeByName(args[1]));
			}
		});
		
		modelStringToCommand.put("error",new Command()
		{
			@Override
			public void doCommand(String[] args) {
				v.showError(m.getErrorCode());
			}
		});
		
		modelStringToCommand.put("message",new Command()
		{
			@Override
			public void doCommand(String[] args) {
				v.showmessage(m.getMessage());
			}
		});
		modelStringToCommand.put("get_all_mazes", new Command() {
			
			@Override
			public void doCommand(String[] args) {
				v.showMazeNameList(m.GetMazesList());
				
			}
		});
		
		
		
		
	}
	
	public HashMap<String, Command> getViewCommands()
	{
		return viewStringToCommand;
	}
	
	public HashMap<String, Command> getModelCommands()
	{
		return modelStringToCommand;
	}
	
	
}
