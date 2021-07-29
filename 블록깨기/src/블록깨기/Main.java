package 블록깨기;

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

	// 위치 절대값 설정
	final static int FRAME_WIDTH = 590;
	final int FRAME_HEIGHT = 880;
	final int STATEPANEL_HEIGHT = 60;
	final int GAMEPANEL_HEIGHT = 780;
	final int INIT_MOUSE_X = 950;
	final int INIT_MOUSE_Y = 890;
	final static int DEADLINE_HEIGHT = 760;

	// 이미지 관련
	String stickImgURL = "res/stick.png";
	String ballImgURL = "res/ball.png";
	ImageIcon ballImg = new ImageIcon(ballImgURL);
	ImageIcon backGroundImg = new ImageIcon("res/game_background.png");
	ImageIcon deadLine = new ImageIcon("res/deadLine.png");

	// 생명을 표시하는 Life Image ArrayList
	ArrayList<ImageIcon> lifeImgList;

	// Timer
	// 진행 시간 관련 Timer
	Timer playingTimer;
	private final int PLAYINGTIMER_DELAY = 1000;
	// 0.005초마다 게임패널 그려주는 Timer
	Timer gamePanelTimer;
	private final int gamePanelTimerDelay = 1;
	// 게임 시작 전 공 위치를 계속 그려주기 위한 Timer
	Timer initPaintTimer;
	private final int initBallPatinTimerDelay = 1;

	/* 메인프레임 시작 */
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
		setMouse();
		initAndAddPanels();
	}

	// 마우스 관련 메소드
	public void setMouse() {
		// 실행 시 마우스 위치 설정
		try {
			robot = new Robot();
			robot.mouseMove(INIT_MOUSE_X, INIT_MOUSE_Y);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 마우스커서 안보이게 설정
		this.setCursor(this.getToolkit().createCustomCursor(new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB),
				new Point(0, 0), "null"));
	}

	public void initAndAddPanels() {
		// StatePanel 초기화, Frame에 추가
		statePanel = new StatePanel();
		statePanel.setLayout(null);
		statePanel.setBounds(0, 0, FRAME_WIDTH, STATEPANEL_HEIGHT);
		statePanel.addMouseListener(new MyMouseListener());
		this.getContentPane().add(statePanel);

		// GamePanel 초기화 및 Frame에 추가
		gamePanel = new GamePanel();
		gamePanel.setLayout(null);
		gamePanel.setBounds(0, 60, FRAME_WIDTH, GAMEPANEL_HEIGHT);
		gamePanel.addMouseListener(new MyMouseListener());
		this.getContentPane().add(gamePanel);
	}
	/* 메인 프레임 끝 */

	/* 패널 클래스 시작 */
	// StatePanel
	public class StatePanel extends JPanel {
		JLabel life, playingTime, score;

		public StatePanel() {
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
			score.setBounds(410, 15, 170, 35);
			score.setFont(new Font("Rix짱구 M", Font.ITALIC, 30));
			add(score);

			// 초기 life(ball) 2개로 초기화
			lifeImgList = new ArrayList<>(2);
			for (int i = 0; i < 2; i++)
				lifeImgList.add(ballImg);

			playingTimer = new Timer(PLAYINGTIMER_DELAY, new PlayingTimerClass());
		}

		// StatePanel 그리는 메소드
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
		public GamePanel() {
			stick = new Stick(stickImgURL, FRAME_WIDTH / 2);
			ball = new Ball(ballImgURL, FRAME_WIDTH / 2, stick.y - 20);

			initPaintTimer = new Timer(initBallPatinTimerDelay, new InitPaintClass());
			initPaintTimer.start();

			gamePanelTimer = new Timer(gamePanelTimerDelay, new GamePanelTimerClass());
		}

		// GamePanel 그리는 메소드
		public void paintComponent(Graphics g) {
			// GamePanel 배경 그리기
			g.drawImage(backGroundImg.getImage(), 0, 0, FRAME_WIDTH, GAMEPANEL_HEIGHT, null);
			// deadLine 그리기
			g.drawImage(deadLine.getImage(), 0, DEADLINE_HEIGHT, FRAME_WIDTH, 5, null);

			// stick 그리기
			try {
				stick.x = this.getMousePosition().x - 45;
				stick.draw(g);
			} catch (Exception e) {
			}

			// ball 그리기
			ball.draw(g);

			/*
			 * 블럭 위치 가이드라인
			 */
			g.setColor(Color.white);
			g.drawLine(0, 100, FRAME_WIDTH, 100);
			g.drawLine(0, 350, FRAME_WIDTH, 350);
		}
	}
	/* 패널 클래스 끝 */

	/* Timer 클래스 시작 */
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
			 * 밑 게임 종료부분 수정 필요 게임 종료 패널뜨게끔
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
	/* Timer 클래스 끝 */

	// 메인 메소드
	public static void main(String[] args) {
		Main main = new Main();
		main.go();
	}
}