
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.swing.JFileChooser;
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
	final JFileChooser fileChooser = new JFileChooser();
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
		startAction.addActionListener(startListener);
		stopAction.addActionListener(stopListener);
		clearAction.addActionListener(clearListener);
		stepAction.addActionListener(stepListener);
		
		
		JMenu fileMenu = new JMenu("File");
		JMenuItem saveAction = new JMenuItem("Save"); 
		JMenuItem openAction = new JMenuItem("Open");
		fileMenu.add(saveAction);
		fileMenu.add(openAction);
		saveAction.addActionListener(saveListener);
		openAction.addActionListener(openListener);
		
		menuBar.add(fileMenu);
		menuBar.add(gameMenu); 
		
	}
	private void updateTitle() {
		this.setTitle("iterating="+paintPanel.iterating);
	}
	private ActionListener saveListener = (event)-> {
		//In response to a button click:
		int returnVal = fileChooser.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			try {
				Files.write(Paths.get(file.getPath()), paintPanel.liveCellsToString().getBytes("UTF-8"), 
						StandardOpenOption.WRITE,
						StandardOpenOption.CREATE,
						StandardOpenOption.TRUNCATE_EXISTING);	
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} 
	};
	private ActionListener openListener = (e)-> {
		int returnVal = fileChooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			
			try {
				byte[] buffer = Files.readAllBytes(file.toPath());
				String str = new String(buffer,"UTF-8");
				paintPanel.stringToLiveCells(str);
				paintPanel.triggerRepaint();
			} catch (IOException e1) {

				e1.printStackTrace();
			}
		} 
	};
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
