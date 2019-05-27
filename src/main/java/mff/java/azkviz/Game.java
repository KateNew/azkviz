import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Main class for game logic. Contains triangle, both players and questions.
 */
class Game{

    public enum States {FIRST_CHOOSES, FIRST_ANSWERS, FIRST_REANSWERS, FIRST_WRONG, SECOND_CHOOSES, SECOND_ANSWERS, SECOND_REANSWERS, SECOND_WRONG, END}
    private States state;       //current game state
    private int size;       //number of lines in triangle
    private int left;       //number of free buttons
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
    boolean checkQuestion(AnswerButton b, int n){return questions.Check(b,n);}

    /**
     * If the type of question is ABC, second player can answer same question.
     * @param n number of question
     * @return true = question is ABC, false = question is OPEN
     */
    boolean canReanswer(int n){
        return questions.getQuestion(n).getType().equals(Questions.Type.ABC);
    }

    /**
     * Function which finds out if there are some free buttons. If not, finds out, who has more correct answers.
     * @return -1 = there are some free buttons, 0 = draw, 1 = first player has more correct answers, 2 = second player has more correct answers
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
    /**
     * Calls function IsConnected() for player.
     * @param player 1 = first player, 2 = second player
     * @return true = is connect, false = not connect
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
     * Game constructor. Counts size of triangle, makes instance of triangle and players. Sets state for FIRST_CHOOSES (beginning of a game).
     * @param q questions class
     */
    Game(Questions q){
        questions=q;
        int n = questions.NumberOfQuestions();      //number of buttons
        this.size =(int)Math.floor(Math.sqrt(2* n));        //number of lines in triangle
        this.left=n;
        triangle= new Triangle(n,size);
        firstPlayer=new Player(n, Color.blue);
        secondPlayer=new Player(n,Color.red);
        state= States.FIRST_CHOOSES;

    }

    /**
     * Inner data of triangle.
     */
    private class Triangle {
        int n;
        int startx;
        int starty;

        int size = 100;
        HexButton lastclicked;

        List<String> labelList = new ArrayList<>();
        String[] labels;        //triangle labels

        //buttons on borders
        int[] left;
        int[] right;
        int[] bottom;

        //graph - incidence matrix
        int[][] graph;

        /**
         * Triangle constructor. Makes labels, counts index of buttons on borders, makes graph.
         * @param n number of buttons
         * @param size number of line in triangle
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
         * Makes graph of neighbour buttons represents by incidence matrix.
         * @param n number of button
         * @return incidence matrix
         */
        private int[][] MakeGraph(int n, int size) {
            int[][] g = new int[n][n];
            int k = 0;
            for (int i = 0; i < size; i++) {
                for (int j = 0; j <= i; j++) {
                    if (k < n - size) {
                        g[k][k + i + 1] = 1;        //button left down
                        g[k + i + 1][k] = 1;
                        g[k][k + i + 2] = 1;        //button right down
                        g[k + i + 2][k] = 1;
                    }
                    if (j != i) {
                        g[k][k + 1] = 1;        //button right
                    }
                    if (j != 0) {
                        g[k][k - 1] = 1;        //button left
                    }
                    k++;
                }
            }
            return g;
        }


        /**
         * Finds out if given buttons connect all sides of triangle.
         * Searches graph using BFS.
         * @param fields index of buttons
         * @return true if sides are connect
         */
        boolean IsConnected(int[] fields) {
            int[] b = new int[size];        //array of buttons on bottom
            for (int i = 0; i < size; i++) {
                b[i] = -1;
            }
            int[] ok = new int[2];
            int index = 0;
            for (int i : fields       //add bottom button
            ) {
                if (Contains(i, bottom)) {
                    b[index] = i;
                }
            }
            if (b[0] == -1)       //no button on bottom, triangle is not connect
                return false;

            Queue<Integer> queue = new ArrayDeque<>();
            for (int item : b
            ) {
                if (item != -1)
                    queue.add(item);        //add all bottom button in queue
            }
            while (queue.size() > 0) {
                int x = queue.remove();     //remove button from queue
                fields = Arrays.stream(fields).filter(a -> a != x).toArray();        //and remove it from array of all buttons

                if (Contains(x, left))       //button on left side
                    ok[0] = 1;
                if (Contains(x, right))      //button on right side
                    ok[1] = 1;

                for (int i = 0; i < graph[x].length; i++) {
                    if (graph[x][i] == 1 && Contains(i, fields)) {      //add all neighbours button in queue
                        queue.add(i);
                    }
                }
            }
            return ok[0] == 1 && ok[1] == 1;        //there is path from bottom to left and right side
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
