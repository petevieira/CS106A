/*
 * File: NameSurferDataBase.java
 * Name: Peter Vieira
 * -----------------------------
 * This class keeps track of the complete database of names.
 * The constructor reads in the database from a file, and
 * the only public method makes it possible to look up a
 * name and get back the corresponding NameSurferEntry.
 * Names are matched independent of case, so that "Eric"
 * and "ERIC" are the same names.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
//import acm.util.*;

public class NameSurferDataBase implements NameSurferConstants {
	
/* Constructor: NameSurferDataBase(filename) */
/**
 * Creates a new NameSurferDataBase and initializes it using the
 * data in the specified file.  The constructor throws an error
 * exception if the requested file does not exist or if an error
 * occurs as the file is being read.
 */
	public NameSurferDataBase(String filename) {
		try {
			BufferedReader rd = new BufferedReader(new FileReader(filename));
			while (true) {
				String line = rd.readLine();
				if (line == null) break;
				NameSurferEntry nameEntry = new NameSurferEntry(line);
				nameDatabase.put(nameEntry.getName(), nameEntry);
			}
			rd.close();
		} catch(IOException e) {
			//throw new ErrorException(e);
		}
	}
	

/* Method: findEntry(name) */
/**
 * Returns the NameSurferEntry associated with this name, if one
 * exists.  If the name does not appear in the database, this
 * method returns null.
 */
	public NameSurferEntry findEntry(String name) {
		
		//change first letter of name to upper case
		char ch = name.charAt(0);
		ch = Character.toUpperCase(ch);
		
		//extract other letters of name and change to lower case
		String others = name.substring(1);
		others = others.toLowerCase();
		
		//combine first letter with others
		name = ch + others;
		if (nameDatabase.containsKey(name)) {
			return nameDatabase.get(name);
		}
		else {
			return null;
		}
	}
	
	HashMap<String, NameSurferEntry> nameDatabase = new HashMap<String, NameSurferEntry>();
}

