package 블록깨기;

import java.awt.Graphics;
import java.awt.Point;

import javax.swing.ImageIcon;

public class Ball extends ImageIcon {
	Stick stick;
	
	int x, y, moveX, moveY;
	int speed = 8;
	final int width = 22, height = 22;
	final int BALLDEADLINE_Y = 760;
	
	public Ball(String img, int x, int y, Stick stick) {
		super(img);
		this.x = x;
		this.y = y;
		this.stick = stick;

		// 공의 초기 방향(왼쪽 or 오른쪽 정함)
		// 0이면 -1(왼쪽), 1이면 1(오른쪽)
		if ((int) (Math.random() * 2) == 0)
			this.moveX = -speed;
		else
			this.moveX = speed;
		moveY = -speed;
	}

	// 좌표 이동 메소드
	public boolean move() {
		// 다른 객체와 부딪혔는지 판단
		isCrash();
		// 공이 떨어졌는지 판단
		if (isFall())
			return true;

		this.x += moveX;
		this.y += moveY;

		return false;
	}

	// 벽/Block/Stick과 부딪혔는지 판단
	// 부딪히면 방향(moveX/moveY) 바꿔주는 메소드
	public void isCrash() {
		// 벽
		if (this.x <= 0 || this.x >= Main.FRAME_WIDTH - 50)
			this.moveX *= -1;
		if (this.y <= 0)
			this.moveY *= -1;

		// stick에 닿은 위치에 따라 다르게 튀게하는 메소드
		if (isCollisionWithStick()) {
			if (stick.x - 12 < this.x && this.x <= stick.x + 12)
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
			if (stick.x + 72 < this.x && this.x <= stick.x + 96)
				moveX = speed;

			moveY *= -1;
		}
	}

	// 공이 deadLine까지 떨어졌는지 판단하는 메소드
	public boolean isFall() {
		if (this.y >= BALLDEADLINE_Y - 15)
			return true;
		else
			return false;
	}

	// 공이 stick에 부딪혔는지 판단하는 메소드
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
