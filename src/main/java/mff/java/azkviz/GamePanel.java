package mff.java.azkviz;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ResourceBundle;

/**
 * Main panel contains TrianglePanel and QuestionPanel.
 */
public class GamePanel extends JPanel implements ActionListener{
    private ResourceBundle rs;
    private Game game;
    private QuestionPanel pQuestion;

    /**
     * Constructor of the GamePanel.
     * Makes instance of Questions and Game and add TrianglePanel and QuestionPanel.
     * Draws the triangle and set state of the game on beginning.
     * @param file file with questions
     * @param rs resourseBundle with strings
     * @throws QuestFormatException format of the file is not correct
     * @throws FileNotFoundException file is not found
     */
    GamePanel(File file, ResourceBundle rs) throws QuestFormatException, FileNotFoundException {
        this.rs=rs;
        Questions questions = new Questions(file,rs);
        game = new Game(questions);
        TrianglePanel pTriangle = new TrianglePanel();
        pQuestion = new QuestionPanel(questions,rs);

        Container gameContainer = new Container();
        gameContainer.setLayout(new GridLayout(1,2));
        gameContainer.add(pTriangle);
        gameContainer.add(pQuestion);
        this.setLayout(new BorderLayout());
        this.add(gameContainer,BorderLayout.CENTER);
        pTriangle.DrawTriangle(game.getSize(),game.getTriangleStartX(),game.getTriangleStartY(),game.getTriangleSize(), game.getTriangleLabels(),this);
        changeIndicator();
    }



    /**
     * If the button have not been chosen yet and the state is *_CHOOSES, call function click().
     * @param e action event of the button
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        HexButton b = (HexButton)e.getSource();
        if (b.state.equals(HexButton.State.NOT_CHOOSEN) && (game.getState()== Game.States.FIRST_CHOOSES || game.getState()== Game.States.SECOND_CHOOSES))
            click(b);
    }

    /**
     * Function after click on a button in triangle. It calls the function which shows question and changes the state to *_ANSWERS.
     * @param button button in triangle
     */
    private void click(HexButton button){
        game.NextHex();
        button.SetColor(3);     //označeno
        int n=button.getNumber();
        game.setTriangleLastClicked(button);
        pQuestion.removeAll();
        if(game.getState()== Game.States.FIRST_CHOOSES)
            game.setState(Game.States.FIRST_ANSWERS);
        else if (game.getState()== Game.States.SECOND_CHOOSES)
            game.setState(Game.States.SECOND_ANSWERS);
        pQuestion.ask(n,new AnswerListener());
    }

    /**
     * ActionListener for processing answers.
     */
    public class AnswerListener implements ActionListener{
        /**
         *  If AnswerButton calls listener and the state is *ANSWERS, it calls function answer() which process the answer.
         *  If JButton calls listener, button says if second player wants answer again. It call function wrongAnswer() or reanswer().
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            Object button = e.getSource();
            if(button instanceof AnswerButton && (
                    game.getState()== Game.States.FIRST_ANSWERS || game.getState()== Game.States.FIRST_REANSWERS ||
                            game.getState()== Game.States.SECOND_ANSWERS || game.getState()== Game.States.SECOND_REANSWERS))
                answer((AnswerButton) e.getSource());
            else if(button instanceof JButton){
                String text = ((JButton) button).getText();
                if (text.equals(rs.getString("no")))
                    wrongAnswer();
                else if (text.equals(rs.getString("yes"))) {
                    reanswer();
                }
            }
        }

    }

    /**
     * Function after click on answer button.
     * If the answer is correct (function checkQuestion), calls function correctAnswer().
     * Else if second player can answer, it calls function secondPlayerAnswer(), else wrongAnswer().
     * Last check if there are some free buttons. If not, calls function end().
     * @param b button with answer
     */
    private void answer(AnswerButton b){
        HexButton button=game.getTriangleLastClicked();
        int n=button.getNumber();
        if (game.checkQuestion(b, n))
            correctAnswer();
        else {
            if (game.canReanswer(n) && (game.getState() == Game.States.FIRST_ANSWERS || game.getState() == Game.States.SECOND_ANSWERS)) {
                if (game.getState() == Game.States.FIRST_ANSWERS)
                    game.setState(Game.States.FIRST_WRONG);
                else
                    game.setState(Game.States.SECOND_WRONG);
                pQuestion.secondPlayerAnswers(new AnswerListener());
            }
            else
                wrongAnswer();
        }
        switch (game.isFull()){
            case 0: {
                end(rs.getString("draw"));
                break;
            }
            case 1:{
                end(rs.getString("firstWins"));
                break;
            }
            case 2:{
                end(rs.getString("secondWins"));
                break;
            }
            default:
                break;
        }

    }

    /**
     * Function which is called if the answers was wrong.
     * Sets color of the button and calls function choose().
     */
    private void wrongAnswer(){
        HexButton button = game.getTriangleLastClicked();
        button.SetColor(0);
        choose();
        this.updateUI();
    }

    /**
     * Function which is called if the answer was correct.
     * Sets color of the button, add button to player's buttons.
     * If the triangle is connect (function isTriangleConnect()), calls end().
     */
    private void correctAnswer(){
        HexButton button=game.getTriangleLastClicked();
        int player = -1;
        String winner="";
        if (game.getState()== Game.States.FIRST_ANSWERS || game.getState() == Game.States.FIRST_REANSWERS) {
            player = 1;
            winner=rs.getString("firstWins");
            game.getFirstPlayer().add(button.getNumber());
        }
        else if (game.getState()== Game.States.SECOND_ANSWERS || game.getState() == Game.States.SECOND_REANSWERS){
            player=2;
            winner=rs.getString("secondWins");
            game.getSecondPlayer().add(button.getNumber());

        }
        button.SetColor(player);
        if(game.isTriangleConnect(player)){
            end(winner);
        }
        else
            choose();

        this.updateUI();
    }

    /**
     * Function for changing state to *_CHOOSES.
     * Calls function changeIndicator().
     */
    private void choose(){
        if (game.getState()== Game.States.FIRST_ANSWERS || game.getState() == Game.States.FIRST_REANSWERS || game.getState()== Game.States.FIRST_WRONG)
            game.setState(Game.States.SECOND_CHOOSES);

        else if (game.getState()== Game.States.SECOND_ANSWERS || game.getState() == Game.States.SECOND_REANSWERS || game.getState()== Game.States.SECOND_WRONG)
            game.setState(Game.States.FIRST_CHOOSES);

        changeIndicator();
    }

    /**
     *  Function which is called if second player wants to answer the same question.
     *  Changes state to *_REANSERS, calls function for changing indicator and calls function ask().
     */
    private void reanswer(){
        int n = game.getTriangleLastClicked().getNumber();

        if (game.getState() == Game.States.FIRST_WRONG)
            game.setState(Game.States.SECOND_REANSWERS);
        else if (game.getState() == Game.States.SECOND_WRONG)
            game.setState(Game.States.FIRST_REANSWERS);
        changeIndicator();
        pQuestion.ask(n, new AnswerListener());

    }

    /**
     * Adds right indicator to the panel.
     */
    private void changeIndicator(){
        if (game.getState()== Game.States.FIRST_ANSWERS || game.getState() == Game.States.FIRST_REANSWERS || game.getState()== Game.States.FIRST_CHOOSES){
            this.remove(game.getSecondPlayerIndicator());
            this.add(game.getFirstPlayerIndicator(),BorderLayout.PAGE_START);
        }
        else{
            this.remove(game.getFirstPlayerIndicator());
            this.add(game.getSecondPlayerIndicator(),BorderLayout.PAGE_START);
        }
        this.updateUI();
    }


    /**
     * Function for end of the game. Changes game state to END and shows label with information about winner.
     * @param string string with information about winner
     */
    private void end(String string){
        game.setState(Game.States.END);
        pQuestion.removeAll();
        JLabel winner = new JLabel(string);
        winner.setFont(new Font("Arial",Font.PLAIN, 40));
        pQuestion.add(winner);
        pQuestion.updateUI();
    }


}
