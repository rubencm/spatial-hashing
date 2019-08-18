package net.rubencm.spatialhashing.swing.components;

import lombok.extern.slf4j.Slf4j;
import net.rubencm.spatialhashing.spatialhashing.Entity;
import net.rubencm.spatialhashing.spatialhashing.SpatialHashing;
import net.rubencm.spatialhashing.services.SpatialHashingFactory;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.util.Random;

public class GridComponent extends JComponent {
    private SpatialHashingFactory spatialHashingFactory;
    private SpatialHashing spatialHashing;

    private List<EntityComponent> entityComponents;

    public GridComponent(Integer gridSize, Integer cellSize, Integer numObjects, Integer objectRadius) {
        this.spatialHashingFactory = new SpatialHashingFactory(); // TODO: DI
        this.spatialHashing = spatialHashingFactory.get(gridSize, cellSize);

        addElements(numObjects, objectRadius);

        this.setPreferredSize(new Dimension(gridSize, gridSize));
        this.addAncestorListener(new EventHandler());
        this.animate();
    }

    private void addElements(int numObjects, int objectRadius) {
        Random rand = new Random();
        entityComponents = new ArrayList<EntityComponent>();

        for(int i = 0; i < numObjects; i++) {
            Entity entity = new Entity(this.spatialHashing, rand.nextInt(spatialHashing.getGridSize()), rand.nextInt(spatialHashing.getGridSize()), objectRadius);
            EntityComponent entityComponent = new EntityComponent(entity);
            add(entityComponent);
        }
    }

    public void add(EntityComponent entityComponent) {
        this.entityComponents.add(entityComponent);
        this.spatialHashing.addEntity(entityComponent.getEntity());
    }

    @Override
    public void paintComponent(Graphics g) {
        int gridSize = spatialHashing.getGridSize();
        int cellSize = spatialHashing.getCellSize();
        int cellsBySide = spatialHashing.getCellsBySide();

        // Paint grid
        g.setColor(Color.LIGHT_GRAY);
        for (int i = 1; i < cellsBySide; i++) {
            g.drawLine(i * cellSize, 0, i * cellSize, gridSize);
            g.drawLine(0, i * cellSize, gridSize, i * cellSize);
        }

        // Print  cell numbers
        g.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i < cellsBySide; i++) {
            for (int j=0; j < cellsBySide; j++) {
                g.drawString(Integer.toString((j*cellsBySide) + i), (i * cellSize) + 5, (j * cellSize) + 15);
            }
        }

        // Paint entities
        for (EntityComponent entityComponent : this.entityComponents) {
            entityComponent.paintComponent(g);
        }
    }

    Timer timer;
    SimulateMovementThread simulateMovement;

    private void animate() {
        // Rewrite component at 60fps
        int framesPerSecond = 60;

        timer = new Timer(1000 / framesPerSecond, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        });

        timer.start();

        // Thread for movement
        simulateMovement = new SimulateMovementThread();
        simulateMovement.start();
    }

    class SimulateMovementThread extends Thread {
        List<Long> execTime = new ArrayList<Long>();
        List<Integer> comparisions = new ArrayList<Integer>();

        @Override
        public void run() {
            while(true) {
                checkCollisions();
                displayPerfomance();
            }
        }

        private void checkCollisions() {
            int comps = 0;
            long start = System.currentTimeMillis();

            for (EntityComponent entityComponent : entityComponents) {
                entityComponent.updatePosition();

                // Check collisions
                entityComponent.setColor(Color.BLUE);
                for (Entity entity : entityComponent.getEntity().getNearObjects()) {
                    // http://stackoverflow.com/questions/1736734/circle-circle-collision
                    // https://stackoverflow.com/questions/481144/equation-for-testing-if-a-point-is-inside-a-circle
                    if (entityComponent.getEntity() != entity &&
                            Math.pow(entityComponent.getEntity().getX() - entity.getX(), 2) +
                                    Math.pow(entity.getY() - entityComponent.getEntity().getY(), 2) <=
                                    Math.pow(entity.getRadius() + entityComponent.getEntity().getRadius(), 2)
                    ) {
                        entityComponent.setColor(Color.RED);
                    }

                    comps++;
                }
            }

            execTime.add(System.currentTimeMillis() - start);
            comparisions.add(comps);
        }

        long last = System.currentTimeMillis();

        private void displayPerfomance() {
            long now = System.currentTimeMillis();

            if(now - last > 5000) {
                double avgExecTime = execTime.stream().mapToLong(val -> val).average().orElse(0.0);
                double avgComparisions = comparisions.stream().mapToInt(val -> val).average().orElse(0.0);

                System.out.println("Avg execution time(ms): " + Math.round(avgExecTime*100)/100f + "\tAvg comparisions: " + Math.round(avgComparisions));

                execTime.clear();
                comparisions.clear();
                last = now;
            }
        }
    }

    class EventHandler implements AncestorListener {
        @Override
        public void ancestorAdded(AncestorEvent ancestorEvent) {

        }

        @Override
        public void ancestorRemoved(AncestorEvent event) {
            simulateMovement.stop();
            timer.stop();
        }

        @Override
        public void ancestorMoved(AncestorEvent ancestorEvent) {

        }
    }
}
