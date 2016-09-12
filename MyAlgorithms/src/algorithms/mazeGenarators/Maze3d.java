package algorithms.mazeGenarators;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * <h1>Maze3d class </h1>
 * Define a 3D maze parameters, an 3D int array,
 * sizes (rows, columns, deepness) and start and end positions
 * 
 * @author Maor Shmueli
 */

public class Maze3d {

	private int [][][] maze3d;
	private int rows;
	private int columns;
	private int deepness;
	private Position startPosition;
	private Position goalPosition;
	public static final int FREE = 0;
	public static final int WALL = 1;

	/**
	 * Maze3d class constructor.
	 * define the maze size by rows, columns, deepness
	 * and define the maze
	 * @param deepness
	 * @param rows
	 * @param columns
	 * @return nothing.
	 */
	public Maze3d(int deepness , int rows ,int columns) {
		this.rows = rows;
		this.columns = columns;
		this.deepness = deepness;
		this.maze3d = new int [deepness][rows][columns];
	}
	
	/**
	 * constructor that gets byte array and construct a maze3d from its bytes
	 * @param byteArr byte array
	 * @throws IOException 
	 */
	public Maze3d(byte[] byteArr) throws IOException
	{
		ByteArrayInputStream in=new ByteArrayInputStream(byteArr);
		DataInputStream dis=new DataInputStream(in);
		//creating a stream that reads primitive types easier
		
		this.deepness=dis.readInt();
		this.rows=dis.readInt();
		this.columns=dis.readInt();
		
		maze3d=new int[deepness][rows][columns];
		
		for(int i=0;i<deepness;i++)
		{
			for(int j=0;j<rows;j++)
			{
				for(int n=0;n<columns;n++)
				{
					maze3d[i][j][n]=dis.read();//reads byte
					
				}
			}
		}
		startPosition=new Position(dis.readInt(), dis.readInt(), dis.readInt());
		
		goalPosition=new Position(dis.readInt(), dis.readInt(), dis.readInt());
	}
	
	/**
	 * Get the maze
	 * @return maze3d
	 */
	public int[][][] getMaze3d() {
		return maze3d;
	}
	
	/**
	 * Set the maze
	 * @param maze3d
	 * @return nothing
	 */
	public void setMaze3d(int[][][] maze3d) {
		this.maze3d = maze3d;
	}
	
	/**
	 * Get the rows
	 * @return rows
	 */
	public int getRows() {
		return rows;
	}
	
	/**
	 * Set the rows
	 * @param rows
	 * @return nothing
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}

	/**
	 * Get the columns
	 * @return columns
	 */
	public int getColumns() {
		return columns;
	}
	
	/**
	 * Set the columns
	 * @param columns
	 * @return nothing
	 */
	public void setColumns(int columns) {
		this.columns = columns;
	}
	
	/**
	 * Get the deepness
	 * @return deepness
	 */
	public int getDeepness() {
		return deepness;
	}
	
	/**
	 * Set the deepness
	 * @param deepness
	 * @return nothing
	 */
	public void setDeepness(int deepness) {
		this.deepness = deepness;
	}
	
	/**
	 * Get the the start position of the maze
	 * @return startPosition
	 */
	public Position getStartPosition() {
		return startPosition;
	}
	
	/**
	 * Set the the start position of the maze
	 * @param startPosition
	 * @return nothing
	 */
	public void setStartPosition(Position startPosition) {
		this.startPosition = startPosition;
	}
	
	/**
	 * Get the the end position of the maze
	 * @return goalPosition
	 */
	public Position getGoalPosition() {
		return goalPosition;
	}
	
	/**
	 * Set the the end position of the maze
	 * @param goalPosition
	 * @return nothing
	 */
	public void setGoalPosition(Position goalPosition) {
		this.goalPosition = goalPosition;
	}
	
	/**
	 * set a wall at a specific maze position
	 * @param x int
	 * @param y int
	 * @param z int
	 * @return nothing
	 */
	public void setWall(int z,int y,int x)
	{
		maze3d[z][y][x] = 1;
	}
	
	/**
	 * set free or remove wall from a specific maze position
	 * @param x int
	 * @param y int
	 * @param z int
	 * @return nothing
	 */
	public void setfree(int z,int y,int x)
	{
		maze3d[z][y][x] = 0;
	}
	
	/**
	 * Print the maze
	 * @return StringBuilder 
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(int z = 0 ; z < deepness ; z++)
		{
			for(int y = 0 ; y < rows ; y++)
			{
				for(int x = 0 ; x < columns ; x++)
				{
					if(z == startPosition.z && y == startPosition.y && x == startPosition.x)
						sb.append("E");
					else if(z == goalPosition.z && y == goalPosition.y && x == goalPosition.x)
						sb.append("X");
					else
						sb.append(maze3d[z][y][x]);
				}
				sb.append("\n");
			}
			sb.append("\n");
		}
		return sb.toString();
	}


	/**
	 * set a value at a specific maze position
	 * @param deepness int
	 * @param rows int
	 * @param columns int
	 * @param num int
	 * @return nothing
	 */
	public void setCell(int deepness , int rows ,int columns, int num)
	{
		this.maze3d[deepness][rows][columns] = num;
	}

	
	/**
	 * fill all the maze with walls
	 * @return nothing
	 */
	public void allWall(){				
		for(int z = 0 ;z < this.deepness ; z++ ){
			for(int y = 0 ; y < this.rows ; y++){
				for(int x = 0 ; x < this.columns ; x++){
					this.maze3d[z][y][x] = 1;
						
	
				}
			}
		}
	
	}
	
	/**
	 * random a position on the sides of the maze
	 * @return p Position
	 */
	public Position randomSideCell()
	{
		int x ,y ,z;
		
		Random r = new Random();
	
		//Random goal position side
		int side = r.nextInt(6);
		
		z = r.nextInt(deepness - 2) + 1;
		y = r.nextInt(rows - 2) + 1;
		x = r.nextInt(columns - 2) + 1;
		
		
		switch (side) {
		
		case 0:
			z = 0;
			break;
		case 1:
			z = deepness - 1;
			break;
		case 2:
			y = 0;
			break;
		case 3:
			y = rows - 1;
			break;
		case 4:
			x = 0;
			break;
		case 5:
			x = columns - 1;
			break;
	
		default:
			break;
		}
		
		Position p = new Position(z , y, x);
		
		return p;
	}
	
	/**
	 * get a random inner cell
	 * @return p Position
	 */
	public Position getRandomCell()
	{
		Random r = new Random();
		
		int x , y , z;
		
		z = r.nextInt(deepness -1) + 1;
		y = r.nextInt(rows -1) + 1;
		x = r.nextInt(columns -1) + 1;
		
		Position p = new Position(z , x , y);
		
		return p;
		
	}

	//
	/**
	 * choose start and goal positions
	 * <b>note: </b>these start and goal are at the sides of the maze 
	 * @return nothing
	 */
	public void createStartGoal()
	{
		this.startPosition = randomSideCell();
		
		this.goalPosition = randomSideCell();
		
		//if same cell choose for start and goal - repeat steps. 
		if(goalPosition.z == startPosition.z && goalPosition.y == startPosition.y &&goalPosition.x == startPosition.x)
			this.createStartGoal();
	}

	/**
	 * removes a specific wall
	 * @param p Position
	 * @return nothing
	 */
	public void removeWall(Position p)
	{
		this.maze3d[p.z][p.y][p.x] = 0;
	}
	
	
	//
	/**
	 * return the closest cell to the entrance (not on sides)
	 * @return return_p Position
	 */
	public Position getFirstInnerCell()
	{
		int z = startPosition.z;
		int y = startPosition.y;
		int x = startPosition.x;
		
		if(z == 0)
			z ++;
		if(z == deepness - 1)
			z --;
		
		if(y == 0)
			y ++;
		if(y == rows - 1)
			y --;
		
		if(x == 0)
			x++;
		if(x == columns - 1)
			x --;
		
		Position return_p = new Position(z, y, x);
		
		return return_p;
		
	}
	

	/**
	 * check if the position is on the outer shell (on the sides)
	 * @param z int
	 * @param y int
	 * @param x int
	 * @return boolean
	 */
	public boolean isOnSides(int z , int y , int x)
	{
		return ((z == 0 || z == this.deepness - 1) || (y == 0 || y == this.rows - 1) || (x == 0 || x == this.columns - 1));
	}

	
	/**
	 * return 2D cross of the maze by Z
	 * @param z int
	 * @return cut int[][]
	 * @exception IndexOutOfBoundsException index out of range
	 * @see IndexOutOfBoundsException
	 */
	public int[][] getCrossSectionByZ(int z) throws IndexOutOfBoundsException
	{
		if(z >= 0 && z < this.deepness)
		{
			int[][] cut = new int[this.columns][this.rows];
			
			for(int x = 0 ; x < this.columns ; x++)
			{
				for(int y = 0 ; y < this.rows ; y++)
				{
					cut[x][y] = this.maze3d[z][y][x];
				}
			}
			
			return cut;
		}
		
		else {
			throw new IndexOutOfBoundsException();
		}
		
	}
	
	/**
	 * return 2D cross of the maze by Y
	 * @param y int
	 * @return cut int[][]
	 * @exception IndexOutOfBoundsException index out of range
	 * @see IndexOutOfBoundsException
	 */
		public int[][] getCrossSectionByY(int y) throws IndexOutOfBoundsException
		{
			if(y >= 0 && y < this.rows)
			{
				int[][] cut = new int[this.deepness][this.columns];
				
				for(int z = 0 ; z < this.deepness ; z++)
				{
					for(int x = 0 ; x < this.columns ; x++)
					{
						cut[z][x] = this.maze3d[z][y][x];
					}
				}
				
				return cut;
			}
			
			else {
				throw new IndexOutOfBoundsException();
			}
			
		}
		
		/**
		 * return 2D cross of the maze by X
		 * @param x int
		 * @return cut int[][]
		 * @exception IndexOutOfBoundsException index out of range
		 * @see IndexOutOfBoundsException
		 */
		public int[][] getCrossSectionByX(int x) throws IndexOutOfBoundsException
		{
			if(x >= 0 && x < this.columns)
			{
				int[][] cut = new int[this.rows][this.deepness];
				
				for(int y = 0 ; y < this.rows ; y++)
				{
					for(int z = 0 ; z < this.deepness ; z++)
					{
						cut[y][z] = this.maze3d[z][y][x];
					}
				}
				
				return cut;
			}
			
			else {
				throw new IndexOutOfBoundsException();
			}
			
		}
		
		/**
		 * return all possible move for a specific position(the max is 6)
		 * @param p Position
		 * @return directions ArrayList<Position>
		 */
		public ArrayList<Position> getPossibleMoves(Position p )
		{
			
			ArrayList<Position> directions = new ArrayList<Position>();
			
			//Z
			//if next step is empty and player next step is not outside of the maze.
			if((p.z +1 <= this.deepness -1) && (this.maze3d[p.z + 1][p.y][p.x] == 0))
			{
				directions.add(new Position(p.z + 1, p.y , p.x));
			}
			
			if((p.z -1 >= 0) && (this.maze3d[p.z - 1][p.y][p.x] == 0))
			{
				directions.add(new Position(p.z - 1, p.y , p.x));
			}
			
			//Y
			if((p.y +1 <= this.rows -1) && (this.maze3d[p.z][p.y + 1][p.x] == 0))
			{
				directions.add(new Position(p.z, p.y + 1 , p.x));
			}
			
			if((p.y -1 >= 0) && (this.maze3d[p.z][p.y - 1][p.x] == 0))
			{
				directions.add(new Position(p.z, p.y - 1 , p.x));
			}
			
			//X
			if((p.x +1 <= this.columns -1) && (this.maze3d[p.z][p.y][p.x + 1] == 0))
			{
				directions.add(new Position(p.z, p.y , p.x + 1));
			}
			
			if((p.x -1 >= 0) && (this.maze3d[p.z][p.y][p.x - 1] == 0))
			{
				directions.add(new Position(p.z, p.y , p.x - 1));
			}
					
			
			
			return directions;
			
		}
		
		/**
		 * get a position and return all its possible free moves by string
		 * @param p Position
		 * @return possibleMoves String[]
		 */
		public String[] getPossibleMovesString(Position p )
		{
			ArrayList<String> directions = new ArrayList<String>();
			
			//Z
			//if next step is empty and player next step is not outside of the maze.
			if((p.z +1 <= this.deepness -1) && (this.maze3d[p.z + 1][p.y][p.x] == 0))
			{
				directions.add("Forward");
			}
			
			if((p.z -1 >= 0) && (this.maze3d[p.z - 1][p.y][p.x] == 0))
			{
				directions.add("Backward");
			}
			
			//Y
			if((p.y +1 <= this.rows -1) && (this.maze3d[p.z][p.y + 1][p.x] == 0))
			{
				directions.add("Up");
			}
			
			if((p.y -1 >= 0) && (this.maze3d[p.z][p.y - 1][p.x] == 0))
			{
				directions.add("Down");
			}
			
			//X
			if((p.x +1 <= this.columns -1) && (this.maze3d[p.z][p.y][p.x + 1] == 0))
			{
				directions.add("Right");
			}
			
			if((p.x -1 >= 0) && (this.maze3d[p.z][p.y][p.x - 1] == 0))
			{
				directions.add("Left");
			}
					
			//create possibleMoves array.
			String[] possibleMoves = new String[directions.size()];
			for(int i = 0 ; i< directions.size() ; i++)
			{
				possibleMoves[i] = directions.get(i);
			}
			
			return possibleMoves;
			
		}

		/**
		 * print a 2d array
		 * @param array int[][]
		 * @return sb.toString() 
		 */
		public String print2DMaze(int[][] array) {
			StringBuilder sb = new StringBuilder();
			for(int i = 0 ; i < array.length ; i++)
			{
				for(int j = 0 ; j < array[0].length ; j++)
				{
					sb.append(array[i][j]);
				}
				sb.append("\n");
				
			}

			return sb.toString();
		}
		



	/**
	 * returning all the maze3d data converted to byte array
	 * format:
	 * 4 bytes of size of x axis,4 bytes of size of y axis,4 bytes of size of z axis,
	 * all the cells of maze 3d matrix,each one as byte,
	 * the start position:3 integers represented by 4 bytes each
	 * the goal position :3 integers represented by 4 bytes each
	 * 
	 * @return byte array with the maze details
	 * @throws IOException 
	 */
	public byte[] toByteArray() 
	{
		//creating a stream that reads primitive types easier
		ByteArrayOutputStream bb=new ByteArrayOutputStream();
		DataOutputStream dis=new DataOutputStream(bb);
		try {
			dis.writeInt(deepness);
	
			dis.writeInt(rows);
			dis.writeInt(columns);
			for(int i=0;i<deepness;i++)
			{
				for(int j=0;j<rows;j++)
				{
					for(int n=0;n<columns;n++)
					{
						dis.write(maze3d[i][j][n]);
					}
				}
			}
			dis.writeInt(startPosition.z);
			dis.writeInt(startPosition.y);
			dis.writeInt(startPosition.x);
	
			dis.writeInt(goalPosition.z);
			dis.writeInt(goalPosition.y);
			dis.writeInt(goalPosition.x);
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}	
		return bb.toByteArray();
	}
	
	
	/**
	 * checks that two mazes are equal
	 * return true if equals otherwise return false
	 */
	public boolean equals(Object obj)
	{

		if(obj==null)
		{
			return false;
		}
		if(this==obj)
		{
			return true;
		}
		if(!(obj instanceof Maze3d))
		{
			return false;
		}
		//we sure that obj is a maze
		Maze3d other=(Maze3d)obj;
		
		if(this.deepness!=other.deepness)
		{
			return false;
		}
		if(this.rows!=other.rows)
		{
			return false;
		}
		if(this.columns!=other.columns)
		{
			return false;
		}
		
		for(int i=0;i<deepness;i++)
		{
			for(int j=0;j<rows;j++)
			{
				for(int n=0;n<columns;n++)
				{
					if(this.maze3d[i][j][n]!=other.getMaze3d()[i][j][n])
					{
						return false;
					}
				}
			}
		}
		if(this.startPosition.equals(other.startPosition)==false)
		{
			return false;
		}
		if(this.goalPosition.equals(other.goalPosition)==false)
		{
			return false;
		}
		return true;
	}
}