package net.rubencm.spatialhashing;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
		JTextField txtMapSize = new JTextField("1000");
		panel1.add(txtMapSize);
		panel1.add(new JLabel("Tamaño de la celda"));
		JTextField txtCellSize = new JTextField("250");
		panel1.add(txtCellSize);
		panel1.add(new JLabel("Numero de objetos"));
		JTextField txtNumObjects = new JTextField("100");
		panel1.add(txtNumObjects);
		panel1.add(new JLabel("Radio de los objetos"));
		JTextField txtObjectsRadius = new JTextField("50");
		panel1.add(txtObjectsRadius);
		
		JPanel panel2 = new JPanel();
		
		JButton button = new JButton("Ejecutar simulación");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Thread t = new Thread(new Runnable() {
				    @Override
				    public void run() {

						MapWindow map = new MapWindow(
								0,
								Integer.parseInt(txtMapSize.getText()),
								Integer.parseInt(txtCellSize.getText()),
								Integer.parseInt(txtNumObjects.getText()),
								Integer.parseInt(txtObjectsRadius.getText()));
						map.addWindowListener(new WindowAdapter() {
							public void windowClosing(WindowEvent e) {
								System.exit(0);
							}
						});
						map.pack();
						map.setVisible(true);
						map.world.run();
				    }

				});
				t.start();
			}
		});
		panel2.add(button);
		
		pane.add(panel1, BorderLayout.NORTH);;
		pane.add(panel2, BorderLayout.SOUTH);
	}

}
