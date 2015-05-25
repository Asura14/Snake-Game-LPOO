package snake.user;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Scanner;


/**
 * Class responsible for managing the file information
 * @author Luis
 *
 */
public class FileManagement implements Serializable{

	public String name;
	/**
	 * Sets the name for the person that set the highscore
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * sets highest score
	 * @param score
	 */
	public void setScore(int score) {
		this.score = score;
	}

	public int score;

	/**
	 * Constructor
	 */
	public FileManagement() {
		name = "";
		score = 0;
	}

	/**
	 * gets name
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets highscore
	 * @return
	 */
	public int getScore() {
		return score;
	}
}
