package presenter;

import java.io.Serializable;

public class Properties implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int threadsNumber;
	private String searchAlgorithm;
	private String generateMazeAlgorithm;
	
	/**
	 * Properties's using fields constructor
	 * @param threadsNumber number of threads
	 * @param searchAlgorithm search algorithm  
	 * @param generateMazeAlgorithm maze generating algorithm
	 */
	public Properties(int threadsNumber, String searchAlgorithm , String generateMazeAlgorithm) {
		super();
		this.threadsNumber = threadsNumber;
		this.searchAlgorithm = searchAlgorithm;
		this.generateMazeAlgorithm = generateMazeAlgorithm;
	}
	
	/**
	 * Properties's default constructor with default values
	 */
	public Properties(){
		super();
		this.threadsNumber = 10;
		this.searchAlgorithm = "bfs";
		this.generateMazeAlgorithm = "simple";
	}
	
	public Properties(Properties p) {
		this.threadsNumber = p.getThreadsNumber();
		this.searchAlgorithm = p.getSearchAlgorithm();
		this.generateMazeAlgorithm = p.getGenerateMazeAlgorithm();
	}
	
	/**
	 * return the number of threads
	 * @return threadsNumber
	 */
	public int getThreadsNumber() {
		return threadsNumber;
	}
	
	/**
	 * set the number of threads
	 * @param threadsNumber
	 */
	public void setThreadsNumber(int threadsNumber) {
		this.threadsNumber = threadsNumber;
	}
	
	/**
	 * return the search algorithm
	 * @return searchAlgorithm
	 */
	public String getSearchAlgorithm() {
		return searchAlgorithm;
	}
	
	/**
	 * set the search algorithm
	 * @param searchAlgorithm 
	 */
	public void setSearchAlgorithm(String searchAlgorithm) {
		this.searchAlgorithm = searchAlgorithm;
	}
	
	/**
	 * return the maze generate algorithm
	 * @return generateMazeAlgorithm
	 */
	public String getGenerateMazeAlgorithm() {
		return generateMazeAlgorithm;
	}
	
	/**
	 * set the maze generating algorithm
	 * @param generateMazeAlgorithm
	 */
	public void setGenerateMazeAlgorithm(String generateMazeAlgorithm) {
		this.generateMazeAlgorithm = generateMazeAlgorithm;
	}
	
	
}
