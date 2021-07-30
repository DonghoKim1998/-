package 블록깨기;

import java.awt.Graphics;
import java.awt.Point;

import javax.swing.ImageIcon;

public class Block extends ImageIcon {
	int x, y;
	private final int width = 50, height = 24;

	String redBlockURL = "res/blocks/block_red.png";
	String orangeBlockURL = "res/blocks/block_orange.png";
	String yellowBlockURL = "res/blocks/block_yellow.png";
	String greenBlockURL = "res/blocks/block_green.png";
	String skyBlockURL = "res/blocks/block_sky.png";
	String blueBlockURL = "res/blocks/block_blue.png";
	String purpleBlockURL = "res/blocks/block_purple.png";
	String pinkBlockURL = "res/blocks/block_pink.png";

	ImageIcon blockImg;

	public Block(int x, int y) {
		this.x = x;
		this.y = y;

		// 생성되는 block 객체의 이미지를 랜덤값으로 골라줌
		int randomNum = (int) (Math.random() * 8);
		switch (randomNum) {
		case 0:
			this.blockImg = new ImageIcon(redBlockURL);
			break;
		case 1:
			this.blockImg = new ImageIcon(orangeBlockURL);
			break;
		case 2:
			this.blockImg = new ImageIcon(yellowBlockURL);
			break;
		case 3:
			this.blockImg = new ImageIcon(greenBlockURL);
			break;
		case 4:
			this.blockImg = new ImageIcon(skyBlockURL);
			break;
		case 5:
			this.blockImg = new ImageIcon(blueBlockURL);
			break;
		case 6:
			this.blockImg = new ImageIcon(purpleBlockURL);
			break;
		case 7:
			this.blockImg = new ImageIcon(pinkBlockURL);
			break;
		}

		// System.out.println("Random Number: " + randomNum);
		this.setImage(blockImg.getImage());
	}

	public boolean isBreak(Ball ball) {
		double distance = getDistanceWithBall(ball);

		// 부딪힘
		if (23 < distance && distance <= 36) {
			// 윗면 || 아랫면
			if (ball.y < this.y || ball.y > this.y)
				ball.moveY *= -1;

			// 우측면 || 좌측면
			if (ball.x > this.x + width || ball.x < this.x)
				ball.moveX *= -1;

			// 1시 대각선
			if (ball.getCenterX() > this.x + this.width && ball.getCenterY() < this.y) {
				ball.moveX = ball.speed;
				ball.moveY = -ball.speed;
			}

			// 5시 대각선
			if (ball.getCenterX() > this.x + this.width && ball.getCenterY() > this.y + this.height) {
				ball.moveX = ball.speed;
				ball.moveY = ball.speed;
			}

			// 7시 대각선
			if (ball.getCenterX() < this.x && ball.getCenterY() < this.y + this.height) {
				ball.moveX = -ball.speed;
				ball.moveY = ball.speed;
			}

			// 11시 대각선
			if (ball.getCenterX() < this.x && ball.getCenterY() < this.y) {
				ball.moveX = -ball.speed;
				ball.moveY = -ball.speed;
			}

			return true;
		}

		return false;
	}

	public double getDistanceWithBall(Ball ball) {
		return Math.sqrt(Math.pow((this.getCenterPoint().x - ball.getCenterPoint().x), 2)
				+ Math.pow((this.getCenterPoint().y - ball.getCenterPoint().y), 2));
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

	public String toString() {
		return "" + this.x + "," + this.y;
	}

	public void draw(Graphics g) {
		g.drawImage(this.getImage(), this.x, this.y, this.width, this.height, null);
	}
}
