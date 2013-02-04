/*
 * File: FacePamphletCanvas.java
 * Name: Peter Vieira
 * -----------------------------
 * This class represents the canvas on which the profiles in the social
 * network are displayed.  NOTE: This class does NOT need to update the
 * display when the window is resized.
 */


import acm.graphics.*;
import java.awt.*;
import java.util.*;

public class FacePamphletCanvas extends GCanvas 
					implements FacePamphletConstants {
	
	/** 
	 * Constructor
	 * This method takes care of any initialization needed for 
	 * the display
	 */
	public FacePamphletCanvas() {
		// You fill this in
	}

	
	/** 
	 * This method displays a message string near the bottom of the 
	 * canvas.  Every time this method is called, the previously 
	 * displayed message (if any) is replaced by the new message text 
	 * passed in.
	 */
	public void showMessage(String msg) {
		//if there's an object in the application message region
		if (getElementAt((getWidth() / 2), getHeight() - BOTTOM_MESSAGE_MARGIN) != null) {
			//then remove it from the canvas
			remove(getElementAt((getWidth() / 2), getHeight() - BOTTOM_MESSAGE_MARGIN));
		}
		GLabel message = new GLabel(msg);
		message.setFont(MESSAGE_FONT);
		message.setLocation((getWidth() / 2) - (message.getWidth() / 2), getHeight() - BOTTOM_MESSAGE_MARGIN);
		add(message);
	}
	
	/** 
	 * This method displays the given profile on the canvas.  The 
	 * canvas is first cleared of all existing items (including 
	 * messages displayed near the bottom of the screen) and then the 
	 * given profile is displayed.  The profile display includes the 
	 * name of the user from the profile, the corresponding image 
	 * (or an indication that an image does not exist), the status of
	 * the user, and a list of the user's friends in the social network.
	 */
	public void displayProfile(FacePamphletProfile profile) {
		removeAll();
		
		if (profile != null) {
			/* displays the profile name */
			GLabel nameLabel = new GLabel(profile.getName());
			nameLabel.setFont(PROFILE_NAME_FONT);
			nameLabel.setLocation(LEFT_MARGIN, TOP_MARGIN + nameLabel.getAscent());
			nameLabel.setColor(Color.BLUE);
			add(nameLabel);
			
			/* displays the profile image */
			if (profile.getImage() == null) {
				GRect rect = new GRect(IMAGE_WIDTH, IMAGE_HEIGHT);
				rect.setLocation(LEFT_MARGIN, nameLabel.getY() + IMAGE_MARGIN);
				add(rect);
				GLabel noImageLabel = new GLabel("No Image");
				noImageLabel.setFont(PROFILE_IMAGE_FONT);
				noImageLabel.setLocation(rect.getX() + (rect.getWidth() / 2) 
										- (noImageLabel.getWidth() / 2), 
										rect.getY() + (rect.getHeight() / 2));
				add(noImageLabel);
			}
			else {
				GImage image = profile.getImage();
				double scaleX = IMAGE_WIDTH / image.getWidth();
				double scaleY = IMAGE_HEIGHT / image.getHeight();
				image.scale(scaleX, scaleY);
				image.setLocation(LEFT_MARGIN, nameLabel.getY() + IMAGE_MARGIN);
				add(image);
			}
			
			/* displays the profile's current status */
			GLabel status = new GLabel(profile.getStatus());
			status.setFont(PROFILE_STATUS_FONT);
			status.setLocation(LEFT_MARGIN, nameLabel.getY() + IMAGE_MARGIN + IMAGE_HEIGHT + STATUS_MARGIN + status.getAscent());
			add(status);
			
			/* displays friend list label */
			GLabel friendListLabel = new GLabel("Friends:");
			friendListLabel.setFont(PROFILE_FRIEND_LABEL_FONT);
			friendListLabel.setLocation(getWidth() / 2, nameLabel.getY() + IMAGE_MARGIN);
			add(friendListLabel);
			
			/* displays friend list */
			String friendList = "";
			Iterator<String> itr;
			int i = 0;
			for (itr = profile.getFriends() ; itr.hasNext() ; ) {
				friendList = itr.next();
				i++;
				GLabel friendsList = new GLabel("");
				friendsList.setFont(PROFILE_FRIEND_FONT);
				friendsList.setLocation(getWidth() / 2, friendListLabel.getY() + friendsList.getAscent() * 1.5 * i);
				friendsList.setLabel(friendList);
				add(friendsList);
			}
		}
	}
}
