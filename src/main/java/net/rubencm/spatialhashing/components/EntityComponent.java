package net.rubencm.spatialhashing.components;

import lombok.Getter;
import lombok.Setter;
import net.rubencm.spatialhashing.spatialhashing.Entity;
import net.rubencm.spatialhashing.spatialhashing.SpatialHashing;

import javax.swing.*;
import java.awt.*;

public class EntityComponent extends JComponent {

    @Getter Entity entity;

    @Getter @Setter private Integer dirX;
    @Getter @Setter private Integer dirY;

    @Getter @Setter Color color;

    public EntityComponent(Entity entity) {
        this.entity = entity;

        this.dirX = 0;
        this.dirY = 0;

        this.color = Color.BLUE;
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(this.color);

        Integer x = entity.getX() - entity.getRadius();
        Integer y = entity.getY() - entity.getRadius();

        g.fillOval(x, y, 2 * entity.getRadius(), 2 * entity.getRadius());
    }

    public void updatePosition() {
        SpatialHashing spatialHashing = entity.getSpatialHashing();

        if(entity.getX() + this.getDirX() >= spatialHashing.getGridSize() || entity.getX() + this.getDirX() < 0) {
            this.setDirX(this.getDirX() * -1);
        }

        if(entity.getY() + this.getDirY() >= spatialHashing.getGridSize() || entity.getY() + this.getDirY() < 0) {
            this.setDirY(this.getDirY() * -1);
        }

        int oldCell  = spatialHashing.getCell(entity.getX(), entity.getY());

        entity.setX(entity.getX()+this.getDirX());
        entity.setY(entity.getY()+this.getDirY());

        int newCell  = spatialHashing.getCell(entity.getX(), entity.getY());

    }
}
