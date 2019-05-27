import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Class with questions.
 */
class Questions{

    enum Type {ABC,OPEN};
    private int number;
    private ResourceBundle rs;

    /**
     * One question
     */
    class Question {
        Type getType() {
            return type;
        }

        String getQuest() {
            return quest;
        }

        String getAnswer() {
            return answer;
        }

        String[] getOptions() {
            return options;
        }

        private Type type;
        private String quest;
        private String answer;
        private String[] options;

        Question(int n, Type t, String q, String a, String[] o) {
            type = t;
            quest = q;
            answer = a;
            options = o;
        }
    }

    Question getQuestion(int number) {
        return questions[number];
    }

    private Question[] questions;

    /**
     * Questions constructor. Calls function for read questions from the file.
     * @param file question file
     * @throws QuestFormatException wrong format of question file
     * @throws FileNotFoundException question file not found
     */
    Questions(File file, ResourceBundle rs) throws QuestFormatException, FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        questions=ReadQuestions(br);
        this.rs=rs;
    }

    /**
     * Read question from file using buffered reader.
     * @param br buffered reader
     * @return question array
     * @throws QuestFormatException wrong format of question file
     */
    private Question[] ReadQuestions(BufferedReader br) throws QuestFormatException{
            List<Question> list = new ArrayList<>();
            String line;
            try {
                while((line=br.readLine())!=null){
                    //skip empty lines
                    while(line.length()==0)
                        line=br.readLine();

                    Type type;
                    int num;
                    String quest;
                    String answer;
                    String[] options=null;     //OPEN question has null options array.
                    int n;

                    String[] head = line.split(" ");
                    //OPEN question has only number of question in the head.
                    if (head.length==1){
                        type=Type.OPEN;
                        num=Integer.parseInt(Character.toString(head[0].charAt(0)));
                        quest=br.readLine();
                        answer=br.readLine();
                    }
                    //ABC question has number of question and number of options in the head.
                    else if (head.length==2){
                        type=Type.ABC;
                        num=Integer.parseInt(head[0]);
                        n=Integer.parseInt(head[1]);
                        quest=br.readLine();
                        answer=br.readLine();
                        options=new String[n];
                        for (int i = 0; i < n; i++) {
                            options[i]=br.readLine();
                        }
                    }
                    //wrong head
                    else{
                        throw new QuestFormatException("wrong question's head format");
                    }
                    Question q = new Question(num, type,quest,answer,options);
                    list.add(q);
                }
            }
            catch (IOException e){
                throw new QuestFormatException();
            }
            //In the head are not numbers.
            catch(NumberFormatException e2){
                throw new QuestFormatException("wrong questions's format");
            }
            //If the number of questions is correct, return array of questions.
            if (CorrectNumber(list.size())){
                number=list.size();
                return list.toArray(Question[]::new);
            }
            //Else throw exception.
            else{
                throw new QuestFormatException("wrong number of questions");
            }


    }

    /**
     * Checks if is it possible to make a triangle from the number of questions.
     * @param num number of question
     * @return true = it is possible, false = it is not possible
     */
    private boolean CorrectNumber(int num){
        int n=(int)Math.floor(Math.sqrt(2*num));
        int n1=(int)Math.ceil(Math.sqrt(2*num));
        return ((n * n1) / 2 == num) && (n > 3 && n < 10);
    }

    /**
     * Checks if the answer is correct.
     * @param button ABC - button with option, OPEN - button correct/wrong
     * @param n number of question
     * @return true = answer is correct, false = answer is wrong
     */
    boolean Check(AnswerButton button, int n){
        if (button.getType().equals(Type.ABC)) {
            return GetQuestion(n).getAnswer().equals(button.getText());
        }
        else if (button.getType().equals(Type.OPEN)) {
            return button.getText().equals(rs.getString("correct"));
        }
        return false;
    }

    int NumberOfQuestions(){
        return number;
    }

    private Question GetQuestion(int n){
        return questions[n];
    }

}
