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

	// ��ġ ���밪 ����
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

	// �̹��� ����
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
	// ���� �ð� ���� Timer
	Timer statePanelTimer;
	private final int STATEPANELTIMER_DELAY = 1000;
	StatePanelTimerClass statePanelTimerClass = new StatePanelTimerClass();
	// 0.005�ʸ��� �����г� �׷��ִ� Timer
	Timer gamePanelTimer;
	private final int GAMEPANELTIMER_DELAY = 1;
	GamePanelTimerClass gamePanelTimerClass = new GamePanelTimerClass();
	// ���� ���� �� �� ��ġ�� ��� �׷��ֱ� ���� Timer
	Timer initPaintTimer;
	private final int INITBALLPAINTTIMER_Delay = 1;
	InitPaintClass initPaintClass = new InitPaintClass();

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
			setComponent();

			statePanelTimer = new Timer(STATEPANELTIMER_DELAY, statePanelTimerClass);
		}

		public void setComponent() {
			// life ����
			life = new JLabel("Life");
			life.setBounds(30, 15, 50, 35);
			life.setFont(new Font("Rix¯�� M", Font.ITALIC, 30));
			life.setForeground(Color.black);
			add(life);

			// playingTime ����
			playingTime = new JLabel("Time 00:00");
			playingTime.setBounds(200, 15, 170, 35);
			playingTime.setFont(new Font("Rix¯�� M", Font.ITALIC, 30));
			playingTime.setForeground(Color.black);
			add(playingTime);

			// score ����
			score = new JLabel("Score");
			score.setBounds(390, 15, 170, 35);
			score.setFont(new Font("Rix¯�� M", Font.ITALIC, 30));
			score.setForeground(Color.black);
			add(score);

			// �ʱ� life 2���� �ʱ�ȭ
			for (int i = 0; i < 2; i++)
				lifeImgList.add(ballImg);
		}

		public void paintComponent(Graphics g) {
			// StatePanel ��� �׸���
			g.drawImage(statePanelBackImg.getImage(), 0, 0, null);

			// ���� life �׸���
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

			// blockList�� Block ��ü ����
			// 6�� 10��
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
			// GamePanel ��� �׸���
			g.drawImage(backGroundImg.getImage(), 0, 0, FRAME_WIDTH, GAMEPANEL_HEIGHT, null);

			// deadLine �׸���
			g.drawImage(deadLineImg.getImage(), 0, DEADLINE_HEIGHT, FRAME_WIDTH, 5, null);

			// stick �׸���
			try {
				stick.x = this.getMousePosition().x - 45;
				stick.draw(g);
			} catch (Exception e) {
			}

			// �� �׸���
			for (ArrayList<Block> firstDimension : blockList)
				for (Block block : firstDimension)
					block.draw(g);

			// ������ �׸���
			for (Item item : itemList)
				item.draw(g);

			// ball �׸���
			for (Ball ball : ballList)
				ball.draw(g);
		}
	}
	/* �г� Ŭ���� �� */

	/* Timer Ŭ���� ���� */
	public class StatePanelTimerClass implements ActionListener {
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

			// 10�ʸ��� ������ ������
			if (time % BLOCKDOWNDELAY == 0) {
				for (ArrayList<Block> firstDimension : blockList) {
					for (Block block : firstDimension)
						block.y += 24;
				}
			}
		}
	}

	// ���� �����ϸ� �����ϴ� Timer Ŭ����
	public class GamePanelTimerClass implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			isBallFall();
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
	/* Listener �� */

	/* ��ɱ��� �޼ҵ� ���� */
	public void endGame() {
		/*
		 * �����ʿ�
		 */
	}

	public void initBallList() {
		this.ballList.clear();
		this.ballList.add(new Ball(ballImgURL, FRAME_WIDTH / 2, stick.y - 20, stick));
	}

	public void isBallFall() {
		// ��� ���� ���ؼ�
		for (Ball ball : ballList)
			// ���������� Ȯ��, ������ ��� toRemoveBall�� ��ü ����
			if (!ball.move())
				toRemoveBall.add(ball);

		// ������ �� ����
		for (Ball removeBall : toRemoveBall)
			ballList.remove(removeBall);

		// ���� ���� ���ٸ�
		if (ballList.size() == 0) {
			// ���� ������ clear
			itemList.clear();

			// life�� ��� ���� �� ����
			if (lifeCount == 0)
				System.exit(1);

			// life�� ���� ��� lifeImgList--, lifeCount--
			lifeImgList.remove(lifeCount - 1);
			lifeCount--;

			// Life �۾��� ��� 0���� �� ����������
			if (lifeCount == 0)
				statePanel.life.setForeground(Color.red);

			// ������ �ٽ� �����ϱ� ���� statePanel, gamePanel �׷��ֱ� ����
			// initPaint ����
			statePanelTimer.stop();
			gamePanelTimer.stop();
			initPaintTimer.start();

			// �ٽ� �����ϱ� �� ���ο� �� ������ֱ�
			initBallList();
		}
	}

	public void isBreakBlock() {
		for (ArrayList<Block> firstDimension : blockList) {
			for (Block block : firstDimension) {
				for (Ball ball : ballList) {
					if (block.isBreak(ball)) {
						// 20% Ȯ���� ������ ���
						// 0~3 �� ������ �־� ������ ���� ����
						if (((int) (Math.random() * 10)) < 10)
							itemList.add(new Item(block.getCenterPoint(), (int) (Math.random() * 4), stick));

						// ���� �߰�
						statePanelTimerClass.score += 5;
						statePanel.score.setText("Score  " + String.format("%d", statePanelTimerClass.score));

						toRemoveBlock.add(block);
					}
				}

				// ���� deadLine�� ���� ���
				if (block.y + block.height >= 550)
					/*
					 * �������� �޼ҵ� ����� �߰��ϱ� !!!!!!!!!!!!!!
					 */
					System.exit(1);
			}

			for (Block removeblock : toRemoveBlock)
				firstDimension.remove(removeblock);
		}

		// ������ ����Ʈ��
		for (Item item : itemList) {
			item.itemDrop();

			// ���� ��ġ ������ ����
			if (item.y >= ITEM_DEADLINE) {
				toRemoveItem.add(item);
				for (Item removeItem : toRemoveItem)
					itemList.remove(removeItem);
			}
		}
	}
	/* ��ɱ��� �޼ҵ� �� */

	// ���� �޼ҵ�
	public static void main(String[] args) {
		Main main = new Main();
		main.go();
	}
}