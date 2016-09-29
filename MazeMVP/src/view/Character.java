package view;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;

import algorithms.mazeGenarators.Position;

/**
 * Character class
 * define the character by position and image
 * @author Maor Shmueli
 *
 */
public class Character {

	private Position pos;
	private Image img;
	
	/**
	 * Character default constructor
	 */
	public Character() {
		this.img = new Image(null, "resources/images/character.png");
	}

	/**
	 * get the position
	 * @return position
	 */
	public Position getPos() {
		return pos;
	}

	/**
	 * set the position
	 * @param pos position
	 */
	public void setPos(Position pos) {
		this.pos = pos;
	}
	
	/**
	 * draw character image
	 * @param cellWidth int
	 * @param cellHight int 
	 * @param gc GC
	 */
	public void draw(int cellWidth , int cellHight, GC gc){
		gc.drawImage(img, 0, 0,img.getBounds().width,img.getBounds().height,cellWidth * pos.x, cellHight * pos.y , cellWidth , cellHight);
	}
	
}
