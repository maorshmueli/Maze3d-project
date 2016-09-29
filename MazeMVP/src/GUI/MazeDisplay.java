package GUI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import algorithms.mazeGenarators.Maze3d;
import algorithms.mazeGenarators.Position;
import algorithms.search.Solution;
import algorithms.search.State;
import view.Character;
import view.MyView;

/**
 * MazeDisplay
 * extends Canvas
 * this class will paint the maze
 * @author Maor Shmueli
 */
public class MazeDisplay extends Canvas {
	
	private String mazeName;
	private int whichFloorAmI;
	private int[][] crossSection = { {0}, {0} };
	private Character character;
	private Image imgGoal;
	private Image imgWinner;
	private Image imgUp;
	private Image imgDown;
	private Image imgUpDown;
	private Image imgWall;
	private Image imgMark;
	private boolean drawMeAHint;
	private Position hintPosition;
	private Position goalPosition;
	private Maze3d maze;
	private boolean solRequested = false;
	private Solution<Position> solution;

	/**
	 * Constructor 
	 * @param Composite parent, int style, MyView view
	 * draw the maze
	 */
	public MazeDisplay(Composite parent, int style) {
		
		super(parent, style);
		
		this.mazeName = null;
		this.whichFloorAmI = 0;
			
		this.character = new Character();
		this.character.setPos(new Position(-1, -1, -1));
		this.imgGoal = new Image(null,"resources/images/goal.png");
		this.imgWinner = new Image(null,"resources/images/winner.gif");
		this.imgUp = new Image(null, "resources/images/up.gif");
		this.imgDown = new Image(null, "resources/images/down.gif");
		this.imgUpDown = new Image(null, "resources/images/updown.gif");
		this.imgWall = new Image(null, "resources/images/wall.gif");
		this.imgMark = new Image(null, "resources/images/mark.png");
		this.drawMeAHint = false;
		this.hintPosition = null;
		this.goalPosition= new Position(-1, -1, -1);
		
		// draw the maze
		this.addPaintListener(new PaintListener() {
			

			/**
			 * {@inheritDoc}
			 */
			@Override
			public void paintControl(PaintEvent e) {
				
				//will be used for determining the cell sizes
				int x, y;
				
				//for sizing
				int canvasWidth = getSize().x;
				int canvasHeight = getSize().y;
				int cellWidth = canvasWidth / crossSection[0].length;
				int cellHeight = canvasHeight / crossSection.length;
				
				//keep the floor displayed
				whichFloorAmI = character.getPos().z;
				
				//the position of the hint up/down
				hintPosition = new Position(-1 ,-1,-1);
				
				//if the user choose a maze
				if(mazeName != null) {
					//loop all the crossSection
					for (int i = 0; i < crossSection.length; i++) {
						for (int j = 0; j < crossSection[i].length; j++) {
							x = i * cellWidth;
							y = j * cellHeight;
							
							if (crossSection[i][j] != 0)//check if a wall - drawing a wall
								e.gc.drawImage(imgWall, 0, 0, imgWall.getBounds().width, imgWall.getBounds().height, x, y, cellWidth, cellHeight);
							
							//if empty - checking if can go up/down for hints
							if (crossSection[i][j] == 0){
								hintPosition.z = whichFloorAmI;
								hintPosition.y = j;
								hintPosition.x = i;
								
								//checking possibility
								if ((maze.checkPossibleMove(hintPosition, "Forward")&&(maze.checkPossibleMove(hintPosition, "Backward")))){
									e.gc.drawImage(imgUpDown, 0, 0, imgUpDown.getBounds().width, imgUpDown.getBounds().height,x, y, cellWidth, cellHeight);
								}
								else {
									if(maze.checkPossibleMove(hintPosition, "Forward"))
										e.gc.drawImage(imgUp, 0, 0, imgUp.getBounds().width, imgUp.getBounds().height, x, y, cellWidth, cellHeight);
									else if (maze.checkPossibleMove(hintPosition, "Backward")){
										e.gc.drawImage(imgDown, 0, 0, imgDown.getBounds().width, imgDown.getBounds().height, x, y, cellWidth, cellHeight);
									}
								}
							}
							//for marking the solution with red light
							if((solRequested) && (solution.getStates().contains(new State<Position>(new Position(whichFloorAmI,j,i))))){
								e.gc.drawImage(imgMark, 0, 0, imgMark.getBounds().width, imgMark.getBounds().height, x, y, cellWidth, cellHeight);
							}
							
							
						}
					}
				}
				
				if (drawMeAHint) {
					drawMeAHint = false;
				}
				
				
				//find if reached the goal position - winner , change to winner image
				if(checkWinner()){
					e.gc.drawImage(imgWinner, 0, 0, imgWinner.getBounds().width, imgWinner.getBounds().height, cellWidth * goalPosition.x, cellHeight * goalPosition.y, cellWidth, cellHeight);
				}
				else {
					character.draw(cellWidth, cellHeight, e.gc);
					if (whichFloorAmI == goalPosition.z)
						e.gc.drawImage(imgGoal, 0, 0, imgGoal.getBounds().width, imgGoal.getBounds().height, cellWidth * goalPosition.x, cellHeight * goalPosition.y, cellWidth, cellHeight);
				} 
					
				
				
				forceFocus();
			}
			
		});
		
	}
		
	
	/**
	 * check if user reached goal position - winner
	 * @param boolean
	 */

	public boolean checkWinner() {
		return (character.getPos().equals(goalPosition));
	}
	

	/**
	 *This method tell us where are we in the maze
	 * @param whichFloorAmI, int
	 */
	public void setWhichFloorAmI(int whichFloorAmI) {
		this.whichFloorAmI = whichFloorAmI;
	}

	/**
	 * paint the maze in crossSection [][]
	 * @param crossSection, crossSection
	 */
	public void setCrossSection(int[][] crossSection) {
		this.crossSection = crossSection;
	}

	/**
	 * set the character position then draw the maze
	 * @param pos, the position 
	 */
	public void setCharacterPosition(Position pos) {
		this.character.setPos(pos);
		redrawMe();
	}

	/**
	 * move the character
	 * @param pos, the position
	 */
	public void moveTheCharacter(Position pos) {
		setCrossSection(maze.getCrossSectionByZ(pos.z));
		this.character.setPos(pos);
		//redrawMe();
	}
	/**
	 * move the character by direction string
	 * @param pos, the position
	 */
	public void moveTheCharacterByDirection(String step) {
		Position nexPos = null;
		
		//checking user key action
		switch (step) {
		case "Right":
			nexPos = new Position(character.getPos().z , character.getPos().y , character.getPos().x + 1);
			break;
		case "Left":
			nexPos = new Position(character.getPos().z , character.getPos().y , character.getPos().x - 1);
			break;
		case "Up":
			nexPos = new Position(character.getPos().z , character.getPos().y - 1 , character.getPos().x);
			break;
		case "Down":
			nexPos = new Position(character.getPos().z , character.getPos().y + 1, character.getPos().x);
			break;
		case "Backward":
			nexPos = new Position(character.getPos().z - 1 , character.getPos().y , character.getPos().x);
			break;
		case "Forward":
			nexPos = new Position(character.getPos().z + 1 , character.getPos().y, character.getPos().x);
			break;
			
		default:
			break;
		}
		//if move is possible
		if(maze.checkPossibleMove(character.getPos(), step)){
			drawHint(nexPos);
			moveTheCharacter(nexPos);
			
		}
		
			
		
	}
	/**
	 * set the Maze Name 
	 * @param String mazeName
	 */
	public void setMazeName(String mazeName) {
		this.mazeName = mazeName;
	}
	
	/**
	 * get the Maze Name 
	 * @param String mazeName
	 */
	public String getMazeName() {
		return mazeName;
	}
	
	/**
	 * set goal position
	 * @param Position goalPosition
	 */
	public void setGoalPosition(Position goalPosition) {
		this.goalPosition = goalPosition;
	}
	
	/**
	 *This method draw a hint to the player
	 * @param PositionhintPos
	 */
	public void drawHint(Position hintPos) {
		this.drawMeAHint = true;
		this.hintPosition = hintPos;
		//redrawMe();
	}
	
	/**
	 *This method readraw the canvas in runnable sync
	 */
	public void redrawMe() {
	
		
		getDisplay().syncExec(new Runnable() {

			@Override
			public void run() {
				setEnabled(true);
				redraw();
			}
			
		});
	}
	
	/**
	 * set the maze value
	 * @param maze Maze3D
	 */
	public void setMaze(Maze3d maze){
		this.maze = maze;
	}
	
	/**
	 * get the character
	 * @return character
	 */
	public Character getCharacter() {
		return character;
	}
	
	/**
	 * mark the solution trace
	 */
	public void markTheSolution(){
		solRequested = true;
	}
	
	/**
	 * unmark the solution trace
	 */
	public void unMarkTheSolution(){
		solRequested = false;
	}


	/**
	 * set the solution of the maze
	 * @param solution the solution
	 */
	public void setSolution(Solution<Position> solution) {
		this.solution = solution;
	}

	
}