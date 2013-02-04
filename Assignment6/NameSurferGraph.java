/*
 * File: NameSurferGraph.java
 * Name: Peter Vieira
 * ---------------------------
 * This class represents the canvas on which the graph of
 * names is drawn. This class is responsible for updating
 * (redrawing) the graphs whenever the list of entries changes or the window is resized.
 */

import acm.graphics.*;
import java.awt.event.*;
import java.awt.*;
//import java.util.*;

@SuppressWarnings("serial")
public class NameSurferGraph extends GCanvas
	implements NameSurferConstants, ComponentListener {

	/**
	* Creates a new NameSurferGraph object that displays the data.
	*/
	public NameSurferGraph() {
		
		addComponentListener(this);
	}
	
	/**
	* Clears the list of name surfer entries stored inside this class.
	*/
	public void clear() {
		
		removeAll();
		drawGraph();
		nOfNames = -1;
		
	}
	
	/* Method: addEntry(entry) */
	/**
	* Adds a new NameSurferEntry to the list of entries on the display.
	* Note that this method does not actually draw the graph, but
	* simply stores the entry; the graph is drawn by calling update.
	*/
	public void addEntry(NameSurferEntry entry) {
		
		//increment "nOfNames" to keep track of name entries. Starts at "-1"
		nOfNames++;
		//check to see if there are already five names entries on graph
		if (nOfNames > 4) {
			clear();
			nOfNames = 0;	//reset "nOfNames" to 0
		} 
		//get name from entry and store it in name array
		name[nOfNames] = entry.getName();
		//get ranks from entry and store in ranks array
		for (int i = 0 ; i < NDECADES ; i++) {
			ranks[nOfNames][i] = entry.getRank(i);
		}
		update();
	}
	
	/* Method: update() */
	/**
	* Updates the display image by deleting all the graphical objects
	* from the canvas and then reassembling the display according to
	* the list of entries. Your application must call update after
	* calling either clear or addEntry; update is also called whenever
	* the size of the canvas changes.
	*/
	public void update() {
		
		//remove all object on the canvas
		removeAll();
		//draw graph background
		drawGraph();
		//draw popularity graph for name
		if (name != null) {
			for (int i = 0 ; i < nOfNames + 1 ; i++) {
				drawNameGraph(name[i], ranks[i], lineColor[i], marker[i]);
			}
		}
	}
		
	private void drawNameGraph(String name, int[] ranks, Color lineColor, String marker) {
		
		//create and add data point labels and connecting lines
		double maxHeight = GRAPH_MARGIN_SIZE - 1 + (getHeight() - 
						GRAPH_MARGIN_SIZE * 2) / (double)MAX_RANK * MAX_RANK;
		for (int i = 0 ; i < NDECADES ; i++) {
			//if ranking is worse than 1000th add the label to
			//the bottom of the graph with an asterisk in place of the rank
			if (ranks[i] == 0) {
				//create data point label
				GLabel pointLabel = new GLabel(name + "*", getWidth() / NDECADES * i + 2, maxHeight);
				//set label color to current "lineColor" color
				pointLabel.setColor(lineColor);
				//add data point label to the graph
				add(pointLabel);
				
				//create data point marker
				GLabel mark = new GLabel(marker, getWidth() / NDECADES * i - 3, maxHeight + 4);
				//set data point marker color to current "lineColor" color
				mark.setColor(lineColor);
				//add data point marker to graph
				add(mark);
				
				//if next ranking is also worse than 1000th
				//draw line with both y-coordinates at bottom of graph
				//if (i == NDECADES - 1) break;
				if (i+1 < NDECADES) {
					if (ranks[i+1] == 0) {
						//create line between to lowest popularity data points
						GLine line = new GLine(getWidth() / NDECADES * i, maxHeight,
											   getWidth() / NDECADES * (i + 1), maxHeight);
						//set line color to current "lineColor" color
						line.setColor(lineColor);
						//add line to graph
						add(line);
					}
					//else if next ranking is better than 1000th
					//then draw line from bottom of graph up to it
					else {
						//create line
						GLine line = new GLine(getWidth() / NDECADES * i, maxHeight,
								getWidth() / NDECADES * (i + 1),
								(int)(GRAPH_MARGIN_SIZE + (getHeight() -
								GRAPH_MARGIN_SIZE * 2) / (double)MAX_RANK * ranks[i+1]));
						//set line color to current "lineColor" color
						line.setColor(lineColor);
						//add line to graph
						add(line);
					}
				}
			}
			//else if ranking is better than 1000th
			//then use ranking's y-coordinate
			else {
				//if the slope of the line is positive or zero
				if (i+1 < NDECADES) {
					//if first d.p. is lower on graph than next d.p., i.e. positive slope
					if (ranks[i] >= ranks[i+1] && ranks[i+1] != 0) {
						//create data point label
						GLabel pointLabel = new GLabel(name + " " + ranks[i], 
													getWidth() / NDECADES * i + 3, 
													GRAPH_MARGIN_SIZE + 12 + (getHeight() - 
													GRAPH_MARGIN_SIZE * 2) / (double)MAX_RANK * ranks[i]);
						//set label color to current "lineColor" color
						pointLabel.setColor(lineColor);
						//add data point label to graph
						add(pointLabel);
					}
					//if first d.p. is higher on graph than next d.p., i.e. negative slope
					if (ranks[i] < ranks[i+1] || ranks[i+1] == 0) {
						GLabel pointLabel = new GLabel(name + " " + ranks[i], 
													getWidth() / NDECADES * i + 2, 
													GRAPH_MARGIN_SIZE - 3 + (getHeight() - 
													GRAPH_MARGIN_SIZE * 2) / (double)MAX_RANK * ranks[i]);
						//set label color to current "lineColor" color
						pointLabel.setColor(lineColor);
						add(pointLabel);
					}
				}
				//while i is the last d.p.
				if (i+1 == NDECADES) {
					GLabel pointLabel = new GLabel(name + " " + ranks[i], getWidth() / NDECADES * i + 3,
												   GRAPH_MARGIN_SIZE + 12 + (getHeight() - 
												   GRAPH_MARGIN_SIZE * 2) / (double)MAX_RANK * ranks[i]);
					//set label color to current color in "lineColor" array
					pointLabel.setColor(lineColor);
					//add data point label to graph
					add(pointLabel);
				}
				//create data point marker
				GLabel mark = new GLabel(marker, 
						getWidth() / NDECADES * i - 3, 
						GRAPH_MARGIN_SIZE + 4 + (getHeight() - 
						GRAPH_MARGIN_SIZE * 2) / (double)MAX_RANK * ranks[i]);
				//set label color to current "lineColor" color
				mark.setColor(lineColor);
				//add marker to graph
				add(mark);
				
				if (i+1 < NDECADES) {
					if (ranks[i+1] == 0) {
						GLine line = new GLine(getWidth() / NDECADES * i, 
								(int)(GRAPH_MARGIN_SIZE + (getHeight() - 
								GRAPH_MARGIN_SIZE * 2) / (double)MAX_RANK * ranks[i]),
								getWidth() / NDECADES * (i + 1),
								(int)(GRAPH_MARGIN_SIZE + (getHeight() -
								GRAPH_MARGIN_SIZE * 2) / (double)MAX_RANK * MAX_RANK));
						//set line color to current "lineColor" color
						line.setColor(lineColor);
						add(line);
					}
					else {
						GLine line = new GLine(getWidth() / NDECADES * i, 
												(int)(GRAPH_MARGIN_SIZE + (getHeight() - 
												GRAPH_MARGIN_SIZE * 2) / (double)MAX_RANK * ranks[i]),
												getWidth() / NDECADES * (i + 1),
												(int)(GRAPH_MARGIN_SIZE + (getHeight() -
												GRAPH_MARGIN_SIZE * 2) / (double)MAX_RANK * ranks[i+1]));
						//set line color to current "lineColor" color
						line.setColor(lineColor);
						add(line);
					}
				}
			}
		}
	}
	
	private void drawGraph() {
		//create and add vertical decade line based on new window size
		int spacing = getWidth() / NDECADES;
		for (int i = 0 ; i < NDECADES ; i++) {
			GLine vertLine = new GLine(i * spacing, 0, i * spacing, getHeight());
			add(vertLine);
		}
		
		//create and add upper margin based on new window size
		GLine upperMargin = new GLine(0, GRAPH_MARGIN_SIZE, 
								getWidth(), GRAPH_MARGIN_SIZE);
		add(upperMargin);
		
		//create and add lower margin based on new window size
		GLine lowerMargin = new GLine(0, getHeight() - GRAPH_MARGIN_SIZE, 
								getWidth(), getHeight() - GRAPH_MARGIN_SIZE);
		add(lowerMargin);
		
		//create and add decade labels at bottom of screen
		for (int i = 0 ; i < NDECADES ; i++) {
			String year = Integer.toString(START_DECADE + i * 10);
			GLabel decades = new GLabel(year, getWidth() / NDECADES * i + 2, 
										getHeight() - GRAPH_MARGIN_SIZE / 3);
			add(decades);
		}
	}
	
	/* Implementation of the ComponentListener interface */
	public void componentHidden(ComponentEvent e) { }
	public void componentMoved(ComponentEvent e) { }
	public void componentResized(ComponentEvent e) { update(); }
	public void componentShown(ComponentEvent e) { }
	
	private String[] name = {null, null, null, null, null};
	private int[][] ranks = new int[5][NDECADES];
	private int nOfNames = -1;
	private Color[] lineColor = {Color.RED, Color.BLUE, Color.GREEN, new Color(148, 0, 211), Color.darkGray};
	private String[] marker = {"x", "o", "#", Character.toString((char)164), Character.toString((char)64)};
	
}
