package net.rubencm.spatialhashing;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

import javax.swing.JFrame;

public class MapWindow extends JFrame{

	private static final long serialVersionUID = 1L;
	World world;
	
	public MapWindow(int min, int max, int cell_size, int objects, int radius) {
		super("Spatial-Hashing Algorithm");
		
		setResizable(false);
		
		world = new World(min, max, cell_size);
		this.add(world);
		
		Random rand = new Random();
		
		for(int i=0; i<objects; i++) {
			Entity o = new Entity(world, radius, rand.nextInt((max-min))+min, rand.nextInt((max-min))+min);
			o.dirX = rand.nextInt(10)-5;
			o.dirY = rand.nextInt(10)-5;
			world.addObject(o);
		}
	}
	
    public static void main(String[] args) {
    	//int min=0, max=400, cell=5, objects=50000, radius=1;
    	int min=0, max=1000, cell=250, objects=100, radius=50;
    	
    	MapWindow window = new MapWindow(min, max, cell, objects, radius);
    	
		window.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		window.pack();
		window.setVisible(true);
	    window.world.run();
   }
	
}
