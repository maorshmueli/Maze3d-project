package algorithms.demo;

import java.awt.List;
import java.util.ArrayList;

import algorithms.mazeGenarators.Maze3d;
import algorithms.mazeGenarators.Position;
import algorithms.search.Searchable;
import algorithms.search.State;


/**
 * <h1>MazeAdapter class </h1>implements Searchable<Position>
 * Define an adaptation between 3D maze and the search algorithms
 */
public class MazeAdapter implements Searchable<Position>{

	private Maze3d maze;
	
	/**
	 * MazeAdapter class constructor.
	 * get a maze and add it to class maze data member.
	 * @param Maze3d
	 * @return nothing.
	 */
	public MazeAdapter(Maze3d maze)
	{
		this.maze= maze;
	}
	
	/**
	 * Get the maze
	 * @return maze
	 */
	public Maze3d getMaze() {
		return maze;
	}

	/**
	 * Set new value for maze
	 * @param maze
	 * @return nothing
	 */
	public void setMaze(Maze3d maze) {
		this.maze = maze;
	}
	
	
	/**
	 * Get the start state of the maze
	 * @return startState
	 */
	@Override
	public State<Position> getStartState() {
		Position startPos = maze.getStartPosition();
		State<Position> startState = new State<Position>(startPos);
		return startState;
	}

	
	/**
	 * Get the end state of the maze
	 * @return goalState
	 */
	@Override
	public State<Position> getGoalState() {
		Position goalPos = maze.getGoalPosition();
		State<Position> goalState = new State<Position>(goalPos);
		return goalState;
	}

	/**
	 * Get a state and return all its possible free moves.
	 * @param state<position>
	 * @return states list of states
	 */
	@Override
	public ArrayList<State<Position>> getAllPossibleStates(State<Position> s) {
		Position currPos = s.getValue();
		
		ArrayList<Position> moves = maze.getPossibleMoves(currPos);
		ArrayList<State<Position>> states = new ArrayList<State<Position>>();
		
		for (Position pos: moves) {
			states.add(new State<Position>(pos));
		}
		return states;		
	}

	/**
	 * return the cost of reaching a position from its neighbor
	 * in the maze all moves have the same cost
	 * @param currState
	 * @param neighbor
	 * @return 1
	 */
	@Override
	public double getMoveCost(State<Position> currState, State<Position> neighbor) {
		return 1; // in the maze all moves have the same cost
	}


	

}
