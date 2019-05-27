package mff.java.azkviz;
import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

/**
 * Panel with questions. Shows questions and options.
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

    /**
     * Calls function for show the answer according the type.
     * @param num number of question
     * @param listener listener for answer
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

    /**
     * Shows OPEN question and button for show the answer.
     * @param q question
     * @param listener listener for answer
     */
    private void showOpenQuestion(Questions.Question q, GamePanel.AnswerListener listener){
        JLabel quest = new JLabel(q.getQuest());
        this.add(quest);
        JButton showAnswer = new JButton(rs.getString("answer"));
        showAnswer.addActionListener((e)-> showOpenAnswer(q,listener));
        this.add(showAnswer);
    }

    /**
     * Shows the OPEN answer and buttons wrong/correct.
     * @param q question
     * @param listener listener for answer
     */
    private void showOpenAnswer(Questions.Question q, GamePanel.AnswerListener listener){

        this.removeAll();
        JLabel quest = new JLabel(q.getQuest());
        JLabel answer = new JLabel(q.getAnswer());

        AnswerButton correct = new AnswerButton(rs.getString("correct"));
        correct.setType(Questions.Type.OPEN);
        correct.addActionListener(listener);

        AnswerButton wrong = new AnswerButton(rs.getString("wrong"));
        wrong.setType(Questions.Type.OPEN);
        wrong.addActionListener(listener);

        this.add(quest);
        this.add(answer);
        this.add(correct);
        this.add(wrong);
        this.updateUI();
    }

    /**
     * Shows the ABC question and buttons with options.
     * @param q question
     * @param listener listener for answer
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


    /**
     *  Asks if second player wants to answer same question.
     *  Only ABC question.
     * @param listener listener for answer
     */
    void secondPlayerAnswers(GamePanel.AnswerListener listener){
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
