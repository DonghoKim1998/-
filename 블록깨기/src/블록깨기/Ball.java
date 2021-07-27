package ºí·Ï±ú±â;

import javax.swing.ImageIcon;

public class Ball extends ImageIcon {

	int x, y, width, height;

	public Ball(String img, int x, int y, int width, int height) {
		super(img);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

}
