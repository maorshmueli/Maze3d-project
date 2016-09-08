package algorithms.search;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>Searchable interface </h1>
 * An interface between structure, that will implement the interface, and a solution for a problem.
 * @param <T> the type of object we will work with
 * 
 * @author Maor Shmueli
 */
public interface Searchable<T> {
	
	/**
	 * Get the start state of the structure
	 * @return startState
	 */
	State<T> getStartState();
	
	/**
	 * Get the end state of the structure
	 * @return goalState
	 */
	State<T> getGoalState();
	
	/**
	 * Get a state and return all its possible free moves.
	 * @param state<position>
	 * @return states list of states
	 */
	ArrayList<State<T>> getAllPossibleStates(State<T> s);
	
	/**
	 * return the cost of reaching a position from its neighbor
	 * @param currState
	 * @param neighbor
	 * @return cost
	 */
	double getMoveCost(State<T> currState, State<T> neighbor);
}
