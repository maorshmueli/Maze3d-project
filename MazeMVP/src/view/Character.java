package view;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;

import algorithms.mazeGenarators.Position;

public class Character {

	private Position pos;
	private Image img;
	
	public Character() {
		this.img = new Image(null, "resources/images/android.png");
	}

	public Position getPos() {
		return pos;
	}

	public void setPos(Position pos) {
		this.pos = pos;
	}
	
	public void draw(int cellWidth , int cellHight, GC gc){
		gc.drawImage(img, 0, 0,img.getBounds().width,img.getBounds().height,cellWidth * pos.x, cellHight * pos.y , cellWidth , cellHight);
	}
	
	public void moveRight() {
		pos.x ++;
	}
}
