package ��ϱ���;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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

	// ��ġ ���밪 ����
	private final int FRAME_WIDTH = 590;
	private final int FRAME_HEIGHT = 880;
	private final int STATEPANEL_HEIGHT = 60;
	private final int GAMEPANEL_HEIGHT = 780;

	// �̹��� ����
	ImageIcon ballImg = new ImageIcon("res/ball.png");
	String stickImgURL = "res/stick.png";

	// ������ ǥ���ϴ� Life Image ArrayList
	ArrayList<ImageIcon> lifeImgList;

	// Timer
	// ���� �ð� ���� Timer
	Timer playingTimer;
	private final int PLAYINGTIMER_DELAY = 1000;

	/* Start of MainFrame */
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
		// ���� ��, Ŀ�� ��ġ ��ȯ
		setMouseInitPosition();

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

	public void setMouseInitPosition() {
		try {
			robot = new Robot();
			robot.mouseMove(950, 850);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/* End of MainFrame */

	/* Start of Panel Classes */
	// StatePanel
	public class StatePanel extends JPanel {
		JLabel life, playingTime, score;

		// ������: Panel ����, JLabel ������ �ڸ��� add()
		public StatePanel() {
			setLayout(null);

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
		Stick stick;

		int mouse_X;

		// 0.005�ʸ��� �����г� �׷��ִ� Timer
		Timer gamePanelTimer;
		private final int gamePanelTimerDelay = 5;

		// GamePanel ������
		public GamePanel() {
			// stick ��ü ����
			stick = new Stick(stickImgURL, FRAME_WIDTH / 2, 90, 80);

			gamePanelTimer = new Timer(gamePanelTimerDelay, new GamePanelTimerClass());
			gamePanelTimer.start();
		}

		public void paintComponent(Graphics g) {
			// GamePanel ��� �׸���
			g.setColor(Color.white);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());

			// stick �׸���
			try {
				stick.x = this.getMousePosition().x - 40;
				stick.draw(g);
			} catch (Exception e) {
			}
		}
	}
	/* End of Panel Classes */

	/* Start of Timer Classes */
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
			gamePanel.repaint();
		}
	}
	/* End of Timer Classes */

	// ���� �޼ҵ�
	public static void main(String[] args) {
		Main main = new Main();
		main.go();
	}
}