package snake.logic;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Random;

/**
 * Class for spider
 * @author Luis
 *
 */
public class SpecialSpider extends Point {

	int type;
	boolean active;
	boolean generated;
	boolean movable;
	long time;
	int direction;
	public int score;
	
	private static SpecialSpider instance = null;
	
	/**
	 * Spider constructor
	 */
	protected SpecialSpider() {
		active = false;
		generated = false;
		movable = false;
		time = 0;
		direction = Direction.NORTH;
		score = 0;
	}
	
	/**
	 * Checks if object already exists and creates a new one in case it hasn't
	 * @return instance
	 */
	public static SpecialSpider getInstance() {
		 if (instance == null) {
	            instance = new SpecialSpider();
	        }
	        return instance;
	}


	//Getters
	/**
	 * 
	 * @return active
	 */
	public boolean getActive() {
		return active;
	}
	/**
	 * 
	 * @return generated
	 */
	public boolean isGenerated() {
		return generated;
	}
	/**
	 * 
	 * @return score
	 */
	public int getScore() {
		return score;
	}
	/**
	 * 
	 * @return direction
	 */
	public int getDirection() {
		return direction;
	}
	
	//Setters
	/**
	 * Sets is spider is active or not
	 * @param active
	 */
	public void setActive(boolean active) {
		this.active = active;
	}
	/**
	 * Sets if spider is generated or not
	 * @param generated
	 */
	public void setGenerated(boolean generated) {
		this.generated = generated;
	}
	
	/**
	 * Has a 20% probability of "activating" the special fruit every time the snake moves
	 */
	public void changeActive() {
		int activate;
		Random randomGenerator = new Random();
		activate = randomGenerator.nextInt(10);
		if (activate == 0 || activate == 1) {
			active = true;
		} else {
			active = false;
		}
	}
	
	/**
	 * Generates a valid location to draw the fruit
	 * @param snake
	 * @param food
	 */
	public void placeSpecialFruit(LinkedList<Point> snake, Point food) {
		Random rand = new Random();
		int randomX = rand.nextInt(SnakeCanvas.GRID_WIDTH);
		int randomY = rand.nextInt(SnakeCanvas.GRID_HEIGHT);
		Point randomPoint = new Point(randomX, randomY);
		while(snake.contains(randomPoint) && randomPoint == food) {
			randomX = rand.nextInt(SnakeCanvas.GRID_WIDTH);
			randomY = rand.nextInt(SnakeCanvas.GRID_HEIGHT);
			randomPoint = new Point(randomX, randomY);
		}
		x = randomX;
		y = randomY;
		generated = true;
		time = System.currentTimeMillis();
	}
	
	/**
	 * Moves Spider
	 * if it encounters the snake or a wall it won't move forward but if it encounters a fruit it'll eat it
	 * @param e
	 * @param snake
	 * @param food
	 * @return
	 */
	public boolean moveSpider(KeyEvent e, LinkedList<Point> snake, Point food) {
		Point newPoint;
		switch(e.getKeyCode()) {
		case KeyEvent.VK_A:
			newPoint = new Point(x-1, y);
			if (x-1 >= 0 && !snake.contains(newPoint)) {
				x -= 1;
				direction = Direction.WEST;
			}
			break;
			
		case KeyEvent.VK_D:
			newPoint = new Point(x+1, y);
			if (x+1 < SnakeCanvas.GRID_WIDTH && !snake.contains(newPoint)) {
				x += 1;
				direction = Direction.EAST;
			}
			break;
			
		case KeyEvent.VK_W:
			newPoint = new Point(x, y-1);
			if (y-1 >= 0 && !snake.contains(newPoint)){
				y -= 1;
				direction = Direction.NORTH;
			}
			break;
			
		case KeyEvent.VK_S:
			newPoint = new Point(x, y+1);
			if (y+1 < SnakeCanvas.GRID_HEIGHT && !snake.contains(newPoint)) {
				y += 1;
				direction = Direction.SOUTH;
			}
			break;
		}
		if (x == food.x && y == food.y) {
			score += 50;
			return true;
		}
		return false;
	}
	
	/**
	 * After 5 secs the Spider is deleted
	 */
	public void deleteSpider() {
		if (movable) { 
			return; 
		} else {
			if (System.currentTimeMillis() >= time + 5000) {
				active = false;
				generated = false;
			}
		}
	}
}
