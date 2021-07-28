package ºí·Ï±ú±â;

import java.awt.Graphics;
import java.awt.Point;

import javax.swing.ImageIcon;

public class Stick extends ImageIcon {
	int x;
	private final int width = 90, height = 80, y = 690;

	public Stick(String img, int x) {
		super(img);
		this.x = x;
	}

	public void draw(Graphics g) {
		g.drawImage(this.getImage(), this.x, this.y, width, height, null);
	}
}
