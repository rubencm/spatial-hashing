package net.rubencm.spatialhashing;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class FormWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	
	public FormWindow() {
		super("Spatial-Hashing Algorithm Test");
		setResizable(false);
	}
	
	public void addComponentsToPane(final Container pane) {
		
		JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayout(0,2,10,10));
		
		panel1.add(new JLabel("Tamaño del mapa"));
		panel1.add(new JTextField("1000"));
		panel1.add(new JLabel("Tamaño de la celda"));
		panel1.add(new JTextField("250"));
		panel1.add(new JLabel("Numero de objetos"));
		panel1.add(new JTextField("100"));
		panel1.add(new JLabel("Tamaño de los objetos"));
		panel1.add(new JTextField("50"));
		
		JPanel panel2 = new JPanel();
		
		JButton button = new JButton("Con Spatial-Hashing");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*
				MapWindow map = new MapWindow(
						0,
						Integer.parseInt(txtMapSize.getText()),
						Integer.parseInt(txtCellSize.getText()),
						Integer.parseInt(txtNumObjects.getText()),
						Integer.parseInt(txtObjectRadius.getText()));
				map.pack();
				map.setVisible(true);
				*/
				/*
				String[] s = new String[0];
				MapWindow.main(s);
				*/
			}
		});
		panel2.add(button);
		
		pane.add(panel1, BorderLayout.NORTH);;
		pane.add(panel2, BorderLayout.SOUTH);
	}

}
