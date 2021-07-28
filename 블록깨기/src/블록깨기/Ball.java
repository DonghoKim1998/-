package ��ϱ���;

import java.awt.Graphics;
import java.awt.Point;

import javax.swing.ImageIcon;

public class Ball extends ImageIcon {
	Stick stick = Main.stick;

	int x, y;
	int moveX, moveY = -7; // 20������ �ν� ����
	private final int width = 22, height = 22;

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
		if (this.x <= 0 || this.x >= Main.FRAME_WIDTH - 45)
			this.moveX *= -1;
		else if (this.y <= 0)
			this.moveY *= -1;
		// stick�� �ε������� �Ǵ��Ͽ� true�� ��� stick�� ��ġ�� ���� �ٸ��� Ƣ��
		else if (isCollisionWithStick()) {
			if (stick.x + 5 - 11 <= this.x && this.x < stick.x + 15 - 11)
				moveX = -5;
			if (stick.x + 15 - 11 <= this.x && this.x < stick.x + 25 - 11)
				moveX = -4;
			if (stick.x + 25 - 11 <= this.x && this.x < stick.x + 35 - 11)
				moveX = -3;
			if (stick.x + 35 - 11 <= this.x && this.x < stick.x + 45 - 11)
				moveX = -2;
			if (stick.x + 45 - 11 <= this.x && this.x < stick.x + 55 - 11)
				moveX = -1;
			if (stick.x + 55 - 11 <= this.x && this.x < stick.x + 65 - 11)
				moveX = 1;
			if (stick.x + 65 - 11 <= this.x && this.x < stick.x + 75 - 11)
				moveX = 2;
			if (stick.x + 75 - 11 <= this.x && this.x < stick.x + 85 - 11)
				moveX = 3;
			if (stick.x + 85 - 11 <= this.x && this.x < stick.x + 95 - 11)
				moveX = 4;
			if (stick.x + 95 - 11 <= this.x && this.x < stick.x + 105 - 11)
				moveX = 5;

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
		if ((stick.x - 5 <= this.x && this.x <= stick.x + stick.width + 5) && (670 < this.y && this.y <= 690))
			return true;
		else
			return false;
	}

	public double getDistance(Point p) {
		return Math.sqrt(Math.pow(this.x - p.x, 2) + Math.pow(this.y - p.y, 2));
	}

	public void draw(Graphics g) {
		g.drawImage(this.getImage(), this.x, this.y, this.width, this.height, null);
	}

}
