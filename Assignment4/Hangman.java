/*
 * File: Hangman.java
 * Name: Peter Vieira
 * ------------------
 * This program plays the Hangman game from
 */


import acm.program.*;

public class Hangman extends ConsoleProgram {			// "Hangman" class which extends the class "ConsoleProgram"
	
	public static void main(String[] args) {
		new Hangman().start(args);
	}
	
	public void init() {								// create initialization method "init()"
		
		canvas = new HangmanCanvas();					// instantiate new canvas "HangmanCanvas()"
		add(canvas);									// add canvas to the window
	}
	
    public void run() {									// main method which runs Hangman game
		
    	setUpGame();									// method to set up the Hangman game window
    	playGame();										// method to play the Hangman game
    }
    
    private void setUpGame() {							// "setUpGame()" method which gets new word and sets canvas up
    	
    	println("Welcome to Hangman!");					// Display "Welcome to Hangman!" in console
    	newWord = HangmanLexicon.getWord();				// get new word from HangmanLexicon method "getWord() and save in to newWord variable
    	int numOfBlanks = newWord.length();				// return length of new word and set "numOfBlanks" equal to it
    	guessesLeft = 8;								// start with a total of 8 guesses to discover word
    	blanks.delete(0, blanks.length());
    	for (int i = 0 ; i < numOfBlanks ; i++) {		// for as many times as there are blanks in the word (length of word)
    		blanks.append("-");							// append a dash to the "blanks" stringbuffer variable
    	}    
    	canvas.reset(blanks.toString());				// call the "reset()" method of canvas object to reset the canvas using with the blanks stringbuffer 
    }
    
    private void playGame() {									// playGame() method which play the hangman game
    	
    	println("The new word looks like this: " + blanks);		// display new word in current state
    	println("You have " + guessesLeft + " guesses.");		// display how many guesses player has left
		while (guessesLeft > 0) {								// while player still has guesses left
		checkGuessInput();										// check guess for errors
		checkForMatch();										// check for the guessed letter in the word
		}
		checkIfLost();											// check if player lost
    }
    
	private void checkGuessInput() {							// checkGuessInput() method which ensures legal guess
		
		guessString = readLine("Your guess: ");
		while (guessString.length() == 0) {
			println("You didn't guess a letter. Please enter a letter.");
			guessString = readLine("Your guess: ");
		}
		guess = Character.toUpperCase(guessString.charAt(0));
		while (guess < 65 || guess > 122) {
			println("That is not a letter. Please guess a letter.");	// tell player to guess a letter
			guess = readLine().charAt(0);
			guess = Character.toUpperCase(guess);
			println("Your guess: " + Character.toUpperCase(guess));	// ask for player's new guess and save it to "guess" variable
		}
	}
	
	private void checkForMatch() {									// checkForMatch() method which check if guess is correct
	
		newIndex = newWord.indexOf(guess);							// find where player's guess is in the word
		if (newIndex != -1) {										// if the word contains the player's guessed letter
			println("That guess is correct!");						// display "That guess is correct!"
			while (newIndex != -1) {								// while the letter is still found in the word
				blanks.setCharAt(newIndex, guess);					// replace the dash in "blanks" where the letter goes
				newIndex = newWord.indexOf(guess, newIndex + 1);	// and check for that letter in the rest of the word
			}														// until it reaches the end of the word and displays -1
			if (blanks.toString().equals(newWord)) {				// if all the letters are guessed
				println("You guessed the word: " + newWord);		// tell player they guessed the word
				println("You win!");								// tell player they win
				restartGame();
			}
			canvas.displayWord(blanks.toString());					// update word blanks on canvas
			if (guessesLeft != 0) {									// if player still has guesses left
			println("The word now looks like this: " + blanks);		// display what "blanks" looks like now
			println("You have " + guessesLeft + " guesses left");	// display how many guesses the player has left
			}
		} else {													// otherwise is the word does not contain the player's guess
			println("There are no " + guess + "'s in the word.");	// display that the word doesn't contain player's guess
			canvas.noteIncorrectGuess(guess);
			println("The word now looks like this: " + blanks);		// display what the blanks word looks like
			guessesLeft -= 1;										// decrement "guessesLeft" variable by one
			println("You have " + guessesLeft + " guesses left.");	// display how many guesses the player has left
		}
	}
	
	private void checkIfLost() {
		
		if (guessesLeft == 0 && !blanks.toString().equals(newWord)) {	// if there are no guesses left
    		println("Yee been hunged, varmon.");						// display text
    		println("The word was: " + newWord);						// display the word
    		println("You lose . . . miserably!");						// tell player that they lost
    		restartGame();
    	}
	}
	
	private void restartGame() {
		
		pause(1500);
		for (int i = 0 ; i < 10 ; i++) println("\n");
		remove(canvas);
		add(canvas);
		run();
	}
	
	private int guessesLeft;							// guessesLeft integer variable to keep track of remaining guesses
	private int newIndex;								// newIndex integer variable to store location of correctly guesses letter
	private char guess;									// guess character to store player's guess each time
	private String guessString;							// guess input in String form before getting first character
	private String newWord;								// newWord string to hold new word
    private StringBuffer blanks = new StringBuffer();	// blanks StringBuffer to hold current guess status of word
    private HangmanCanvas canvas;						// canvas HangmanCanvas object to create canvas for game
}
