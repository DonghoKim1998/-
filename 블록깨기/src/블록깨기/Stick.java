package ºí·Ï±ú±â;

import java.awt.Graphics;
import java.awt.Point;

import javax.swing.ImageIcon;

public class Stick extends ImageIcon {
	int x;
	public int width = 90, height = 15, y = 690;

	public Stick(String img, int x) {
		super(img);
		this.x = x;
	}

	public Point getPoint() {
		return new Point(this.x, this.y);
	}

	public void draw(Graphics g) {
		g.drawImage(this.getImage(), this.x, this.y, width, height, null);
	}
}
