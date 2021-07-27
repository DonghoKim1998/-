package 블록깨기;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.Timer;

public class Main extends JFrame {

	StatePanel statePanel;
	GamePanel gamePanel;

	// 위치 절대값 설정
	private final int FRAME_WIDTH = 600;
	private final int FRAME_HEIGHT = 880;
	private final int STATEPANEL_HEIGHT = 60;
	private final int GAMEPANEL_HEIGHT = 820;

	// 이미지
	ImageIcon ballImg = new ImageIcon("res/ball.png");

	// 생명을 표시하는 Life Image ArrayList
	ArrayList<ImageIcon> lifeImgList;

	// Timer
	// 진행 시간 관련 Timer
	Timer playingTimer;
	private final int PLAYINGTIMER_DELAY = 1000;

	/* Start of MainFrame */
	// (1) 생성자: Frame 설정
	public Main() {
		this.setTitle("블록깨기");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setLayout(null);
		this.setVisible(true);
	}

	// (2) 시작 메소드
	public void go() {
		// StatePanel 초기화, Frame에 추가
		statePanel = new StatePanel();
		this.getContentPane().add(statePanel);

		// GamePanel 초기화 및 Frame에 추가
		gamePanel = new GamePanel();
		this.getContentPane().add(gamePanel);
	}
	/* End of MainFrame */

	/* Start of Panel Classes */
	// StatePanel
	public class StatePanel extends JPanel {
		JLabel life, playingTime, score;

		// 생성자: Panel 설정, JLabel 고정된 자리에 add()
		public StatePanel() {
			// panel 설정 메소드
			setStatePanel();

			// life 설정
			life = new JLabel("Life");
			life.setBounds(50, 15, 50, 35);
			life.setFont(new Font("Rix짱구 M", Font.ITALIC, 30));
			add(life);

			// playingTime 설정
			playingTime = new JLabel("Time 00:00");
			playingTime.setBounds(220, 15, 170, 35);
			playingTime.setFont(new Font("Rix짱구 M", Font.ITALIC, 30));
			add(playingTime);

			// score 설정
			score = new JLabel("Score");
			score.setBounds(415, 15, 170, 35);
			score.setFont(new Font("Rix짱구 M", Font.ITALIC, 30));
			add(score);

			// 초기 life(ball) 2개로 초기화
			lifeImgList = new ArrayList<>(2);
			for (int i = 0; i < 2; i++)
				lifeImgList.add(ballImg);

			playingTimer = new Timer(PLAYINGTIMER_DELAY, new PlayingTimerClass());
		}

		// StatePanel 설정 메소드
		public void setStatePanel() {
			setLayout(null);
			setBounds(0, 0, FRAME_WIDTH, STATEPANEL_HEIGHT);
		}

		public void paintComponent(Graphics g) {
			// StatePanel 배경 그리기
			g.setColor(Color.yellow);
			g.fillRect(0, 0, FRAME_WIDTH, STATEPANEL_HEIGHT);

			// 남아있는 life 그리기
			for (int i = 1; i <= lifeImgList.size(); i++)
				g.drawImage(lifeImgList.get(i - 1).getImage(), 70 + (i * 40), 12, 40, 40, null);
		}
	}

	// GamePanel
	public class GamePanel extends JPanel {
//		// stick 이미지
//		String stickImgURL = "res/stick.png";
//		ImageIcon stickImg = new ImageIcon(stickImgURL);
//
//		// stick초기화
//		Stick stick = new Stick(stickImgURL, FRAME_WIDTH / 2, 80, 80);

		// 0.005초마다 게임패널 그려주는 Timer
		private final int gamePanelTimerDelay = 5;
		Timer gamePanelTimer;

		// GamePanel 생성자
		public GamePanel() {
			setGamePanel();

			gamePanelTimer = new Timer(gamePanelTimerDelay, new GamePanelTimerClass());
			gamePanelTimer.start();
		}

		// GamePanel 초기화 메소드
		public void setGamePanel() {
			setLayout(null);
			setBounds(0, 0, FRAME_WIDTH, GAMEPANEL_HEIGHT);

			addMouseListener(new MyMouseListener());
		}

		public void paintComponent(Graphics g) {
			// GamePanel 배경 그리기
			g.setColor(Color.white);
			g.fillRect(0, STATEPANEL_HEIGHT, FRAME_WIDTH, FRAME_HEIGHT);

			// stick 그리기
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
			// playingTime 설정
			time++;
			statePanel.playingTime
					.setText("Time " + String.format("%02d", time / 60) + ":" + String.format("%02d", time % 60));

			// Score 설정
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

	// 메인 메소드
	public static void main(String[] args) {
		Main main = new Main();
		main.go();
	}
}