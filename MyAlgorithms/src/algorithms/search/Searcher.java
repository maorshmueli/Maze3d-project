package algorithms.search;

/**
 * <h1>Searcher interface </h1>
 * define a method of solving a problem.
 * @param <T> the type of object we will work with
 */
public interface Searcher<T> {
	/**
	 * Find a solution for the problem using BFS algorithm
	 * @param searchable object, adapter that will be type of searchable interface will be injected.
	 * @return solution<T> - the solution for the problem, a state<T> ArrayList.
	 * 
	 * @author Maor Shmueli
	 */
    public Solution<T> search(Searchable s);
    
	/**
	 * return the number of evaluated states.
	 * @return evaluatedNodes - number of evaluated states.
	 */
    public int getNumberOfNodesEvaluated();
}
