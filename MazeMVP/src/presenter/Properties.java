package presenter;

import java.io.Serializable;

import javax.xml.bind.annotation.*;

/**
 * Properties class implements Serializable
 * use for storing the Xml file properties as an object
 * @author Maor Shmueli
 *
 */
@XmlRootElement
public class Properties implements Serializable{

	
	private int threadsNumber;
	private String searchAlgorithm;
	private String generateMazeAlgorithm;
	private String gameType;
	
	/**
	 * Properties's using fields constructor
	 * @param threadsNumber number of threads
	 * @param searchAlgorithm search algorithm  
	 * @param generateMazeAlgorithm maze generating algorithm
	 * @param gameType game interface CLI/GUI
	 */
	public Properties(int threadsNumber, String searchAlgorithm, String generateMazeAlgorithm , String gameType) {
		this.threadsNumber = threadsNumber;
		this.searchAlgorithm = searchAlgorithm;
		this.generateMazeAlgorithm = generateMazeAlgorithm;
		this.gameType = gameType;
	}
	
	/**
	 * Properties's default constructor with default values
	 */
	public Properties(){
		super();
		this.threadsNumber = 10;
		this.searchAlgorithm = "bfs";
		this.generateMazeAlgorithm = "simple";
		this.gameType = "CLI";
	}
	
	/**
	 * Properties copy constructor
	 * @param p
	 */
	public Properties(Properties p) {
		this.threadsNumber = p.getThreadsNumber();
		this.searchAlgorithm = p.getSearchAlgorithm();
		this.generateMazeAlgorithm = p.getGenerateMazeAlgorithm();
		this.gameType = p.gameType;
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
	@XmlElement
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
	@XmlElement
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
	@XmlElement
	public void setGenerateMazeAlgorithm(String generateMazeAlgorithm) {
		this.generateMazeAlgorithm = generateMazeAlgorithm;
	}

	/**
	 * return the game type CLI/GUI
	 * @return generateMazeAlgorithm
	 */
	@XmlElement
	public String getGameType() {
		return gameType;
	}

	/**
	 * set the game type CLI/GUI
	 * @param generateMazeAlgorithm
	 */
	public void setGameType(String gameType) {
		this.gameType = gameType;
	}
	
	
}
