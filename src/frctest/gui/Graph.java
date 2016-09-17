package frctest.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
/*
 *  This file is part of frcjcss.
 *
 *  frcjcss is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  frcjcss is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with frcjcss.  If not, see <http://www.gnu.org/licenses/>.
 */


/**
 * Handles a scrolling graph, for a quantity over time.
 * 
 * @author Patrick Jameson
 * @version 11.20.2010.0
 */
public class Graph extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6204156868436347448L;
	
	/**
	 * Y coordinate of the X axis.  Distance from the TOP of the graph.
	 */
	private int xAxisLoc;
	
	private int width, height, mousePreY, pointsPerGraph;

	private double yPixelsPerUnit, xPixelsPerPoint, xPixelsPerGridline;
	
	//Distance the grid has been shifted. Reset once it reaches one gridline.  Always less than one gridline-width.
	private double xOffsetDist; 
		
	//in pixel coordinates
	private RandomAccessBuffer<Integer> points;
	
	/**
	 * 
	 * Creates a Graph.  Gridelines will be added once per second.
	 * 
	 * @param width Width of the graph, in px.
	 * @param height Height of the graph, in px.
	 * @param unitsPerGraph Number of units (and lines) from the bottom to the top of the graph.
	 * @param xAxisHeight Height of the X axis, in pixels, from the bottom of the graph.
	 */
	public Graph(int width, int height, double lengthSec, double pointsPerSec, double unitsPerGraph, int xAxisHeight)
	{
		setPreferredSize(new Dimension(width, height));
		this.width = width;
		this.height = height;
	
		pointsPerGraph = (int)Math.round(lengthSec * pointsPerSec);
		
		this.xPixelsPerPoint = ((double)width) / pointsPerGraph;
		this.xPixelsPerGridline = width / pointsPerSec;
		this.yPixelsPerUnit = height / unitsPerGraph;	
		
		xOffsetDist = 0;
		
		this.xAxisLoc = height - xAxisHeight;
		
		points = new RandomAccessBuffer<>(pointsPerGraph);
		
		setDoubleBuffered(true);
	}
	
	/**
	 * Draws the graph.
	 */
	public void paintComponent(Graphics g) {
		//clears graph.
		g.setColor(Color.gray);
		g.fillRect(0,0,width,height);
		
		//draws grid.
		g.setColor(new Color(230, 230, 230));//greyish
		
		//clears graphing area.
		g.setColor(Color.white);
		g.fillRect(0, 0, width, height);
		
		//draw grid
		g.setColor(new Color(230, 230, 230));//greyish
		for (int x = 0;x <= width;x+=xPixelsPerGridline)
		{
			g.drawLine(x + (int)xOffsetDist, 0, x + (int)xOffsetDist, height);
		}
		
		for (int y = 0;y <=height;y+=yPixelsPerUnit)
		{
			g.drawLine(0, y, width, y);
		}
		

				
		//draws x axis.
		g.setColor(Color.black);
		g.drawLine(0, xAxisLoc, width, xAxisLoc);//y axis.
		
		//draw points
		g.setColor(Color.red);
		
		int lastX = 0, lastY = 0;
				
		final int numPoints = points.getSize();
		
		for (int i = 0;i < numPoints ;i++) 
		{
			int x = (int)((numPoints - i - 1) * xPixelsPerPoint);
			int y = height - points.get(i) - xAxisLoc; //swing vertical coordinates are reversed
			
			//g.fillOval(x-scale/6, y-scale/6, scale/3, scale/3);//draws a dot at each point.
			if (i > 0)
			{
				g.drawLine(x, y, lastX, lastY);
			}
			
			lastX = x;
			lastY = y;
		}
		
		//if the graph line covers the whole graph, 
		//we want the gridlines to move backward to make it look like the graph is moving forward
		if(numPoints == pointsPerGraph)
		{
			if(xOffsetDist <= -xPixelsPerGridline)
			{
				xOffsetDist += xPixelsPerGridline;
			}
			xOffsetDist -= xPixelsPerPoint;
		}
		
		//System.out.println(points.toString());
		
		//draws box next to mouse showing points.
		/*g.setColor(Color.black);
		double x = (xAxisLoc-mouseX)/(double)scale;
		double y = (yAxisLoc-mouseY)/(double)scale;
		g.drawString("("+round(x, 2) + ", ", mouseX, mouseY);
		g.drawString(round(y, 2)+")", mouseX+60, mouseY);*/
	}
	
	public double round(double preNum, int decPlaces) {
    	return (double)Math.round((preNum*Math.pow(10, decPlaces)))/Math.pow(10, decPlaces);
    }
	
	/**
	 * Sets the points to be graphed.
	 * 
	 * Points are shifted vertically according to the X axis height, so that 0 equals the X axis height.  So points can be negative if the x axis height is greater than 0.
	 * 
	 * @param _points points to be graphed in the format of _points[point number][0 for x and 1 for y]
	 */
	public void addPoint(double point) 
	{
		//System.out.println("point: " + point);
		
		int pointPx = (int)(point * yPixelsPerUnit);
		points.enqueue(pointPx);
		repaint();
	}
	
	/**
	 * Gets the starting point for use in mouseDragged.
	 */
	public void mousePressed(MouseEvent e) {
		mousePreY = e.getY();
	}
	
	/**
	 * Moves graph with the dragging of the mouse.
	 */
	public void mouseDragged(MouseEvent e) {
		moveGraph(e.getY() - mousePreY);
		mousePreY = e.getY();

	}
	
	/**
	 * moves the graph vertically by mX
	 * @param mX move the x axis mX pixels
	 */
	public void moveGraph(int mX) 
	{
		int newXAxisLoc = xAxisLoc + mX;
		if(newXAxisLoc >= 0 && xAxisLoc < height)
		xAxisLoc += mX;
		repaint();
	}
	
	/**
	 * Sets the position of the X axis in pixels.
	 * @param pY New height of X axis (from the bottom of the graph).  Must not be greater than height.
	 */
	public void setXAxisPosition(int pX)
	{
		if(pX < 0 || pX > height)
		{
			throw new IllegalArgumentException("Invalid X axis position " + pX);
		}
		
		xAxisLoc = height - pX;
		repaint();
	}
	
	//extra stuffs
	public void mouseReleased(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
}
