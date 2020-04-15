


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

class Pair{
	
	public int col;
	public int row; 
	public Pair(int c, int r) {
		col = c; 
		row = r; 
	}
	@Override
	public boolean equals(Object o) {
		Pair p = (Pair)o;
		return (this.col == p.col && this.row == p.row);
	}
	@Override 
	public int hashCode(){
		return Integer.hashCode(col)*31+Integer.hashCode(row);
	}
	@Override
	public String toString() {
		return String.format("(col=%d,row=%d)", col,row);
	}
}


public class PaintPanel extends JPanel implements ActionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int numRows = 180; 
	private static final int numCols = 200;
	private static final int cellSize = 16; 
	
	private PaintPanel paintPanel;
	public HashMap<Pair, Boolean> liveCells = new HashMap<Pair,Boolean>();
	public boolean iterating = false; 
	
	Timer timer = new Timer(250,this);//250 milli seconds 
	public PaintPanel() throws Exception{
		timer.start();
		this.setPreferredSize(new Dimension(800,600));
		this.setSize(new Dimension(800,600));
		this.setBackground(Color.white);
		this.addMouseListener(mouseListener);
		paintPanel = this; 
	}
	
	
	

	//timer fires
	@Override
	public void actionPerformed(ActionEvent e){
		if (!iterating) {
			return; //do nothing 
		}
		liveCells=nextState(liveCells);
		triggerRepaint();
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		drawUniverse(g2);
	}
	public void triggerRepaint() {
		paintPanel.validate();
		paintPanel.repaint();
	}
	public void stringToLiveCells(String s) {
		String[] lines = s.split("\\n");
		for(String line:lines) {
			String numStr[] = line.split(",");
			int col = Integer.valueOf(numStr[0]);
			int row = Integer.valueOf(numStr[1]);
			Pair p = new Pair(col,row);
			liveCells.put(p, true); 
		}
	}
	public String liveCellsToString() {
		return liveCells.keySet().stream().map(p->String.format("%d,%d",p.col, p.row)).collect(Collectors.joining("\n"));
	}
	private MouseListener mouseListener = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
		
			Point point = e.getLocationOnScreen(); 
			//System.out.println(point);
			SwingUtilities.convertPointFromScreen(point, paintPanel);
			Pair pos = calcCellCoordinates(point);
			//System.out.println(pos);
			Boolean exist = liveCells.get(pos);
			if (exist == null) {
				liveCells.put(pos, true);
			} else {
				liveCells.remove(pos);
			}
			triggerRepaint();
		}
	};
	private void drawUniverse(Graphics2D g2) {
		for(int r=0;r<numRows;r++) {
			for (int c=0;c<numCols;c++) {
				Boolean on = liveCells.get(new Pair(c,r));
				if (on == null  ) {
					drawCell(g2,c*cellSize,r*cellSize,cellSize,cellSize,Color.white,Color.black);
				} else {
					drawCell(g2,c*cellSize,r*cellSize,cellSize,cellSize,Color.green,Color.black);
				}
			}
		}
	}
	private void drawCell(Graphics2D g2,int x, int y, int width, int height, Color fillColor, Color strokeColor) {
		g2.setColor(strokeColor);
		g2.drawRect(x, y, width, height);
		g2.setColor(fillColor);
		g2.fillRect(x, y, width, height);
	}
	private Pair calcCellCoordinates(Point point) {
		int col = point.x / cellSize;
		int row = point.y / cellSize;
		return new Pair(col,row);
	}
	
	private int countLiveNeighbors(Pair pos, HashMap<Pair, Boolean> currState) {
		int count = 0; 
		ArrayList<Pair> neighbors = calcNeighborCells(pos); 
		for(Pair p:neighbors) {
			if (currState.get(p) != null) {
				count++;
			}
		}
		return count;
	}
	
	private ArrayList<Pair> calcNeighborCells(Pair pos){
		Pair p1 = new Pair(pos.col-1,pos.row);
		Pair p2 = new Pair(pos.col+1,pos.row);
		Pair p3 = new Pair(pos.col,pos.row+1);
		Pair p4 = new Pair(pos.col,pos.row-1);
		Pair p5 = new Pair(pos.col-1,pos.row-1);
		Pair p6 = new Pair(pos.col+1,pos.row-1);
		Pair p7 = new Pair(pos.col-1,pos.row+1);
		Pair p8 = new Pair(pos.col+1,pos.row+1);
		ArrayList<Pair> list = new ArrayList<Pair>();
		list.add(p1);
		list.add(p2);
		list.add(p3);
		list.add(p4);
		list.add(p5);
		list.add(p6);
		list.add(p7);
		list.add(p8);
		return list;
	}
	
	private HashMap<Pair, Boolean> nextState(HashMap<Pair, Boolean> curr){
		HashMap<Pair, Boolean> next = new HashMap<Pair, Boolean>();
		HashMap<Pair, Boolean> allDeadNeighborsOfLiveCells = new HashMap<Pair, Boolean>();
		for (Pair liveCell:curr.keySet()) {
			int count = countLiveNeighbors(liveCell, curr);
			if (count == 2 || count == 3) {
				next.put(liveCell, true);//continue live 
			}
			ArrayList<Pair> neighbors = calcNeighborCells(liveCell);
			for (Pair p:neighbors) {
				if (curr.get(p) == null) {
					allDeadNeighborsOfLiveCells.put(p, true);//it will remove duplicates 
				}
			}
		}
		
		for (Pair cell:allDeadNeighborsOfLiveCells.keySet()) {
			int count = countLiveNeighbors(cell, curr);
			if (count == 3) {
				next.put(cell, true);//become alive 
			}
		}
		
		
		return next; 
	}
}