import javax.swing.*;
import java.awt.*;

/**
 * Informace o hráči. Obsahuje indikátor stavu hry pro daného hráče.
 */
class Player {
    private int[] played;       //správně zozpovězené otázky
    private int correct;

    int getCorrect() {
        return correct;
    }
    Indicator getIndicator() {
        return indicator;
    }

    private Indicator indicator;

    Player(int num,Color color){
        played=new int[num];
        for (int i = 0; i < num; i++) {
            played[i]=-1;
        }
        indicator=new Indicator(color);
    }

    void add(int x){
        if (correct ==0 || played[correct -1] != x){
            played[correct]=x;
            correct++;
        }

    }

    int[] played(){
        return played;
    }

    /**
     * Indikátor stavu hry.
     */
    private static class Indicator extends JPanel {
        private int width = 3000;
        private int height = 10;
        private Color color;

        Indicator(Color color) {
            this.color=color;
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.drawRect(0,0, width, height);
            g.setColor(color);
            g.fillRect(0,0,width,height);
        }

        public Dimension getPreferredSize() {
            return new Dimension(width, height); // appropriate constants
        }
    }



}


