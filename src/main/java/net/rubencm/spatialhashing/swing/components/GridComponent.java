package net.rubencm.spatialhashing.swing.components;

import net.rubencm.spatialhashing.services.SpatialHashingFactory;
import net.rubencm.spatialhashing.spatialhashing.Entity;
import net.rubencm.spatialhashing.spatialhashing.Position;
import net.rubencm.spatialhashing.spatialhashing.SpatialHashing;
import net.rubencm.spatialhashing.swing.components.threads.CheckCollisionsThread;
import net.rubencm.spatialhashing.swing.components.threads.PrintStatsThread;
import net.rubencm.spatialhashing.swing.components.threads.SimulateMovementThread;
import net.rubencm.spatialhashing.swing.components.threads.UpdateEntitiesCellThread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GridComponent extends JComponent {
    private SpatialHashing spatialHashing;

    private List<EntityComponent> entityComponents;

    public GridComponent(Integer gridSize, Integer cellSize, Integer numObjects, Integer objectRadius) {
        this.spatialHashing = SpatialHashingFactory.get(gridSize, cellSize);

        // Add objects to grid
        addElements(numObjects, objectRadius);

        this.setPreferredSize(new Dimension(gridSize, gridSize));
        this.start();
    }

    private void addElements(int numObjects, int objectRadius) {
        Random rand = new Random();
        entityComponents = new ArrayList<EntityComponent>();

        for(int i = 0; i < numObjects; i++) {
            Entity entity = new Entity(
                    new Position(
                            rand.nextInt(spatialHashing.getGridSize()),
                            rand.nextInt(spatialHashing.getGridSize())
                    ),
                    objectRadius,
                    rand.nextInt(500)
            );

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
        super.paintComponent(g);

        paintGrid(g);
        paintCellNumbers(g);
        paintEntities(g);
    }

    private void paintGrid(Graphics g) {
        int gridSize = spatialHashing.getGridSize();
        int cellSize = spatialHashing.getCellSize();
        int cellsBySide = spatialHashing.getCellsBySide();

        g.setColor(Color.LIGHT_GRAY);

        for (int i = 1; i < cellsBySide; i++) {
            g.drawLine(i * cellSize, 0, i * cellSize, gridSize);
            g.drawLine(0, i * cellSize, gridSize, i * cellSize);
        }
    }

    private void paintCellNumbers(Graphics g) {
        int cellSize = spatialHashing.getCellSize();
        int cellsBySide = spatialHashing.getCellsBySide();

        g.setColor(Color.LIGHT_GRAY);

        for (int i = 0; i < cellsBySide; i++) {
            for (int j=0; j < cellsBySide; j++) {
                g.drawString(Integer.toString((j*cellsBySide) + i), (i * cellSize) + 5, (j * cellSize) + 15);
            }
        }
    }

    private void paintEntities(Graphics g) {
        for (EntityComponent entityComponent : this.entityComponents) {
            entityComponent.paintComponent(g);
        }
    }

    Timer timer;
    SimulateMovementThread simulateMovement;
    PrintStatsThread printStatsThread;
    CheckCollisionsThread checkCollisionsThread;
    UpdateEntitiesCellThread updateEntitiesCellThread;

    private void start() {
        // Rewrite component at 60fps
        int framesPerSecond = 60;

        timer = new Timer(1000 / framesPerSecond, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        });

        timer.start();

        // Start threads
        simulateMovement = new SimulateMovementThread(entityComponents, spatialHashing);
        simulateMovement.start();

        printStatsThread = new PrintStatsThread();
        printStatsThread.start();

        checkCollisionsThread = new CheckCollisionsThread(spatialHashing, entityComponents);
        checkCollisionsThread.start();

        updateEntitiesCellThread = new UpdateEntitiesCellThread(spatialHashing, entityComponents);
        updateEntitiesCellThread.start();
    }

    public void terminate() {
        simulateMovement.interrupt();
        printStatsThread.interrupt();
        checkCollisionsThread.interrupt();
        updateEntitiesCellThread.interrupt();
        timer.stop();
    }
}
