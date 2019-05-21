import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.util.ResourceBundle;


public class Main {
    /**
     * Vytvoří hlavní okno.
     */
    private static void createAndShowGUI() {
        ResourceBundle rs = MyResourses.getBundle("MyResourses");
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());

        JButton play = new JButton(rs.getString("startGame"));
        SettingPanel setting = new SettingPanel(rs,play);
        //play odstraní panel s nastavením a přídá panel se hrou
        play.addActionListener(e -> {
            try {
                frame.remove(setting);
                GamePanel game = new GamePanel(setting.GetFile(),rs);
                frame.add(game);
                game.updateUI();
            }
            catch (FileNotFoundException | QuestFormatException e1){
                JOptionPane.showMessageDialog(frame, e1.getMessage());

            }


        });
        frame.add(setting,BorderLayout.CENTER);
        frame.setDefaultCloseOperation(3);
        frame.setSize(3000,2000);
        frame.setVisible(true);


    }


    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

}