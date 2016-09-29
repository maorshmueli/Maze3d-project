package GUI;

import org.eclipse.swt.widgets.Menu;

import org.eclipse.swt.widgets.MenuItem;

import java.awt.MenuBar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.ExtendedSSLSession;
import javax.swing.JComboBox;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.theme.GroupDrawData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import algorithms.mazeGenarators.ByLastCell;
import algorithms.mazeGenarators.GrowingTreeGenerator;
import algorithms.mazeGenarators.Maze3d;
import algorithms.mazeGenarators.Position;
import algorithms.search.Solution;
import algorithms.search.State;
import boot.XMLManager;
import presenter.Properties;


/**
 * MazeWindow class extends BaseWindow
 * define the main screen(shell) functionality
 * define all the button, widgets and other windows
 * @author Maor Shmueli
 *
 */
public class MazeWindow extends BaseWindow {

	private MazeDisplay mazeDisplay;
	private Properties properties;
	private String[] mazes;
	private String currentMaze;
	private boolean isDisplayed = false;
	
	
	/**
	 * MazeWindow  constructor
	 */
	public MazeWindow(Properties properties) {
		this.properties = properties;
		this.currentMaze = null;
	}
	
	/**
	 * initial all main window's widgets 
	 */
	@Override
	protected void initWidgets() {
		
		GridLayout gridLayout = new GridLayout(2, false);
		
		//setting the shell layout
		shell.setLayout(gridLayout);
		shell.setText("Maze 3D Game");
		shell.setBackgroundImage(new Image(null, "resources/images/backgroundBig.jpg"));
		
		
		// Open in center of screen
		Rectangle bounds = display.getPrimaryMonitor().getBounds();
		Rectangle rect = shell.getBounds();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		shell.setLocation(x, y);

		// handle with the RED X
		shell.addListener(SWT.CLOSE, new Listener() {
			
			@Override
			public void handleEvent(Event arg0) {
				exit();
				
			}
		});
		
		//define new widgets group
		Composite btnGroup = new Composite(shell, SWT.BORDER);
		RowLayout rowLayout = new RowLayout(SWT.VERTICAL);
		btnGroup.setLayout(rowLayout);
		btnGroup.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
		
		//menu image
		Label lblImg = new Label(btnGroup, SWT.CENTER);
		lblImg.setImage(new Image(null, "resources/images/menu.png"));
		
		//empty label
		Label lblempty1 = new Label(btnGroup, SWT.CENTER);
		
		//solve maze button
		Button btnSolveMaze = new Button(btnGroup, SWT.PUSH);
		btnSolveMaze.setText("Solve maze");
		
		//empty label
		Label lblempty2 = new Label(btnGroup, SWT.CENTER);
		
		//solve maze button listener
		btnSolveMaze.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(isDisplayed){
					
					//notify observers to for solving
					setChanged();
					notifyObservers("solve "+ currentMaze);
					
					//notify observers to for displaying solution
					setChanged();
					notifyObservers("display_solution "+ currentMaze);
				}
				
				else {
					showMessageBox("Please display the maze first!");
				}
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				//
			}
		});
		
		//display maze button
		Button btnDisplayMaze = new Button(btnGroup, SWT.PUSH);
		btnDisplayMaze.setText("Display maze");	
		
		//display maze button listener
		btnDisplayMaze.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(currentMaze != null){
					//setting the maze name
					mazeDisplay.setMazeName(currentMaze);
					
					//notify observers to for returning the maze
					setChanged();
					notifyObservers("display "+ currentMaze);
					isDisplayed = true;
				}
				
				else {
					showMessageBox("Please choose a maze from the maze menu!");
				}
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//define the maze area, will be the maze Canvas
		mazeDisplay = new MazeDisplay(shell, SWT.BORDER);
        mazeDisplay.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true)); //fill all the window size
        mazeDisplay.setBackgroundImage(new Image(null, "resources/images/backgroundBig.jpg"));
        mazeDisplay.setFocus();
        
        //the key listener for walking the character throw the maze cells
        mazeDisplay.addKeyListener(new KeyListener() {
			String Step = null;
			@Override
			public void keyReleased(KeyEvent e) {
				switch (e.keyCode) {
				case SWT.ARROW_RIGHT:
					Step = "Right";
					break;
				case SWT.ARROW_LEFT:
					Step = "Left";
					break;
				case SWT.ARROW_UP:
					Step = "Up";
					break;
				case SWT.ARROW_DOWN:
					Step = "Down";
					break;
				case SWT.PAGE_DOWN:
					Step = "Backward";
					break;
				case SWT.PAGE_UP:
					Step = "Forward";
					break;
				default:
					break;
				}
				
				if(Step != null)
				{
					//moving the character and redram the maze
					mazeDisplay.moveTheCharacterByDirection(Step);
					mazeDisplay.redrawMe();
					
					//check if user reached goal position - winner
					if(mazeDisplay.checkWinner())
						showMessageBox("You are the winner!");
					
				}
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
        
        //******************Menu Bar********************/
        //new bar
        Menu menuBar = new Menu(shell, SWT.BAR);
        
        //top items
        //file menu
        MenuItem fileMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
        fileMenuHeader.setText("&File");
        Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
        fileMenuHeader.setMenu(fileMenu);
        
        //edit menu
        MenuItem editMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
        editMenuHeader.setText("&Edit");
        Menu editMenu = new Menu(shell, SWT.DROP_DOWN);
        editMenuHeader.setMenu(editMenu);
        
        //maze menu
        MenuItem mazeMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
        mazeMenuHeader.setText("&Maze"); 
        Menu mazeMenu = new Menu(shell, SWT.DROP_DOWN);
        mazeMenuHeader.setMenu(mazeMenu);
        
        //Childs Items
        //File
        MenuItem fileLoadItem = new MenuItem(fileMenu, SWT.PUSH);
        fileLoadItem.setText("&Load maze from file");
        MenuItem fileSaveItem = new MenuItem(fileMenu, SWT.PUSH);
        fileSaveItem.setText("&Save maze to file");
        MenuItem filePropertiesItem = new MenuItem(fileMenu, SWT.PUSH);
        filePropertiesItem.setText("&Load properties file");
        MenuItem fileexitItem = new MenuItem(fileMenu, SWT.PUSH);
        fileexitItem.setText("&Exit");
        
        //Edit
        MenuItem editPropertiesItem = new MenuItem(editMenu, SWT.PUSH);
        editPropertiesItem.setText("&Edit properties file");
        
        //Maze
        MenuItem mazeChooseItem = new MenuItem(mazeMenu, SWT.PUSH);
        mazeChooseItem.setText("&Choose a maze");
        MenuItem mazeGenerateItem = new MenuItem(mazeMenu, SWT.PUSH);
        mazeGenerateItem.setText("&Generate maze");
        
        //Menu listeners
        //generate maze listener
        mazeGenerateItem.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				//starting generate maze window
				showGenerateMazeOptions();
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
        
        //choose maze listener
        mazeChooseItem.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				//notify observers to get mazes list
				setChanged();
				notifyObservers("get_all_mazes");
				
				showChooseMaze();

				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
        
        //File listeners
        //exit listener
        fileexitItem.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				//close window and send the observers exit command
				exit();
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
        
        //save maze listener
        fileSaveItem.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(currentMaze != null){
					FileDialog fd = new FileDialog(shell, SWT.SAVE);
			        fd.setText("Save");
			        fd.setFilterPath("C:/");
			        //only *.maz files
			        String[] filterExt = { "*.maz"};
			        fd.setFilterExtensions(filterExt);
			        String selected = fd.open();
			        
					//notify observers so save the current maze
					setChanged();
					notifyObservers("save_maze " + currentMaze + " " + selected);
				}
				else
					showMessageBox("Please choose a maze from the maze menu!");
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				//
			}
		});
        
        //load maze listener
        fileLoadItem.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				       FileDialog fd = new FileDialog(shell, SWT.OPEN);
				        fd.setText("Open");
				        fd.setFilterPath("C:/");
				      //only *.maz files
				        String[] filterExt = { "*.maz"};
				        fd.setFilterExtensions(filterExt);
				        String selected = fd.open();
				        
				        showLoadMazeName(selected);

			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				//
			}
		});
        
        //load file properties listener
        filePropertiesItem.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				       FileDialog fd = new FileDialog(shell, SWT.OPEN);
				        fd.setText("Open");
				        fd.setFilterPath("C:/");
				      //only *.xml files
				        String[] filterExt = { "*.xml"};
				        fd.setFilterExtensions(filterExt);
				        String selected = fd.open();
				        
						//notify observers to generate the maze
						setChanged();
						notifyObservers("properties " + selected);

			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				//
			}
		});
        
        editPropertiesItem.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				showEditProperties();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				//
			}
		});
        shell.setMenuBar(menuBar);
        //******************END Menu Bar********************/
		
	}

	/**
	 * creating the generate maze window
	 */
	protected void showGenerateMazeOptions() {
		//new shell window
		Shell shell = new Shell();
		shell.setText("Generate Maze");
		shell.setSize(300, 200);
		
		
		
		// Open in center of screen
		Rectangle bounds = display.getPrimaryMonitor().getBounds();
		Rectangle rect = shell.getBounds();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		shell.setLocation(x, y);
		
		GridLayout layout = new GridLayout(2, false);
		shell.setLayout(layout);
		
		Label lblFloors = new Label(shell, SWT.NONE);
		lblFloors.setText("Floors: ");
		Text txtFloors = new Text(shell, SWT.BORDER);
		txtFloors.setText("4");
		
		Label lblRows = new Label(shell, SWT.NONE);
		lblRows.setText("Rows: ");
		Text txtRows = new Text(shell, SWT.BORDER);
		txtRows.setText("4");
		
		Label lblCols = new Label(shell, SWT.NONE);
		lblCols.setText("Cols: ");
		Text txtCols = new Text(shell, SWT.BORDER);
		txtCols.setText("4");
		
		Label lblName = new Label(shell, SWT.NONE);
		lblName.setText("Maze name: ");
		Text txtName = new Text(shell, SWT.BORDER);
		txtName.setText("Maze1");
		
		Button btnGenerate = new Button(shell, SWT.PUSH);
		btnGenerate.setText("Generate maze");
		

		//generate maze button listener
		btnGenerate.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {	
				
				//notify observers to generate the maze
				setChanged();
				notifyObservers("generate_3d_maze" + " "
						+ txtName.getText() + " " + txtFloors.getText() + " "
						+ txtRows.getText() + " " + txtCols.getText() + " "
						+ properties.getGenerateMazeAlgorithm());
				shell.dispose();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				//
			}
		});
		
		
		
		shell.open();		
		
	
	}
	
	/**
	 * open a window for use input to set the loaded maze name
	 * @param file loaded file name
	 */
	protected void showLoadMazeName(String file) {
		//new window shell
		Shell shell = new Shell();
		shell.setText("Load Maze");
		shell.setSize(270, 80);
		
		
		
		// Open in center of screen
		Rectangle bounds = display.getPrimaryMonitor().getBounds();
		Rectangle rect = shell.getBounds();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		shell.setLocation(x, y);
		
		GridLayout layout = new GridLayout(2, false);
		shell.setLayout(layout);
		
		Composite composite = new Composite(shell, SWT.NONE);
		RowLayout rowLayout = new RowLayout(SWT.HORIZONTAL);
		composite.setLayout(rowLayout);
		composite.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
		
		Label lblMazeName = new Label(composite, SWT.NONE);
		lblMazeName.setText("Choose maze name: ");
		Text txtMazeName = new Text(composite, SWT.BORDER);
		txtMazeName.setText("noam");
		Button btnMazeName = new Button(composite, SWT.PUSH);
		btnMazeName.setText("Load");
		

		//set maze name button listener
		btnMazeName.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				//notify observers so save the current maze
				setChanged();
				notifyObservers("load_maze " + file + " " + txtMazeName.getText());
				shell.dispose();
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		
		shell.open();	

	}
	
	/**
	 * creating a window with list of mazes to choose a maze
	 */
	protected void showChooseMaze() {
		//new shell window
		Shell shell = new Shell();
		shell.setText("Choose Maze");
		shell.setSize(250, 100);
		
		
		
		// Open in center of screen
		Rectangle bounds = display.getPrimaryMonitor().getBounds();
		Rectangle rect = shell.getBounds();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		shell.setLocation(x, y);
		
		GridLayout layout = new GridLayout(2, false);
		shell.setLayout(layout);
		
		//label for choosing a maze
		Label lblChoose = new Label(shell, SWT.NONE);
		lblChoose.setText("Please Choose a maze: ");

		//comboBox list of all availiable mazes
		Combo combo = new Combo(shell, SWT.READ_ONLY);
		combo.setItems(mazes);
		
		
		Button btnChoose = new Button(shell, SWT.PUSH);
		btnChoose.setText("Choose maze");
		

		//choose maze button listener
		btnChoose.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {	
				currentMaze = combo.getText();
				shell.dispose();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				//
			}
		});
		
		
		

		shell.open();		
		
	
	}

	/**
	 * creating a window to edit the properties file
	 */
	protected void showEditProperties() {
		//new shell window
		Shell shell = new Shell();
		shell.setText("Choose Maze");
		shell.setSize(300, 190);
		
		
		
		// Open in center of screen
		Rectangle bounds = display.getPrimaryMonitor().getBounds();
		Rectangle rect = shell.getBounds();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		shell.setLocation(x, y);
		
		GridLayout layout = new GridLayout(2, false);
		shell.setLayout(layout);
		
		Label lblGame = new Label(shell, SWT.NONE);
		lblGame.setText("Game interface: ");
		Text txtGame = new Text(shell, SWT.BORDER);
		txtGame.setText(properties.getGameType());
		
		Label lblGenerate = new Label(shell, SWT.NONE);
		lblGenerate.setText("Generate maze algorithm: ");
		Text txtGenerate = new Text(shell, SWT.BORDER);
		txtGenerate.setText(properties.getGenerateMazeAlgorithm());
		txtGenerate.setSize(100, txtGenerate.getSize().y);
		
		Label lblSearch = new Label(shell, SWT.NONE);
		lblSearch.setText("Search algorithm: ");
		Text txtSearch = new Text(shell, SWT.BORDER);
		txtSearch.setText(properties.getSearchAlgorithm());
		
		Label lblThreads = new Label(shell, SWT.NONE);
		lblThreads.setText("Threads number: ");
		Text txtThreads = new Text(shell, SWT.BORDER);
		txtThreads.setText(Integer.toString(properties.getThreadsNumber()));
		
		Button btnApply = new Button(shell, SWT.PUSH);
		btnApply.setText("Apply");
		

		//choose maze button listener
		btnApply.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {	
				Properties p = new Properties(Integer.parseInt(txtThreads.getText()), txtSearch.getText(), txtGenerate.getText(),txtGame.getText());
				XMLManager.writeXml(p);
				shell.dispose();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				//
			}
		});
		
		
		

		shell.open();		
		
	
	}

	/**
	 * popping a message box
	 * @param str the message
	 */
	public void showMessageBox(String str){
		MessageBox msg = new MessageBox(shell);
		msg.setMessage(str);
		msg.open();	
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start() {
		run();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showError(String message) {
		showMessageBox(message);
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showDisplayCrossSectionBy(String crossMazeBySection) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showSaveMaze(String str) {
		showmessage(str);
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showLoadMaze(String str) {
		showmessage(str);
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showSolve(String message) {
		// TODO Auto-generated method stub
		
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showDirPath(String[] dirArray) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showmessage(String message) {
		showMessageBox(message);
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showHelp() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showGenerate3dMaze(String message) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showDisplayName(byte[] byteArr) {

		//unmark solution trace from old mazes
		mazeDisplay.unMarkTheSolution();
		
		//Convert maze from byeArray
		Maze3d maze3d = null;
		try {
			maze3d = new Maze3d(byteArr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		Position startPos = maze3d.getFirstInnerCell(); //first cell
		//Position startPos = maze3d.getStartPosition(); //first cell
		mazeDisplay.setCharacterPosition(startPos);
		
		mazeDisplay.setMaze(maze3d);
		int [][] crossSection = maze3d.getCrossSectionByZ(startPos.z);
		
		mazeDisplay.setCrossSection(crossSection);
		//mazeDisplay.setGoalPosition(new Position(3,3,3));
		mazeDisplay.setGoalPosition(maze3d.getGoalPosition());
		

		//to update the changes in the shell
		shell.layout(true,true);
		


		
	}
	
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showDisplaySolution(Solution<Position> sol) {
		//setting the solution in mazeDisplay
		mazeDisplay.setSolution(sol);
		
		//mark the solution with RED light
		mazeDisplay.markTheSolution();
		
		this.animationSolutionTask = new TimerTask() {
			
			int i = 0;
			
			@Override
			public void run() {
				if (i < sol.getStates().size()){ //if didn't reached the goal position
					mazeDisplay.moveTheCharacter(sol.getStates().get(i++).getValue());
					mazeDisplay.redrawMe();
				}
				
			}
		};
		//for showing solution animation
		this.showSolutionByAnimation = new Timer();
		this.showSolutionByAnimation.scheduleAtFixedRate(this.animationSolutionTask, 0, 500);
		



	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showMazeNameList(String[] mazes) {
		this.mazes = mazes;
	}

}