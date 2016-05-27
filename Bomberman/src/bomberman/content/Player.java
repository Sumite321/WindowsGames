package bomberman.content;

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import game.engine2D.Entity;

public class Player extends Entity implements KeyListener, Runnable {
	private final Game game;
	private final Image[][] skins = new Image[4][2];
	private boolean leftPressed, rightPressed, upPressed, downPressed, spacePressed;
	private int bombs, explosion_size, skin, running;
	private double speed;

	/**
	 * Since this is a 2D game and their are collision checks therefore i use a
	 * bounding box which stores the x, y width and height of the player since
	 * the player has multiple skin stored in the sprite sheet each of them are
	 * cut and placed in a multidimensional array where the first array
	 * represent
	 * 
	 * @param character
	 *            the character id currently only 1 available
	 * @param x
	 *            the exact x position of the player
	 * @param y
	 *            the exact y position of the player
	 * @param game
	 *            the game which the player belongs to
	 * 
	 * @see Player#setSkins(int) setSkin(character_id)
	 * @see BoundingBox#BoundingBox(int, int, int, int)
	 *      BoundingBox(x,y,width,height)
	 */
	public Player(int character, int x, int y, Game game) {
		super(x+4,y+6,32,36,game);
		this.game = game;
		this.bombs = 1;
		this.speed = 2;
		this.explosion_size = 1;
		skin = 0;
		running = 0;
		setSkins(character);
	}

	/**
	 * The skin id(j) which is what position left, right etc. Then the second(i)
	 * represent running state another set of sprite will be added for running =
	 * 2 later to give it a moving animation.
	 * 
	 * @param character
	 *            the ID of the character
	 * 
	 * @see Game#getSprite(int, int) getSprite(x,y)
	 */
	private void setSkins(int character) {
		if (character == 1) {
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < 4; j++) {
					int running = i + 1;
					skins[j][i] = game.getSprite(j, running, 36, 36);
				}
			}
		}
	}

	/**
	 * Allows player to move up if it is within the battlefield
	 * <p>
	 * The amount of steps the player moves per button press is dependent on the
	 * speed
	 * 
	 * @see BoundingBox#moveY(int, double) moveY(original, multiplier)
	 * @see BoundingBox#moveX(int, double) moveX(original, multiplaier)
	 */
	public void moveUp() {
		if (getY() > 40)
			getBoundingBox().moveY(1, -speed);
	}

	public void moveDown() {
		if (getY() < (11 * 40))
			getBoundingBox().moveY(1, speed);
	}

	public void moveLeft() {
		if (getX() > 40)
			getBoundingBox().moveX(1, -speed);
	}

	public void moveRight() {
		if ((getX() < (17 * 40)))
			getBoundingBox().moveX(1, speed);
	}

	/**
	 * for each boolean value it changes the skin first and then moves the
	 * character if their is a collision then the opposite move is used to make
	 * the character look as if it is stationary
	 * 
	 * @see Player#plantBomb() plantBomb()
	 * 
	 */
	public void play() {
		
		if (leftPressed) {
			skin = 2;
			moveLeft();
			if (game.checkCollision(getBoundingBox())) {
				moveRight();
			}
			game.checkCollision(getBoundingBox());
		} else if (rightPressed) {
			skin = 3;
			moveRight();
			if (game.checkCollision(getBoundingBox())) {
				moveLeft();
			}
			game.checkCollision(getBoundingBox());
		} else if (upPressed) {
			skin = 1;
			moveUp();
			if (game.checkCollision(getBoundingBox())) {
				moveDown();
			}
		} else if (downPressed) {
			skin = 0;
			moveDown();
			if (game.checkCollision(getBoundingBox())) {
				moveUp();
			}
			game.checkCollision(getBoundingBox());
		} else if (spacePressed) {
			plantBomb();
		}
	}

	/**
	 * player position are exact therefore need to be changed to box type to
	 * make it realistic the position of the bomb is decided by the midpoint of
	 * the character
	 * 
	 * @see Bomb#Bomb(int, int, int, int, Game) Bomb(x,y,duration,size,game)
	 * 
	 */
	public void plantBomb() {
		int middleX = getX() + (getWidth() / 2);
		int x = middleX / 40;
		int middleY = getY() + (getHeight() / 2);
		int y = middleY / 40;
		new Bomb(x, y, 4, explosion_size, game); // collision check needs to be
													// added
	}

	/**
	 * @return the bombs
	 */
	public int getBombs() {
		return bombs;
	}

	/**
	 * @param bombs
	 *            the bombs to set
	 */
	public void setBombs(int bombs) {
		this.bombs = bombs;
	}

	/**
	 * @return the speed
	 */
	public double getSpeed() {
		return speed;
	}

	/**
	 * @param speed
	 *            the speed to set
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}

	/**
	 * @return the explostion_size
	 */
	public int getExplosion_size() {
		return explosion_size;
	}

	/**
	 * @param explostion_size
	 *            the explostion_size to set
	 */
	public void setExplostion_size(int explosion_size) {
		this.explosion_size = explosion_size;
	}

	/**
	 * <b> What skin stands for <b>
	 * <p>
	 * <li>skin[0] = facing_down</li>
	 * <li>skin[1] = facing_up</li>
	 * <li>skin[2] = facing_left</li>
	 * <li>skin[3] = facing_right</li>
	 * 
	 * @return image according to which skin is chosen and whether the running
	 *         is 0 or 1
	 */
	public Image getImage() {
		return skins[skin][running];
	}

	@Override
	/**
	 * what is executed when thread is set to run or start TODO : implement this
	 */
	public void run() {
		play();
	}


	@Override
	/**
	 * a series of if statement to check if the different key is pressed if so
	 * then changes the boolean value of key...Pressed
	 * 
	 * @see Player#play() play()
	 */
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			leftPressed = true;
			running = 1;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			rightPressed = true;
			running = 1;
		}
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			upPressed = true;
			running = 1;
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			downPressed = true;
			running = 1;
		}
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			spacePressed = true;
		}
	}

	@Override
	/**
	 * the method checks if the button is no longer pressed therefore changes
	 * running back to 0
	 */
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			leftPressed = false;
			running = 0;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			rightPressed = false;
			running = 0;
		}
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			upPressed = false;
			running = 0;
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			downPressed = false;
			running = 0;
		}
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			spacePressed = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

}
