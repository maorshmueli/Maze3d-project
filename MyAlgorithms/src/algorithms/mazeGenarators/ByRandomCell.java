package algorithms.mazeGenarators;

import java.util.ArrayList;
import java.util.Random;

//used to get a random cell from a ArrayList.
public class ByRandomCell implements Select{

	@Override
	public Position getNextCell(ArrayList<Position> cells) {
		Random r = new Random();
		return cells.get(r.nextInt(cells.size()));
	}
	
	
	
}


