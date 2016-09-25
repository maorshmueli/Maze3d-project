package algorithms.search;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;


/**
 * <h1>DFS class </h1>extends CommonSearcher<T>
 * Define DFS algorithm using Stack
 * @param <T> the type of object we will work with
 * 
 * @author Maor Shmueli
 */
public class DFS<T> extends CommonSearcher<T> {
	
	private Stack<State<T>> openList;
	private ArrayList<State<T>> closedList;
	
	/**
	 * DFS class constructor.
	 * Define start value for evaluatedNodes,
	 * define new Stack and ArrayList.
	 * @return nothing.
	 */
	public DFS() {
		evaluatedNodes = 0;
		this.openList=new Stack<State<T>>();
		this.closedList = new ArrayList<State<T>>();
	}
	
	/**
	 * Find a solution for the problem using DFS algorithm
	 * @param searchable s object, adapter that will be type of searchable interface will be injected.
	 * @return solution<T> - the solution for the problem, a state<T> ArrayList.
	 */
	@Override
	public Solution<T> search(Searchable s) {
		
		//if there is no object to search on
		if (s == null)
			return null;
		
		//Add the start state to the stack
		openList.add(s.getStartState());
		//get the goal state
		State<T> goalState = s.getGoalState();
		
		//loop all states
		while (!openList.isEmpty()) {
			//Pop form stack the last pushed state (LIFO)
			State<T> currState  = openList.pop();
			//add current state to closed list, so we wont check it again.
			closedList.add(currState);
			//count number of state evaluated.
			evaluatedNodes++;
			
			//check if reached the end
			if (currState.equals(goalState)) {
				return backTrace(currState);
			}
			
			//get all the possible states for currState
			List<State<T>> neighbors = s.getAllPossibleStates(currState);
			
			//Loop all the possible states for currState
			for (State<T> neighbor : neighbors) {
				//If this State not visited yet, make it part of the solution
				if(!closedList.contains(neighbor)) {			
					neighbor.setCameFrom(currState);
					openList.push(neighbor);
			    }
			 }
			
		}
		return null;
	}

	/**
	 * return the number of evaluated states.
	 * @return evaluatedNodes - number of evaluated states.
	 */
	@Override
	public int getNumberOfNodesEvaluated() {
		return evaluatedNodes;
	}
}
