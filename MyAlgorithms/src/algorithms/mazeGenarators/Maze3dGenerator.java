package algorithms.mazeGenarators;

/**
 * <h1>Maze3dGenerator interface </h1>
 * Define a interface of way to generating the 3D maze
 * 
 * @author Maor Shmueli
 */

public interface Maze3dGenerator {
	
	/**
	 * generate the maze
	 * @param deepness int
	 * @param rows int
	 * @param columns int
	 * @return maze3d 
	 */
	Maze3d generate(int deepness , int rows , int columns);
	
	/**
	 * Measure generate algorithm time 
	 * @param deepness int
	 * @param rows int
	 * @param columns int
	 * @return String time measured
	 */
	String measureAlgorithmTime(int deepness , int rows , int columns);
	
}
