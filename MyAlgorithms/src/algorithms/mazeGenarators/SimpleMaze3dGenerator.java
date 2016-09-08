package algorithms.mazeGenarators;

import java.util.Random;

public class SimpleMaze3dGenerator extends Maze3dGeneratorBase{

	@Override
	public Maze3d generate(int deepness , int rows ,int columns) {
		
		Maze3d maze = new Maze3d(deepness, 2*rows +1, 2*columns +1);
		Random r = new Random();
		int num;
		
		maze.allWall();   //fill the maze with walls
		
		//loop all the maze without the outer shell(the sides)
		for(int z = 1 ;z < maze.getDeepness() -1 ; z++ ){
			for(int y = 1 ; y < maze.getRows() -1 ; y++){
				for(int x = 1 ; x < maze.getColumns() -1 ; x++){	
					num = r.nextInt(2);
					maze.setCell(z , y , x , num);

				}
			}
		}
		
		//create start and goal points for the maze
		maze.createStartGoal();
		
		//put a hole if the entrance and exit (start and goal)
		maze.removeWall(maze.getStartPosition());
		maze.removeWall(maze.getGoalPosition());
		
		//remove the first wall if exist after the start position.
		Position p = maze.getFirstInnerCell();
		maze.removeWall(p);
		
		//randomize directions
		int direction;
		
		//backup position for going step back.
		Position backup_p = new Position(p);
		

		
		//while the didn't reached the goal position
		while(!p.equals(maze.getGoalPosition()))
		{
			direction = r.nextInt(3); //3 possible moves on x line , on y line or on z line
			
			//backup position
			backup_p.z = p.z;
			backup_p.y = p.y;
			backup_p.x = p.x;
			
			
			//check which direction choose
			switch (direction) {
			case 0:	
				if(p.z > maze.getGoalPosition().z && p.z > 0)
					p.z --;
				if(p.z < maze.getGoalPosition().z && p.z < maze.getDeepness())
					p.z ++;
				
				break;
			case 1:
				if(p.y > maze.getGoalPosition().y && p.y > 0)
					p.y --;
				if(p.y < maze.getGoalPosition().y && p.y < maze.getRows())
					p.y ++;
				break;
			case 2:
				if(p.x > maze.getGoalPosition().x && p.x > 0)
					p.x --;
				if(p.x < maze.getGoalPosition().x && p.x < maze.getColumns())
					p.x ++;
				break;

			default:
				break;
			}
			
			//check if on side but not reached the goal, in this case - go step back.
			if(maze.isOnSides(p.z,p.y,p.x) )
			{
				if (!p.equals(maze.getGoalPosition()))
				{
				//go step back (restore)
				p.z = backup_p.z;
				p.y = backup_p.y;
				p.x = backup_p.x;
				}
			}
				
			
			else  //else remove the wall is exists
			{
				//create a way from start to goal
				maze.removeWall(p);
			}
		
			
			
		}
		
		return maze;
	}

	
}
