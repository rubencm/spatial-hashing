package net.rubencm.spatialhashing.swing.components.threads;

import net.rubencm.spatialhashing.spatialhashing.Entity;
import net.rubencm.spatialhashing.spatialhashing.SpatialHashing;
import net.rubencm.spatialhashing.swing.components.EntityComponent;

import java.awt.*;
import java.util.List;

public class CheckCollisionsThread extends Thread {

    SpatialHashing spatialHashing;
    List<EntityComponent> entityComponents;

    public CheckCollisionsThread(SpatialHashing spatialHashing, List<EntityComponent> entityComponents) {
        this.spatialHashing = spatialHashing;
        this.entityComponents = entityComponents;
    }
//    List<Long> execTime = new ArrayList<Long>();
//    List<Integer> comparisions = new ArrayList<Integer>();
    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()) {
//            int comps = 0;
//            long start = System.currentTimeMillis();
            for (EntityComponent entityComponent : entityComponents) {
                //spatialHashing.updateEntity(entityComponent.getEntity());

                // Check collisions
                boolean collision = false;
                for (Entity entity : spatialHashing.getNearEntities(entityComponent.getEntity())) {

                    if (isCollision(entityComponent.getEntity(), entity)) {
                        entityComponent.setColor(Color.RED);
                        collision = true;
                        break;
                    }

//                    comps++;
                }
                if(!collision) entityComponent.setColor(Color.BLUE);
            }

//            execTime.add(System.currentTimeMillis() - start);
//            comparisions.add(comps);
        }
    }

    // Currently: http://stackoverflow.com/questions/1736734/circle-circle-collision
    // Will use (character vision range): https://stackoverflow.com/questions/481144/equation-for-testing-if-a-point-is-inside-a-circle
    private boolean isCollision(Entity entity1, Entity entity2) {
        return entity1 != entity2 &&
                Math.pow(entity1.getPosition().getX() - entity2.getPosition().getX(), 2) +
                        Math.pow(entity2.getPosition().getY() - entity1.getPosition().getY(), 2) <=
                        Math.pow(entity2.getRadius() + entity1.getRadius(), 2);
    }
}
