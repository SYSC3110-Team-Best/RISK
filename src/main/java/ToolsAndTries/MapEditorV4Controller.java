package ToolsAndTries;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MapEditorV4Controller implements MouseListener {
    private MapEditorV4Model model;

    public MapEditorV4Controller(MapEditorV4Model model)
    {
       this.model = model;
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        JComponent component = (JComponent) e.getSource();
        Point p = component.getMousePosition();
        model.addTerritory(p.x,p.y);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
