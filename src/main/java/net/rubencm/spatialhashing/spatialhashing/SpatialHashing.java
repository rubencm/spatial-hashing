package net.rubencm.spatialhashing.spatialhashing;

import lombok.Getter;

import java.util.*;

public class SpatialHashing {

    @Getter
    private int gridSize;

    @Getter
    private int cellSize;

    @Getter
    private int cellsBySide; // gridSize / cellSize

    @Getter
    private int buckets; // cellsBySide * cellsBySide

    private float conversionFactor; // 1f / cellSize

    private Map<Entity, Integer> objectIndex;
    private Map<Integer, List<Entity>> hashtable;


    public SpatialHashing(int gridSize, int cellSize) {
        this.gridSize = gridSize;
        this.cellSize = cellSize;

        cellsBySide = gridSize / cellSize;
        buckets = cellsBySide * cellsBySide;
        conversionFactor = 1f / cellSize;

        // Initialize objectIndex
        objectIndex = Collections.synchronizedMap(new HashMap<Entity, Integer>());

        // Initialize hashtable
        hashtable = Collections.synchronizedMap(new HashMap<Integer, List<Entity>>());
        for (int i = 0; i < buckets; i++) {
            hashtable.put(i, Collections.synchronizedList(new ArrayList<Entity>()));
        }
    }

    /**
     * Add entity to grid
     *
     * @param entity
     */
    public void addEntity(Entity entity) {
        Position position = entity.getPosition();
        int cell = getCell(position.getX(), position.getY());

        synchronized (entity) {
            objectIndex.put(entity, cell);
            hashtable.get(cell).add(entity);
        }
    }

    /**
     * Update entity cell
     *
     * @param entity
     */
    public void updateEntity(Entity entity) {
        Position position = entity.getPosition();
        int newCell = getCell(position.getX(), position.getY());

        synchronized (entity) {
            int oldCell = getCell(entity);

            if(oldCell != newCell) {
                objectIndex.put(entity, newCell);

                hashtable.get(oldCell).remove(entity);
                hashtable.get(newCell).add(entity);
            }
        }
    }

    /**
     * Delete entity from grid
     *
     * @param entity
     */
    public void deleteEntity(Entity entity) {
        synchronized (entity) {
            int cell = objectIndex.get(entity);

            objectIndex.remove(entity);
            hashtable.get(cell).remove(entity);
        }
    }

    /**
     * Get objects from a cell
     *
     * @param cell
     * @return
     */
    public List<Entity> getEntitiesInCell(Integer cell) {
        return hashtable.get(cell);
    }

    /**
     * Get cell number by coordinates
     *
     * @param x
     * @param y
     * @return
     */
    public int getCell(Integer x, Integer y) {
        return (int) (x * conversionFactor) + (int) (y * conversionFactor) * cellsBySide;
    }

    /**
     * Get cell number of entity
     *
     * @param entity
     * @return
     */
    public int getCell(Entity entity) {
        return objectIndex.get(entity);
    }

    /**
     * Get cells the object is occupying
     *
     * @param entity
     * @return
     */
    public List<Integer> getCells(Entity entity) {
        Set<Integer> cells = new HashSet<Integer>();
        int pos[][] = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
        Position position = entity.getPosition();

        // Look all surrounding cells
        for(int i = 0; i < pos.length; i++) {
            int cornerX = position.getX() + (pos[i][0] * entity.getRadius());
            int cornerY = position.getY() + (pos[i][1] * entity.getRadius());

            int cell = getCell(cornerX, cornerY);

            // If the cell exist, add it
            if (cell >= 0 && cell < buckets) {
                cells.add(cell);
            }
        }

        return new ArrayList<>(cells);
    }

    /**
     * Get entities near an entity
     *
     * @param entity
     * @return
     */
    public List<Entity> getNearEntities(Entity entity) {
        List<Entity> entities = new ArrayList<Entity>();
        List<Integer> cells = this.getCells(entity);

        for(int cell: cells) {
            entities.addAll(getEntitiesInCell(cell));
        }

        return entities;
    }
}
