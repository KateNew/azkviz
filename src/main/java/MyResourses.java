import java.util.Enumeration;
import java.util.ResourceBundle;

public class MyResourses extends ResourceBundle {
    @Override
    protected Object handleGetObject(String key) {
        if (key.equals("startGame")) return "Hrát";
        if (key.equals("wantAnswer")) return "Chce odpovídat druhý hráč?";
        if (key.equals("yes")) return "Ano";
        if (key.equals("no")) return "Ne";
        if (key.equals("correct")) return "Správně";
        if (key.equals("wrong")) return "Špatně";
        if (key.equals("firstWins")) return "První hráč vyhrál!";
        if (key.equals("secondWins")) return "Druhý hráč vyhrál!";
        if (key.equals("draw")) return "Remíza!";
        if (key.equals("chooseFile")) return "Vyber soubor s otázkami.";
        if (key.equals("answer")) return "Odpověď";

        return null;
    }

    @Override
    public Enumeration<String> getKeys() {
        return null;
    }
}
