package net.rubencm.spatialhashing.swing.windows;

import net.rubencm.spatialhashing.swing.components.GridComponent;

import javax.swing.*;

public class GridWindow extends JFrame {

    public GridWindow(Integer gridSize, Integer cellSize, Integer numObjects, Integer objectRadius) {
        super("Test");

        GridComponent gridComponent = new GridComponent(gridSize, cellSize, numObjects, objectRadius);
        this.add(gridComponent);

        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
