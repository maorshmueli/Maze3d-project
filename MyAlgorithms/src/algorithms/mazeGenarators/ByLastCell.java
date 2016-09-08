package algorithms.mazeGenarators;

import java.util.ArrayList;
import java.util.Random;


//used to get the last cell from a ArrayList.
public class ByLastCell implements Select{

	@Override
	public Position getNextCell(ArrayList<Position> cells) {
		return cells.get(cells.size() -1);
	}
	
	
	
}

