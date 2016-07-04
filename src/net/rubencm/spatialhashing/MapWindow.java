package net.rubencm.spatialhashing;

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
	
}
