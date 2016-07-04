package net.rubencm.spatialhashing;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Comprobar, un objeto no puede ser nunca mas grande que una celda
public class Entity {
	
	public class Position {
		public float x;
		public float y;
		
		public Position(float x, float y) {
			this.x = x;
			this.y = y;
		}
	}
	
	public World world;
	public Position position;
	public int radius;
	
	public int currentCell;
	
	int dirX, dirY;
	
	Color color = Color.BLUE;
	
	public Entity(World world, int radius, float x, float y) {
		this.world = world;
		this.radius = radius;
		this.position = new Position(x, y);
	}
	
	// Devuelve la celda donde está el objeto
	public int getCell() {
		return world.getCell(position.x, position.y);
	}
	
	// Devuelve los indices de las celdas que ocupa el objeto
	public Set<Integer> getCells() {
		Set<Integer> cells = new HashSet<Integer>();
		int[][] pos = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
		int c;
		
		for(int i=0; i<pos.length; i++) {
			c = world.getCell(position.x+(pos[i][0]*radius), position.y+(pos[i][1]*radius));
			if(c>=0 && c<world.buckets) {
				cells.add(c);
			}
		}
		
		return cells;
	}
	
	// Obtiene los objetos cercanos en todas las celdas que ocupa
	public List<Entity> getNearObjects() {
		List<Entity> objects = new ArrayList<Entity>();
		Set<Integer> cells = getCells();
		
		for(int s: cells) {
			objects.addAll(world.getObjectsInCell(s));
		}
		
		return objects;
	}
	
	// Dibujar circulo
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillOval((int)position.x-radius, (int)position.y-radius, 2*radius, 2*radius);
	}
	
	// Simular movimiento
	public void update() {
		if(position.x+dirX >= world.max || position.x+dirX < world.min) {
			dirX*=-1;
		}
			
		if(position.y+dirY >= world.max || position.y+dirY < world.min) {
			dirY*=-1;
		}
		
		position.x+=dirX;
		position.y+=dirY;
		
		// Renueva la posicion del objeto en el hashtable
		// con la nueva posicion
		int newCell = world.getCell(position.x, position.y);
		if(newCell != currentCell) {
			world.hashtable.get(currentCell).remove(this);
			currentCell = newCell;
			world.hashtable.get(currentCell).add(this);
		}
	}
}
	