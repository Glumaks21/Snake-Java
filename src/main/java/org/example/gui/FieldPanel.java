package org.example.gui;

import org.example.logic.Cords;
import org.example.logic.Field;
import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class FieldPanel extends JPanel implements PropertyChangeListener {
    private final FieldCellPanel[][] grid;
    private final Field field;

    public FieldPanel(Field field) {
        this.field = field;
        field.addPropertyChangeListener(this);

        grid = new FieldCellPanel[field.getGridLength()][field.getGridLength()];
        setLayout(new GridLayout(field.getGridLength(), field.getGridLength(), 1, 1));
        for (int y = 0; y < field.getGridLength(); y++) {
            for (int x = 0; x < field.getGridLength(); x++) {
                FieldCellPanel cell = new FieldCellPanel();
                cell.setState(field.getCellStateAt(new Cords(x, y)));
                grid[y][x] = cell;
                add(cell);
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("grid")) {
            new Thread(() -> {
                for (int y = 0; y < field.getGridLength(); y++) {
                    for (int x = 0; x < field.getGridLength(); x++) {
                        Field.CellState newState = field.getCellStateAt(new Cords(x, y));
                        grid[y][x].setState(newState);
                    }
                }
            }).start();
        }
    }

    private class FieldCellPanel extends JPanel {
        private Field.CellState state;

        public FieldCellPanel() {
            Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
            int min = (int) (Math.min(dimension.width, dimension.height) * 0.6);
            dimension = new Dimension(min / grid.length, min / grid.length);
            setPreferredSize(dimension);
        }

        public void setState(Field.CellState newState) {
            if (state != newState) {
                state = newState;
                updateView();
            }
        }

        private void updateView() {
            switch (state) {
                case EMPTY:
                    setBackground(Color.BLACK);
                    break;
                case CHERRY:
                    setBackground(Color.RED);
                    break;
                case SNAKE:
                    setBackground(Color.GREEN);
                    break;
            }
        }
    }}
