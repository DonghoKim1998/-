package 블록깨기;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Main extends JFrame {
	Robot robot;
	StatePanel statePanel;
	GamePanel gamePanel;
	Stick stick;
	Item item;

	// 화면 크기값
	final static int FRAME_WIDTH = 560;
	final int FRAME_HEIGHT = 880;
	final int STATEPANEL_HEIGHT = 60;
	final int GAMEPANEL_HEIGHT = 820;
	final int TEXT_HEIGHT = 15;
	// 초기 마우스 좌표값
	final int INIT_MOUSE_X = 950;
	final int INIT_MOUSE_Y = 890;
	// 기준선
	final int DEADLINE_HEIGHT = 550;
	final int ITEM_DEADLINE = 720;
	// 블럭 떨어지는 시간값
	final int BLOCKDOWNDELAY = 10;
	// 생명 카운트 변수
	int lifeCount = 2;
	// 아이템 나올 확률
	int probability = 3;

	// Image
	String stickImgURL = "res/stick.png";
	String ballImgURL = "res/ball.png";
	String stickPlusURL = "res/items/stickPlus";
	String stickMinusURL = "res/items/stickMinus";
	String ballPlusURL = "res/items/ballPlus";
	String ballMinusURL = "res/items/ballMinus";
	String blockA = "res/blocks/blockA.png";
	ImageIcon ballImg = new ImageIcon(ballImgURL);
	ImageIcon backGroundImg = new ImageIcon("res/backGround.jpg");
	ImageIcon statePanelBackImg = new ImageIcon("res/statePanel.jpg");
	ImageIcon deadLineImg = new ImageIcon("res/deadLine.png");
	ImageIcon iconImage = new ImageIcon("res/blocks/blockA.png");

	// Sounds
	File gameBGM = new File("res/sounds/GAME BGM.wav");
	File getItem = new File("res/sounds/item.wav");
	File crash = new File("res/sounds/crash.wav");
	File fail = new File("res/sounds/fail.wav");
	Clip gameBGMClip, getItemClip, crashClip, failClip;

	// ArrayList
	// 공 저장 ArrayList
	ArrayList<Ball> ballList = new ArrayList<Ball>();
	// 공 삭제 ArrayList
	ArrayList<Ball> toRemoveBall = new ArrayList<Ball>();
	// 남은 life ArrayList
	ArrayList<ImageIcon> lifeImgList = new ArrayList<>(2);
	// 블럭 저장 ArrayList
	ArrayList<ArrayList<Block>> blockList = new ArrayList<ArrayList<Block>>();
	// 블럭 생성 ArrayList
	ArrayList<Block> tempList;
	// 블럭 제거 ArrayList
	ArrayList<Block> toRemoveBlock = new ArrayList<Block>();
	// 아이템 저장 ArrayList
	ArrayList<Item> itemList = new ArrayList<Item>();
	// 아이템 삭제 ArrayList
	ArrayList<Item> toRemoveItem = new ArrayList<Item>();

	// Timer
	// 1초마다 실행되는 Timer
	Timer oneSecondTimer;
	private final int ONESECOND = 1000;
	OneSecondTimerClass OneSecondTimerClass = new OneSecondTimerClass();
	// 0.005초마다 실행되는 Timer
	Timer oneMilliSecondTimer;
	private final int ONEMILLISECOND = 1;
	OneMilliSecondTimerClass oneMilliSecondTimerClass = new OneMilliSecondTimerClass();
	// 게임 시작 전 공 위치를 계속 그려주기 위한 Timer
	Timer initPaintTimer;
	private final int INITBALLPAINTTIMER_DELAY = 1;
	InitPaintClass initPaintClass = new InitPaintClass();

	/* 메인프레임 시작 */
	// (1) 생성자: Frame 설정
	public Main() {
		this.setTitle("Break COVID-19");
		this.setIconImage(iconImage.getImage());
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

	// 패널 초기화 및 add()
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

		// sound 초기화 및 gameBGM 재생
		try {
			gameBGMClip = AudioSystem.getClip();
			gameBGMClip.open(AudioSystem.getAudioInputStream(gameBGM));
			gameBGMClip.loop(Clip.LOOP_CONTINUOUSLY);
			gameBGMClip.start();

			getItemClip = AudioSystem.getClip();
			getItemClip.open(AudioSystem.getAudioInputStream(getItem));

			crashClip = AudioSystem.getClip();
			crashClip.open(AudioSystem.getAudioInputStream(crash));

			failClip = AudioSystem.getClip();
			failClip.open(AudioSystem.getAudioInputStream(fail));
		} catch (Exception exception) {
		}
	}
	/* 메인 프레임 끝 */

	/* 패널 클래스 시작 */
	// StatePanel
	public class StatePanel extends JPanel {
		JLabel life, playingTime, score;

		public StatePanel() {
			// label, lifeImgList 초기화
			setComponent();

			oneSecondTimer = new Timer(ONESECOND, OneSecondTimerClass);
		}

		public void setComponent() {
			// life
			life = new JLabel("Life");
			life.setBounds(30, TEXT_HEIGHT, 50, 35);
			life.setFont(new Font("Rix짱구 M", Font.ITALIC, 30));
			life.setForeground(Color.white);
			add(life);

			// playingTime
			playingTime = new JLabel("Time 00:00");
			playingTime.setBounds(200, TEXT_HEIGHT, 170, 35);
			playingTime.setFont(new Font("Rix짱구 M", Font.ITALIC, 30));
			playingTime.setForeground(Color.white);
			add(playingTime);

			// score
			score = new JLabel("Score");
			score.setBounds(390, TEXT_HEIGHT, 170, 35);
			score.setFont(new Font("Rix짱구 M", Font.ITALIC, 30));
			score.setForeground(Color.white);
			add(score);

			// life 2개로 초기화
			for (int i = 0; i < 2; i++)
				lifeImgList.add(ballImg);
		}

		public void paintComponent(Graphics g) {
			// StatePanel 배경
			g.drawImage(statePanelBackImg.getImage(), 0, 0, null);

			// 잔여 life
			for (int i = 1; i <= lifeImgList.size(); i++)
				g.drawImage(lifeImgList.get(i - 1).getImage(), 50 + (i * 40), 12, 40, 40, null);
		}
	}

	// GamePanel
	public class GamePanel extends JPanel {
		public GamePanel() {
			// stick, ballList, blockList 초기화
			initialize();

			initPaintTimer.start();
		}

		public void initialize() {
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

			initPaintTimer = new Timer(INITBALLPAINTTIMER_DELAY, initPaintClass);
			oneMilliSecondTimer = new Timer(ONEMILLISECOND, oneMilliSecondTimerClass);
		}

		public void paintComponent(Graphics g) {
			// GamePanel 배경
			g.drawImage(backGroundImg.getImage(), 0, 0, FRAME_WIDTH, GAMEPANEL_HEIGHT, null);

			// deadLine
			g.drawImage(deadLineImg.getImage(), 0, DEADLINE_HEIGHT, FRAME_WIDTH, 5, null);

			// stick
			try {
				stick.x = this.getMousePosition().x - 45;
				stick.draw(g);
			} catch (Exception e) {
			}

			// block
			for (ArrayList<Block> firstDimension : blockList)
				for (Block block : firstDimension)
					block.draw(g);

			// item
			for (Item item : itemList)
				item.draw(g);

			// ball
			for (Ball ball : ballList)
				ball.draw(g);
		}
	}
	/* 패널 클래스 끝 */

	/* Timer 클래스 시작 */
	public class OneSecondTimerClass implements ActionListener {
		int time = 0;
		int score = 0;

		@Override
		public void actionPerformed(ActionEvent e) {
			// playingTime
			time++;
			statePanel.playingTime
					.setText("Time " + String.format("%02d", time / 60) + ":" + String.format("%02d", time % 60));

			// score
			score += 1;
			statePanel.score.setText("Score  " + String.format("%d", score));

			// 10초마다 블럭들이 내려옴
			if (time % BLOCKDOWNDELAY == 0) {
				for (ArrayList<Block> firstDimension : blockList) {
					for (Block block : firstDimension)
						block.y += 24;
				}

				tempList = new ArrayList<Block>();
				for (int i = 0; i < 10; i++)
					tempList.add(new Block((i * 51) + 15, 34));
				blockList.add(tempList);
			}
		}
	}

	// 게임 시작하면 시작하는 Timer 클래스
	public class OneMilliSecondTimerClass implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// ball 움직여주며 추락했는지 확인
			isBallFall();
			// block이 깨졌는지 확인
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
			oneMilliSecondTimer.start();
			oneSecondTimer.start();
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
	// ballList 초기화 메소드
	public void initBallList() {
		this.ballList.clear();
		this.ballList.add(new Ball(ballImgURL, FRAME_WIDTH / 2, stick.y - 20, stick));
	}

	// ball 움직여주며 추락했는지 확인하는 메소드
	public void isBallFall() {
		// 모든 공에 대해서
		for (Ball ball : ballList)
			// 떨어졌는지 확인, 떨어진 경우 toRemoveBall에 객체 삽입
			if (ball.move()) {
				// 효과음 재생
				failClip.start();
				failClip.setFramePosition(0);

				toRemoveBall.add(ball);
			}

		// 떨어진 공 삭제
		for (Ball removeBall : toRemoveBall)
			ballList.remove(removeBall);

		// 남은 공이 없다면
		if (ballList.size() == 0) {
			// 생긴 아이템 clear
			itemList.clear();

			// life가 모두 소진 시 게임 초기화
			if (lifeCount == 0) {
				gameBGMClip.setFramePosition(0);
				resetGame();
				return;
			}

			// life가 남은 경우 lifeImgList--, lifeCount--
			lifeImgList.remove(lifeCount - 1);
			lifeCount--;

			// Life 글씨를 목숨 0개일 때 빨간색으로
			if (lifeCount == 0)
				statePanel.life.setForeground(Color.red);

			// 다시 시작하기 전 새로운 공 만들어주기
			initBallList();

			// 게임을 다시 시작하기 위해 statePanel, gamePanel 그려주기 멈춤
			// initPaint 시작
			oneSecondTimer.stop();
			oneMilliSecondTimer.stop();
			initPaintTimer.start();
		}
	}

	// 벽돌 깬 경우
	public void isBreakBlock() {
		for (ArrayList<Block> firstDimension : blockList) {
			for (Block block : firstDimension) {
				for (Ball ball : ballList) {
					if (block.isBreak(ball)) {
						// 효과음 재생
						crashClip.start();
						crashClip.setFramePosition(0);

						// 30% 확률로 아이템 드랍
						// 0~3 중 난수를 주어 아이템 종류 결정
						if (((int) (Math.random() * 10)) < probability)
							itemList.add(new Item(block.getCenterPoint(), (int) (Math.random() * 4), stick));

						// 점수 추가
						OneSecondTimerClass.score += 5;
						statePanel.score.setText("Score  " + String.format("%d", OneSecondTimerClass.score));

						toRemoveBlock.add(block);
					}
				}

				// 블럭이 deadLine을 넘은 경우
				if (block.y + block.height >= 550) {
					resetGame();
					return;
				}
			}

			for (Block removeblock : toRemoveBlock)
				firstDimension.remove(removeblock);
		}

		// 아이템 떨어트림
		for (Item item : itemList) {
			item.itemDrop();

			// sitck에 닿으면
			if ((stick.x <= item.x && item.x <= stick.x + stick.width)
					&& (stick.y <= item.y && item.y <= stick.y + stick.height)) {
				// 효과음 재생
				getItemClip.start();
				getItemClip.setFramePosition(0);

				switch (item.type) {
				// ballMinus
				case 0:
					if (ballList.size() > 1)
						ballList.remove(ballList.size() - 1);
					break;
				// ballPlus
				case 1:
					ballList.add(new Ball(ballImgURL, (int) (Math.random() * FRAME_WIDTH / 2 + 250),
							(int) (Math.random() * FRAME_HEIGHT / 2 + 300), stick));
					break;
				// stickMinus
				case 2:
					if (stick.width > 70)
						stick.width -= 5;
					break;
				// stickPlus
				case 3:
					stick.width += 5;
					break;
				}

				toRemoveItem.add(item);
			}

			// 일정 위치 지나면 삭제
			if (item.y >= ITEM_DEADLINE)
				toRemoveItem.add(item);
		}

		for (Item removeItem : toRemoveItem)
			itemList.remove(removeItem);
	}

	// 게임 초기화 메소드
	public void resetGame() {
		// 타이머 정지
		oneSecondTimer.stop();
		oneMilliSecondTimer.stop();

		// 데이터 초기화
		statePanel.playingTime.setText("Time 00:00");
		statePanel.score.setText("Score ");
		OneSecondTimerClass.time = 0;
		OneSecondTimerClass.score = 0;
		lifeImgList.clear();
		for (int i = 0; i < 2; i++)
			lifeImgList.add(ballImg);
		statePanel.repaint();

		blockList.clear();
		for (int i = 0; i < 6; i++) {
			tempList = new ArrayList<Block>();
			for (int j = 0; j < 10; j++)
				tempList.add(new Block((j * 51) + 15, (i * 24) + 34));
			blockList.add(tempList);
		}
		lifeCount = 2;

		// 공 초기화
		initBallList();
		// 다시 시작
		initPaintTimer.start();
	}
	/* 기능구현 메소드 끝 */

	// 메인 메소드
	public static void main(String[] args) {
		Main main = new Main();
		main.go();
	}
}