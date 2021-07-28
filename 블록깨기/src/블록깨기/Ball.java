package ��ϱ���;

import java.awt.Graphics;

import javax.swing.ImageIcon;

public class Ball extends ImageIcon {

	int x, y;
	int moveX, moveY = -4;
	private final int width = 25, height = 25;

	public Ball(String img, int x, int y) {
		super(img);
		this.x = x;
		this.y = y;

		// ���� �ʱ� ����(���� or �������� ������)
		// 0�̸� -1(����), 1�̸� 1(������)
		if ((int) (Math.random() * 2) == 0)
			this.moveX = -4;
		else
			this.moveX = 4;
	}

	public void move() {
		this.x += moveX;
		this.y += moveY;
	}

	public void draw(Graphics g) {
		g.drawImage(this.getImage(), this.x, this.y, this.width, this.height, null);
	}

}
