package algorithms.search;

import java.io.Serializable;

/**
 * <h1>State class </h1>
 * Wraps a specific T type with member that will help for the search algorithms
 * @param <T> the type of object we will work with
 * 
 * @author Maor Shmueli
 */
public class State<T> implements Comparable<State<T>>,Serializable {


	private static final long serialVersionUID = 1L;
	private State<T> cameFrom;
	private double cost;
	private T value;
	private String key;
	
	/**
	 * State class constructor.
	 * Define a value for the T type,
	 * @param value T type
	 * @return nothing.
	 */
	public State(T value){
		this.value = value;
	}
	
	
	/**
	 * get the T type object parent
	 * @return cameFrom T
	 */
	public State<T> getCameFrom() {
		return cameFrom;
	}



	/**
	 * Set the T type object parent
	 * @param cameFrom state
	 * @return nothing
	 */
	public void setCameFrom(State<T> cameFrom) {
		this.cameFrom = cameFrom;
	}



	/**
	 * Get the cost
	 * @return cost double
	 */
	public double getCost() {
		return cost;
	}



	/**
	 * Set the cost
	 * @param cost double
	 * @return nothing.
	 */
	public void setCost(double cost) {
		this.cost = cost;
	}



	/**
	 * get the T type object value
	 * @return value T
	 */
	public T getValue() {
		return value;
	}



	/**
	 * Set the T type object value
	 * @param value T
	 * @return nothing
	 */
	public void setValue(T value) {
		this.value = value;
	}



	/**
	 * get key data member
	 * @return key String
	 */
	public String getKey() {
		return key;
	}



	/**
	 * Set key data member
	 * @param key String
	 * @return nothing.
	 */
	public void setKey(String key) {
		this.key = key;
	}


	/**
	 * print the T object value
	 * @return value.toString()
	 */
	public String toString(){
		return value.toString();
	}
	
	
	/**
	 * Compare 2 states
	 * @return boolean
	 */
	public boolean equals(Object obj){
		State<T> s = (State<T>)obj;
		return s.value.equals(this.value);
	}
	
	
	/**
	 * Define a compare rule of comparing 2 states
	 * @return int
	 */
	@Override
	public int compareTo(State<T> s) {
		return (int)(this.getCost() - s.getCost());	
	}

}
