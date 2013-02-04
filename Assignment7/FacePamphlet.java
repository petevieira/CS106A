/* 
 * File: FacePamphlet.java
 * Name: Peter Vieira
 * -----------------------
 * This program implements a basic social network
 * management system.
 */

import acm.program.*;
import acm.graphics.*;
import acm.util.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class FacePamphlet extends Program 
					   implements FacePamphletConstants {

	public static void main(String[] args) {
		new FacePamphlet().start(args);
	}
	
	/**
	 * This method has the responsibility for initializing the 
	 * interactors in the application, and taking care of any other 
	 * initialization that needs to be performed.
	 */
	public void init() {
		
		addNorthInteractors();
		addWestInteractors();
		addSouthInteractors();
		addActionListeners();
		fpDatabase = new FacePamphletDatabase();
		canvas = new FacePamphletCanvas();
		add(canvas);
	}
	
	private void addNorthInteractors() {
		
		//create name JLabel and
		//add it to the North region of the window
		JLabel name = new JLabel("Name");
		add(name, NORTH);
		
		//create nameField JTextField and
		//add it to the North region of the window
		nameField = new JTextField("", TEXT_FIELD_SIZE);
		add(nameField, NORTH);
		nameField.addActionListener(this);
		nameField.setFocusable(true);
		nameField.requestFocus();
		
		//create add JButton and
		//add it to the North region of the window
		addButton = new JButton("Add");
		add(addButton, NORTH);
		
		//create delete JButton and
		//add it to the North region of the window
		deleteButton = new JButton("Delete");
		add(deleteButton, NORTH);
		
		//create lookup JButton and
		//add it to the North region of the window
		lookupButton = new JButton("Lookup");
		add(lookupButton, NORTH);
    }
	
	private void addWestInteractors() {
		
		//create change status text field and
		//add it to the West region of the window
		changeStatusField = new JTextField(TEXT_FIELD_SIZE);
		add(changeStatusField, WEST);
		changeStatusField.addActionListener(this);
		changeStatusField.setFocusable(true);
		
		//create change status button and
		//add it to the West region of the window
		changeStatusButton = new JButton("Change Status");
		add(changeStatusButton, WEST);
		
		//create and add space
		add(new JLabel(EMPTY_LABEL_TEXT), WEST);
		
		//create change picture text field and
		//add it to the West region of the window
		changePictureField = new JTextField(TEXT_FIELD_SIZE);
		add(changePictureField, WEST);
		changePictureField.addActionListener(this);
		changePictureField.setFocusable(true);
		
		//create change picture button and
		//add it to the West region of the window
		changePictureButton = new JButton("Change Picture");
		add(changePictureButton, WEST);
		
		//create and add space
		add(new JLabel(EMPTY_LABEL_TEXT), WEST);
		
		//create add friend text field and 
		//add it to the West region of the window
		addFriendField = new JTextField(TEXT_FIELD_SIZE);
		add(addFriendField, WEST);
		addFriendField.addActionListener(this);
		addFriendField.setFocusable(true);
		
		//create add friend button and
		//add it to the West region of the window
		addFriendButton = new JButton("Add Friend");
		add(addFriendButton, WEST);
	}
    
	private void addSouthInteractors() {
		
		saveAndQuitButton = new JButton("Save and Quit");
		add(saveAndQuitButton, SOUTH);
	}
	
    /**
     * This class is responsible for detecting when the buttons are
     * clicked or interactors are used, so you will have to add code
     * to respond to these actions.
     */
    public void actionPerformed(ActionEvent e) {
	  
    	checkNorthInteractors(e);
    	checkWestInteractors(e);
    	checkSouthInteractor(e);
    }
    
    private void checkNorthInteractors(ActionEvent e) {
    	
    	//if the nameField text box is not empty
    	if (nameField.getText().length() != 0) {
    		String name = nameField.getText();
    		//and if user presses 'enter' key or the "Add" button
			if (e.getSource() == nameField || e.getSource() == addButton) {
				//if database already contains the profile for the given name
				if (fpDatabase.containsProfile(name)) {
					//display entered profile which already exists
					canvas.displayProfile(fpDatabase.getProfile(name));
					//display message saying so by calling the 'showMessage()' method
					canvas.showMessage("The profile " + fpDatabase.getProfile(name).getName() + " already exists");
				}
				else {
					//else create new profile from name
					FacePamphletProfile fpProfile = new FacePamphletProfile(name);
					//add profile to the database
					fpDatabase.addProfile(fpProfile);
					//display profile on the canvas
					canvas.displayProfile(fpProfile);
					//display message explaining action performed
					canvas.showMessage("New profile created for: " + fpProfile.getName());
				}
				currentProfile = fpDatabase.getProfile(name);
				nameField.requestFocus();
	    	}
			//else if user presses the "Delete" button
	    	if (e.getSource() == deleteButton) {
	    		//if database contains profile with the given name
	    		if (fpDatabase.containsProfile(name)) {
	    			//display empty canvas by removing previous profile
	    			canvas.displayProfile(null);
	    			//display message saying entered profile was deleted
	    			canvas.showMessage("The profile for " + (fpDatabase.getProfile(name)).getName() + " has been deleted");
	    			//delete profile from the database
	    			fpDatabase.deleteProfile(name);
	    		}
	    		else {
	    			//clear canvas
	    			canvas.displayProfile(null);
	    			//display message saying the requested profile does not exist
	    			canvas.showMessage("A profile for " + fpDatabase.formatName(name) + " doesn't exist in the database");
	    		}
	    		currentProfile = null;
	    		nameField.requestFocus();
	    	}
	    	//else if user presses the "Lookup" button
	    	if (e.getSource() == lookupButton) {
	    		//if database contains profile with the given name
	    		if (fpDatabase.containsProfile(name)) {
	    			//set currentProfile to the request one
	    			currentProfile = fpDatabase.getProfile(name);
	    			//display the file that was looked up
	    			canvas.displayProfile(currentProfile);
	    			//display a message telling user the profile that was looked up
	    			canvas.showMessage("Displaying " + currentProfile.getName() + "'s profile");
	    		}
	    		else {
	    			//set currentProfile to null
	    			currentProfile = null;
	    			//clear the canvas
	    			canvas.displayProfile(currentProfile);
	    			//display message saying looking up entered profile
	    			canvas.showMessage("A profile for " + fpDatabase.formatName(nameField.getText()) + " doesn't exist in the database");
	    		}
	    	}
	    	//clear the nameField text box
	    	nameField.setText("");
	    	nameField.requestFocus();
	    }
    }
    
    private void checkWestInteractors(ActionEvent e) {
	    
    	//if changeStatusField text box is not empty, and user presses 'enter' key or "Change Status" button
    	if ((changeStatusField.getText().length() != 0) && (e.getSource() == changeStatusField || e.getSource() == changeStatusButton)) {
    		if (currentProfile != null) {
    			currentProfile.setStatus(changeStatusField.getText());
    			canvas.displayProfile(currentProfile);
    			canvas.showMessage(currentProfile.getName() + "'s status has been changed");
    		}
    		else {
    			canvas.showMessage("You need to select a profile to change the status of");
    		}
    		//clear contents changeStatusField text box
    		changeStatusField.setText("");
    		nameField.requestFocus();
    	}
    	//else if changePictureField text box is not empty, and user presses 'enter' key or "Change Picture" button
    	if ((changePictureField.getText().length() != 0) && (e.getSource() == changePictureField || e.getSource() == changePictureButton)) {
    		if (currentProfile != null) {
    			GImage image = null;
    			try {
    				image = new GImage(changePictureField.getText());
    			} 
    			catch (ErrorException ex) {
    				canvas.showMessage("Unable to open the image file <" + changePictureField.getText() + ">. Select a different one");
    			}
    			if (image != null) {
    				currentProfile.setImage(image);
    				//update canvas
    				canvas.displayProfile(currentProfile);
    				//display message saying picture changed to entered text
        			canvas.showMessage("Picture changed to: \"" + changePictureField.getText() + "\"");
        			//clear contents of changePictureField text box
        			changePictureField.setText("");
    			}
    		}
    		else {
    			canvas.showMessage("You need to select a profile to change the status of");
    			nameField.requestFocus();
    		}
    	}
    	//else if addFriendField text box is not empty, and user presses 'enter' key or "Add Friend" button
    	else if ((addFriendField.getText().length() != 0) && (e.getSource() == addFriendField || e.getSource() == addFriendButton)) {
    		if (currentProfile != null) {
    			//format entered friend name and assign it to the string 'friendToAdd'
    			String friendToAdd = fpDatabase.formatName(addFriendField.getText());
    			//if there is a profile with the entered name in the database and it's not that of the current profile
    			if (fpDatabase.containsProfile(friendToAdd) && !friendToAdd.equals(currentProfile.getName())) {
    				//and if the method 'addFriend' returns true then they are not a friend yet
    				if (currentProfile.addFriend(friendToAdd)) {
    					//so add them to the friend list
    					currentProfile.addFriend(friendToAdd);
    					//and add the current profile's name to the friend's friend list
    					fpDatabase.getProfile(friendToAdd).addFriend(currentProfile.getName());
    					//update the current profile display
    					canvas.displayProfile(currentProfile);
    					//display the change in friendship on the current profile
    					canvas.showMessage(currentProfile.getName() + " and " + friendToAdd + " are now friends");
    				} 
    				//else if the method 'addFriend' returns false, then they are already friends
    				else {
    					//so display text telling user that current profile is already friends with entered name
    					canvas.showMessage(currentProfile.getName() + " is already friends with " + friendToAdd);
    				}
    			}
    			//else if there is no profile in the database with the entered name
    			else if (!fpDatabase.containsProfile(friendToAdd)) {
    				//then display text telling user nobody exist in the database with that name
    				canvas.showMessage("There's nobody in the database named " + friendToAdd);
    			}
    			//else if the name entered is the same as that of the current profile
    			else {
    				//then tell user that they entered there own name and that they can't be friends with themselves
    				canvas.showMessage("You can't be friends with yourself, silly");
    			}
    		}
    		//else if no profile has been selected to add a friend to
    		else {
    			//then display text telling the user to select a profile first
    			canvas.showMessage("You need to select a profile to change the status of");
    			//and put the cursor in the 'nameField' text box
    			nameField.requestFocus();
    		}
    		//clear contents of addFriendField text box
    		addFriendField.setText("");
    	}
    }
    
    private void checkSouthInteractor(ActionEvent e) {
    	
    	if (e.getSource() == saveAndQuitButton) {
    	
    		System.exit(0);
    	}
    }

    //North interactors
    private JTextField nameField;
    private JButton addButton;
    private JButton deleteButton;
    private JButton lookupButton;
    //West interactors
    private JTextField changeStatusField;
    private JTextField changePictureField;
    private JTextField addFriendField;
    private JButton changeStatusButton;
    private JButton changePictureButton;
    private JButton addFriendButton;
    //South interactor
    private JButton saveAndQuitButton;
    
    private FacePamphletCanvas canvas;
    
    private FacePamphletProfile currentProfile;
    
    private FacePamphletDatabase fpDatabase;
    
}
