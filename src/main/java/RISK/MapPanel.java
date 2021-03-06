package RISK;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 * MapPanel class for add map on GUI
 */
public class MapPanel extends JPanel implements Serializable{

    private BufferedImage image;

    /**
     * Initial method
     */
    public MapPanel(Board board) {
        try {

            image = board.getImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * draw the graph as the background of this panel
     * @param g add image to the component
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this); // see javadoc for more info on the parameters
    }





}