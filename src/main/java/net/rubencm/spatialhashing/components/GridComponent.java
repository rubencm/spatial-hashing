package net.rubencm.spatialhashing.components;

import net.rubencm.spatialhashing.spatialhashing.Entity;
import net.rubencm.spatialhashing.spatialhashing.SpatialHashing;
import net.rubencm.spatialhashing.services.SpatialHashingFactory;

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.util.Random;


public class GridComponent extends JComponent implements Runnable {
    private SpatialHashingFactory spatialHashingFactory;
    private SpatialHashing spatialHashing;

    List<EntityComponent> entityComponents;

    public GridComponent(Integer gridSize, Integer cellSize, Integer numObjects, Integer objectRadius) {
        this.spatialHashingFactory = new SpatialHashingFactory();
        this.spatialHashing = spatialHashingFactory.get(gridSize, cellSize);

        this.setPreferredSize(new Dimension(gridSize, gridSize));

        this.entityComponents = new ArrayList<EntityComponent>();

        // Add elements
        Random rand = new Random();
        for(int i = 0; i < numObjects; i++) {
            Entity entity = new Entity(this.spatialHashing, rand.nextInt(gridSize), rand.nextInt(gridSize), objectRadius);

            EntityComponent entityComponent = new EntityComponent(entity);
            entityComponent.setDirX(rand.nextInt(10) - 5);
            entityComponent.setDirY(rand.nextInt(10) - 5);

            this.add(entityComponent);
        }
    }

    public void add(EntityComponent entityComponent) {
        this.entityComponents.add(entityComponent);
        this.spatialHashing.addEntity(entityComponent.getEntity());
    }

    @Override
    public void paintComponent(Graphics g) {
        Integer gridSize = this.spatialHashing.getGridSize();
        Integer cellSize = this.spatialHashing.getCellSize();
        Integer cellsBySide = this.spatialHashing.getCellsBySide();

        // Paint grid
        for (int i = 1; i < cellsBySide; i++) {
            g.setColor(Color.LIGHT_GRAY);
            g.drawLine(i * cellSize, 0, i * cellSize, gridSize);
            g.drawLine(0, i * cellSize, gridSize, i * cellSize);
        }

        // Paint entities
        for (EntityComponent entityComponent : this.entityComponents) {
            entityComponent.paintComponent(g);
        }
    }

    public void run() {
        while(true) {
            for(EntityComponent entityComponent : this.entityComponents) {
                entityComponent.updatePosition();

                // Check collisions
                entityComponent.setColor(Color.BLUE);
                for(Entity entity : entityComponent.getEntity().getNearObjects()) {
                    // http://stackoverflow.com/questions/1736734/circle-circle-collision
                    if(entityComponent.getEntity() != entity &&
                            Math.pow(entityComponent.getEntity().getX()-entity.getX(), 2) +
                                    Math.pow(entity.getY()-entityComponent.getEntity().getY(), 2) <=
                                    Math.pow(entity.getRadius()+entityComponent.getEntity().getRadius(), 2)
                    ) {
                        entityComponent.setColor(Color.RED);
                    }
                }

                // Redraw
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {
                        public void run() {
                            SpatialHashing spatialHashing = entityComponent.getEntity().getSpatialHashing();
                            paintImmediately(0, 0, spatialHashing.getGridSize(), spatialHashing.getGridSize());
                        }
                    });
                } catch(Exception e) {}
            }
        }
    }
}
