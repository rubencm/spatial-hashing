package net.rubencm.spatialhashing.windows;

import net.rubencm.spatialhashing.components.GridComponent;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GridWindow extends JFrame {

    public GridWindow(Integer gridSize, Integer cellSize, Integer numObjects, Integer objectRadius) {
        super("Test");

        GridComponent gridComponent = new GridComponent(gridSize, cellSize, numObjects, objectRadius);
        this.add(gridComponent);
        Thread thread = new Thread(gridComponent);
        thread.start();

        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                thread.stop();
            }
        });
    }
}
