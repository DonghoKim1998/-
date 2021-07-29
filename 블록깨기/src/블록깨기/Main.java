package ��ϱ���;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Main extends JFrame {

	Robot robot;
	StatePanel statePanel;
	GamePanel gamePanel;
	static Stick stick;
	Ball ball;

	// ��ġ ���밪 ����
	final static int FRAME_WIDTH = 590;
	final int FRAME_HEIGHT = 880;
	final int STATEPANEL_HEIGHT = 60;
	final int GAMEPANEL_HEIGHT = 780;
	final int INIT_MOUSE_X = 950;
	final int INIT_MOUSE_Y = 890;
	final static int DEADLINE_HEIGHT = 760;

	// �̹��� ����
	String stickImgURL = "res/stick.png";
	String ballImgURL = "res/ball.png";
	ImageIcon ballImg = new ImageIcon(ballImgURL);
	ImageIcon backGroundImg = new ImageIcon("res/game_background.png");
	ImageIcon deadLine = new ImageIcon("res/deadLine.png");

	// ������ ǥ���ϴ� Life Image ArrayList
	ArrayList<ImageIcon> lifeImgList;

	// Timer
	// ���� �ð� ���� Timer
	Timer playingTimer;
	private final int PLAYINGTIMER_DELAY = 1000;
	// 0.005�ʸ��� �����г� �׷��ִ� Timer
	Timer gamePanelTimer;
	private final int gamePanelTimerDelay = 1;
	// ���� ���� �� �� ��ġ�� ��� �׷��ֱ� ���� Timer
	Timer initPaintTimer;
	private final int initBallPatinTimerDelay = 1;

	/* ���������� ���� */
	// (1) ������: Frame ����
	public Main() {
		this.setTitle("��ϱ���");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setLayout(null);
		this.setVisible(true);
	}

	// (2) ���� �޼ҵ�
	public void go() {
		setMouse();
		initAndAddPanels();
	}

	// ���콺 ���� �޼ҵ�
	public void setMouse() {
		// ���� �� ���콺 ��ġ ����
		try {
			robot = new Robot();
			robot.mouseMove(INIT_MOUSE_X, INIT_MOUSE_Y);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// ���콺Ŀ�� �Ⱥ��̰� ����
		this.setCursor(this.getToolkit().createCustomCursor(new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB),
				new Point(0, 0), "null"));
	}

	public void initAndAddPanels() {
		// StatePanel �ʱ�ȭ, Frame�� �߰�
		statePanel = new StatePanel();
		statePanel.setLayout(null);
		statePanel.setBounds(0, 0, FRAME_WIDTH, STATEPANEL_HEIGHT);
		statePanel.addMouseListener(new MyMouseListener());
		this.getContentPane().add(statePanel);

		// GamePanel �ʱ�ȭ �� Frame�� �߰�
		gamePanel = new GamePanel();
		gamePanel.setLayout(null);
		gamePanel.setBounds(0, 60, FRAME_WIDTH, GAMEPANEL_HEIGHT);
		gamePanel.addMouseListener(new MyMouseListener());
		this.getContentPane().add(gamePanel);
	}
	/* ���� ������ �� */

	/* �г� Ŭ���� ���� */
	// StatePanel
	public class StatePanel extends JPanel {
		JLabel life, playingTime, score;

		public StatePanel() {
			// life ����
			life = new JLabel("Life");
			life.setBounds(50, 15, 50, 35);
			life.setFont(new Font("Rix¯�� M", Font.ITALIC, 30));
			add(life);

			// playingTime ����
			playingTime = new JLabel("Time 00:00");
			playingTime.setBounds(220, 15, 170, 35);
			playingTime.setFont(new Font("Rix¯�� M", Font.ITALIC, 30));
			add(playingTime);

			// score ����
			score = new JLabel("Score");
			score.setBounds(410, 15, 170, 35);
			score.setFont(new Font("Rix¯�� M", Font.ITALIC, 30));
			add(score);

			// �ʱ� life(ball) 2���� �ʱ�ȭ
			lifeImgList = new ArrayList<>(2);
			for (int i = 0; i < 2; i++)
				lifeImgList.add(ballImg);

			playingTimer = new Timer(PLAYINGTIMER_DELAY, new PlayingTimerClass());
		}

		// StatePanel �׸��� �޼ҵ�
		public void paintComponent(Graphics g) {
			// StatePanel ��� �׸���
			g.setColor(Color.yellow);
			g.fillRect(0, 0, FRAME_WIDTH, STATEPANEL_HEIGHT);

			// �����ִ� life �׸���
			for (int i = 1; i <= lifeImgList.size(); i++)
				g.drawImage(lifeImgList.get(i - 1).getImage(), 70 + (i * 40), 12, 40, 40, null);
		}
	}

	// GamePanel
	public class GamePanel extends JPanel {
		public GamePanel() {
			stick = new Stick(stickImgURL, FRAME_WIDTH / 2);
			ball = new Ball(ballImgURL, FRAME_WIDTH / 2, stick.y - 20);

			initPaintTimer = new Timer(initBallPatinTimerDelay, new InitPaintClass());
			initPaintTimer.start();

			gamePanelTimer = new Timer(gamePanelTimerDelay, new GamePanelTimerClass());
		}

		// GamePanel �׸��� �޼ҵ�
		public void paintComponent(Graphics g) {
			// GamePanel ��� �׸���
			g.drawImage(backGroundImg.getImage(), 0, 0, FRAME_WIDTH, GAMEPANEL_HEIGHT, null);
			// deadLine �׸���
			g.drawImage(deadLine.getImage(), 0, DEADLINE_HEIGHT, FRAME_WIDTH, 5, null);

			// stick �׸���
			try {
				stick.x = this.getMousePosition().x - 45;
				stick.draw(g);
			} catch (Exception e) {
			}

			// ball �׸���
			ball.draw(g);

			/*
			 * �� ��ġ ���̵����
			 */
			g.setColor(Color.white);
			g.drawLine(0, 100, FRAME_WIDTH, 100);
			g.drawLine(0, 350, FRAME_WIDTH, 350);
		}
	}
	/* �г� Ŭ���� �� */

	/* Timer Ŭ���� ���� */
	public class PlayingTimerClass implements ActionListener {
		int time = 0;
		int score = 0;

		@Override
		public void actionPerformed(ActionEvent e) {
			// playingTime ����
			time++;
			statePanel.playingTime
					.setText("Time " + String.format("%02d", time / 60) + ":" + String.format("%02d", time % 60));

			// Score ����
			score += 1;
			statePanel.score.setText("Score  " + String.format("%d", score));
		}
	}

	public class MyMouseListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			initPaintTimer.stop();

			gamePanelTimer.start();
			playingTimer.start();
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}
	}

	public class GamePanelTimerClass implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			/*
			 * �� ���� ����κ� ���� �ʿ� ���� ���� �гζ߰Բ�
			 */
			if (!ball.move())
				System.exit(1);
			gamePanel.repaint();
		}
	}

	public class InitPaintClass implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			ball.x = stick.x + 33;
			gamePanel.repaint();
		}

	}
	/* Timer Ŭ���� �� */

	// ���� �޼ҵ�
	public static void main(String[] args) {
		Main main = new Main();
		main.go();
	}
}