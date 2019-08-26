package net.rubencm.spatialhashing.swing.components;

import lombok.Getter;
import lombok.Setter;
import net.rubencm.spatialhashing.spatialhashing.Entity;

import javax.swing.*;
import java.awt.*;

public class EntityComponent extends JComponent {

    @Getter
    private Entity entity;

    @Getter @Setter
    private Color color;

    public EntityComponent(Entity entity) {
        this.entity = entity;
        color = Color.BLUE;
    }

    @Override
    public void paintComponent(Graphics g) {
        int posX = entity.getPosition().getX() - entity.getRadius();
        int posY = entity.getPosition().getY() - entity.getRadius();

        g.setColor(this.color);
        g.fillOval(posX, posY, 2 * entity.getRadius(), 2 * entity.getRadius());
    }
}
