package net.rubencm.spatialhashing;

import net.rubencm.spatialhashing.spatialhashing.Entity;
import net.rubencm.spatialhashing.spatialhashing.SpatialHashing;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class SpatialHashingTests {

    private SpatialHashing spatialHashing;
    private Entity entity;

    @Before
    public void setUp() {
        int gridSize = 1000;
        int cellSize = 250;
        spatialHashing = new SpatialHashing(gridSize, cellSize);

        entity = new Entity(spatialHashing, 550, 550, 100);
        spatialHashing.addEntity(entity);

        spatialHashing.addEntity(new Entity(spatialHashing, 0, 0, 100));
        spatialHashing.addEntity(new Entity(spatialHashing, 375, 375, 100));
        spatialHashing.addEntity(new Entity(spatialHashing, 700, 550, 100));
    }

    @Test
    public void testGetObjectsInCell() {
        assertEquals(spatialHashing.getObjectsInCell(0).size(), 1);
        assertEquals(spatialHashing.getObjectsInCell(1).size(), 0);
        assertEquals(spatialHashing.getObjectsInCell(5).size(), 1);
        assertEquals(spatialHashing.getObjectsInCell(10).size(), 2);
    }

    @Test
    public void testGetCell() {
        assertEquals(spatialHashing.getCell(0, 0), 0);
        assertEquals(spatialHashing.getCell(700, 700), 10);
        assertEquals(spatialHashing.getCell(900, 700), 11);
    }

    @Test
    public void testEntityGetCells() {
        assertThat(entity.getCells(), containsInAnyOrder(5, 6, 9, 10));
    }

    @Test
    public void testEntityNearBojects() {
        assertEquals(entity.getNearObjects().size(), 3);
    }
}
