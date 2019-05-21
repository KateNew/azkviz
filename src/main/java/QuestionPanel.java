import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

/**
 * Panel s otázkami. Zobrazuje otázky a odpovědi.
 */
class QuestionPanel extends JPanel{
    private ResourceBundle rs;
    private Questions questions;

    QuestionPanel(Questions q, ResourceBundle rs) {
        this.rs=rs;
        this.setLayout(new GridLayout(20,1));
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        questions=q;
    }

    /** Zavolá funkci na zobrazení otázky podle typu otázky
     * @param num číslo otázky
     * @param listener listener na odpověď
     */
    void ask(int num, GamePanel.AnswerListener listener){
        Questions.Question q = questions.getQuestion(num);

        if(q.getType().equals(Questions.Type.ABC)){
            showABCQuestion(q,listener);

        } else if (q.getType().equals(Questions.Type.OPEN)) {
            showOpenQuestion(q,listener);
        }
        this.updateUI();
    }

    /** Zobrazí OPEN otázku a tlačítko na zobrazení odpovědi
     * @param q otázka
     * @param listener  listener na odpověď
     */
    private void showOpenQuestion(Questions.Question q, GamePanel.AnswerListener listener){
        JLabel quest = new JLabel(q.getQuest());
        this.add(quest);
        JButton showAnswer = new JButton(rs.getString("answer"));
        showAnswer.addActionListener((e)-> showOpenAnswer(q,listener));
        this.add(showAnswer);
    }

    /** Zobrazí odpověď na OPEN otázku a tlačítka ano/ne
     * @param q otázka
     * @param listener listener na odpověď
     */
    private void showOpenAnswer(Questions.Question q, GamePanel.AnswerListener listener){

        this.removeAll();
        JLabel answer = new JLabel(q.getAnswer());

        AnswerButton correct = new AnswerButton(rs.getString("correct"));
        correct.setType(Questions.Type.OPEN);
        correct.addActionListener(listener);

        AnswerButton wrong = new AnswerButton(rs.getString("wrong"));
        wrong.setType(Questions.Type.OPEN);
        wrong.addActionListener(listener);

        this.add(answer);
        this.add(correct);
        this.add(wrong);
        this.updateUI();
    }

    /** Zobrazí ABC otázku a tlačítka s odpověďmi
     * @param q otázka
     * @param listener listener na odpověď
     */
    private void showABCQuestion(Questions.Question q, GamePanel.AnswerListener listener){
        this.removeAll();
        JLabel quest = new JLabel(q.getQuest());
        this.add(quest);

        for (int i = 0; i < q.getOptions().length; i++) {
            String option = q.getOptions()[i];
            AnswerButton b = new AnswerButton(option);
            b.setType(Questions.Type.ABC);
            b.addActionListener(listener);
            this.add(b);
        }
        this.updateUI();
    }


    /** Funkce, která se zeptá, zda chce druhý hráč odpovídat.
     *  Volá se jen u ABC otázek.
     * @param n číslo otázky
     * @param listener listener na odpověď
     */
    void secondPlayerAnswers(int n, GamePanel.AnswerListener listener){
        JLabel emptyCell = new JLabel();
        this.add(emptyCell);
        JLabel wantAnswer = new JLabel(rs.getString("wantAnswer"));
        this.add(wantAnswer);

        JButton yes = new JButton(rs.getString("yes"));
        yes.addActionListener(listener);
        this.add(yes);

        JButton no  = new JButton(rs.getString("no"));
        no.addActionListener(listener);
        this.add(no);
        this.updateUI();
    }



}
