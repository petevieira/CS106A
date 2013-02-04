/*
 * File: HangmanCanvas.java
 * Name: Peter Vieira
 * ------------------------
 * This file keeps track of the Hangman display.
 */

import acm.graphics.*;

public class HangmanCanvas extends GCanvas {
	
/** Resets the display so that only the scaffold appears */
	public void reset(String word) {
		removeAll();
		GLine scaffoldPole = new GLine(getWidth() / 2 - BEAM_LENGTH, getHeight() / 10, getWidth() / 2 - BEAM_LENGTH, getHeight() / 10 + SCAFFOLD_HEIGHT);
		GLine scaffoldOffset = new GLine(getWidth() / 2 - BEAM_LENGTH, getHeight() / 10, getWidth() / 2, getHeight() / 10);
		GLine rope = new GLine(getWidth() / 2, getHeight() / 10, getWidth() / 2, getHeight() / 10 + ROPE_LENGTH);
		add(scaffoldPole);
		add(scaffoldOffset);
		add(rope);
		displayWord = new GLabel(word, 100, getHeight() - 50);
		add(displayWord);
		incorrectGuess = new GLabel("", 100, getHeight() - 20);
		add(incorrectGuess);
		wrongGuesses.delete(0, wrongGuesses.length());
		
		
	}

/**
 * Updates the word on the screen to correspond to the current
 * state of the game. The argument string shows what letters have
 * been guessed so far; unguessed letters are indicated by hyphens.
 */
	public void displayWord(String word) {
		
		displayWord.setLabel(word);
		
	}

/**
 * Updates the display to correspond to an incorrect guess by the
 * user.  Calling this method causes the next body part to appear
 * on the scaffold and adds the letter to the list of incorrect
 * guesses that appears at the bottom of the window.
 */
	public void noteIncorrectGuess(char letter) {
		GOval head = new GOval(getWidth() / 2 - HEAD_RADIUS, getHeight()/10 + ROPE_LENGTH, HEAD_RADIUS * 2, HEAD_RADIUS * 2);
		GLine body = new GLine(head.getX() + HEAD_RADIUS, head.getY() + 2 * HEAD_RADIUS, head.getX() + HEAD_RADIUS, head.getY() + 2 * HEAD_RADIUS + BODY_LENGTH);
		GLine leftArm = new GLine(head.getX() + HEAD_RADIUS, head.getY() + 2 * HEAD_RADIUS + ARM_OFFSET_FROM_HEAD, head.getX() + HEAD_RADIUS - UPPER_ARM_LENGTH, head.getY() + 2 * HEAD_RADIUS + ARM_OFFSET_FROM_HEAD);
		GLine leftHand = new GLine(head.getX() + HEAD_RADIUS - UPPER_ARM_LENGTH, head.getY() + 2 * HEAD_RADIUS + ARM_OFFSET_FROM_HEAD, head.getX() + HEAD_RADIUS - UPPER_ARM_LENGTH, head.getY() + 2 * HEAD_RADIUS + ARM_OFFSET_FROM_HEAD + LOWER_ARM_LENGTH);
		GLine rightArm = new GLine(head.getX() + HEAD_RADIUS, head.getY() + 2 * HEAD_RADIUS + ARM_OFFSET_FROM_HEAD, head.getX() + HEAD_RADIUS + UPPER_ARM_LENGTH, head.getY() + 2 * HEAD_RADIUS + ARM_OFFSET_FROM_HEAD);
		GLine rightHand = new GLine(head.getX() + HEAD_RADIUS + UPPER_ARM_LENGTH, head.getY() + 2 * HEAD_RADIUS + ARM_OFFSET_FROM_HEAD, head.getX() + HEAD_RADIUS + UPPER_ARM_LENGTH, head.getY() + 2 * HEAD_RADIUS + ARM_OFFSET_FROM_HEAD + LOWER_ARM_LENGTH);
		GLine leftHip = new GLine(head.getX() + HEAD_RADIUS, head.getY() + 2 * HEAD_RADIUS + BODY_LENGTH, head.getX() + HEAD_RADIUS - HIP_WIDTH, head.getY() + 2 * HEAD_RADIUS + BODY_LENGTH);
		GLine leftLeg = new GLine(head.getX() + HEAD_RADIUS - HIP_WIDTH, head.getY() + 2 * HEAD_RADIUS + BODY_LENGTH, head.getX() + HEAD_RADIUS - HIP_WIDTH, head.getY() + 2 * HEAD_RADIUS + BODY_LENGTH + LEG_LENGTH);
		GLine leftFoot = new GLine(head.getX() + HEAD_RADIUS - HIP_WIDTH, head.getY() + 2 * HEAD_RADIUS + BODY_LENGTH + LEG_LENGTH, head.getX() + HEAD_RADIUS - HIP_WIDTH - FOOT_LENGTH, head.getY() + 2 * HEAD_RADIUS + BODY_LENGTH + LEG_LENGTH);
		GLine rightHip = new GLine(head.getX() + HEAD_RADIUS, head.getY() + 2 * HEAD_RADIUS + BODY_LENGTH, head.getX() + HEAD_RADIUS + HIP_WIDTH, head.getY() + 2 * HEAD_RADIUS + BODY_LENGTH);
		GLine rightLeg = new GLine(head.getX() + HEAD_RADIUS + HIP_WIDTH, head.getY() + 2 * HEAD_RADIUS + BODY_LENGTH, head.getX() + HEAD_RADIUS + HIP_WIDTH, head.getY() + 2 * HEAD_RADIUS + BODY_LENGTH + LEG_LENGTH);
		GLine rightFoot = new GLine(head.getX() + HEAD_RADIUS + HIP_WIDTH, head.getY() + 2 * HEAD_RADIUS + BODY_LENGTH + LEG_LENGTH, head.getX() + HEAD_RADIUS + HIP_WIDTH + FOOT_LENGTH, head.getY() + 2 * HEAD_RADIUS + BODY_LENGTH + LEG_LENGTH);
		
		incorrectGuess.setLabel(wrongGuesses.append(letter).append(" ").toString());
		
		switch (incorrectGuesses) {
			case 1:	add(head);
					incorrectGuesses++;
					break;
			case 2: add(body);
					incorrectGuesses++;
					break;
			case 3: add(leftArm);
					add(leftHand);
					incorrectGuesses++;
					break;
			case 4: add(rightArm);
					add(rightHand);
					incorrectGuesses++;
					break;
			case 5: add(leftHip);
					add(leftLeg);
					incorrectGuesses++;
					break;
			case 6: add(rightHip);
					add(rightLeg);
					incorrectGuesses++;
					break;
			case 7: add(leftFoot);
					incorrectGuesses++;
					break;
			case 8: add(rightFoot);
					incorrectGuesses++;
					break;
		}
	}

/* Constants for the simple version of the picture (in pixels) */
	private static final int SCAFFOLD_HEIGHT = 360;
	private static final int BEAM_LENGTH = 144;
	private static final int ROPE_LENGTH = 18;
	private static final int HEAD_RADIUS = 36;
	private static final int BODY_LENGTH = 144;
	private static final int ARM_OFFSET_FROM_HEAD = 28;
	private static final int UPPER_ARM_LENGTH = 72;
	private static final int LOWER_ARM_LENGTH = 44;
	private static final int HIP_WIDTH = 36;
	private static final int LEG_LENGTH = 108;
	private static final int FOOT_LENGTH = 28;

/* Instance variables */
	private GLabel displayWord;
	public GLabel incorrectGuess;
	private int incorrectGuesses = 1;
	StringBuffer wrongGuesses = new StringBuffer();
	
}
