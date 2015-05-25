package snake.test;

import static org.junit.Assert.*;

import java.awt.Point;
import org.junit.Test;
import snake.logic.Snake;
import snake.logic.SnakeCanvas;

/**
 * Class responsible for snake unit tests
 * @author Luis
 *
 */
public class snakeTest {
	/**
	 * Tests if snake constructor is correct
	 * Snake should start with head at (0,2)
	 */
	@Test
	public void testInitialSnake() {
		Snake snake = new Snake();
		assertTrue(snake.getScore() == 0);
		assertTrue(snake.isSnakeAlive());
	}

	/**
	 * Tests if snake moved one space forward
	 * initial snakes's head coords (0,2) -> final (0,3) 
	 */
	@Test
	public void testMove() {
//		Arrange
		Snake snake = new Snake();
//		Act
		move(snake);
//		Assert
		assertEquals(3, snake.peekFirst().y);
		assertEquals(0, snake.peekFirst().x);
	}

	/**
	 * Tests if snake ate fruit and grew
	 * Initial snake's size is 3 and becomes 4
	 */
	@Test
	public void testGetFruit() {
//		Arrange
		Snake snake = new Snake();
//		act
		move(snake);
		move(snake);
//		Assert
		assertTrue(snake.size() == 4);
	}

	/**
	 * Overload of original move function from SnakeCanvas (only moving down)
	 * @param snake
	 */
	public void move(Snake snake) {
		Point food = new Point(0, 4);
		Point head = snake.peekFirst();
		Point newPoint = head;
		//move down
		newPoint = new Point(head.x, head.y + 1);
		//delete tail
		snake.remove(snake.peekLast());

		if (newPoint.equals(food)) {
			//snake eats food
			Point addPoint = (Point) newPoint.clone();
			newPoint = new Point(head.x, head.y + 1);
			snake.push(addPoint);

		} else if (newPoint.x < 0 || newPoint.x > SnakeCanvas.GRID_WIDTH -1 || newPoint.y < 0 || newPoint.y > SnakeCanvas.GRID_HEIGHT -1) {
			//out of bounds
			snake.setDeath();
		}
		snake.push(newPoint);
	}

}
