package net.rubencm.spatialhashing.swing.components.threads;

import net.rubencm.spatialhashing.spatialhashing.Entity;
import net.rubencm.spatialhashing.spatialhashing.Position;
import net.rubencm.spatialhashing.spatialhashing.SpatialHashing;
import net.rubencm.spatialhashing.swing.components.EntityComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Simulate movement like in a "real" game
public class SimulateMovementThread extends Thread {

    /**
     * Move entities each 500 miliseconds
     */
    private final int moveTime = 500;

    Random rand;
    List<EntityComponent> entityComponents;
    SpatialHashing spatialHashing;

    public SimulateMovementThread(List<EntityComponent> entityComponents, SpatialHashing spatialHashing) {
        this.rand = new Random();
        this.entityComponents = entityComponents;
        this.spatialHashing = spatialHashing;
    }

    @Override
    public void run() {
        long now;
        long last = 0;

        while(!Thread.currentThread().isInterrupted()) {
            now = System.currentTimeMillis();

            if(now - last > moveTime) {

                // Select a random 10% of the list
                List<EntityComponent> randomElements = selectRandom(entityComponents, entityComponents.size()/10);

                for (EntityComponent element : randomElements) {
                    element.getEntity().setPosition(
                            new Position(
                                    rand.nextInt(spatialHashing.getGridSize()),
                                    rand.nextInt(spatialHashing.getGridSize())
                            )
                    );
                }

                last = now;
            }

            // Sleep while there is nothing to do
            long sleepTime = moveTime - (System.currentTimeMillis() - last);
            if (sleepTime > 0) {
                try {
                    Thread.sleep(moveTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    /**
     * Select random elements from a list
     *
     * @param input
     * @param quantity
     * @return
     */
    private List<EntityComponent> selectRandom(List<EntityComponent> input, int quantity) {
        List<EntityComponent> output = new ArrayList<>();

        for(int i=0; i< quantity; i++) {
            output.add(input.get(rand.nextInt(input.size())));
        }

        return output;
    }
}