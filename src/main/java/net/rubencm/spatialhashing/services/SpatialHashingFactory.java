package net.rubencm.spatialhashing.services;

import net.rubencm.spatialhashing.spatialhashing.SpatialHashing;

public class SpatialHashingFactory {
    public static SpatialHashing get(Integer gridSize, Integer cellSize) {
        return new SpatialHashing(gridSize, cellSize);
    }
}
