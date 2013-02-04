/*
 * File: FacePamphletProfile.java
 * Name: Peter Vieira
 * ------------------------------
 * This class keeps track of all the information for one profile
 * in the FacePamphlet social network.  Each profile contains a
 * name, an image (which may not always be set), a status (what 
 * the person is currently doing, which may not always be set),
 * and a list of friends.
 */

import acm.graphics.*;
import java.util.*;
//import java.awt.MediaTracker;

public class FacePamphletProfile implements FacePamphletConstants {
	
	/** 
	 * Constructor
	 * This method takes care of any initialization needed for
	 * the profile.
	 */
	public FacePamphletProfile(String name) {
		
		nOfProfiles++;
		profileName = formatName(name);
	}

	/** This method returns the name associated with the profile. */ 
	public String getName() {
		
		return profileName;
	}

	/** 
	 * This method returns the image associated with the profile.  
	 * If there is no image associated with the profile, the method
	 * returns null. */ 
	public GImage getImage() {
		
		return profileImage;
	}

	/** This method sets the image associated with the profile. */ 
	public void setImage(GImage image) {
		
		profileImage = image;
	}
	
	/** 
	 * This method returns the status associated with the profile.
	 * If there is no status associated with the profile, the method
	 * returns the empty string ("").
	 */ 
	public String getStatus() {
		
		if (currentStatus != "") {
			return (this.getName() + " " + currentStatus);
		}
		else {
			return "No current status";
		}
	}
	
	/** This method sets the status associated with the profile. */ 
	public void setStatus(String status) {
		
		currentStatus = status;
	}

	/** 
	 * This method adds the named friend to this profile's list of 
	 * friends.  It returns true if the friend's name was not already
	 * in the list of friends for this profile (and the name is added 
	 * to the list).  The method returns false if the given friend name
	 * was already in the list of friends for this profile (in which 
	 * case, the given friend name is not added to the list of friends 
	 * a second time.)
	 */
	public boolean addFriend(String friend) {
		
		String friendToAdd = formatName(friend);
		if (friends.contains(friendToAdd)) {
			return false;
		}
		else {
			friends.add(friendToAdd);
			return true;
		}
	}

	/** 
	 * This method removes the named friend from this profile's list
	 * of friends.  It returns true if the friend's name was in the 
	 * list of friends for this profile (and the name was removed from
	 * the list).  The method returns false if the given friend name 
	 * was not in the list of friends for this profile (in which case,
	 * the given friend name could not be removed.)
	 */
	public boolean removeFriend(String friend) {
		
		String friendToRemove = formatName(friend);
		
		if (friends.contains(friendToRemove)) {
			friends.remove((friends.indexOf(friendToRemove)));
			return true;
		}
		else {
			return false;
		}
	}
		
	/**
	 * Receives a String of the friend name to be formatted
	 * and capitalizes the first letter of each name,
	 * changes the other letters to lower case and
	 * returns the formatted name as a String
	 */
	private String formatName(String name) {
		
		String formattedName = "";
		String others = "";
		int i = 0;
		while (i < name.length()) {
			//change first letter of first name to upper case
			char ch = name.charAt(i);
			ch = Character.toUpperCase(ch);
			formattedName += ch;
			
			//change rest of letters in first name to lower case
			if ((i + 1) < name.length()) {
				if (name.indexOf(" ", i) != -1) {
					int spaceIndex = name.indexOf(" ", i + 1);
					others = name.substring(i + 1, spaceIndex + 1);
					i = spaceIndex + 1;
				}
				else {
					others = name.substring(i + 1);
					i = name.length();
				}
				others = others.toLowerCase();
				formattedName += others;
			}
			else if ((i + 1) == name.length()) {
				i = name.length();
			}
		}
		return formattedName;
	}
	
	/** 
	 * This method returns an iterator over the list of friends 
	 * associated with the profile.
	 */ 
	public Iterator<String> getFriends() {
		Iterator<String> itr = friends.iterator();
		if (friends != null) {
			return itr;
		}
		else {
			return null;
		}
	}
	
	/** 
	 * This method returns a string representation of the profile.  
	 * This string is of the form: "name (status): list of friends", 
	 * where name and status are set accordingly and the list of 
	 * friends is a comma separated list of the names of all of the 
	 * friends in this profile.
	 * 
	 * For example, in a profile with name "Alice" whose status is 
	 * "coding" and who has friends Don, Chelsea, and Bob, this method 
	 * would return the string: "Alice (coding): Don, Chelsea, Bob"
	 */ 
	public String toString() {
		
		String friendList = "";
		for (int i = 0 ; i < friends.size() ; i++) {
			friendList += (friends.get(i) + ", ");
		}
		return ("\"" + getName() + " (" + getStatus() + "): " + friendList + "\"");
	}
	
	private int nOfProfiles = 0;
	private String profileName;
	private String currentStatus = "";
	private ArrayList <String> friends = new ArrayList<String>();
	private GImage profileImage = null;
	//private MediaTracker tracker;
}
