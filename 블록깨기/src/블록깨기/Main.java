package ��ϱ���;

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

	// ȭ�� ũ�Ⱚ
	final static int FRAME_WIDTH = 560;
	final int FRAME_HEIGHT = 880;
	final int STATEPANEL_HEIGHT = 60;
	final int GAMEPANEL_HEIGHT = 820;
	final int TEXT_HEIGHT = 15;
	// �ʱ� ���콺 ��ǥ��
	final int INIT_MOUSE_X = 950;
	final int INIT_MOUSE_Y = 890;
	// ���ؼ�
	final int DEADLINE_HEIGHT = 550;
	final int ITEM_DEADLINE = 720;
	// �� �������� �ð���
	final int BLOCKDOWNDELAY = 10;
	// ���� ī��Ʈ ����
	int lifeCount = 2;
	// ������ ���� Ȯ��
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
	// �� ���� ArrayList
	ArrayList<Ball> ballList = new ArrayList<Ball>();
	// �� ���� ArrayList
	ArrayList<Ball> toRemoveBall = new ArrayList<Ball>();
	// ���� life ArrayList
	ArrayList<ImageIcon> lifeImgList = new ArrayList<>(2);
	// �� ���� ArrayList
	ArrayList<ArrayList<Block>> blockList = new ArrayList<ArrayList<Block>>();
	// �� ���� ArrayList
	ArrayList<Block> tempList;
	// �� ���� ArrayList
	ArrayList<Block> toRemoveBlock = new ArrayList<Block>();
	// ������ ���� ArrayList
	ArrayList<Item> itemList = new ArrayList<Item>();
	// ������ ���� ArrayList
	ArrayList<Item> toRemoveItem = new ArrayList<Item>();

	// Timer
	// 1�ʸ��� ����Ǵ� Timer
	Timer oneSecondTimer;
	private final int ONESECOND = 1000;
	OneSecondTimerClass OneSecondTimerClass = new OneSecondTimerClass();
	// 0.005�ʸ��� ����Ǵ� Timer
	Timer oneMilliSecondTimer;
	private final int ONEMILLISECOND = 1;
	OneMilliSecondTimerClass oneMilliSecondTimerClass = new OneMilliSecondTimerClass();
	// ���� ���� �� �� ��ġ�� ��� �׷��ֱ� ���� Timer
	Timer initPaintTimer;
	private final int INITBALLPAINTTIMER_DELAY = 1;
	InitPaintClass initPaintClass = new InitPaintClass();

	/* ���������� ���� */
	// (1) ������: Frame ����
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

	// �г� �ʱ�ȭ �� add()
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

		// sound �ʱ�ȭ �� gameBGM ���
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
	/* ���� ������ �� */

	/* �г� Ŭ���� ���� */
	// StatePanel
	public class StatePanel extends JPanel {
		JLabel life, playingTime, score;

		public StatePanel() {
			// label, lifeImgList �ʱ�ȭ
			setComponent();

			oneSecondTimer = new Timer(ONESECOND, OneSecondTimerClass);
		}

		public void setComponent() {
			// life
			life = new JLabel("Life");
			life.setBounds(30, TEXT_HEIGHT, 50, 35);
			life.setFont(new Font("Rix¯�� M", Font.ITALIC, 30));
			life.setForeground(Color.white);
			add(life);

			// playingTime
			playingTime = new JLabel("Time 00:00");
			playingTime.setBounds(200, TEXT_HEIGHT, 170, 35);
			playingTime.setFont(new Font("Rix¯�� M", Font.ITALIC, 30));
			playingTime.setForeground(Color.white);
			add(playingTime);

			// score
			score = new JLabel("Score");
			score.setBounds(390, TEXT_HEIGHT, 170, 35);
			score.setFont(new Font("Rix¯�� M", Font.ITALIC, 30));
			score.setForeground(Color.white);
			add(score);

			// life 2���� �ʱ�ȭ
			for (int i = 0; i < 2; i++)
				lifeImgList.add(ballImg);
		}

		public void paintComponent(Graphics g) {
			// StatePanel ���
			g.drawImage(statePanelBackImg.getImage(), 0, 0, null);

			// �ܿ� life
			for (int i = 1; i <= lifeImgList.size(); i++)
				g.drawImage(lifeImgList.get(i - 1).getImage(), 50 + (i * 40), 12, 40, 40, null);
		}
	}

	// GamePanel
	public class GamePanel extends JPanel {
		public GamePanel() {
			// stick, ballList, blockList �ʱ�ȭ
			initialize();

			initPaintTimer.start();
		}

		public void initialize() {
			stick = new Stick(stickImgURL, FRAME_WIDTH / 2);

			ballList.add(new Ball(ballImgURL, FRAME_WIDTH / 2, stick.y - 20, stick));

			// blockList�� Block ��ü ����
			// 6�� 10��
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
			// GamePanel ���
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
	/* �г� Ŭ���� �� */

	/* Timer Ŭ���� ���� */
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

			// 10�ʸ��� ������ ������
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

	// ���� �����ϸ� �����ϴ� Timer Ŭ����
	public class OneMilliSecondTimerClass implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// ball �������ָ� �߶��ߴ��� Ȯ��
			isBallFall();
			// block�� �������� Ȯ��
			isBreakBlock();

			statePanel.repaint();
			gamePanel.repaint();
		}
	}

	// ���ӽ��� �� ���� ��ƽ �׷��ִ� Timer Ŭ����
	public class InitPaintClass implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			for (Ball ball : ballList)
				ball.x = stick.x + 33;

			gamePanel.repaint();
		}
	}
	/* Timer Ŭ���� �� */

	/* Listener ���� */
	public class MyMouseListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			// �����г� Ŭ���ϸ� ó�� �׷��ִ°� ���߰�
			initPaintTimer.stop();

			// gamePanel�� statePanel �׷���
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
	/* Listener �� */

	/* ��ɱ��� �޼ҵ� ���� */
	// ballList �ʱ�ȭ �޼ҵ�
	public void initBallList() {
		this.ballList.clear();
		this.ballList.add(new Ball(ballImgURL, FRAME_WIDTH / 2, stick.y - 20, stick));
	}

	// ball �������ָ� �߶��ߴ��� Ȯ���ϴ� �޼ҵ�
	public void isBallFall() {
		// ��� ���� ���ؼ�
		for (Ball ball : ballList)
			// ���������� Ȯ��, ������ ��� toRemoveBall�� ��ü ����
			if (ball.move()) {
				// ȿ���� ���
				failClip.start();
				failClip.setFramePosition(0);

				toRemoveBall.add(ball);
			}

		// ������ �� ����
		for (Ball removeBall : toRemoveBall)
			ballList.remove(removeBall);

		// ���� ���� ���ٸ�
		if (ballList.size() == 0) {
			// ���� ������ clear
			itemList.clear();

			// life�� ��� ���� �� ���� �ʱ�ȭ
			if (lifeCount == 0) {
				gameBGMClip.setFramePosition(0);
				resetGame();
				return;
			}

			// life�� ���� ��� lifeImgList--, lifeCount--
			lifeImgList.remove(lifeCount - 1);
			lifeCount--;

			// Life �۾��� ��� 0���� �� ����������
			if (lifeCount == 0)
				statePanel.life.setForeground(Color.red);

			// �ٽ� �����ϱ� �� ���ο� �� ������ֱ�
			initBallList();

			// ������ �ٽ� �����ϱ� ���� statePanel, gamePanel �׷��ֱ� ����
			// initPaint ����
			oneSecondTimer.stop();
			oneMilliSecondTimer.stop();
			initPaintTimer.start();
		}
	}

	// ���� �� ���
	public void isBreakBlock() {
		for (ArrayList<Block> firstDimension : blockList) {
			for (Block block : firstDimension) {
				for (Ball ball : ballList) {
					if (block.isBreak(ball)) {
						// ȿ���� ���
						crashClip.start();
						crashClip.setFramePosition(0);

						// 30% Ȯ���� ������ ���
						// 0~3 �� ������ �־� ������ ���� ����
						if (((int) (Math.random() * 10)) < probability)
							itemList.add(new Item(block.getCenterPoint(), (int) (Math.random() * 4), stick));

						// ���� �߰�
						OneSecondTimerClass.score += 5;
						statePanel.score.setText("Score  " + String.format("%d", OneSecondTimerClass.score));

						toRemoveBlock.add(block);
					}
				}

				// ���� deadLine�� ���� ���
				if (block.y + block.height >= 550) {
					resetGame();
					return;
				}
			}

			for (Block removeblock : toRemoveBlock)
				firstDimension.remove(removeblock);
		}

		// ������ ����Ʈ��
		for (Item item : itemList) {
			item.itemDrop();

			// sitck�� ������
			if ((stick.x <= item.x && item.x <= stick.x + stick.width)
					&& (stick.y <= item.y && item.y <= stick.y + stick.height)) {
				// ȿ���� ���
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

			// ���� ��ġ ������ ����
			if (item.y >= ITEM_DEADLINE)
				toRemoveItem.add(item);
		}

		for (Item removeItem : toRemoveItem)
			itemList.remove(removeItem);
	}

	// ���� �ʱ�ȭ �޼ҵ�
	public void resetGame() {
		// Ÿ�̸� ����
		oneSecondTimer.stop();
		oneMilliSecondTimer.stop();

		// ������ �ʱ�ȭ
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

		// �� �ʱ�ȭ
		initBallList();
		// �ٽ� ����
		initPaintTimer.start();
	}
	/* ��ɱ��� �޼ҵ� �� */

	// ���� �޼ҵ�
	public static void main(String[] args) {
		Main main = new Main();
		main.go();
	}
}