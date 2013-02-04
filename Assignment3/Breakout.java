/*
 * File: Breakout.java
 * -------------------
 * Name: Peter Vieira
 * This file implements the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

/* **************** CONSTANTS ****************/
/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 480;
	public static final int APPLICATION_HEIGHT = 600;
	
/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

/** Separation between bricks */
	private static final int BRICK_SEP = 4;

/** Width of a brick */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW + 1) * BRICK_SEP) / NBRICKS_PER_ROW;

/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

/** Number of turns */
	private static final int NTURNS = 3;
	
/** Delay time between movement of the ball */
	private static final int DELAY = 3;

/* **************** VARIABLES ****************/
	/** Instance variable "brick", which is a GRect object */
	private GRect brick;
	
/** Instance variable "paddle", which is a GRect object */
	private GRect paddle;
	
/** Instance variable "ball", which is a GOval object */
	private GOval ball;
	
/** Instance variable "scoreLabel", which is a GLabel object */
	private GLabel scoreLabel;
	
/** Instance variable "lastX" */
	private double lastX;
	
/** Instance variable "vx" and "vy" for velocity components of the ball */
	private double vx, vy;
	
/** Instance variable "rgen" for random generator */
	private RandomGenerator rgen = RandomGenerator.getInstance();
	
/** Instance variable "score" for keeping score */
	private int score = 0;
	
/** Instance variable "collider", which is a GObject */
	private GObject collider;
	
/** Instance variable "numberOfBricks" for brick count to determine if player has won or lost */
	private int numberOfBricks = NBRICK_ROWS * NBRICKS_PER_ROW;

/** Instance variable "bounceClip" for audio clip to be played when ball hits something */
	private AudioClip bounceClip = MediaTools.loadAudioClip("bounce.au");
	
	public static void main(String[] args) {
		new Breakout().start(args);
	}
	
/* Method: run() */
/** Runs the Breakout program. */
	public void run() {

		makeBricks();			// calls "makeBricks" method to make game bricks
		makePaddle();			// calls "makePaddle" method to make game paddle
		makeScoreLabel();		// calls "makeScoreLabel" method to make the score box
		addMouseListeners();	// calls "addMouseListeners" method to add MouseListener program to the canvas
		makeBall();				// calls "makeBall" method to make game ball
		initializeBall();		// calls "initializeGame" method to set up the game dynamics
		runGame();				// calls "runGame" method to run the dynamics of the game during play
	}
	
/* Method: makeBricks() */
/** Creates all the rows of bricks. 
 * 	private method with no return type or parameters */
	private void makeBricks() {
		
		for (int row = 0 ; row < NBRICK_ROWS ; row++) {		// for loop for as many times as there are rows of bricks
			
			for (int column = 0 ; column < NBRICKS_PER_ROW ; column++) {	// for loop for as many bricks as there are in a row
				
				/* Starting x-position of the first brick is calculated by starting at the middle of the canvas window,
				 * subtracting have the width of all the bricks in one row and half the width of all the separations
				 * in one row.
				 * Starting y-position of the first brick is calculated by adding the BRICK_Y_OFFSET constant and then adding
				 * the height of a brick and a separation for every row that is added. 
				 */
				brick = new GRect((getWidth() / 2 - ((NBRICKS_PER_ROW / 2) * (BRICK_WIDTH + BRICK_SEP)) + (column * (BRICK_WIDTH + BRICK_SEP))), 
								  BRICK_Y_OFFSET + (BRICK_HEIGHT + BRICK_SEP) * (row), BRICK_WIDTH, BRICK_HEIGHT);
				brick.setFilled(true);					// set brick to be filled in
				if (row < 2) {							// if it's the first or second rows
					brick.setColor(Color.RED); }		// set color of the bricks in those rows to red
				if (row == 2 || row == 3) {				// if it's the third or fourth rows
					brick.setColor(Color.RED); }		// set color of the bricks in those rows to orange
				if (row == 4 || row == 5) {				// if it's the fifth or sixth rows
					brick.setColor(Color.YELLOW); }		// set color of the bricks in those rows to yellow
				if (row == 6 || row == 7) {				// if it's the seventh or eighth rows
					brick.setColor(Color.GREEN); }		// set color of the bricks in those rows to green
				if (row == 8 || row == 9) {				// if it's the ninth or tenth rows
					brick.setColor(Color.BLUE); }		// set color of the bricks in those rows to cyan
				add(brick);								// add defined brick object to graphics window
			}
		}
	}

/* Method: makePaddle() */
/** Creates a paddle to be used with MouseMotionListener interface.
 * 	Size of paddle is determined by constants declared at start
 *  of class. */
	private void makePaddle() {
		
		/* instantiate new rectangle for the paddle at the center of the canvas
		 * width-wise and PADDLE_Y_OFFSET above the bottom of the canvas.
		 * Can center the paddle by taking half the width of the canvas and
		 * subtracting half the width of the paddle. The height of the paddle
		 * is determined by taking the height (bottom) of the canvas and
		 * subtracting the PADDLE_Y_OFFSET and the PADDLE_HEIGHT.
		 */
		paddle = new GRect((WIDTH / 2) - (PADDLE_WIDTH / 2), HEIGHT - PADDLE_Y_OFFSET - PADDLE_HEIGHT, PADDLE_WIDTH, PADDLE_HEIGHT);
		paddle.setFilled(true);			// set paddle object to filled in
		add(paddle);					// add paddle object to canvas
	}

/* Method: makeScoreLabel */
/** Create a label for the score, below the paddle.
 *  This method does not return a value. */
	private void makeScoreLabel() {
		scoreLabel = new GLabel("Score: 0");
		scoreLabel.setFont(new Font("Serif", Font.BOLD, 10));
		add(scoreLabel, paddle.getX() + scoreLabel.getWidth() / 2, HEIGHT - PADDLE_Y_OFFSET + PADDLE_HEIGHT);
	}
	
/* Method: makeBall() */
/** Creates a ball in the center of the screen */
	private void makeBall() {
		
		/* instantiate new oval for the ball at the center of the screen.
		 * the ball origin is in the top left of the ball. So x-position
		 * is half the width of the canvas minus the radius the ball.
		 * The y-position is half the height of the canvas minus the 
		 * radius of the ball.
		 */
		ball = new GOval((getWidth() / 2) - (BALL_RADIUS), (getHeight() / 2) - (BALL_RADIUS), BALL_RADIUS, BALL_RADIUS);
		ball.setFilled(true);			// set ball to be filled in
		add(ball);						// add ball object to canvas
	}
	
/* Method: initalizeBall() */
/** Sets a random velocity in the x-direction
 *  and sets initial velocity in the y-direction.
 *  This method does not return a value. */
	private void initializeBall() {
		
		vx = rgen.nextDouble(0.5, 1.0);			// generate a random number in between 0.5 - 1.0 and set vx to it
		if (rgen.nextBoolean(0.5)) vx = -vx;	// conditional if statement that's true 50% of the time and changes x-direction of motion
		vy = 1.0;								// set y velocity to 1.0
	}
	
/* Method: runGame() */
/** Runs the dynamics of the game during play. 
 *  This method does not return a value. */
	private void runGame() {
		
		for (int lives = 0 ; lives < NTURNS ; lives++) {						// for loop for the number of lives set (currently 3)
			clickToStart();														// call clickToStart method to display text on screen and wait for click
			while (true) {														// infinite while loop that ends only if ball reaches bottom of canvas
				ball.move(vx, vy);												// move ball vx in the x-direction and vy in the y-direction
				pause(DELAY);													// pause for duration of time in ms defined by global invariant "DELAY"
				if (ball.getX() < 0 || ball.getX() > WIDTH - BALL_RADIUS) {		// get x-position of ball and see if it reaches either edge of the canvas 
					vx = -vx; 													// and reverse x-direction of motion to simulate bouncing off wall
				}
				if (ball.getY() <= 0) vy = -vy;									// get y-position of ball, reverse y-direction of motion if it reaches top of canvas 
				if (ball.getY() >= HEIGHT) {									// get y-position of ball, break while loop if it reaches bottom of canvas
					break;														// break while loop 
				}
				collider = getCollidingObject();
				if (collider == paddle) {										// if collider object is the paddle, then . . .
					if ((ball.getY() + (2 * BALL_RADIUS)) >= (HEIGHT - PADDLE_Y_OFFSET - PADDLE_HEIGHT) // if ball hits anywhere in b/t top and bottom of paddle
							&& (ball.getY() + (2 * BALL_RADIUS)) < (HEIGHT - PADDLE_Y_OFFSET)) { 
						bounceClip.play();										// play bounceClip audio file
						vy = -vy;												// reverse y-direction of motion
					if ((ball.getX() + BALL_RADIUS) <= (paddle.getX() + (PADDLE_WIDTH / 3)) && vx > 0) vx = -vx;	// if ball hits left third of paddle it will go to the left
					if ((ball.getX() + BALL_RADIUS) > (paddle.getX() + (PADDLE_WIDTH * 2 / 3)) && vx < 0) vx = -vx;	// if ball hits right third of paddle it will go to the right
					}
				}
				else if (collider != null && collider != scoreLabel ) {		// otherwise if the collider is not null, meaning it's an object other than the paddle
					bounceClip.play();				// play bounceClip audio file
					updateScore();					// call updateScore() method to update score
					remove(collider);				// remove the brick that the ball hit
					numberOfBricks--;				// decrement the "numberOfBricks" counter to keep track of bricks left
					if (numberOfBricks == 0) {		// if the are no bricks left . . .
						remove(ball);				// then remove the ball object
						printWinner();				// and call the printWinner() method to display "You Win"
						return;						// return to the run() method
					}
					if (numberOfBricks == NBRICK_ROWS * NBRICKS_PER_ROW - 8 			// if player has hit 8 bricks
						|| numberOfBricks == NBRICK_ROWS * NBRICKS_PER_ROW - 24) {		// or 24 bricks
						if (vx > 0) {													// and if ball is moving to the right
							vx += .2;													// increase x-direction motion by .2
							vy -= .2;													// increase y-direction motion by .2
						}
						if (vx < 0) {													// and if ball is moving to the left
							vx -= .2;													// increase x-direction motion by .2
							vy -= .2;													// increase y-direction motion by .2
						}
					}
					vy = -vy;															// reverse direction of y-velocity
				}
			}
			remove(ball);																// remove the ball object from the canvas
			makeBall();																	// add the ball object to the canvas
		}
		remove(ball);																	// remove the ball object from the canvas
		printLoser();																	// call printLoser() method
		
	}
	
/* Method: clickToStart() */
/** Displays "Click to start" on canvas and waits for
 * 	user to click the left mouse button, at which point
 *  the program will return to the run() method and
 *  begin playing the game. This method does not return
 *  a value. */
	private void clickToStart() {
		
		GLabel startLabel = new GLabel("Click to start");								// instantiate GLabal "startLabel" with the string "Click to Start"
		startLabel.setFont(new Font("Serif", Font.BOLD, 48));							// set label's font to Serif, style to Bold, size to 48
		startLabel.setColor(Color.BLUE);												// set label's color to Blue
		add (startLabel, (WIDTH / 2) - (startLabel.getWidth() / 2), HEIGHT / 2);		// add the label to the center of the canvas
		waitForClick();																	// call the java waitForClick() method to wait for mouse click from player
		remove(startLabel);																// remove startLabel after mouse is clicked
		
	}
	
/* Method: getCollidingObject() */
/** Checks to see if there is an object in quasi-contact
 *  with the ball, determines if it's the paddle or a brick
 *  and bounces off the paddle or removes the brick and 
 *  bounces off. This method return a GObject (either the ball,
 *  paddle, or null). */
	private GObject getCollidingObject() {
		
		if (getElementAt(ball.getX(), ball.getY()) != null) {													// if there's an object at origin of the ball
			return getElementAt(ball.getX(), ball.getY());														// return that object
		} else if (getElementAt(ball.getX() + (BALL_RADIUS * 2), ball.getY()) != null) {						// if there's an object at top right corner of ball
			return getElementAt(ball.getX() + (BALL_RADIUS * 2), ball.getY());									// return that object
		} else if (getElementAt(ball.getX() + (BALL_RADIUS * 2), ball.getY() + (BALL_RADIUS * 2)) != null) {	// if there's an object at bottom right corner of ball
			return getElementAt(ball.getX() + (BALL_RADIUS * 2), ball.getY() + (BALL_RADIUS * 2));				// return that object
		} else if (getElementAt(ball.getX(), ball.getY() + (BALL_RADIUS * 2)) != null) {						// if there's an object at bottom left corner of the ball
			return getElementAt(ball.getX(), ball.getY() + (BALL_RADIUS * 2));									// return that object
		} else return null;																						// otherwise return "null" if there is no object
		
	}
		
/* Method: updateScore() */
/** Update the scoreLabel depending on what color
 *  brick is destroyed. This method does not return a value. */
	private void updateScore() {
	
		if (collider.getColor() == Color.BLUE) {							// if destroyed brick is blue
			score += 100;													// add 100 points to the score
			scoreLabel.setLabel("Score: " + score);							// update scoreLabel
			return; 														
		}
		if (collider.getColor() == Color.GREEN) {							// if destroyed brick is green
			score += 200;													// add 200 points to the score
			scoreLabel.setLabel("Score: " + score);							// update scoreLabel
			return;
		}
		if (collider.getColor() == Color.YELLOW) {							// if destroyed brick is yellow
			score += 300;													// add 300 points to the score
			scoreLabel.setLabel("Score: " + score);							// update scoreLabel
			return;
		}
		if (collider.getColor() == Color.ORANGE) {							// if destroyed brick is orange
			score += 400;													// add 400 points to the score
			scoreLabel.setLabel("Score: " + score);							// update scoreLabel
			return;
		}
		if (collider.getColor() == Color.RED) {								// if destroyed brick is red
			score += 500;													// add 500 points to the score
			scoreLabel.setLabel("Score: " + score);							// update scoreLabel
			return;
		}
		
	}
	
/* Method: printWinner() */
/** Prints "You Win" on the canvas after player destroys
 * 	all the bricks. This method does not return a value. */
	private void printWinner() {
		
		GLabel winnerLabel = new GLabel("You Win");									// instantiate label reading "You Win" string	
		winnerLabel.setFont(new Font("Serif", Font.BOLD, 48));						// set label font to Serif, style to Bold, size to 48
		winnerLabel.setColor(Color.BLACK);											// set label color to black
		add(winnerLabel, (WIDTH / 2) - (winnerLabel.getWidth() / 2), HEIGHT / 2);	// add the label to the center of the canvas
		
	}
	
/* Method: printWinner() */
/** Prints "Game Over" on the canvas after player destroys
 * 	all the bricks. This method does not return a value. */
	private void printLoser() {
		
		GLabel loserLabel = new GLabel("Game Over");								// instantiate label reading "Game Over"
		loserLabel.setFont(new Font("Serif", Font.BOLD, 48));						// set label font to Serif, style to Bold, size to 48
		loserLabel.setColor(Color.BLACK);											// set label color to black
		add(loserLabel, (WIDTH / 2) - (loserLabel.getWidth() / 2), HEIGHT / 2);		// add the label to the center of the canvas
		
	}
		
/* Method: mouseMoved(MouseEvent e) */
/** Creates a mouse event for when the mouse is moved on the canvas.
 *  This method does not return a value. */	
	public void mouseMoved(MouseEvent e) {
		
		lastX = e.getX();																			// set lastX to x-coordinate of the mouse
		if (e.getX() < (getWidth() - (PADDLE_WIDTH / 2)) && (e.getX() > (PADDLE_WIDTH / 2))) {		// if the mouse is on the canvas
			paddle.setLocation(lastX - PADDLE_WIDTH / 2, HEIGHT - PADDLE_Y_OFFSET - PADDLE_HEIGHT);	// then set the paddle x-position to the mouse's
			scoreLabel.setLocation(lastX - scoreLabel.getWidth() / 2, HEIGHT - PADDLE_Y_OFFSET + PADDLE_HEIGHT);	// set scoreLabel x-position to the mouse's
		}	
	}
	
}
	

