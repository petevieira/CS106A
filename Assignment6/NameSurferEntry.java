/*
 * File: NameSurferEntry.java
 * Name: Peter Vieira
 * --------------------------
 * This class represents a single entry in the database.  Each
 * NameSurferEntry contains a name and a list giving the popularity
 * of that name for each decade stretching back to 1900.
 */

//import acm.util.*;
import java.util.*;
//import java.io.*;

public class NameSurferEntry implements NameSurferConstants {

/* Constructor: NameSurferEntry(line) */
/**
 * Creates a new NameSurferEntry from a data line as it appears
 * in the data file.  Each line begins with the name, which is
 * followed by integers giving the rank of that name for each
 * decade.
 */
	public NameSurferEntry(String line) {
		
		parseLine(line);
	}

/* Method: parseLine(line) */
/**
 * Parses a single line from the nameDatabase file and 
 * returns a NameSurferEntry object that contains the
 * information from the line.
 */
	private void parseLine(String line) {
		
		//parse out name into a string
		int nameEnd = line.indexOf(" ");
		entryName = line.substring(0, nameEnd);
		
		//parse out ranking values and put into "ranks" array
		String numbers = line.substring(nameEnd + 1);
		StringTokenizer tokenizer = new StringTokenizer(numbers);
		for (int i = 0 ; tokenizer.hasMoreTokens() ; i++) {
			int popularityRank = Integer.parseInt(tokenizer.nextToken());
			ranks[i] = popularityRank;
		}
	}
	
/* Method: getName() */
/**
 * Returns the name associated with this entry.
 */
	public String getName() {
		return entryName;
	}

/* Method: getRank(decade) */
/**
 * Returns the rank associated with an entry for a particular
 * decade.  The decade value is an integer indicating how many
 * decades have passed since the first year in the database,
 * which is given by the constant START_DECADE.  If a name does
 * not appear in a decade, the rank value is 0.
 */
	public int getRank(int decade) {
		
		return ranks[decade];
	}

/* Method: toString() */
/**
 * Returns a string that makes it easy to see the value of a
 * NameSurferEntry.
 */
	public String toString() {
		
		String entryValue =  ("\"" + entryName + " [");
		for (int i = 0 ; i < NDECADES ; i++) {
			entryValue += (ranks[i]);
			if (i < 10) entryValue += (" ");
		}
		entryValue += "]\"";
		return entryValue;
	}
	
	private String entryName;
	private int[] ranks = new int[NDECADES];
}

