package 블록깨기;

import java.awt.Graphics;
import java.awt.Point;

import javax.swing.ImageIcon;

public class Ball extends ImageIcon {
	Stick stick = Main.stick;

	int x, y;
	int moveX, moveY = -7; // 20까지는 인식 가능
	private final int width = 22, height = 22;

	public Ball(String img, int x, int y) {
		super(img);
		this.x = x;
		this.y = y;

		// 공의 초기 방향(왼쪽 or 오른쪽을 정해줌)
		// 0이면 -1(왼쪽), 1이면 1(오른쪽)
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

	// 벽/Block/Stick과 부딪혔는지 판단하며
	// 부딪히면 방향(moveX/moveY)를 바꿔주는 메소드
	public void isCrash() {
		if (this.x <= 0 || this.x >= Main.FRAME_WIDTH - 45)
			this.moveX *= -1;
		else if (this.y <= 0)
			this.moveY *= -1;
		// stick과 부딪혔는지 판단하여 true인 경우 stick의 위치에 따라 다르게 튀김
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

	// 공이 deadLine까지 떨어졌는지 판단하는 메소드
	public boolean isFall() {
		if (this.y >= Main.DEADLINE_HEIGHT - 20)
			return false;
		else
			return true;
	}

	// 공이 stick에 부딪혔는지 판단하는 메소드
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
