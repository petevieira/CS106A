/*
 * File: HangmanLexicon.java
 * Name: Peter Vieira
 * -------------------------
 * This file contains implementation of the HangmanLexicon
 * class.
 */

import java.io.*;
import java.util.*;
import acm.util.*;
import acm.program.*;

public class HangmanLexicon extends ConsoleProgram {
	
	public static void getWord() {																	
		
		FileReader inputStream = null;
		
		try {
			inputStream = new FileReader("map.txt");
			System.out.print("hello");
			int c;
			while ((c = inputStream.read()) != -1) {
				System.out.print(c + "\n");
			}
			inputStream.close();
		} catch (IOException ex) {
			System.out.print("can't open file");
		}
	} 
}
