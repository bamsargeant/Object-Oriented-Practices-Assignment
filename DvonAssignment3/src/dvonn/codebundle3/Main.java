package dvonn.codebundle3;

import javax.swing.JFrame;

import java.awt.Dimension;

public class Main {
	public static void main(String[] args){
		Board b = new Board();
	    
		JFrame mainFrame = new JFrame("DVONN");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(new Dimension(845,446));
		mainFrame.setContentPane(b);
		mainFrame.setVisible(true);
	}
}