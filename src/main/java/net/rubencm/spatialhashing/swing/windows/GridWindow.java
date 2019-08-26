package net.rubencm.spatialhashing.swing.windows;

import net.rubencm.spatialhashing.swing.components.GridComponent;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GridWindow extends JFrame {

    public GridWindow(Integer gridSize, Integer cellSize, Integer numObjects, Integer objectRadius) {
        super("Test");

        GridComponent gridComponent = new GridComponent(gridSize, cellSize, numObjects, objectRadius);
        this.add(gridComponent);

        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                gridComponent.terminate();

                super.windowClosing(e);
            }
        });
    }
}
