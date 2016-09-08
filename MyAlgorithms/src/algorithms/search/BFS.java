package algorithms.search;

import java.awt.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;


/**
 * <h1>BFS class </h1>extends CommonSearcher<T>
 * Define BFS algorithm using Priority Queue
 * @param <T> the type of object we will work with
 * @author Maor Shmueli
 */
public class BFS<T> extends CommonSearcher {
	
	private PriorityQueue<State<T>> openList;
	private Set<State<T>> closedList;
	
	
	/**
	 * BFS class constructor.
	 * Define start value for evaluatedNodes,
	 * define new PriorityQueue and HashSet.
	 * @return nothing.
	 */
	public BFS() {
		evaluatedNodes = 0;
		openList = new PriorityQueue<State<T>>();
		closedList  = new HashSet<State<T>>();
	}
	
	/**
	 * Find a solution for the problem using BFS algorithm
	 * @param searchable object, adapter that will be type of searchable interface will be injected.
	 * @return solution<T> - the solution for the problem, a state<T> ArrayList.
	 */
	@Override
	public Solution<T> search (Searchable s){
		//get the start state of the maze
		State<T> startState = s.getStartState();
		
		//get the end state of the maze
		State<T> goalState = s.getGoalState();
		
		//add the start state to the empty priority queue.
		openList.add(startState);
		
		//loop while the queue is not empty (worst case: loop all possible state of the maze)
		while(!openList.isEmpty()) {
			//pool last state ordered by the property queue.
			State<T> currState = openList.poll();
		
			
			//count number of state evaluated.
			evaluatedNodes++;
			
			//check if reached the end
			if(currState.equals(goalState)) {
				//return the Solution of the maze by came from.
				return backTrace(currState);
				
			}
			
			//get all possible neighbors of the current state. 
			ArrayList<State<T>> neighbors = s.getAllPossibleStates(currState);
			
			//loop all current state neighbors.
			for(State<T> neighbor : neighbors) {
				//check if we already checked this state.
				if(!openList.contains(neighbor) && !closedList.contains(neighbor)) {
					//check state came from and update the cost.
					neighbor.setCameFrom(currState);
					neighbor.setCost(currState.getCost() + s.getMoveCost(currState, neighbor));
					//add neighbor to the queue.
					openList.add(neighbor);
				}
				
				//if we already check this state.
				else {
					double newPathCost = currState.getCost() + s.getMoveCost(currState, neighbor);
					
					if(neighbor.getCost() > newPathCost) {
						neighbor.setCost(newPathCost);
						neighbor.setCameFrom(currState);
						
						
						if(!openList.contains(neighbor)) {
							openList.add(neighbor);
						}
						
						else {
							openList.remove(neighbor);
							openList.add(neighbor);
						}
					}
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
