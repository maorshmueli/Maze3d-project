package algorithms.mazeGenarators;

/**
 * <h1>Maze3dGeneratorBase abstract class </h1> implements Maze3dGenerator
 * Define a way of generating the 3D maze
 * 
 * @author Maor Shmueli
 */

public abstract class Maze3dGeneratorBase implements Maze3dGenerator{

	/**
	 * generate the maze
	 * @param deepness int
	 * @param rows int
	 * @param columns int
	 * @return maze3d 
	 */
	public abstract Maze3d generate(int deepness , int rows , int columns);

	/**
	 * Measure generate algorithm time 
	 * @param deepness int
	 * @param rows int
	 * @param columns int
	 * @return String time measured
	 */
	public String measureAlgorithmTime(int deepness , int rows , int columns) {
		long startTime = System.currentTimeMillis();
		this.generate(deepness, rows , columns);
		long endTime = System.currentTimeMillis();
		return String.valueOf(endTime - startTime);
	}
	
	
}
