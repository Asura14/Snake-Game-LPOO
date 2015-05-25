package snake.logic;

import java.awt.Point;
import java.util.LinkedList;
/**
 * Snake.java - Class responsible for creating and tracking the snake
 * @author Luis
 * @see Direction
 */
public class Snake extends LinkedList<Point> {

	private Point head;
	private Point tail;
	private int size;
	private Boolean alive;
	private int highscore;
	private int extraScore;
	
	/**
	 * Snake Constructor
	 */
	public Snake() {
		this.add(new Point(0,2));
		this.add(new Point(0,1));
		this.add(new Point(0,0));
		alive = true;
		head = getHead();
		tail = getTail();
		int extraScore = 0;
	}
	
	/**
	 * Set's snake's size
	 */
	public void setSize() {
		size = this.size();
	}
	/**
	 * Sets snake's death
	 */
	public void setDeath() {
		alive = false;
	}
	/**
	 * sets highscore
	 * @param highscore
	 */
	public void setHighscore(int highscore) {
		this.highscore = highscore;
	}
	/**
	 * Increments extraScore
	 * @param extraScore
	 */
	public void addExtraScore(int extraScore) {
		this.extraScore += extraScore;
	}
	
	/**
	 * Returns snake's tail
	 * @return tail
	 */
	public Point getTail() {
		return tail;
	}
	/**
	 * Returns snake's head
	 * @return head
	 */
	public Point getHead() {
		return head;
	}
	/**
	 * Returns the score from the spider 
	 * @return extraScore
	 */
	public int getExtraScore() {
		return extraScore;
	}
	
	/**
	 * Returns the score from the apples
	 * @return highscore
	 */
	public int getHighscore() {
		if (highscore < this.getScore()) {
			highscore = this.getScore();
		}
		return highscore;
	}
	/**
	 * Calculates the score based on the snake's size and the number of spiders it has eaten
	 * @return
	 */
	public int getScore() {
		if (size() == 3) {
			return 0;
		} else return ((size() - 3) * 10) + extraScore;
	}
	/**
	 *  Returns true if snake is alive
	 * @return
	 */
	public Boolean isSnakeAlive() {
		return alive;
	}
	
}
