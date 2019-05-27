import javax.swing.*;
import java.awt.*;

/**
 * Hexagon button in triangle.
 */
public class HexButton extends JButton {
    enum State {WRONG, FIRST, SECOND,CHOOSEN,NOT_CHOOSEN};
    private Polygon p = new Polygon();
    private Color color;
    private int number;
    State state;

    int getNumber() {
        return number;
    }
    void setNumber(int number) {
        this.number = number;
    }
    private Color[] colors = new Color[]{Color.lightGray,Color.blue,Color.red,Color.CYAN,Color.white};

    HexButton(String label) {
        super(label);

        this.setFont(new Font("Arial",Font.PLAIN, 30));
        Dimension size = getPreferredSize();
        size.width = size.height = Math.max(size.width, size.height);
        setPreferredSize(size);

        setContentAreaFilled(false);
        //points of a hexagon
        for (int i = 0; i < 6; i++){
            p.addPoint((int) (50 + 50 * Math.sin(i * 2 * Math.PI / 6)),
                    (int) (50 + 50 * Math.cos(i * 2 * Math.PI / 6)));
        }
        state=State.NOT_CHOOSEN;
        color=colors[4];
    }

    /**
     * Function for draw the button.
     * @param g graphic for drawing
     */
    protected void paintComponent(Graphics g) {
        g.setColor(color);
        g.fillPolygon(p);

        super.paintComponent(g);
    }

    protected void paintBorder(Graphics g) {
        g.setColor(getForeground());
        g.drawPolygon(p);
    }

    public boolean contains(int x, int y) {
        return p.contains(x, y);
    }

    /**
     * Changes color of the button and state.
     * @param c 0 - wrong, 1 - correct first player, 2 - correct second player, 3 - current choose
     */
    void SetColor(int c){
        color=colors[c];
        switch (c){
            case 0:{
                state=State.WRONG;
                break;
            }
            case 1:{
                state=State.FIRST;
                break;
            }
            case 2:{
                state=State.SECOND;
                break;
            }
            case 3:{
                state=State.CHOOSEN;
                break;
            }

        }
    }
}

