package ��ϱ���;

import java.awt.Graphics;
import java.awt.Point;

import javax.swing.ImageIcon;

public class Ball extends ImageIcon {
	Stick stick;
	int x, y;
	int moveX, moveY; // 20������ �ν� ����
	final int width = 22, height = 22;
	int speed = 8; // �ʱ� ���ǵ�: 8

	public Ball(String img, int x, int y, Stick stick) {
		super(img);
		this.x = x;
		this.y = y;
		this.stick = stick;

		// ���� �ʱ� ����(���� or �������� ������)
		// 0�̸� -1(����), 1�̸� 1(������)
		if ((int) (Math.random() * 2) == 0)
			this.moveX = -speed;
		else
			this.moveX = speed;
		moveY = -speed;
	}

	public boolean move() {
		isCrash();
		if (!isFall())
			return false;

		this.x += moveX;
		this.y += moveY;

		return true;
	}

	// ��/Block/Stick�� �ε������� �Ǵ��ϸ�
	// �ε����� ����(moveX/moveY)�� �ٲ��ִ� �޼ҵ�
	public void isCrash() {
		// �������� ���� ��� ���ֱ�
		if (Main.FRAME_WIDTH - 30 < this.x && this.x <= Main.FRAME_WIDTH)
			this.moveX = -6;

		if (this.x <= 0 || this.x >= Main.FRAME_WIDTH - 45)
			this.moveX *= -1;
		if (this.y <= 0)
			this.moveY *= -1;

		// stick�� �ε������� �Ǵ��Ͽ� true�� ��� stick�� ��ġ�� ���� �ٸ��� Ƣ��
		if (isCollisionWithStick()) {
			if (stick.x - 12 < this.x && this.x <= stick.x)
				moveX = -speed;
			if (stick.x < this.x && this.x <= stick.x + 12)
				moveX = -speed;
			if (stick.x + 12 < this.x && this.x < stick.x + 24)
				moveX = -(speed / 2);
			if (stick.x + 24 < this.x && this.x <= stick.x + 36)
				moveX = -1;
			if (stick.x + 36 < this.x && this.x <= stick.x + 48)
				moveX = 0;
			if (stick.x + 48 < this.x && this.x <= stick.x + 60)
				moveX = 1;
			if (stick.x + 60 < this.x && this.x <= stick.x + 72)
				moveX = speed / 2;
			if (stick.x + 72 < this.x && this.x <= stick.x + 84)
				moveX = speed;

			moveY *= -1;
		}
	}

	// ���� deadLine���� ���������� �Ǵ��ϴ� �޼ҵ�
	public boolean isFall() {
		if (this.y >= Main.DEADLINE_HEIGHT - 20)
			return false;
		else
			return true;
	}

	// ���� stick�� �ε������� �Ǵ��ϴ� �޼ҵ�
	public boolean isCollisionWithStick() {
		if ((stick.x - 11 <= this.x && this.x <= stick.x + stick.width + 11) && (670 < this.y && this.y <= 690))
			return true;
		else
			return false;
	}

	public double getDistance(Point p) {
		return Math.sqrt(Math.pow(this.x - p.x, 2) + Math.pow(this.y - p.y, 2));
	}

	public Point getCenterPoint() {
		return new Point(this.x + this.width / 2, this.y + this.height / 2);
	}

	public int getCenterX() {
		return this.x + this.width / 2;
	}

	public int getCenterY() {
		return this.y + this.height / 2;
	}

	public void draw(Graphics g) {
		g.drawImage(this.getImage(), this.x, this.y, this.width, this.height, null);
	}
}
