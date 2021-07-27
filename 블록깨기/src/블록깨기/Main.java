package ��ϱ���;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.Timer;

public class Main extends JFrame {

	StatePanel statePanel;
	GamePanel gamePanel;

	// ��ġ ���밪 ����
	private final int FRAME_WIDTH = 600;
	private final int FRAME_HEIGHT = 880;
	private final int STATEPANEL_HEIGHT = 60;
	private final int GAMEPANEL_HEIGHT = 820;

	// �̹���
	ImageIcon ballImg = new ImageIcon("res/ball.png");

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
		// StatePanel �ʱ�ȭ, Frame�� �߰�
		statePanel = new StatePanel();
		this.getContentPane().add(statePanel);

		// GamePanel �ʱ�ȭ �� Frame�� �߰�
		gamePanel = new GamePanel();
		this.getContentPane().add(gamePanel);
	}
	/* End of MainFrame */

	/* Start of Panel Classes */
	// StatePanel
	public class StatePanel extends JPanel {
		JLabel life, playingTime, score;

		// ������: Panel ����, JLabel ������ �ڸ��� add()
		public StatePanel() {
			// panel ���� �޼ҵ�
			setStatePanel();

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
			score.setBounds(415, 15, 170, 35);
			score.setFont(new Font("Rix¯�� M", Font.ITALIC, 30));
			add(score);

			// �ʱ� life(ball) 2���� �ʱ�ȭ
			lifeImgList = new ArrayList<>(2);
			for (int i = 0; i < 2; i++)
				lifeImgList.add(ballImg);

			playingTimer = new Timer(PLAYINGTIMER_DELAY, new PlayingTimerClass());
		}

		// StatePanel ���� �޼ҵ�
		public void setStatePanel() {
			setLayout(null);
			setBounds(0, 0, FRAME_WIDTH, STATEPANEL_HEIGHT);
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
//		// stick �̹���
//		String stickImgURL = "res/stick.png";
//		ImageIcon stickImg = new ImageIcon(stickImgURL);
//
//		// stick�ʱ�ȭ
//		Stick stick = new Stick(stickImgURL, FRAME_WIDTH / 2, 80, 80);

		// 0.005�ʸ��� �����г� �׷��ִ� Timer
		private final int gamePanelTimerDelay = 5;
		Timer gamePanelTimer;

		// GamePanel ������
		public GamePanel() {
			setGamePanel();

			gamePanelTimer = new Timer(gamePanelTimerDelay, new GamePanelTimerClass());
			gamePanelTimer.start();
		}

		// GamePanel �ʱ�ȭ �޼ҵ�
		public void setGamePanel() {
			setLayout(null);
			setBounds(0, 0, FRAME_WIDTH, GAMEPANEL_HEIGHT);

			addMouseListener(new MyMouseListener());
		}

		public void paintComponent(Graphics g) {
			// GamePanel ��� �׸���
			g.setColor(Color.white);
			g.fillRect(0, STATEPANEL_HEIGHT, FRAME_WIDTH, FRAME_HEIGHT);

			// stick �׸���
//			try {
//				stick.x = this.getMousePosition().x - 40;
//				stick.draw(g);
//			} catch (Exception e) {
//			}
//			;
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
			statePanel.score.setText("Score      " + String.format("%d", score));
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
			//gamePanel.repaint();
		}
	}
	/* End of Timer Classes */

	// ���� �޼ҵ�
	public static void main(String[] args) {
		Main main = new Main();
		main.go();
	}
}