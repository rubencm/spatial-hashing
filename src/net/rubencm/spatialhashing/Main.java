package net.rubencm.spatialhashing;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] argc) {
        FormWindow f = new FormWindow();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.addComponentsToPane(f.getContentPane());
        f.pack();
        f.setVisible(true);
	}
	
}
