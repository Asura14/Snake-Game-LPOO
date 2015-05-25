package snake.logic;
import java.applet.Applet;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;

import snake.user.MenuCanvas;
import java.io.*;

/**
 * SnakeApplet.java - Class that initializes the Applet on which everything shall be displayed
 * @author Luis
 *
 */
public class SnakeApplet extends Applet implements ActionListener{

	public static final int MAIN_MENU = 0;
	public static final int SECOND_MENU = 1;
	public static final int SNAKE_GAME = 2;
	public static final int HIGHSCORE = 3;
	public static final int ALTERNATE_GAME = 3;

	private static SnakeCanvas snakeC;
	private static MenuCanvas menu;
	public static int visibleCanvas = 0;


	/*
	 * (non-Javadoc)
	 * @see java.applet.Applet#init()
 	*/
	public void init() {
		playSound();
		initStandardGame();
		initMainMenu();

		menu.setBackground(Color.black);
		menu.setVisible(true);
		menu.setFocusable(true);
		menu.setPreferredSize(new Dimension(640, 480));
	}

	public void paint(Graphics g) {
		this.setBackground(Color.black);
		this.setSize(new Dimension(640, 480));
	}

	/**
	 * Initializes initial Menu
	 */
	public void initMainMenu() {

		menu = new MenuCanvas();
		this.add(menu);
		this.setVisible(true);
		this.setSize(new Dimension(640, 480));
	}

	/**
	 * Initializes snake game canvas
	 */
	public void initStandardGame() {

		snakeC = new SnakeCanvas();
		this.add(snakeC);
		this.setVisible(true);
		this.setSize(new Dimension(640, 480));
	}

	/**
	 * Switches between Menus and the game
	 */
	public static void switchMenus() {
		switch (visibleCanvas) {
		case 0:

			snakeC.setVisible(false);
			menu.setBackground(Color.black);
			menu.setVisible(true);
			menu.setFocusable(true);
			menu.setPreferredSize(new Dimension(640, 480));
			menu.revalidate();
			break;

		case 2:
			snakeC.setBackground(Color.black);
			snakeC.setPreferredSize(new Dimension(640, 480));
			snakeC.setVisible(true);
			snakeC.setFocusable(true);
			snakeC.requestFocus();
			snakeC.revalidate();
			break;
		}
	}

	
	@Override
	/**
	 * 
	 */
	public void actionPerformed(ActionEvent arg0) {

	}
	
	/**
	 * Function responsible for the background music
	 */
	public static synchronized void playSound() {
		new Thread(new Runnable() {
			// The wrapper thread is unnecessary, unless it blocks on the
			// Clip finishing; see comments.
			public void run() {
				try {
					Clip clip = AudioSystem.getClip();
					AudioInputStream inputStream = AudioSystem.getAudioInputStream(
							SnakeApplet.class.getResourceAsStream("soundtrack2.wav"));
					clip.open(inputStream);
					clip.loop(Clip.LOOP_CONTINUOUSLY);
				} catch(UnsupportedAudioFileException ae){
					System.err.println("Audio not supported");
				} catch (Exception e) {
				}
			} 
		}).start();
	}
}
