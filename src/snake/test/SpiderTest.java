package snake.test;

import static org.junit.Assert.*;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

import org.junit.Test;

import snake.logic.Direction;
import snake.logic.Snake;
import snake.logic.SnakeCanvas;
import snake.logic.SpecialSpider;

/**
 * class responsible for snake's unit tests
 * @author Luis
 *
 */
public class SpiderTest {

	/**
	 * Tests if spider is initialized as expected
	 */
	@Test
	public void tesInitSpider() {
		//Arranje
		SpecialSpider spider = SpecialSpider.getInstance();
		//Assert
		assertTrue(spider.getScore() == 0);
	}

	/**
	 * Tests if spider moved down
	 */
	@Test
	public void testSpiderMove() {
		//Arranje
		Snake snake = new Snake();
		Point fruit = new Point();	
		SpecialSpider spider = SpecialSpider.getInstance();
		spider.x = 5; spider.y = 5;
		//Act
		moveSpider(spider, snake, fruit);
		//Assert
		assertTrue(spider.x == 5 && spider.y == 6);
	}

	/**
	 * Tests if spider's score increases after eating the food
	 */
	@Test
	public void testSpiderEatingFruit() {
//		Arrange
		Snake snake = new Snake();
		Point fruit = new Point(5,7);
		SpecialSpider spider = SpecialSpider.getInstance();
		spider.x = 5; spider.y = 5;
//		Act
		moveSpider(spider, snake, fruit);
		moveSpider(spider, snake, fruit);
//		Assert
		assertEquals(50,spider.getScore());
	}

	/**
	 * Overload of the Spider move function (only moving down)
	 * @param spider
	 * @param snake
	 */
	public void moveSpider(SpecialSpider spider, LinkedList<Point> snake, Point food) {
		Point newPoint;
		newPoint = new Point(spider.x, spider.y+1);
		if (spider.y >= 0 && !snake.contains(newPoint)) {
			spider.y += 1;
		}
		if (spider.x == food.x && spider.y == food.y) {
			spider.score += 50;
		}
	}
}
