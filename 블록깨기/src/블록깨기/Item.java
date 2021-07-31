package ºí·Ï±ú±â;

import java.awt.Graphics;

import javax.swing.ImageIcon;

public class Item extends ImageIcon {
	int x, y;
	int width, height;

	public Item(String img, int x, int y, int width, int height) {
		super(img);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void draw(Graphics g) {
		g.drawImage(this.getImage(), x, y, width, height, null);
	}
}
