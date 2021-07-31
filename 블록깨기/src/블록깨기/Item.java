package ��ϱ���;

import java.awt.Graphics;
import java.awt.Point;

import javax.swing.ImageIcon;

public class Item extends ImageIcon {
	int x, y, type;
	final int width = 25, height = 25;
	ImageIcon itemImg;

	String ballMinus = "res/items/ballMinus.png";
	String ballPlus = "res/items/ballPlus.png";
	String stickMinus = "res/items/stickMinus.png";
	String stickPlus = "res/items/stickPlus.png";

	public Item(Point p, int type, Stick stick) {
		this.x = p.x;
		this.y = p.y;
		this.type = type;

		// type�� ���� ������ ����
		switch (type) {
		case 0:
			this.itemImg = new ImageIcon(ballMinus);
			break;
		case 1:
			this.itemImg = new ImageIcon(ballPlus);
			break;
		case 2:
			this.itemImg = new ImageIcon(stickMinus);
			break;
		case 3:
			this.itemImg = new ImageIcon(stickPlus);
			break;
		}
		this.setImage(itemImg.getImage());
	}

	public void itemDrop() {
		this.y += 3;
	}

	public void draw(Graphics g) {
		g.drawImage(this.getImage(), x, y, width, height, null);
	}
}
