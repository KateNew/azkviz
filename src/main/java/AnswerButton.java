import javax.swing.*;

class AnswerButton extends JButton {

    Questions.Type getType() {
        return type;
    }

    void setType(Questions.Type type) {
        this.type = type;
    }

    private Questions.Type type;

    AnswerButton(String text){
        super(text);
    }

}
