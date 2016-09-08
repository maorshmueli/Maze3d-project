package algorithms.mazeGenarators;

import java.util.ArrayList;

//use for select the way to choose cells from a ArrayList.
public interface Select {

	public Position getNextCell(ArrayList<Position> cells);
}
