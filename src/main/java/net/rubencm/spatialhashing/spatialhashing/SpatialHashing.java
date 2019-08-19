package net.rubencm.spatialhashing.spatialhashing;

import lombok.Getter;

import java.util.*;

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

    private Map<Entity, Integer> objectIndex;
    private Map<Integer, List<Entity>> hashtable;


    public SpatialHashing(Integer gridSize, Integer cellSize) {
        this.gridSize = gridSize;
        this.cellSize = cellSize;

        this.cellsBySide = gridSize / cellSize;
        this.buckets = cellsBySide * cellsBySide;
        this.conversionFactor = 1f / cellSize;

        // Initialize objectIndex
        this.objectIndex = new HashMap<Entity, Integer>();

        // Initialize hashtable
        this.hashtable = new HashMap<Integer, List<Entity>>();
        for (int i = 0; i < buckets; i++) {
            this.hashtable.put(i, new ArrayList<Entity>());
        }
    }

    // Add entity to the List and HashTable
    public void add(Entity entity) {
        int cell = entity.getCell();

        synchronized (this) {
            objectIndex.put(entity, cell);
            hashtable.get(cell).add(entity);
        }
    }

    public void update(Entity entity) {
        int oldCell = objectIndex.get(entity);
        int newCell = entity.getCell();

        if(oldCell != newCell) {
            synchronized (this) {
                objectIndex.put(entity, newCell);

                hashtable.get(oldCell).remove(entity);
                hashtable.get(newCell).add(entity);
            }
        }
    }

    public void delete(Entity entity) {
        int cell = objectIndex.get(entity);

        synchronized (this) {
            objectIndex.remove(entity);
            hashtable.get(cell).remove(entity);
        }
    }

    // Get objects from a cell
    public List<Entity> getObjectsInCell(Integer cell) {
        return hashtable.get(cell);
    }

    // Given some coordinates, get the cell
    public int getCell(Integer x, Integer y) {
        return (int) (x * conversionFactor) + (int) (y * conversionFactor) * cellsBySide;
    }
}
