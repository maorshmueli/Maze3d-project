package view;

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
import presenter.Properties;


public class MazeWindow extends BaseWindow {

	private MazeDisplay mazeDisplay;
	private Properties properties;
	private String[] mazes;
	private String currentMaze;
	private boolean isDisplayed = false;
	
	
	public MazeWindow(Properties p) {
		this.properties = p;
		this.currentMaze = null;
	}
	
	@Override
	protected void initWidgets() {
		
		GridLayout gridLayout = new GridLayout(2, false);
		
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
		
		
		Composite btnGroup = new Composite(shell, SWT.BORDER);
		RowLayout rowLayout = new RowLayout(SWT.VERTICAL);
		btnGroup.setLayout(rowLayout);
		btnGroup.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
		
		
		Button btnSolveMaze = new Button(btnGroup, SWT.PUSH);
		btnSolveMaze.setText("Solve maze");
		
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
		
		Button btnDisplayMaze = new Button(btnGroup, SWT.PUSH);
		btnDisplayMaze.setText("Display maze");	
		
		/*
		//text box got which maze do we working on
		Label lblMaze = new Label(btnGroup, SWT.NONE);
		lblMaze.setText("Maze Name: ");
		Text txtMaze = new Text(btnGroup, SWT.BORDER);
		txtMaze.setText("<Choose_Maze>"); ---------------------------------------------------------
		
		//textbox default value
		txtMaze.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent arg0) {
				
			}
			
			@Override
			public void focusGained(FocusEvent arg0) {
				txtMaze.setText("");


			}
		});
*/
		
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
		
		mazeDisplay = new MazeDisplay(shell, SWT.BORDER);
        mazeDisplay.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true)); //fill all the window size
        mazeDisplay.setBackgroundImage(new Image(null, "resources/images/backgroundBig.jpg"));
        mazeDisplay.setFocus();
        
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
        MenuItem fileMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
        fileMenuHeader.setText("&File");
        Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
        fileMenuHeader.setMenu(fileMenu);
        
        MenuItem mazeMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
        mazeMenuHeader.setText("&Maze"); 
        Menu mazeMenu = new Menu(shell, SWT.DROP_DOWN);
        mazeMenuHeader.setMenu(mazeMenu);
        
        //Childs Items
        //File
        MenuItem mazeLoadItem = new MenuItem(fileMenu, SWT.PUSH);
        mazeLoadItem.setText("&Load maze from file");
        MenuItem mazeSaveItem = new MenuItem(fileMenu, SWT.PUSH);
        mazeSaveItem.setText("&Save maze to file");
        MenuItem fileexitItem = new MenuItem(fileMenu, SWT.PUSH);
        fileexitItem.setText("&Exit");
        
        //Maze
        MenuItem mazeChooseItem = new MenuItem(mazeMenu, SWT.PUSH);
        mazeChooseItem.setText("&Choose a maze");
        MenuItem mazeGenerateItem = new MenuItem(mazeMenu, SWT.PUSH);
        mazeGenerateItem.setText("&Generate maze");
        
        //Menu listeners
        mazeGenerateItem.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {

				showGenerateMazeOptions();
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
        
        mazeChooseItem.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				//notify observers to generate the maze to get mazes list
				setChanged();
				notifyObservers("get_all_mazes");
				
				
				showChooseMaze();

				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
        
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
        
        shell.setMenuBar(menuBar);
        //******************END Menu Bar********************/
		
	}

	protected void showGenerateMazeOptions() {
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
	protected void showChooseMaze() {
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



	public void showMessageBox(String str){
		MessageBox msg = new MessageBox(shell);
		msg.setMessage(str);
		msg.open();	
	}
	
	//@Override
	public void displayMessage(String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start() {
		run();
	}


	@Override
	public void showError(String message) {
		showMessageBox(message);
		
	}


	@Override
	public void showDisplayCrossSectionBy(String crossMazeBySection) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showSaveMaze(String str) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showLoadMaze(String str) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showSolve(String message) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void showDirPath(String[] dirArray) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showmessage(String message) {
		showMessageBox(message);
		
	}

	@Override
	public void showHelp() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showGenerate3dMaze(String message) {
		// TODO Auto-generated method stub
		
	}

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
		
		mazeDisplay.setCrossSection(crossSection, null, null);
		//mazeDisplay.setGoalPosition(new Position(3,3,3));
		mazeDisplay.setGoalPosition(maze3d.getGoalPosition());
		
		/*
		MessageBox msg = new MessageBox(shell);
		msg.setMessage(maze3d.toString());
		msg.open();	
		*/
		//DEBUG
		Label lbltest = new Label(shell, SWT.NONE);
		lbltest.setText("start: " + startPos + "\n" + "goal: " + maze3d.getGoalPosition());
		//to update the changes in the shell
		shell.layout(true,true);
		
		/*
		MessageBox msg = new MessageBox(shell);
		msg.setMessage("start: " + startPos + "\n" + "goal: " + maze3d.getGoalPosition());
		msg.open();	
		
		*/

		
	}
	
	


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
		this.showSolutionByAnimation = new Timer();
		this.showSolutionByAnimation.scheduleAtFixedRate(this.animationSolutionTask, 0, 500);
		



	}

	@Override
	public void showMazeNameList(String[] mazes) {
		this.mazes = mazes;
	}

}