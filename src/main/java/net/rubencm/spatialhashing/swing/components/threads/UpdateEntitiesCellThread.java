package net.rubencm.spatialhashing.swing.components.threads;

import net.rubencm.spatialhashing.spatialhashing.SpatialHashing;
import net.rubencm.spatialhashing.swing.components.EntityComponent;

import java.util.List;

public class UpdateEntitiesCellThread extends Thread {

    /**
     * Each how much time update entities position
     */
    final int updateTime = 0;

    SpatialHashing spatialHashing;
    List<EntityComponent> entityComponents;

    public UpdateEntitiesCellThread(List<EntityComponent> entityComponents) {
        this.entityComponents = entityComponents;
    }

    @Override
    public void run() {
        long now;
        long last = 0;

        while(!Thread.currentThread().isInterrupted()) {
            now = System.currentTimeMillis();

            if(now - last > updateTime) {
                for(EntityComponent entityComponent: entityComponents) {
                    spatialHashing.updateEntity(entityComponent.getEntity());
                }

                last = now;
            }

            // Sleep while there is nothing to do
            long sleepTime = updateTime - (System.currentTimeMillis() - last);
            if (sleepTime > 0) {
                try {
                    Thread.sleep(updateTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
