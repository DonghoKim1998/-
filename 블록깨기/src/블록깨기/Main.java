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
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Main extends JFrame {
	Robot robot;
	StatePanel statePanel;
	GamePanel gamePanel;
//	Ball ball;
	Stick stick;
	Item item;

	// 위치 절대값 설정
	final static int FRAME_WIDTH = 560;
	final int FRAME_HEIGHT = 880;
	final int STATEPANEL_HEIGHT = 60;
	final int GAMEPANEL_HEIGHT = 820;
	final int INIT_MOUSE_X = 950;
	final int INIT_MOUSE_Y = 890;
	final int DEADLINE_HEIGHT = 550;
	final int BLOCKDOWNDELAY = 10;
	final int ITEM_DEADLINE = 710;
	int lifeCount = 2;

	// 이미지 관련
	String stickImgURL = "res/stick.png";
	String ballImgURL = "res/ball.png";
	String stickPlusURL = "res/items/stickPlus";
	String stickMinusURL = "res/items/stickMinus";
	String ballPlusURL = "res/items/ballPlus";
	String ballMinusURL = "res/items/ballMinus";
	ImageIcon ballImg = new ImageIcon(ballImgURL);
	ImageIcon backGroundImg = new ImageIcon("res/game_background.png");
	ImageIcon deadLineImg = new ImageIcon("res/deadLine.png");
	ImageIcon statePanelBackImg = new ImageIcon("res/statePanelBackImg.png");

	ArrayList<Ball> ballList = new ArrayList<Ball>();
	ArrayList<Ball> toRemoveBall = new ArrayList<Ball>();
	ArrayList<ImageIcon> lifeImgList = new ArrayList<>(2);
	ArrayList<ArrayList<Block>> blockList = new ArrayList<ArrayList<Block>>();
	ArrayList<Block> tempList;
	ArrayList<Block> toRemoveBlock = new ArrayList<Block>();
	ArrayList<Item> itemList = new ArrayList<Item>();
	ArrayList<Item> toRemoveItem = new ArrayList<Item>();

	// Timer
	// 진행 시간 관련 Timer
	Timer statePanelTimer;
	private final int STATEPANELTIMER_DELAY = 1000;
	StatePanelTimerClass statePanelTimerClass = new StatePanelTimerClass();
	// 0.005초마다 게임패널 그려주는 Timer
	Timer gamePanelTimer;
	private final int GAMEPANELTIMER_DELAY = 1;
	GamePanelTimerClass gamePanelTimerClass = new GamePanelTimerClass();
	// 게임 시작 전 공 위치를 계속 그려주기 위한 Timer
	Timer initPaintTimer;
	private final int INITBALLPAINTTIMER_Delay = 1;
	InitPaintClass initPaintClass = new InitPaintClass();

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
			setComponent();

			statePanelTimer = new Timer(STATEPANELTIMER_DELAY, statePanelTimerClass);
		}

		public void setComponent() {
			// life 설정
			life = new JLabel("Life");
			life.setBounds(30, 15, 50, 35);
			life.setFont(new Font("Rix짱구 M", Font.ITALIC, 30));
			life.setForeground(Color.black);
			add(life);

			// playingTime 설정
			playingTime = new JLabel("Time 00:00");
			playingTime.setBounds(200, 15, 170, 35);
			playingTime.setFont(new Font("Rix짱구 M", Font.ITALIC, 30));
			playingTime.setForeground(Color.black);
			add(playingTime);

			// score 설정
			score = new JLabel("Score");
			score.setBounds(390, 15, 170, 35);
			score.setFont(new Font("Rix짱구 M", Font.ITALIC, 30));
			score.setForeground(Color.black);
			add(score);

			// 초기 life 2개로 초기화
			for (int i = 0; i < 2; i++)
				lifeImgList.add(ballImg);
		}

		public void paintComponent(Graphics g) {
			// StatePanel 배경 그리기
			g.drawImage(statePanelBackImg.getImage(), 0, 0, null);

			// 남은 life 그리기
			for (int i = 1; i <= lifeImgList.size(); i++)
				g.drawImage(lifeImgList.get(i - 1).getImage(), 50 + (i * 40), 12, 40, 40, null);
		}
	}

	// GamePanel
	public class GamePanel extends JPanel {
//		Stick stick;

		public GamePanel() {
			stick = new Stick(stickImgURL, FRAME_WIDTH / 2);
			ballList.add(new Ball(ballImgURL, FRAME_WIDTH / 2, stick.y - 20, stick));

			// blockList에 Block 객체 삽입
			// 6행 10열
			for (int i = 0; i < 6; i++) {
				tempList = new ArrayList<Block>();
				for (int j = 0; j < 10; j++)
					tempList.add(new Block((j * 51) + 15, (i * 24) + 34));
				blockList.add(tempList);
			}

			initPaintTimer = new Timer(INITBALLPAINTTIMER_Delay, initPaintClass);
			initPaintTimer.start();

			gamePanelTimer = new Timer(GAMEPANELTIMER_DELAY, gamePanelTimerClass);
		}

		public void paintComponent(Graphics g) {
			// GamePanel 배경 그리기
			g.drawImage(backGroundImg.getImage(), 0, 0, FRAME_WIDTH, GAMEPANEL_HEIGHT, null);

			// deadLine 그리기
			g.drawImage(deadLineImg.getImage(), 0, DEADLINE_HEIGHT, FRAME_WIDTH, 5, null);

			// stick 그리기
			try {
				stick.x = this.getMousePosition().x - 45;
				stick.draw(g);
			} catch (Exception e) {
			}

			// 블럭 그리기
			for (ArrayList<Block> firstDimension : blockList)
				for (Block block : firstDimension)
					block.draw(g);

			// 아이템 그리기
			for (Item item : itemList)
				item.draw(g);

			// ball 그리기
			for (Ball ball : ballList)
				ball.draw(g);
		}
	}
	/* 패널 클래스 끝 */

	/* Timer 클래스 시작 */
	public class StatePanelTimerClass implements ActionListener {
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

			// 10초마다 블럭들이 내려옴
			if (time % BLOCKDOWNDELAY == 0) {
				for (ArrayList<Block> firstDimension : blockList) {
					for (Block block : firstDimension)
						block.y += 24;
				}
			}
		}
	}

	// 게임 시작하면 시작하는 Timer 클래스
	public class GamePanelTimerClass implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			isBallFall();
			isBreakBlock();

			statePanel.repaint();
			gamePanel.repaint();
		}
	}

// 게임시작 전 공과 스틱 그려주는 Timer 클래스
	public class InitPaintClass implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			for (Ball ball : ballList)
				ball.x = stick.x + 33;
			
			gamePanel.repaint();
		}
	}
	/* Timer 클래스 끝 */

	/* Listener 시작 */
	public class MyMouseListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			// 게임패널 클릭하면 처음 그려주는거 멈추고
			initPaintTimer.stop();

			// gamePanel과 statePanel 그려줌
			gamePanelTimer.start();
			statePanelTimer.start();
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
	/* Listener 끝 */

	/* 기능구현 메소드 시작 */
	public void endGame() {
		/*
		 * 구현필요
		 */
	}

	public void initBallList() {
		this.ballList.clear();
		this.ballList.add(new Ball(ballImgURL, FRAME_WIDTH / 2, stick.y - 20, stick));
	}

	public void isBallFall() {
		// 모든 공에 대해서
		for (Ball ball : ballList)
			// 떨어졌는지 확인, 떨어진 경우 toRemoveBall에 객체 삽입
			if (!ball.move())
				toRemoveBall.add(ball);

		// 떨어진 공 삭제
		for (Ball removeBall : toRemoveBall)
			ballList.remove(removeBall);

		// 남은 공이 없다면
		if (ballList.size() == 0) {
			// 생긴 아이템 clear
			itemList.clear();

			// life가 모두 소진 시 종료
			if (lifeCount == 0)
				System.exit(1);

			// life가 남은 경우 lifeImgList--, lifeCount--
			lifeImgList.remove(lifeCount - 1);
			lifeCount--;

			// Life 글씨를 목숨 0개일 때 빨간색으로
			if (lifeCount == 0)
				statePanel.life.setForeground(Color.red);

			// 게임을 다시 시작하기 위해 statePanel, gamePanel 그려주기 멈춤
			// initPaint 시작
			statePanelTimer.stop();
			gamePanelTimer.stop();
			initPaintTimer.start();

			// 다시 시작하기 전 새로운 공 만들어주기
			initBallList();
		}
	}

	public void isBreakBlock() {
		for (ArrayList<Block> firstDimension : blockList) {
			for (Block block : firstDimension) {
				for (Ball ball : ballList) {
					if (block.isBreak(ball)) {
						// 20% 확률로 아이템 드랍
						// 0~3 중 난수를 주어 아이템 종류 결정
						if (((int) (Math.random() * 10)) < 10)
							itemList.add(new Item(block.getCenterPoint(), (int) (Math.random() * 4), stick));

						// 점수 추가
						statePanelTimerClass.score += 5;
						statePanel.score.setText("Score  " + String.format("%d", statePanelTimerClass.score));

						toRemoveBlock.add(block);
					}
				}

				// 블럭이 deadLine을 넘은 경우
				if (block.y + block.height >= 550)
					/*
					 * 게임종료 메소드 만들어 추가하기 !!!!!!!!!!!!!!
					 */
					System.exit(1);
			}

			for (Block removeblock : toRemoveBlock)
				firstDimension.remove(removeblock);
		}

		// 아이템 떨어트림
		for (Item item : itemList) {
			item.itemDrop();

			// 일정 위치 지나면 삭제
			if (item.y >= ITEM_DEADLINE) {
				toRemoveItem.add(item);
				for (Item removeItem : toRemoveItem)
					itemList.remove(removeItem);
			}
		}
	}
	/* 기능구현 메소드 끝 */

	// 메인 메소드
	public static void main(String[] args) {
		Main main = new Main();
		main.go();
	}
}