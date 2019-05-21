import javax.swing.*;
import java.awt.*;

class TrianglePanel extends JPanel {
    TrianglePanel() {
        setLayout(null);                //layout je null, aby se políčka trojúhelníku mohly umístit absolutně
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    /**
     * Vytvoří HexButton jako políčka trojúhelníku a přidá je na panel
     * @param triangleSize počet řádek trojúhelníku
     * @param startx počáteční pozice x
     * @param starty počáteční pozice y
     * @param size velikost tlačítka
     * @param labels pole popisků
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
