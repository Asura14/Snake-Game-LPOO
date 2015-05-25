package snake.user;
import snake.logic.*;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.annotation.Generated;
import javax.imageio.ImageIO;

import snake.logic.SnakeApplet;

/**
 * Class responsible for the managing of the menus
 * @author Luis
 *
 */
public class MenuCanvas extends Canvas implements Runnable, KeyListener {

	private final int BOX_HEIGHT = 20, BOX_WIDTH = 20;
	private int choice1 = 1, choice2;
	private Boolean game = false;
	private int chosenMenu = 1;
	private URL menu1Path, menu2Path, secondMenu1Path, secondMenu2Path, secondMenu3Path;

	BufferedImage youLoose, youWin;
	Image menuImage, secondMenuImage;

	private Thread runThread;
	
	/**
	 * sets menus images
	 */
	public void setImages() {
		menu1Path = MenuCanvas.class.getResource("MainMenu1.png");
		menu2Path = MenuCanvas.class.getResource("MainMenu2.png");
		secondMenu1Path = MenuCanvas.class.getResource("SecondMenu1.png");
		secondMenu2Path = MenuCanvas.class.getResource("SecondMenu2.png");
		secondMenu3Path = MenuCanvas.class.getResource("SecondMenu3.png");
		
	}

	/**
	 * paints/udates screen depending on the option chosen and the menu displaying
	 */
	public void paint(Graphics g) {

		setImages();
		if (runThread == null) {
			this.setPreferredSize(new Dimension(640, 480));
			this.addKeyListener(this);
			runThread = new Thread(this);
			runThread.start();
		}
		if (chosenMenu == 1)  {
			drawMainMenu(g);
		} else if (chosenMenu == 2) {
			drawSecondMenu(g);
		}

	}

	/**
	 * Draws main menu
	 * Alternating between images depending on the player's choice
	 * @param g
	 */
	public void drawMainMenu(Graphics g) {
		if (choice1 == 1) {
			//URL imagePath = MenuCanvas.class.getResource("MainMenu1.png");
			menuImage = Toolkit.getDefaultToolkit().getImage(menu1Path);
		} else if (choice1 == 2) {
			//URL imagePath = MenuCanvas.class.getResource("MainMenu2.png");
			menuImage = Toolkit.getDefaultToolkit().getImage(menu2Path);
		}

		g.drawImage(menuImage,0,0,640,480,this);
	}

	/**
	 * Draws second menu
	 * Alternating between images depending on the player's choice
	 * @param g
	 */
	public void drawSecondMenu(Graphics  g) {
		if (choice2 == 1) {
			//URL imagePath = MenuCanvas.class.getResource("SecondMenu1.png");
			secondMenuImage = Toolkit.getDefaultToolkit().getImage(secondMenu1Path);
		} else if (choice2 == 2) {
			//URL imagePath = MenuCanvas.class.getResource("SecondMenu2.png");
			secondMenuImage = Toolkit.getDefaultToolkit().getImage(secondMenu2Path);
		} else if (choice2 == 3) {
			//URL imagePath = MenuCanvas.class.getResource("SecondMenu3.png");
			secondMenuImage = Toolkit.getDefaultToolkit().getImage(secondMenu3Path);
		}

		g.drawImage(secondMenuImage,0,0,640,480,this);
	}


	@Override
	/**
	 * 
	 */
	public void keyPressed(KeyEvent e) {

	}


	@Override
	/**
	 * User controls for navigating in the menu
	 * @param e
	 */
	public void keyReleased(KeyEvent e) {
		if (chosenMenu == 1) {
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				choice1 -= 1;
				if (choice1 == 0) {
					choice1 = 2;
				}
			} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				choice1 += 1;
				if (choice1 == 3) {
					choice1 = 1;
				}
			} else if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE) {
				if (choice1 == 1) {
					chosenMenu = 2;
					choice2 = 1;
				} else if (choice1 == 2) {
					System.exit(0);
				}
			}
		} else if (chosenMenu == 2) {
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				choice2 = choice2 - 1;
				if (choice2 == 0) {
					choice2 = 3;
				}
			} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				choice2 = choice2 + 1;
				if (choice2 == 4) {
					choice2 = 1;
				}
			} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				chosenMenu = 1;
			} else if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE) {
				if (choice2 == 1) {
					SnakeCanvas.alternate = false;
					SnakeApplet.visibleCanvas = 2;
					SnakeApplet.switchMenus();
				} else if (choice2 == 2) {
					SnakeCanvas.alternate = true;
					SnakeApplet.visibleCanvas = 2;
					SnakeApplet.switchMenus();
				} else if (choice2 == 3) {
					System.exit(0);
				}
			}
		}
	}

	@Override
	/**
	 * 
	 */
	public void keyTyped(KeyEvent e) {
		
	}


	@Override
	/**
	 * Function responsible for the menu's thread
	 */
	public void run() {
		repaint();
		this.revalidate();
		while (!game) {
			repaint();
			try {
				Thread.currentThread();
				Thread.sleep(100);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
