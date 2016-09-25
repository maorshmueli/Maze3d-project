package algorithms.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <h1>Solution class </h1>
 * Define a list of state that is the solution of a problem
 * @param <T> the type of object we will work with
 * 
 * @author Maor Shmueli
 */
public class Solution<T> implements Serializable{


	private static final long serialVersionUID = 1L;
	private List<State<T>> states = new ArrayList<State<T>>();

	/**
	 * get the states list
	 * @return states list of states
	 */
	public List<State<T>> getStates() {
		return states;
	}

	
	/**
	 * set values for the states list
	 * @return nothing
	 */
	public void setStates(List<State<T>> states) {
		this.states = states;
	}
	
	/**
	 * return a StringBuilder of the states list for printing it
	 * @return sb StringBuilder
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (State<T> s : states) {
			sb.append(s.toString()).append(" ");
		}
		
		return sb.toString();
	}
}
