package org.example;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
            for (int y = 0; y < field.getGridLength(); y++) {
                for (int x = 0; x < field.getGridLength(); x++) {
                    Field.GridCell newState = field.getCellStateAt(new Cords(x, y));
                    grid[y][x].setState(newState);
                }
            }
        }
    }

    public static class FieldCellPanel extends JPanel {
        private final JLabel label;
        private Field.GridCell state;

        public FieldCellPanel() {
            setBackground(Color.GREEN);
            setPreferredSize(new Dimension(50, 50));
            label = new JLabel();
            label.setFont(new Font(Font.DIALOG, Font.BOLD, 40));
            add(label);
        }

        public void setState(Field.GridCell newState) {
            if (state != newState) {
                state = newState;
                updateView();
            }
        }

        private void updateView() {
            switch (state) {
                case EMPTY:
                    label.setText(" . ");
                    break;
                case CHERRY:
                    label.setText(" * ");
                    break;
                case SNAKE:
                    label.setText(" @ ");
                    break;
            }
        }
    }
}
