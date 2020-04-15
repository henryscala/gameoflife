
import java.awt.event.ActionListener;


import javax.swing.JFrame;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;



public class GameOfLifeFrame extends JFrame  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PaintPanel paintPanel = new PaintPanel();
	public static void main(String[] args) throws Exception {
		
		
		GameOfLifeFrame frame = new GameOfLifeFrame(); 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		frame.setSize(800, 600);
		frame.pack();
		frame.setVisible(true);
		
	}
	
	
	
	
	public GameOfLifeFrame() throws Exception {
		super(); 
		this.getContentPane().add(paintPanel);
		updateTitle();
		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
		JMenu gameMenu = new JMenu("Game");
		JMenuItem clearAction = new JMenuItem("Clear"); 
		JMenuItem startAction = new JMenuItem("Start"); 
		JMenuItem stopAction = new JMenuItem("Stop"); 
		JMenuItem stepAction = new JMenuItem("Step"); 
		gameMenu.add(startAction);
		gameMenu.add(stopAction);
		gameMenu.add(clearAction);
		gameMenu.add(stepAction);
		menuBar.add(gameMenu); 
		
		startAction.addActionListener(startListener);
		stopAction.addActionListener(stopListener);
		clearAction.addActionListener(clearListener);
		stepAction.addActionListener(stepListener);
		
	}
	private void updateTitle() {
		this.setTitle("iterating="+paintPanel.iterating);
	}
	private ActionListener stopListener =(e)-> {
		paintPanel.iterating = false; 
		updateTitle();
	};
	private ActionListener startListener =(e)-> {
		paintPanel.iterating = true;
		updateTitle();
	};
	private ActionListener clearListener =(e)-> {
		paintPanel.liveCells.clear();
		paintPanel.triggerRepaint();
	};
	private ActionListener stepListener =(e)-> {
		paintPanel.iterating = true; 
		paintPanel.actionPerformed(null);
		paintPanel.iterating = false; 
		updateTitle();
	};

}
