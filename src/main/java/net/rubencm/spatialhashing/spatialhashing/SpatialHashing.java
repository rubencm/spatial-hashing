package net.rubencm.spatialhashing.spatialhashing;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class SpatialHashing {

    @Getter
    private Integer gridSize;
    @Getter
    private Integer cellSize;

    @Getter
    private Integer cellsBySide; // gridSize / cellSize
    @Getter
    private Integer buckets; // cellsBySide * cellsBySide
    private Float conversionFactor; // 1f / cellSize

    private List<Entity> entities; // TODO: Revisar paper, entities no hace nada
    private Hashtable<Integer, List<Entity>> hashtable;

    public SpatialHashing(Integer gridSize, Integer cellSize) {
        this.gridSize = gridSize;
        this.cellSize = cellSize;

        this.cellsBySide = gridSize / cellSize;
        this.buckets = cellsBySide * cellsBySide;
        this.conversionFactor = 1f / cellSize;

        this.entities = new ArrayList<Entity>();

        // Initialize hashtable
        this.hashtable = new Hashtable<Integer, List<Entity>>();
        for (int i = 0; i < buckets; i++) {
            this.hashtable.put(i, new ArrayList<Entity>());
        }
    }

    // Add entity to the List and HashTable
    public void addEntity(Entity entity) {
        entities.add(entity);
        hashtable.get(entity.getCell()).add(entity);
    }

    // Get objects from a cell
    public List<Entity> getObjectsInCell(Integer cell) {
        return hashtable.get(cell);
    }

    // Given some coordinates, get the cell
    public int getCell(Integer x, Integer y) {
        return (int) (x * conversionFactor) + (int) (y * conversionFactor) * cellsBySide;
    }

    public void updatePosition(Entity entity) {
        // FIXME
        for(List<Entity> entities: hashtable.values()) {
            entities.remove(entities.remove(entity));
        }

        hashtable.get(entity.getCell()).add(entity);
    }
}
