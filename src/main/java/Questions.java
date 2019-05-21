import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

class Questions{

    enum Type {ABC,OPEN};
    private int number;
    private ResourceBundle rs;

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
     * Konstruktor Questions. Zavolá funkci na načtení otázek.
     * @param file Soubor s otázkami
     * @throws QuestFormatException
     * @throws FileNotFoundException
     */
    Questions(File file, ResourceBundle rs) throws QuestFormatException, FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        questions=ReadQuestions(br);
        this.rs=rs;
    }

    /**
     * Načítání otázek ze souboru.
     * @param br BufferedReader
     * @return pole otázek
     * @throws QuestFormatException
     */
    private Question[] ReadQuestions(BufferedReader br) throws QuestFormatException{
            List<Question> list = new ArrayList<>();
            String line;
            try {
                while((line=br.readLine())!=null){
                    //přeskočí prázdné řádky
                    while(line.length()==0)
                        line=br.readLine();

                    Type type;
                    int num;
                    String quest;
                    String answer;
                    String[] options=new String[0];     //pokud je typ otázky OPEN options zůstane nulové pole
                    int n;

                    String[] head = line.split(" ");
                    //OPEN otázky mají v hlavičce jen číslo otázky
                    if (head.length==1){
                        type=Type.OPEN;
                        num=Integer.parseInt(Character.toString(head[0].charAt(0)));
                        quest=br.readLine();
                        answer=br.readLine();
                    }
                    //ABC mají navíc počet možností
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
                    //špatná hlavička
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
            catch(NumberFormatException e2){
                throw new QuestFormatException("wrong questions's format");
            }
            //zkontroluje, jestli je správný počet otázek, pokud ano, vrátí pole otázek
            if (CorrectNumber(list.size())){
                number=list.size();
                return list.toArray(Question[]::new);
            }
            else{
                throw new QuestFormatException("wrong number of questions");
            }


    }

    /**
     * Zkontroluje, jestli z daného počtu otázek jde udělat trojúhelník.
     * @param num počet otázek
     * @return true pokud ano, false pokud ne
     */
    private boolean CorrectNumber(int num){
        int n=(int)Math.floor(Math.sqrt(2*num));
        int n1=(int)Math.ceil(Math.sqrt(2*num));
        return ((n * n1) / 2 == num) && (n > 3 && n < 10);
    }

    /**
     * Zkontroluje, jestli je odpověď správná.
     * @param button tlačítko s odpovědí u ABC otázek, tlačítko ano/ne u OPEN otázek
     * @param n číslo otázky
     * @return
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
