package ºí·Ï±ú±â;

import java.awt.Graphics;
import java.awt.Point;

import javax.swing.ImageIcon;

public class Stick extends ImageIcon {
	int x, width, height;
	private final int y = 700;

	public Stick(String img, int x, int width, int height) {
		super(img);
		this.x = x;
		this.width = width;
		this.height = height;
	}

	public void draw(Graphics g) {
		g.drawImage(this.getImage(), this.x, this.y, width, height, null);
	}
}
