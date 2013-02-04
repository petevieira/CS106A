/*
 * File: Yahtzee.java
 * Name: Peter Vieira
 * ------------------
 * This program plays the Yahtzee game.
 */

import java.applet.*;
import acm.io.*;
import acm.program.*;
import acm.util.*;

public class Yahtzee extends GraphicsProgram implements YahtzeeConstants {
	
	public static void main(String[] args) {
		new Yahtzee().start(args);
	}
	
	public void run() {
		IODialog dialog = getDialog();
		nPlayers = dialog.readInt("Enter number of players");
		playerNames = new String[nPlayers];
		for (int i = 1; i <= nPlayers; i++) {
			playerNames[i - 1] = dialog.readLine("Enter name for player " + i);
		}
		display = new YahtzeeDisplay(getGCanvas(), playerNames);
		initialize();
		playGame();
	}
	
	private void initialize() {
		for (int i = 0 ; i < nPlayers ; i++) {
			score[i] = 0;
			upperScore[i] = 0;
			lowerScore[i] = 0;
			totalScore[i] = 0;
			categoriesLeft[i] = 13;
		}
	}

	private void playGame() {
		for (int i = 0 ; i < N_SCORING_CATEGORIES ; i++) {
			for (int j = 1 ; j <= nPlayers ; j++) {
				display.printMessage(playerNames[j-1] + ", it's your turn. Roll the dice by clicking the \"Roll Dice\" button");	//display who's turn it is
				display.waitForPlayerToClickRoll(j);	//wait for player to click "Roll" button
				display.displayDice(rollDice(N_DICE));	//roll dice, return them as an array and display them
				rerolls(j-1);							//allows player to re-roll selected dice twice
				display.printMessage(playerNames[j-1] + ", select a category for this roll");
				int categoryNumber = display.waitForPlayerToSelectCategory();
				int score = checkCategory(categoryNumber, j-1);
				while (score == -1) {
					categoryNumber = display.waitForPlayerToSelectCategory();
					score = checkCategory(categoryNumber, j-1);
				}
				display.updateScorecard(categoryNumber, j, score);
				updateTotalScore(categoryNumber, j-1);
			}
			checkIfGameIsOver();
		}
		int winner = checkWinner();
		winClip.play();
		display.printMessage("Congratulation, " + playerNames[winner] + ", you are the winner with a score of " + totalScore[winner]);
	}
		
/**	Method: rollDice(int numOfDice)
 * 
 * Preconditions:  Player must push the "Roll" button
 * 					Must declare number of dice being rolled
 * 					Must declare number of sides on die in YahtzeeConstants.java
 * Postconditions: Returns an integer array of specified number of dice
 *  				integer array is parameter for "displayDice" method
 *  				which displays dice values just rolled */
	private int[] rollDice(int numOfDice) {
		fiveDiceRollClip.play();
		rgen = RandomGenerator.getInstance();
		for (int i = 0 ; i < N_DICE ; i++) {
			dice[i] = rgen.nextInt(1, N_SIDES_ON_DIE);
		}
		return dice;	
	}
	
/**	Method: rerolls()
 * 
 * Preconditions:  Player must select the dice to re-roll
 * 					Must push the "Roll Again" button to re-roll selected dice
 * 					Must repeat the above steps once
 * Postconditions: Ends with a final integer array "dice[]"
 *  				that is used for scoring in the next
 *  				portion of the game */
	private void rerolls(int player) {
		
		for (int i = 0 ; i < 2 ; i++) {				// allow player to re-roll twice
			int rerollNum = i + 1;
			display.printMessage("Re-roll #" + rerollNum + ": " + playerNames[player] + ", select the dice you wish to re-roll, and click \"Roll Again\". Otherwise just click \"Roll Again\".");
			display.waitForPlayerToSelectDice();	//let player select dice they want to re-roll
			display.displayDice(reroll());			//let player re-roll dice and display them
		}
	}
	
/**	Method: reroll()
 * 
 * Preconditions:  Player must select dice to re-roll, or press "Roll"
 * 					Player must push the "Roll" button to re-roll
 * 					Must declare number of dice being rolled (N_DICE)
 * 					Must declare number of sides on die in YahtzeeConstants.java (N_SIDES_ON_DIE)
 * Postconditions: Returns an integer array of specified number of dice
 *  				integer array is parameter for "displayDice" method
 *  				which displays dice values just rolled */
	private int[] reroll() {
		nDiceSelected = 0;
		for (int i = 0 ; i < N_DICE ; i++) {
			if (display.isDieSelected(i)) {
				nDiceSelected++;
				dice[i] = rgen.nextInt(1, N_SIDES_ON_DIE); 
			}
		}
		makeRerollSound();
		return dice;
	}
	
	private void makeRerollSound() {
		switch (nDiceSelected) {
			case 1:	oneDiceRollClip.play();
					break;
			case 2: twoDiceRollClip.play();
					break;
			case 3: threeDiceRollClip.play();
					break;
			case 4: fourDiceRollClip.play();
					break;
			case 5: fiveDiceRollClip.play();
					break;
			case 0: break;
		}
	}
	
/**	Method: checkCategory(int category)
 * 
 * Preconditions:  Player must select a category for his turn
 * 					which will return an integer, which will be
 * 					used as the parameter for this method
 * Postconditions: Returns an integer specifying score for that category
 *  				integer array is parameter for "displayDice" method
 *  				which displays dice values just rolled */
	private int checkCategory(int category, int player) {
		if (playercats[player][category] == 1) {
			display.printMessage(playerNames[player] + ", please select an unused category.");
			return -1;
		}
		score[player] = 0;
		categoriesLeft[player]--;
		playercats[player][category] = 1;
		for (int i = 0 ; i < 6 ; i++) numOfEachDie[i] = 0;
		if (category >= ONES && category <= SIXES) {		//ones through sixes
			for (int i = 0 ; i < 5 ; i++) {
				if (dice[i] == category) {
					score[player] += category;
				}
			} return score[player];
		} else if (category == THREE_OF_A_KIND) {			//three of a kind
				for (int i = 0 ; i < 5 ; i++) {
					score[player] += dice[i];
					switch (dice[i]) {
					case 1:	numOfEachDie[0]++;
							break;
					case 2: numOfEachDie[1]++;
							break;
					case 3: numOfEachDie[2]++;
							break;
					case 4: numOfEachDie[3]++;
							break;
					case 5: numOfEachDie[4]++;
							break;
					case 6: numOfEachDie[5]++;
							break;
					}
				}
				for (int i = 0 ; i < 6 ; i++) {
					if (numOfEachDie[i] >= 3) {
						return score[player];
					}
				} score[player] = 0;
				return 0;
		} else if (category == FOUR_OF_A_KIND) {			//four of a Kind
			for (int i = 0 ; i < 5 ; i++) {
				score[player] += dice[i];
				switch (dice[i]) {
					case 1:	numOfEachDie[0]++;
							break;
					case 2: numOfEachDie[1]++;
							break;
					case 3: numOfEachDie[2]++;
							break;
					case 4: numOfEachDie[3]++;
							break;
					case 5: numOfEachDie[4]++;
							break;
					case 6: numOfEachDie[5]++;
							break;
				}
			}
			for (int i = 0 ; i < 6 ; i++) {
				if (numOfEachDie[i] >= 4) {
					return score[player];
					}
				} score[player] = 0; 
				return 0; 
		} else if (category == FULL_HOUSE) {			//full house
			for (int i = 0 ; i < 5 ; i++) {
				switch (dice[i]) {
					case 1:	numOfEachDie[0]++;
							break;
					case 2: numOfEachDie[1]++;
							break;
					case 3: numOfEachDie[2]++;
							break;
					case 4: numOfEachDie[3]++;
							break;
					case 5: numOfEachDie[4]++;
							break;
					case 6: numOfEachDie[5]++;
							break;
				}
			}
			for (int i = 0 ; i < 6 ; i++) {
				if (numOfEachDie[i] == 3) {
					for (int j = 0 ; j < 6 ; j++) {
						if (numOfEachDie[j] == 2) {
							score[player] = 25;
							return 25;
						}
					}  
				}
			} score[player] = 0;
			return 0;
		} else if (category == SMALL_STRAIGHT) {			//small straight
			for (int i = 0 ; i < 5 ; i++) {
				if (dice[i] == 3) {
					for (int j = 0 ; j < 5 ; j++) {
						if (dice[j] == 4) {
							for (int k = 0 ; k < 5 ; k++) {
								if (dice[k] == 2) {
									for (int l = 0 ; l < 5 ; l++) {
										if (dice[l] == 5 || dice[l] == 1) {
											score[player] = 30;
											return 30;
										}
									}
								} else if (dice[k] == 5) {
									for (int l = 0 ; l < 5 ; l++) {
										if (dice[l] == 6 || dice[l] == 2) {
											score[player] = 30;
											return 30;
										}
									}
								}
							}
						}
					}
				}
			} score[player] = 0;
			return 0;
		} else if (category == LARGE_STRAIGHT) {			//large straight
			for (int i = 0 ; i < 5 ; i++) {
				if (dice[i] == 3) {
					for (int j = 0 ; j < 5 ; j++) {
						if (dice[j] == 4) {
							for (int k = 0 ; k < 5 ; k++) {
								if (dice[k] == 2) {
									for (int l = 0 ; l < 5 ; l++) {
										if (dice[l] == 5) {
											for (int m = 0 ; m < 5 ; m++) {
												if (dice[m] == 6 || dice[m] == 1) {
													score[player] = 40;
													return 40;		
												}
											}
										}
									}
								}
							}
						}
					}
				}
			} score[player] = 0;
			return 0;
		} else if (category == YAHTZEE) {			//yahtzee
			for (int i = 0 ; i < 5 ; i++) {
				switch (dice[i]) {
					case 1:	numOfEachDie[0]++;
							break;
					case 2: numOfEachDie[1]++;
							break;
					case 3: numOfEachDie[2]++;
							break;
					case 4: numOfEachDie[3]++;
							break;
					case 5: numOfEachDie[4]++;
							break;
					case 6: numOfEachDie[5]++;
							break;
				}
			}
			for (int i = 0 ; i < 6 ; i++) {
				if (numOfEachDie[i] == 5) {
					score[player] = 50;
					return 50;
				}
			} score[player] = 0;
			return 0;
		} else if (category == CHANCE) {			//chance
			for (int i = 0 ; i < 5 ; i++) {
				score[player] += dice[i];
			}
			return score[player];
		} score[player] = 0;
		return 0;
	}
	
/**	Method: updateUpperAndLowerScores(int category)
 * 
 * Preconditions:  Player must select a category for his turn
 * 					which will return an integer, which will be
 * 					used as the parameter for this method.
 * 					The score must have been determined for the chosen category
 * Postconditions: Updates scorecard categories "UpperScore" and "LowerScore" */
	private void updateTotalScore(int category, int player) {
		if (category >= 1 && category <= 6) {
			upperScore[player] += score[player];
		}
		if (category >= 9 && category <= 15) {
			lowerScore[player] += score[player];
		}
		totalScore[player] += score[player];
		display.updateScorecard(TOTAL, player+1, totalScore[player]);
	}
	
/**	Method: checkIfGameIsOver()
 * 
 * Preconditions:  Players must have each used all
 * 					thirteen categories and game is over.
 * Postconditions: Updates scorecard categories "Upper Score",
 * 					"Upper Bonus", "Lower Score"
 * 					and "Total". */
	private void checkIfGameIsOver() {
		if (categoriesLeft[nPlayers - 1] == 0) {
			for (int i = 1 ; i <= nPlayers ; i++) {	
				display.updateScorecard(UPPER_SCORE, i, upperScore[i-1]);
				if (upperScore[i-1] > 63) {
					display.updateScorecard(UPPER_BONUS, i, 35);
					totalScore[i-1] += 35;
				}
				display.updateScorecard(UPPER_BONUS, i, 0);
				display.updateScorecard(LOWER_SCORE, i, lowerScore[i-1]);
				display.updateScorecard(TOTAL, i, totalScore[i-1]);
			}
		} return;
	}	

/**	Method: checkWinner()
 * 
 * Preconditions:  Players must have each used all
 * 					thirteen categories and game is over.
 * Postconditions: Updates scorecard categories "Upper Score",
 * 					"Upper Bonus", "Lower Score"
 * 					and "Total". */
	private int checkWinner() {
		int winner = 0;
		for (int i = 0 ; i < MAX_PLAYERS-1 ; i++) {
			if (totalScore[i+1] > totalScore[i]) {
				 winner++;
			}
		} return winner;
	}
	
/* Private instance variables */
	private int nPlayers;
	private int nDiceSelected = 0;
	private int[] dice = new int[N_DICE];
	private int[] numOfEachDie = new int[6];
	private int[] score = new int[MAX_PLAYERS];
	private int[] totalScore = new int[MAX_PLAYERS];
	private int[] upperScore = new int[MAX_PLAYERS];
	private int[] lowerScore = new int[MAX_PLAYERS];
	private int[] categoriesLeft = new int[MAX_PLAYERS];
	private int[][] playercats = new int[4][17];
	private String[] playerNames;
	private YahtzeeDisplay display;
	private RandomGenerator rgen = new RandomGenerator();
	private AudioClip fiveDiceRollClip = MediaTools.loadAudioClip("fiveDiceRollClip.wav");
	private AudioClip fourDiceRollClip = MediaTools.loadAudioClip("fourDiceRollClip.wav");
	private AudioClip threeDiceRollClip = MediaTools.loadAudioClip("threeDiceRollClip.wav");
	private AudioClip twoDiceRollClip = MediaTools.loadAudioClip("twoDiceRollClip.wav");
	private AudioClip oneDiceRollClip = MediaTools.loadAudioClip("oneDiceRollClip.wav");
	private AudioClip winClip = MediaTools.loadAudioClip("winClip.wav");

}
