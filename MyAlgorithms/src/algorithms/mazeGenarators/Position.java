package algorithms.mazeGenarators;

import java.io.Serializable;

/**
 * <h1>Position class </h1>
 * Define a position object identified by 3 indexs
 * 
 * @author Maor Shmueli
 */
public class Position implements Serializable {


	private static final long serialVersionUID = 1L;
	public int z;
	public int y;
	public int x;
	
	/**
	 * Position class constructor.
	 * define values for z , y , x data members
	 * @param z int
	 * @param y int
	 * @param z int
	 * @return nothing.
	 */
	public Position(int z,int y,int x) {
	
		this.z = z;
		this.y = y;
		this.x = x;
	}
	
	/**
	 * Position class copy constructor.
	 * copy z , y , x values from cp to the data members
	 * @param cp Position
	 * @return nothing.
	 */
	public Position(Position cp) {
		
		this.z = cp.z;
		this.y = cp.y;
		this.x = cp.x;
	}
	
	
	/**
	 * Print the position
	 * @return string
	 */
	@Override
	public String toString() {
		return "(" + z + "," + y + "," + x + ")";
	}
	
	/**
	 * Compare 2 positions
	 * @exception IllegalArgumentException if not a position object
	 * @return boolean
	 */
	public boolean equals(Object obj)
	{
		if(!(obj instanceof Position))
			throw new IllegalArgumentException("Not a Position object!");
		
		Position pos = (Position)obj;
		return (this.z == pos.z && this.y == pos.y && this.x == pos.x);
	}
}
