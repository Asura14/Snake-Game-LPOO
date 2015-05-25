package snake.logic;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.Random;
import javax.swing.JOptionPane;
import snake.user.FileManagement;

/**
 * SnakeCanvas.java - Class responsible for the actual game Canvas
 * @author Luis
 *
 */
public class SnakeCanvas extends Canvas implements Runnable, KeyListener {

	public final int BOX_HEIGHT = 20, BOX_WIDTH = 20;
	public static final int GRID_HEIGHT = 20;
	public static final int GRID_WIDTH = 20;

	Snake snake;
	private Point food;
	SpecialSpider specialFruit = SpecialSpider.getInstance();

	private int direction = Direction.NO_DIRECTION;
	Boolean gameRun = true;
	int highscore = 0;
	String name = "";
	public static Boolean alternate;
	FileManagement file = new FileManagement();

	//
	Image snakeBody;
	Image apple;
	Image headUp, snakeDown, snakeRight, headLeft;
	Image background;
	Image spiderUp, spiderDown, spiderLeft, spiderRight;
	//


	private Thread runThread;	


	/**
	 * Function that draws all the game members
	 */
	public void paint(Graphics g) {

		if (runThread == null) {
			this.setPreferredSize(new Dimension(640, 480));
			this.addKeyListener(this);
			runThread = new Thread(this);
			runThread.start();
		}

		if (snake == null || !snake.isSnakeAlive()) {
			generateDefaultSnake();	
			placeFruit();
		}

		drawWallsOnly(g);
		drawFood(g);
		drawSnake(g);
		drawScore(g);
		drawHighscore(g);
	}

	/**
	 * Function responsible for the double buffering
	 */
	public void update (Graphics g) {

		Graphics offScreenGraphics;
		BufferedImage offScreenImage = null;
		Dimension d = this.getSize();

		offScreenImage = new BufferedImage(d.width, d.height,BufferedImage.TYPE_INT_ARGB);
		offScreenGraphics = offScreenImage.getGraphics();
		offScreenGraphics.setColor(Color.black);
		offScreenGraphics.fillRect(0, 0, d.width, d.height);
		offScreenGraphics.setColor(Color.black);
		paint(offScreenGraphics);

		//flip

		g.drawImage(offScreenImage, 0, 0, this);
	}

	/**
	 * Generates the initial snake that starts at the top left corner of the screen
	 * Also (if alternate game is chosen) generates the spider
	 */
	public void generateDefaultSnake() {
		try {
			getFromFile();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		name = file.getName();
		highscore = file.getScore();
		snake = new Snake();
		snake.setHighscore(highscore);
		direction = Direction.NO_DIRECTION;

		if (alternate) {
			specialFruit.x = 5;
			specialFruit.y = 5;
			specialFruit.setActive(true);
			specialFruit.movable = true;
			specialFruit.setGenerated(true);
			specialFruit.score = 0;
		} else {
			specialFruit.setActive(false);
			specialFruit.movable = false;
		}
	}

	/**
	 * Move function that moves the snake forward
	 * Also is responsible for all the other game objects, if they are eaten or 
	 */
	public void move() {
		if (specialFruit.getActive()  && !specialFruit.isGenerated() && !specialFruit.movable) {
			specialFruit.placeSpecialFruit(snake, food);
			specialFruit.setGenerated(true);
		}
		//Check is sniper should be deleted
		specialFruit.deleteSpider();

		Point head = snake.peekFirst();
		Point newPoint = head;
		switch (direction) {
		case Direction.NORTH:
			newPoint = new Point(head.x, head.y - 1);
			break;

		case Direction.SOUTH:
			newPoint = new Point(head.x, head.y + 1);
			break;

		case Direction.WEST:
			newPoint = new Point(head.x - 1, head.y);
			break;

		case Direction.EAST:
			newPoint = new Point(head.x + 1, head.y);
			break;
		}

		//removes snake's tail
		if (this.direction != Direction.NO_DIRECTION) {
			snake.remove(snake.peekLast());
		}
		if (food != null) {

			if (newPoint.equals(food)) {
				//snake eats food
				Point addPoint = (Point) newPoint.clone();
				switch (direction) {
				case Direction.NORTH:
					newPoint = new Point(head.x, head.y - 1);
					break;

				case Direction.SOUTH:
					newPoint = new Point(head.x, head.y + 1);
					break;

				case Direction.WEST:
					newPoint = new Point(head.x - 1, head.y);
					break;

				case Direction.EAST:
					newPoint = new Point(head.x + 1, head.y);
					break;
				}
				snake.push(addPoint);
				placeFruit();
				if (!specialFruit.active) {
					specialFruit.changeActive();
				}
			} else if(newPoint.equals(specialFruit)) {
				snake.addExtraScore(50);
				specialFruit.active = false;
				specialFruit.setGenerated(false);
			} else if (newPoint.x < 0 || newPoint.x > GRID_WIDTH -1 || newPoint.y < 0 || newPoint.y > GRID_HEIGHT -1) {
				//out of bounds
				if (highscore < snake.getHighscore()) {
					highscore = snake.getHighscore();
					file.setScore(highscore);
					do {
						try {
							name = JOptionPane.showInputDialog("Congratulations! New Highscore set. Enter your name:");
						} catch(Exception e) {
							System.out.println(e);
						}
						if(name==null)
							name="";
					} while(name=="");
				}
				file.setName(name);
				try {
					saveToFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				snake.setDeath();
				return;
			} else if (snake.contains(newPoint)) {
				//snake collides with itself
				if (highscore < snake.getHighscore()) {
					highscore = snake.getHighscore();
					file.setScore(highscore);
					do {
						try {
							name = JOptionPane.showInputDialog("Congratulations! New Highscore set. Enter your name:");
						} catch(Exception e) {
							System.out.println(e);
						}
						if(name==null)
							name="";
					} while(name=="");
				}
				file.setName(name);
				try {
					saveToFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				snake.setDeath();
				return;
			} else if (snake.size() == (GRID_HEIGHT*GRID_WIDTH)) {
				generateDefaultSnake();

			} else if (newPoint.equals(food)) {

			}
		}
		//if we reach this point game continues
		snake.push(newPoint);
	}

	/**
	 * Draws Grid
	 * Function not used it final code but useful for visualization and testing purposes
	 * @param g
	 */
	public void drawGrid(Graphics g) {
		//Draw the outside frame for the game
		g.drawRect(0,0, GRID_WIDTH * BOX_WIDTH, GRID_HEIGHT * BOX_HEIGHT);

		//Draw the horizontal Grid lines
		for (int i = BOX_WIDTH; i < GRID_WIDTH * BOX_WIDTH; i += BOX_WIDTH) {
			g.drawLine(i, 0, i, BOX_HEIGHT*GRID_HEIGHT);
		}

		//Draw the Vertical Grid lines
		for (int i = BOX_HEIGHT; i < GRID_HEIGHT*BOX_HEIGHT; i += BOX_HEIGHT) {
			g.drawLine(0, i, GRID_WIDTH*BOX_WIDTH, i);
		}
	}

	/**
	 * Draws the game's walls
	 * @param g
	 */
	public void drawWallsOnly(Graphics g) {
		//Draw the outside frame for the game
		g.setColor(Color.white);
		g.drawRect(0,0, GRID_WIDTH * BOX_WIDTH, GRID_HEIGHT * BOX_HEIGHT);
		g.drawImage(background,0,0, GRID_WIDTH * BOX_WIDTH, GRID_HEIGHT * BOX_HEIGHT, this);
	}

	/**
	 * Draws Snake
	 * @param g
	 */
	public void drawSnake(Graphics g) {
		//g.setColor(Color.green);
		Iterator<Point> iterator = snake.iterator();
		while(iterator.hasNext()){
			Point p = iterator.next();
			//g.fillRect(p.x * BOX_WIDTH, p.y * BOX_HEIGHT, BOX_WIDTH, BOX_HEIGHT);
			if (p == snake.peekFirst()){
				if (this.direction == Direction.NORTH) {
					g.drawImage(headUp, p.x * BOX_WIDTH, p.y * BOX_HEIGHT, BOX_WIDTH, BOX_HEIGHT, this);
				} else if (this.direction == Direction.SOUTH || this.direction == Direction.NO_DIRECTION) {
					g.drawImage(snakeDown, p.x * BOX_WIDTH, p.y * BOX_HEIGHT, BOX_WIDTH, BOX_HEIGHT, this);
				} else if (this.direction == Direction.EAST) {
					g.drawImage(snakeRight, p.x * BOX_WIDTH, p.y * BOX_HEIGHT, BOX_WIDTH, BOX_HEIGHT, this);
				} else if (this.direction == Direction.WEST) {
					g.drawImage(headLeft, p.x * BOX_WIDTH, p.y * BOX_HEIGHT, BOX_WIDTH, BOX_HEIGHT, this);
				}
			} else {
				g.drawImage(snakeBody, p.x * BOX_WIDTH, p.y * BOX_HEIGHT, BOX_WIDTH, BOX_HEIGHT, this);
			}
		}
		g.setColor(Color.black);
	}

	/**
	 * Draws food, both fruit and the spider
	 * @param g
	 */
	public void drawFood(Graphics g) {
		/*
		g.setColor(Color.red);
		g.fillOval(food.x * BOX_WIDTH, food.y * BOX_HEIGHT, BOX_WIDTH, BOX_HEIGHT);
		g.setColor(Color.black);*/
		if (specialFruit.generated) {
			switch (specialFruit.direction) {
			case Direction.NORTH:
				g.drawImage(spiderUp, specialFruit.x * BOX_WIDTH, specialFruit.y * BOX_HEIGHT, BOX_WIDTH, BOX_HEIGHT, this);
				break;

			case Direction.SOUTH:
				g.drawImage(spiderDown, specialFruit.x * BOX_WIDTH, specialFruit.y * BOX_HEIGHT, BOX_WIDTH, BOX_HEIGHT, this);
				break;

			case Direction.WEST:
				g.drawImage(spiderLeft, specialFruit.x * BOX_WIDTH, specialFruit.y * BOX_HEIGHT, BOX_WIDTH, BOX_HEIGHT, this);
				break;

			case Direction.EAST:
				g.drawImage(spiderRight, specialFruit.x * BOX_WIDTH, specialFruit.y * BOX_HEIGHT, BOX_WIDTH, BOX_HEIGHT, this);
				break;
			}
		}

		g.drawImage(apple, food.x * BOX_WIDTH, food.y * BOX_HEIGHT, BOX_WIDTH, BOX_HEIGHT, this);
	}

	/**
	 * draws player's/snake's current score
	 * @param g
	 */
	public void drawScore(Graphics g) {
		g.setColor(Color.red);
		g.setFont(new Font("BankGothic", Font.PLAIN, 30));
		g.drawString("Score: " + snake.getScore(), 0, BOX_HEIGHT * GRID_HEIGHT + 30); 

		if (alternate){
			g.drawString("Spider: " + specialFruit.score, 200, BOX_HEIGHT * GRID_HEIGHT + 30);
		}
	}

	/**
	 * Draws player/snake's highscore on the screen
	 * @param g
	 */
	public void drawHighscore(Graphics g) {
		g.setColor(Color.green);
		g.setFont(new Font("BankGothic", Font.PLAIN, 35));
		g.drawString("Highscore", GRID_WIDTH * BOX_WIDTH + 20 , 30);

		g.setColor(Color.red);
		g.setFont(new Font("BankGothic", Font.PLAIN, 55));
		g.drawString(Integer.toString(snake.getHighscore()), GRID_WIDTH * BOX_WIDTH + 60 , 95);

		g.setFont(new Font("BankGothic", Font.PLAIN, 30));
		g.drawString(name, GRID_WIDTH * BOX_WIDTH + 65, 130);
		g.drawString("Press ESC", GRID_WIDTH * BOX_WIDTH + 20, 330);
		g.setFont(new Font("BankGothic", Font.PLAIN, 20));
		g.drawString("to go to main menu", GRID_WIDTH * BOX_WIDTH + 10, 350);
	}

	/**
	 * Finds a valid random place to generate the fruit
	 */
	public void placeFruit() {
		Random rand = new Random();
		int randomX = rand.nextInt(GRID_WIDTH);
		int randomY = rand.nextInt(GRID_HEIGHT);
		Point randomPoint = new Point(randomX, randomY);
		while(snake.contains(randomPoint) || specialFruit == randomPoint) {
			randomX = rand.nextInt(GRID_WIDTH);
			randomY = rand.nextInt(GRID_HEIGHT);
			randomPoint = new Point(randomX, randomY);
		}
		food = randomPoint;
	}

	/**
	 * Loads images to use in the actual game
	 */
	public void loadImages() {
		URL imagePath = SnakeCanvas.class.getResource("Snakebody.png");
		snakeBody = Toolkit.getDefaultToolkit().getImage(imagePath);
		URL imagePath2 = SnakeCanvas.class.getResource("SnakeHeadUP.gif");
		headUp = Toolkit.getDefaultToolkit().getImage(imagePath2);
		URL imagePath3 = SnakeCanvas.class.getResource("SnakeHeadDOWN.gif");
		snakeDown = Toolkit.getDefaultToolkit().getImage(imagePath3);
		URL imagePath4 = SnakeCanvas.class.getResource("SnakeHeadLEFT.gif");
		headLeft = Toolkit.getDefaultToolkit().getImage(imagePath4);
		URL imagePath5 = SnakeCanvas.class.getResource("SnakeHeadRIGHT.gif");
		snakeRight = Toolkit.getDefaultToolkit().getImage(imagePath5);
		URL imagePath6 = SnakeCanvas.class.getResource("apple20.png");
		apple = Toolkit.getDefaultToolkit().getImage(imagePath6);
		URL imagePath7 = SnakeCanvas.class.getResource("background.png");
		background = Toolkit.getDefaultToolkit().getImage(imagePath7);
		URL imagePath8 = SnakeCanvas.class.getResource("spiderGIF.gif");
		spiderUp = Toolkit.getDefaultToolkit().getImage(imagePath8);
		URL imagePath9 = SnakeCanvas.class.getResource("spiderGIFdown.gif");
		spiderDown = Toolkit.getDefaultToolkit().getImage(imagePath9);
		URL imagePath10 = SnakeCanvas.class.getResource("spiderGIFleft.gif");
		spiderLeft = Toolkit.getDefaultToolkit().getImage(imagePath10);
		URL imagePath11 = SnakeCanvas.class.getResource("spiderGIFright.gif");
		spiderRight = Toolkit.getDefaultToolkit().getImage(imagePath11);
	}
	
	@Override
	/**
	 * Standard run function responsible for game's cicle
	 */
	public void run() {
		this.revalidate();
		loadImages();
		repaint();

		while(gameRun) {
			if (this.direction != Direction.NO_DIRECTION) {
				move();
			}
			repaint();

			try {
				Thread.currentThread();
				Thread.sleep(80);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	/**
	 * If game type is alternate then move spider
	 * @param e
	 */
	public void keyPressed(KeyEvent e) {
		if (alternate) {
			if (specialFruit.moveSpider(e, snake, food)) {
				placeFruit();
			}
		}
	}

	@Override
	/**
	 * Controls for user
	 * @param e
	 */
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			if(direction != Direction.SOUTH){
				direction = Direction.NORTH;
			}
			break;
		case KeyEvent.VK_DOWN:
			if (direction != Direction.NORTH) {
				direction = Direction.SOUTH;
			}
			break;
		case KeyEvent.VK_RIGHT:
			if (direction != Direction.WEST){
				direction = Direction.EAST;
			}
			break;
		case KeyEvent.VK_LEFT:
			if(direction != Direction.EAST) {
				direction = Direction.WEST;
			}
			break;
		case KeyEvent.VK_ESCAPE:
			SnakeApplet.visibleCanvas = 0;
			SnakeApplet.switchMenus();
			generateDefaultSnake();
			break;
		}
		if (alternate) {
			if (specialFruit.moveSpider(e, snake, food)) {
				placeFruit();
			}
		}
	}

	@Override
	/**
	 * 
	 */
	public void keyTyped(KeyEvent e) {

	}



	/**
	 * Read Highscore from File
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void getFromFile() throws ClassNotFoundException, IOException {
		ObjectInputStream is = null;
		try {
			is = new ObjectInputStream(new FileInputStream("Highscores.dat"));
			file = (FileManagement) is.readObject();
		}
		catch (IOException e) {
		}
		finally { 
			if (is != null)
				is.close(); 
		}
	}

	/**
	 * Save highscore to file
	 * @throws IOException
	 */
	public void saveToFile() throws IOException {
		ObjectOutputStream os = null;
		try {
			os = new ObjectOutputStream(new FileOutputStream ("Highscores.dat"));
			os.writeObject(file);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (os!= null){
				os.close();
			}
		}
	}
}
