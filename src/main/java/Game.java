import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Hlavní třída pro logiku hry. Obsahuje instanci trojúhelníku, obou hráčů a otázek.
 */
class Game{

    public enum States {FIRST_CHOOSES, FIRST_ANSWERS, FIRST_REANSWERS, FIRST_WRONG, SECOND_CHOOSES, SECOND_ANSWERS, SECOND_REANSWERS, SECOND_WRONG, END}
    private States state;       //aktuální stav hry
    private int size;       //délka spodní strany
    private int left;
    private Triangle triangle;
    private Player secondPlayer;
    private Player firstPlayer;
    private Questions questions;

    int getSize() { return size; }
    int getTriangleStartX(){ return triangle.startx; }
    int getTriangleStartY(){ return triangle.starty; }
    int getTriangleSize(){ return triangle.size; }
    String[] getTriangleLabels(){ return triangle.labels; }
    JPanel getFirstPlayerIndicator() { return firstPlayer.getIndicator(); }
    Player getFirstPlayer() { return firstPlayer; }
    Player getSecondPlayer() { return secondPlayer; }
    JPanel getSecondPlayerIndicator() { return secondPlayer.getIndicator(); }
    void setTriangleLastClicked(HexButton b){ triangle.lastclicked=b; }
    HexButton getTriangleLastClicked(){ return triangle.lastclicked; }
    void setState(States state) { this.state = state; }
    States getState() { return state; }
    void NextHex(){left-- ;}
    boolean CheckQuestion(AnswerButton b,int n){return questions.Check(b,n);}

    /**
     * Pokud je typ otázky ABC, je možné na ni odpovídat znovu.
     * @param n číslo otázky
     * @return true = otázka je ABC, false = otázka je OPEN
     */
    boolean canReanswer(int n){
        return questions.getQuestion(n).getType().equals(Questions.Type.ABC);
    }

    /**
     * Funkce, která zjistí, jestli už je trojúhelník zcela zaplněn a pokud ano, kdo má víc správných odpovědí.
     * @return -1 = ještě zbývají volná políčka, 0 = remíza, 1 = první hráč má víc správných, 2 = druhý hráč má víc správných
     */
    int isFull(){
        if (left>0)
            return -1;
        else if (firstPlayer.getCorrect() > secondPlayer.getCorrect())
            return 1;
        else if (firstPlayer.getCorrect() < secondPlayer.getCorrect())
            return 2;
        else
            return 0;

    }
    /**Zavolá funkci, která zjistí, jestli daný hráč propojil trojúhelník
     * @param player číslo hráče
     * @return true = propojil, false = nepropojil
     */
    boolean isTriangleConnect(int player) {
        switch (player) {
            case 1:
                return triangle.IsConnected(firstPlayer.played());
            case 2:
                return triangle.IsConnected(secondPlayer.played());
        }
        return false;
    }

    /**
     * Konstruktor Game. Spočítá velikost trojúhelníku, vytvoří instanci trojúhelníku a hráčů. Nastaví stav na FIRST_CHOOSES (začátek hry).
     * @param q třída s otázkami
     */
    Game(Questions q){
        questions=q;
        int n = questions.NumberOfQuestions();      //počet políček
        this.size =(int)Math.floor(Math.sqrt(2* n));        //počet řádků/počet políček v posledním řádku
        this.left=n;
        triangle= new Triangle(n,size);
        firstPlayer=new Player(n, Color.blue);
        secondPlayer=new Player(n,Color.red);
        state= States.FIRST_CHOOSES;

    }

    /**
     * Vnitřní data trojúhelníku.
     */
    private class Triangle {
        int n;
        int startx;
        int starty;

        int size = 100;
        HexButton lastclicked;

        List<String> labelList = new ArrayList<>();
        String[] labels;        //popisky políček

        //krajní políčka
        int[] left;
        int[] right;
        int[] bottom;

        //graf - matice sousednosti políček
        int[][] graph;

        /** Konstruktor Triangle. Vytvoří popisky, spočte indexy krajních políček, vytvoří graf.
         * @param n počet políček
         * @param size velikost trojúhelníku
         */
        Triangle(int n, int size) {
            this.startx = 380;
            this.starty = (9-size)*50;
            this.n = n;

            for (int i = 0; i < n; i++) {
                labelList.add(String.valueOf(i + 1));
            }
            labels = labelList.toArray(String[]::new);
            left = new int[size];
            right = new int[size];
            bottom = new int[size];

            int l = 0;
            int r = 0;
            int b = n - size;
            for (int i = 0; i < size; i++) {
                left[i] = l;
                right[i] = r;
                bottom[i] = b;
                l += i + 1;
                r += i + 2;
                b += 1;
            }
            graph = MakeGraph(n, size);
        }

        /**
         * Vytvoří graf sousedních políček v trojúhelníku reprezentovaný maticí sousednosti.
         *
         * @param n počet políček
         * @return matice sousednosti
         */
        private int[][] MakeGraph(int n, int size) {
            int[][] g = new int[n][n];
            int k = 0;
            for (int i = 0; i < size; i++) {
                for (int j = 0; j <= i; j++) {
                    if (k < n - size) {
                        g[k][k + i + 1] = 1;        //políčko vlevo dole
                        g[k + i + 1][k] = 1;
                        g[k][k + i + 2] = 1;        //políčko vpravo dole
                        g[k + i + 2][k] = 1;
                    }
                    if (j != i) {
                        g[k][k + 1] = 1;        //políčko vpravo
                    }
                    if (j != 0) {
                        g[k][k - 1] = 1;        //políčko vlevo
                    }
                    k++;
                }
            }
            return g;
        }


        /**
         * Zjišťuje, zda daná políčka propojují všechny tři strany trojúhelníku.
         * Prohledává graf sousedních vrachoů pomocí BFS
         *
         * @param fields indexy daných políček
         * @return true, pokud jsou strany propojeny; false jinak
         */
        boolean IsConnected(int[] fields) {
            int[] b = new int[size];        //pole políček na dolní straně
            for (int i = 0; i < size; i++) {
                b[i] = -1;
            }
            int[] ok = new int[2];
            int index = 0;
            for (int i : fields       //přidáme dolní políčka
            ) {
                if (Contains(i, bottom)) {
                    b[index] = i;
                }
            }
            if (b[0] == -1)       //žádné dolní políčko neexistuje, trojúhelník není propojený
                return false;

            Queue<Integer> queue = new ArrayDeque<>();
            for (int item : b
            ) {
                if (item != -1)
                    queue.add(item);        //přidáme do fronty všechny dolní políčka
            }
            while (queue.size() > 0) {
                int x = queue.remove();     //odebereme políčko z fronty
                fields = Arrays.stream(fields).filter(a -> a != x).toArray();        //a z pole všech políček

                if (Contains(x, left))       //políčko je na levé straně
                    ok[0] = 1;
                if (Contains(x, right))      //políčko je na pravé straně
                    ok[1] = 1;

                for (int i = 0; i < graph[x].length; i++) {
                    if (graph[x][i] == 1 && Contains(i, fields)) {      //přidáme do fronty všechny sousední políčka
                        queue.add(i);
                    }
                }
            }
            return ok[0] == 1 && ok[1] == 1;        //existuje cesta z dolní strany do levé i pravé
        }

        boolean Contains(int a, int[] field) {
            for (int i : field
            ) {
                if (a == i)
                    return true;
            }
            return false;
        }
    }





}
