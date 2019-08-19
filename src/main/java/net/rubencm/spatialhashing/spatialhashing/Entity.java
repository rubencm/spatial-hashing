package net.rubencm.spatialhashing.spatialhashing;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Entity {
    @Getter private SpatialHashing spatialHashing;

    @Getter @Setter protected Integer x;
    @Getter @Setter protected Integer y;
    @Getter protected Integer radius;

//    @Getter private Integer id;
//    private static int index = 0;

    public Entity(SpatialHashing spatialHashing, Integer x, Integer y, Integer radius) {
        this.spatialHashing = spatialHashing;

//        id = index++;

        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    // Return current cell
    public Integer getCell() {
        return this.spatialHashing.getCell(this.x, this.y);
    }

    // Return the cells the object is occupying
    public List<Integer> getCells() {
        Set<Integer> cells = new HashSet<Integer>();
        Integer pos[][] = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};

        // Look all surrounding cells
        for(int i = 0; i < pos.length; i++) {
            Integer x = this.x + (pos[i][0] * radius);
            Integer y = this.y + (pos[i][1] * radius);
            Integer cell = this.spatialHashing.getCell(x, y);

            // If the cell exist, add it
            if (cell >= 0 && cell < this.spatialHashing.getBuckets()) {
                cells.add(cell);
            }
        }

        return new ArrayList<>(cells);
    }

    // Get all entities in the surrounding cells
    public List<Entity> getNearObjects() {
        List<Entity> objects = new ArrayList<Entity>();
        List<Integer> cells = this.getCells();

        for(int cell: cells) {
            objects.addAll(this.spatialHashing.getObjectsInCell(cell));
        }

        return objects;
    }

    public void updatePosition(int x, int y) {
        setX(x);
        setY(y);

        spatialHashing.update(this);
    }

//    @Override
//    public boolean equals(Object o) {
//        if (o == this) return true;
//
//        if (!(o instanceof Entity)) return false;
//
//        return ((Entity)o).getId() == id;
//    }
//
//    @Override
//    public int hashCode() {
//        return id.hashCode();
//    }
}
