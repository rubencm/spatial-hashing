package net.rubencm.spatialhashing.swing.windows;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class FormWindow extends JFrame {
    private JPanel pnl;
    private JButton btnSimulation;

    private JLabel lblGridSize;
    private JLabel lblCellSize;
    private JLabel lblNumObjects;
    private JLabel lblObjectRadius;

    private JTextField txtGridSize;
    private JTextField txtCellSize;
    private JTextField txtNumObjects;
    private JTextField txtObjectRadius;

    public FormWindow() {
        super("Spatial-Hashing");

        Map<String, String> data = null;
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            data = mapper.readValue(new File(getClass().getClassLoader().getResource("FormWindow.yaml").getFile()), Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.lblGridSize.setText(data.get("lblGridSize"));
        this.lblCellSize.setText(data.get("lblCellSize"));
        this.lblNumObjects.setText(data.get("lblNumObjects"));
        this.lblObjectRadius.setText(data.get("lblObjectRadius"));

        this.txtGridSize.setText(data.get("txtGridSize"));
        this.txtCellSize.setText(data.get("txtCellSize"));
        this.txtNumObjects.setText(data.get("txtNumObjects"));
        this.txtObjectRadius.setText(data.get("txtObjectRadius"));

        this.btnSimulation.setText(data.get("btnSimulation"));

        // Properties
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        this.setContentPane(this.pnl);

        // Events
        this.btnSimulation.addActionListener(new BtnActionListener());
    }

    private class BtnActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {

                    Integer gridSize = Integer.parseInt(FormWindow.this.txtGridSize.getText());
                    Integer cellSize = Integer.parseInt(FormWindow.this.txtCellSize.getText());
                    Integer numObjects = Integer.parseInt(FormWindow.this.txtNumObjects.getText());
                    Integer objectRadius = Integer.parseInt(FormWindow.this.txtObjectRadius.getText());

                    if(!validate(gridSize, cellSize, numObjects, objectRadius)) {
                        return;
                    }

                    JFrame frame = new GridWindow(gridSize, cellSize, numObjects, objectRadius);
                    frame.pack();
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                }
            });
        }

        public Boolean validate(Integer gridSize, Integer cellSize, Integer numObjects, Integer objectRadius) {
            if (gridSize % cellSize != 0) {
                JOptionPane.showMessageDialog(null, "Grid size should be divisible by cell size.");
                return false;
            }

            if(cellSize < objectRadius * 2) {
                JOptionPane.showMessageDialog(null, "Cell size should be bigger than the diameter..");
                return false;
            }

            return true;
        }
    }
}
