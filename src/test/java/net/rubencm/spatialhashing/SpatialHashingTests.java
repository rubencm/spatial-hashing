package net.rubencm.spatialhashing;

import net.rubencm.spatialhashing.spatialhashing.Entity;
import net.rubencm.spatialhashing.spatialhashing.Position;
import net.rubencm.spatialhashing.spatialhashing.SpatialHashing;
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

        entity = new Entity(new Position(550, 550), 100, 50);
        spatialHashing.addEntity(entity);

        spatialHashing.addEntity(new Entity(new Position(0, 0), 100, 50));
        spatialHashing.addEntity(new Entity(new Position(375, 375), 100, 50));
        spatialHashing.addEntity(new Entity(new Position(700, 550), 100, 50));
    }

    @Test
    public void testGetObjectsInCell() {
        assertEquals(spatialHashing.getEntitiesInCell(0).size(), 1);
        assertEquals(spatialHashing.getEntitiesInCell(1).size(), 0);
        assertEquals(spatialHashing.getEntitiesInCell(5).size(), 1);
        assertEquals(spatialHashing.getEntitiesInCell(10).size(), 2);
    }

    @Test
    public void testGetCell() {
        assertEquals(spatialHashing.getCell(0, 0), 0);
        assertEquals(spatialHashing.getCell(700, 700), 10);
        assertEquals(spatialHashing.getCell(900, 700), 11);
    }

    @Test
    public void testEntityGetCells() {
        assertThat(spatialHashing.getCells(entity), containsInAnyOrder(5, 6, 9, 10));
    }

    @Test
    public void testEntityNearBojects() {
        assertEquals(spatialHashing.getNearEntities(entity).size(), 3);
    }
}
