/*
 * File: NameSurfer.java
 * Name: Peter Vieira
 * ---------------------
 * This program implements the viewer for
 * the baby-name database.
 */

import acm.program.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class NameSurfer extends Program implements NameSurferConstants {

	public static void main(String[] args) {
		new NameSurfer().start(args);
	}
	
/* Method: init() */
/**
 * This method has the responsibility for reading in the data base
 * and initializing the interactors at the bottom of the window.
 */
	public void init() {
		
		//Set up initial display with interactors and canvas
		
		//create name JLabel with "Name" text 
		//and add it to SOUTH region of window
		JLabel name = new JLabel("Name");
		add(name, SOUTH);
		
		//create nameField JTextField of width TEXT_FIELD_WIDTH
		//and add it to SOUTH region of window
		nameField = new JTextField(TEXT_FIELD_WIDTH);
		add(nameField, SOUTH);
		
		//create graph JButton with "Graph" text
		//and add it to SOUTH region of window
		graphButton = new JButton("Graph");
		add(graphButton, SOUTH);
		
		//create clear JButton with "Clear" text
		//and add it to SOUTH region of window
		clear = new JButton("Clear");					
		add(clear, SOUTH);

		//make the nameField JTextField focusable for the cursor
		//and set cursor to be in the nameField JTextField
		nameField.setFocusable(true);					
	    nameField.requestFocus();
	    
	    //add ActionListener for the nameField JTextField
	    //and the JButtons
	    nameField.addActionListener(this);
	    addActionListeners();
	    
	    //read file of names and add them to nameSurferDatabase database
	    nameDataBase = new NameSurferDataBase(NAMES_DATA_FILE);
	    
	    //create and add NameSurferGraph 'graph' to program
	    graph = new NameSurferGraph();
	    add(graph);
	}

/* Method: actionPerformed(e) */
/**
 * This class is responsible for detecting when the buttons are
 * clicked, so you will have to define a method to respond to
 * button actions.
 */
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == clear) {	//if ActionCommand is the clear JButton
			graph.clear();
		} else if (e.getSource() == nameField || e.getSource() == graphButton) {			//if Object source of action is the nameField JTextField
			/* get the text from the nameField JLabel and use it as a parameter 
			to call the "findEntry" method in the nameSurferDatabase class */
			NameSurferEntry entry = nameDataBase.findEntry(nameField.getText());
			nameField.setText("");		//clear the nameField JTextField contents
			if (entry != null) {
				graph.addEntry(entry);							
			}
		}
	}
	
/*  Interactors for text field label, text field, graph button, clear button */

	private JTextField nameField;
	private JButton graphButton;
	private JButton clear;
	private NameSurferDataBase nameDataBase;
	private NameSurferGraph graph;
}
