package algorithms.search;

import java.util.List;

/**
 * <h1>CommonSearcher class </h1>implements Searcher
 * Define number of evaluatedNodes for search algorithms and the method to calculate the trace between states.
 * @param <T> the type of object we will work with
 * 
 * @author Maor Shmueli
 */
public abstract class CommonSearcher<T> implements Searcher {

		protected int evaluatedNodes;

		/**
		 * get the number of evaluated nodes.
		 * @return evaluatedNodes
		 */
		public int getEvaluatedNodes() {
			return evaluatedNodes;
		}

		
		/**
		 * Calculate the trace between states
		 * @param goalState, the finish state of the trace.
		 * @return solution<T> - the solution for the problem, a state<T> ArrayList.
		 */
		protected Solution<T> backTrace(State<T> goalState){
			//define the return solution
			Solution<T> sol = new Solution<T>();
			
			//start with the end state
			State<T> currState = goalState;
			//link between states and sol (solution) states ArrayList
			List<State<T>> states = sol.getStates();
			
			//while didn't reached first state
			while (currState != null) {		
				states.add(0, currState);
				currState = currState.getCameFrom();
			}
			return sol;
		}
		
		
}
