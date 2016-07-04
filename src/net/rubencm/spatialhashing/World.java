	package net.rubencm.spatialhashing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

public class World extends JComponent {

	private static final long serialVersionUID = 1L;
	
	public float min; // inicio mapa
	public float max; // fin mapa
	public int cell_size; // tamaño lado celda
	
	public int width; // numero de celdas por lado
	public int buckets; // celdas totales
	public float conversion_factor;

	public List<Entity> objects;
	public Hashtable<Integer, List<Entity>> hashtable;
	
	public World(int min, int max, int cell_size) {
		
		this.min = min;
		this.max = max;
		this.cell_size = cell_size;
		
		width = (max-min)/cell_size;
		buckets = width*width;
		conversion_factor = 1f/cell_size;
		
		// Todos los objetos
		objects = new ArrayList<Entity>();
		
		// Inicializar hashtable
		hashtable = new Hashtable<Integer, List<Entity>>();
		for(int i=0; i<buckets; i++) {
			hashtable.put(i, new ArrayList<Entity>());
		}
		
		setPreferredSize(new Dimension(max-min, max-min));
	}
	
	// Añade un objeto al mundo
	public void addObject(Entity o) {
		objects.add(o);
		hashtable.get(o.getCell()).add(o);
	}
	
	// Obtiene los objetos de una celda
	public List<Entity> getObjectsInCell(int cell) {
		return hashtable.get(cell);
	}
	
	// Dadas unas coordenadas obtiene la celda a la que pertenece
	public int getCell(float x, float y) {
		return (int)(x*conversion_factor)+(int)(y*conversion_factor)*width;
	}
	
	public void paint(Graphics g) {
		
		// pintar lineas
		for(int i=1; i<width; i++) {
			g.setColor(Color.LIGHT_GRAY);
			g.drawLine(i*cell_size, 0, i*cell_size, (int) max);
			g.drawLine(0, i*cell_size, (int) max, i*cell_size);
		}
		
		// pintar entidades
		for(Entity o: objects) {
			o.draw(g);
		}
	}

	public static final int FPS = 60;
	
	public void run() {
		
		long last = System.nanoTime();
		long now;
		
		double diff = 0;
		double nanosegundosPorFPS = 1000000000.0 / FPS;
		
		int fotogramas = 0;
		int comparaciones = 0;
		long showInfo = System.currentTimeMillis();
		
		while(true) {
			
			now = System.nanoTime();
			diff = (now - last);
			
			if (diff >= nanosegundosPorFPS) {
				
				// Redibujar objetos
				for(Entity o: objects) {
					o.update();
					
					o.color = Color.BLUE;
					for(Entity n: o.getNearObjects()) {
						// http://stackoverflow.com/questions/1736734/circle-circle-collision
						if(n != o &&
							Math.pow(n.position.x-o.position.x, 2) +
							Math.pow(o.position.y-n.position.y, 2) <=
							Math.pow(o.radius+n.radius, 2)
						) {
							o.color = Color.RED;
						}
						comparaciones++;
					}
				}
				
				// Redibujar
				try {
					SwingUtilities.invokeAndWait(
					new Runnable() {
						public void run() {
							paintImmediately((int)min, (int)min, (int)(max-min), (int)(max-min));
						}
					});
				} catch (Exception e) {}
				
				fotogramas++;
				last = now;
			} else { // Si se quita esto se obtienen el total de fps, sino se pierde ~1
				try {
					Thread.sleep(1000/FPS); // Esperamos 1 fps
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			long showInfo2 = System.currentTimeMillis();
			if (showInfo2 - showInfo > 1000) {
				showInfo = showInfo2;
				System.out.println("FPS: "+fotogramas+", Comparaciones/seg: "+comparaciones);
				fotogramas = 0;
				comparaciones = 0;
			}

		}
	}
}
