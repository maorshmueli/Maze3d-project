package algorithms.mazeGenarators;

import java.util.ArrayList;
import java.util.Random;


public class GrowingTreeGenerator extends Maze3dGeneratorBase{

	private Random r = new Random();
	private Select selection;
	
	public GrowingTreeGenerator(Select selection) {
		this.selection = selection;
	}
	@Override
	public Maze3d generate(int deepness, int rows, int columns) {
		
		//maze creation
		Maze3d maze = new Maze3d(deepness, 2*rows +1, 2*columns +1);
		
		
		maze.allWall();  //fill the maze with walls.
		
		ArrayList<Position> C = new ArrayList<>();
		
		//choose a random start position for the maze.
		maze.setStartPosition(randomPosition(maze));
		
		
		//put a hole if the entrance and exit (start and goal)
		maze.removeWall(maze.getStartPosition());
		//DEBUG maze.removeWall(maze.getGoalPosition());
		
		C.add(maze.getStartPosition());  //insert the first cell to the list.
		
		while(!C.isEmpty())
		{
			//get the last/random cell from the list - used Strategy Design Pattern.
			Position p = selection.getNextCell(C);
			
			//find all the unvisited neighbors of the cell.
			ArrayList<Position> neighbors = findUnvisitedNeighbors(maze, p);
			
			if(!neighbors.isEmpty()) {
				//choose a random neighbor.
				int num = r.nextInt(neighbors.size());
				Position neighbor = neighbors.get(num);
				
				//make a path between the cell and his neighbor.
				createPath(maze, p, neighbor);
				C.add(neighbor);
			}
			
			else {
				//remove from C if cell have no neighbors.
				C.remove(p);
			}
			
			
		}
		
		//random a goal position.
		Position goal = chooseRandomGoal(maze);
		//set the goal position.
		maze.setGoalPosition(goal);
		
		return maze;
	}
	
	
	//find all possible neighbors of a specific cell.
	private ArrayList<Position> findUnvisitedNeighbors(Maze3d maze, Position p)
	{
		int[][][] m = maze.getMaze3d();
		ArrayList<Position> neighbors = new ArrayList<Position>(); //neighbors array.
		
		//identify the neighbor.
		//Z
		if(p.z - 1 > 0 ) {
			if((m[p.z - 1][p.y][p.x] == Maze3d.WALL) && !(maze.isOnSides(p.z -1, p.y, p.x))){
				neighbors.add(new Position(p.z - 1,p.y , p.x));
			}
		}
		if(p.z + 1 < maze.getDeepness() -1) {
			if((m[p.z + 1][p.y][p.x] == Maze3d.WALL) && !(maze.isOnSides(p.z +1, p.y, p.x))) {
				neighbors.add(new Position(p.z + 1,p.y , p.x));
			}
		}
		
		
		//Y
		if(p.y - 2 >= 0 ) {
			if((m[p.z][p.y - 2][p.x] == Maze3d.WALL) && !(maze.isOnSides(p.z, p.y -2, p.x))) {
				neighbors.add(new Position(p.z,p.y - 2 , p.x));
			}
		}
		if(p.y + 2 < maze.getRows()) {
			if((m[p.z][p.y + 2][p.x] == Maze3d.WALL) && !(maze.isOnSides(p.z, p.y +2, p.x))) {
				neighbors.add(new Position(p.z,p.y + 2 , p.x));
			}
		}
		
		//X
		if(p.x - 2 >= 0 ) {
			if((m[p.z][p.y][p.x - 2] == Maze3d.WALL) && !(maze.isOnSides(p.z, p.y -2, p.x))) {
				neighbors.add(new Position(p.z,p.y , p.x - 2));
			}
		}
		if(p.x + 2 < maze.getColumns()) {
			if((m[p.z][p.y][p.x + 2] == Maze3d.WALL) && !(maze.isOnSides(p.z, p.y +2, p.x))) {
				neighbors.add(new Position(p.z,p.y , p.x + 2));
			}
		}
		
		
		return neighbors;
	}
	
	//create path between cell and his neighbor.
	private void createPath(Maze3d maze , Position p , Position neighbor)
	{
		//Z
		if(neighbor.z == p.z + 1){
			maze.removeWall(new Position(p.z + 1 , p.y , p.x));
			//maze.removeWall(new Position(p.z + 2 , p.y , p.x));
		}
		else if(neighbor.z == p.z - 1){
			maze.removeWall(new Position(p.z - 1 , p.y , p.x));
			//maze.removeWall(new Position(p.z - 2 , p.y , p.x));
		}
		
		//Y
		else if(neighbor.y == p.y + 2){
			maze.removeWall(new Position(p.z , p.y + 1 , p.x));
			maze.removeWall(new Position(p.z , p.y + 2 , p.x));
		}
		else if(neighbor.y == p.y - 2){
			maze.removeWall(new Position(p.z , p.y - 1 , p.x));
			maze.removeWall(new Position(p.z , p.y - 2 , p.x));
		}
		
		//X
		else if(neighbor.x == p.x + 2){
			maze.removeWall(new Position(p.z , p.y , p.x + 1));
			maze.removeWall(new Position(p.z , p.y , p.x + 2));
		}
		else if(neighbor.x == p.x - 2){
			maze.removeWall(new Position(p.z , p.y , p.x - 1));
			maze.removeWall(new Position(p.z , p.y , p.x - 2));
		}
		
	}
	
	//find a goal cell that is free and on one of the sides of the maze.
	private Position chooseRandomGoal(Maze3d maze)
	{
		int z = r.nextInt(maze.getDeepness());
		int y = r.nextInt(maze.getRows());
		int x = r.nextInt(maze.getColumns());
		
		
		while(maze.getMaze3d()[z][y][x] == Maze3d.WALL /*&& !(maze.isOnSides(goal))*/)
		{
			z = r.nextInt(maze.getDeepness());
			y = r.nextInt(maze.getRows());
			x = r.nextInt(maze.getColumns());
		}
		
		return new Position(z,y,x);
	}

	
	//random a position.
	private Position randomPosition(Maze3d maze)
	{
		int z = 0, y = 0, x = 0;
		
		z = r.nextInt(maze.getDeepness());
		while(y % 2 != 1) { y = r.nextInt(maze.getRows());}
		while(x % 2 != 1) { x = r.nextInt(maze.getColumns());}
		
		return new Position(z,y,x);
		
	}
}

