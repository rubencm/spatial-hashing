package net.rubencm.spatialhashing.swing.components;

import lombok.Getter;
import lombok.Setter;
import net.rubencm.spatialhashing.spatialhashing.Entity;
import net.rubencm.spatialhashing.spatialhashing.SpatialHashing;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class EntityComponent extends JComponent {

    @Getter
    private Entity entity;
    private SpatialHashing spatialHashing;

    // Speed in pixels/second
    private int speedX;
    private int speedY;

    private boolean dirX;
    private boolean dirY;

    @Getter @Setter
    private Color color;

    public EntityComponent(Entity entity) {
        Random rand = new Random();

        this.entity = entity;
        spatialHashing = entity.getSpatialHashing();

        // Random speed
        speedX = rand.nextInt(200);
        speedY = rand.nextInt(200);

        // Random directions
        dirX = rand.nextBoolean();
        dirY = rand.nextBoolean();

        color = Color.BLUE;
    }

    @Override
    public void paintComponent(Graphics g) {
        int posX = entity.getX() - entity.getRadius();
        int posY = entity.getY() - entity.getRadius();

        g.setColor(this.color);
        g.fillOval(posX, posY, 2 * entity.getRadius(), 2 * entity.getRadius());
    }

    public void updatePosition() {
        entity.updatePosition(calculateNextPositionX(), calculateNextPositionY());
    }

    private long lastUpdateX = System.nanoTime();

    private int calculateNextPositionX() {
        long now = System.nanoTime();

        float secondsSinceLastUpdateX = (now-lastUpdateX)/1_000_000_000f;
        int movX = (int)(speedX * secondsSinceLastUpdateX);

        if (movX != 0) {
            if (entity.getX() + mov(movX, dirX) >= spatialHashing.getGridSize() || entity.getX() + mov(movX, dirX) < 0) {
                dirX = !dirX;
            }

            lastUpdateX = now;
        }

        return entity.getX()+mov(movX, dirX);
    }

    private long lastUpdateY = System.nanoTime();

    private int calculateNextPositionY() {
        long now = System.nanoTime();

        float secondsSinceLastUpdateY = (now-lastUpdateY)/1_000_000_000f;
        int movY = (int)(speedY * secondsSinceLastUpdateY);

        if(movY != 0) {
            if (entity.getY() + mov(movY, dirY) >= spatialHashing.getGridSize() || entity.getY() + mov(movY, dirY) < 0) {
                dirY = !dirY;
            }

            lastUpdateY = now;
        }

        return entity.getY()+mov(movY, dirY);
    }

    // Movement with symbol
    private int mov(int mov, boolean dir) {
        return mov * (dir ? 1 : -1);
    }
}
