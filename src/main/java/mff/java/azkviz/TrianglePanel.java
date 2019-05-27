import javax.swing.*;
import java.awt.*;

/**
 * Panel with the triangle.
 */
class TrianglePanel extends JPanel {
    TrianglePanel() {
        setLayout(null);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    /**
     * Make HexButtons in the triangle and add them on the panel.
     * @param triangleSize number of row in the triangle
     * @param startx start position x
     * @param starty start position y
     * @param size size of the button
     * @param labels array of the button labels
     */
    void DrawTriangle(int triangleSize, int startx, int starty, int size, String[] labels,GamePanel gamePanel) {
        int index=0;
        for (int i = 0; i < triangleSize; i++) {
            int start = (int) Math.round(startx - (i * size * Math.sqrt(3) / 2) / 2);
            for (int j = 0; j < i + 1; j++) {
                HexButton button = new HexButton(labels[index]);
                button.setNumber( Integer.parseInt(labels[index]) - 1);
                index++;
                button.setBounds((int) Math.round(start + (j * size * Math.sqrt(3) / 2)), i * (size * 3 / 4) + starty, 100, 100);
                button.addActionListener(gamePanel);
                this.add(button);
            }
        }
    }
}
