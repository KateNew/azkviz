import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ResourceBundle;

import static java.awt.Label.CENTER;

public class GamePanel extends JPanel implements ActionListener{
    private ResourceBundle rs;
    private Game game;
    private QuestionPanel pQuestion;

    /**
     * Vytvoří instanci Questions a Game a přidá panel s trojúhelníkem a panel pro otázky.
     * Vykreslí trojúhelník a nastaví stav hry na začátek.
     * @param file soubor s otázkami
     * @param rs resourseBundle s texty
     * @throws QuestFormatException soubor má špatný formát
     * @throws FileNotFoundException soubor, který zadal uživatel nebyl nalezen
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
     * Pokud políčko ještě nebylo vybráno a stav je nastaven na *_CHOOSES, závolá funkci click(b).
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        HexButton b = (HexButton)e.getSource();
        if (b.state.equals(HexButton.State.NOT_CHOOSEN) && (game.getState()== Game.States.FIRST_CHOOSES || game.getState()== Game.States.SECOND_CHOOSES))
            click(b);
    }

    /**
     * Funkce po kliknutí na políčko v trojúhelníku. Zavolá funkci na zobrazení otázky a změní stav na *_ANSWERS.
     * @param button políčko v trojúhelníku
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
     *  Pokud se zavolal listener po stisknutí tlačíka AnswerButton (u ABC otázek) a stav je nastaven na odpovídání, zavolá se funkce answer, která zpracuje odpověd
     *  Pokud se zavolal z JButton jde o odpověď, jestli chce odpovídat druhý hráč. Zavolá se buď funkce wrongAnswer nebo reanswer.
     */
    public class AnswerListener implements ActionListener{
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
     * Funkce po po kliknutí na odpověď.
     * Pokud je odpověď správná (funkce CheckQuestion) zavolá se fuknce correctAnswer.
     * Jinak pokud je možné, aby odpovídat druhý hráč, zavolá se funkce SecondPlayerAnswer, jinak wrongAnswer
     * @param b tlačítko s odpovědí, na které uživatel klikl
     */
    private void answer(AnswerButton b){
        HexButton button=game.getTriangleLastClicked();
        int n=button.getNumber();
        if (game.CheckQuestion(b, n))
            correctAnswer();
        else {
            if (game.canReanswer(n) && (game.getState() == Game.States.FIRST_ANSWERS || game.getState() == Game.States.SECOND_ANSWERS)) {
                //kdyby zůstalo *_ANSWERS šlo by stisknout tlačítko na odpověď i v situaci, kdy se ptáme, jestli chce odpovídat druhý hráč
                if (game.getState() == Game.States.FIRST_ANSWERS)
                    game.setState(Game.States.FIRST_WRONG);
                else
                    game.setState(Game.States.SECOND_WRONG);
                pQuestion.SecondPlayerAnswers(n, new AnswerListener());
            } else
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
     * Funkce, která se zavolá, pokud byla odpověď špatná a není možné znovu odpovídat.
     * Nastaví políčku barvu na špatně zodpovězeno a zavolá funkci na změnu stavu na *_CHOOSES
     */
    private void wrongAnswer(){
        HexButton button = game.getTriangleLastClicked();
        button.SetColor(0);
        choose();
        this.updateUI();
    }

    /**
     * Funkce, která se zavolá, pokud byla odpověď správná.
     * Nastaví barvu políčka, přidá políčko do políček daného hráče a zkontroluje, jestli nebyl propojen trojúhelník.
     * Pokud ano, ukončí hru. Pokud ne, zavolá funkci na změnu stavu na *_CHOOSES.
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
     * Funkce na změnu stavu na *_CHOOSES.
     * Zavolá funkci na změnu barvy indikátoru.
     */
    private void choose(){
        if (game.getState()== Game.States.FIRST_ANSWERS || game.getState() == Game.States.FIRST_REANSWERS || game.getState()== Game.States.FIRST_WRONG)
            game.setState(Game.States.SECOND_CHOOSES);

        else if (game.getState()== Game.States.SECOND_ANSWERS || game.getState() == Game.States.SECOND_REANSWERS || game.getState()== Game.States.SECOND_WRONG)
            game.setState(Game.States.FIRST_CHOOSES);

        changeIndicator();
    }

    /**
     *  Funkce pro odpovídání druhého hráče (pokud první odpověděl špatně).
     *  Změní stav na *_REANSWERS, zavolá funkci na změnu barvy indikátoru a zavolá funkci ask.
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
     * Přidá na panel aktuální indikátor.
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
     * Funkce pro ukončení hry. Nastaví stav hry na END a zobrazí label s informací o vítězi.
     * @param string Řetězec, který informuje o vítězi.
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
