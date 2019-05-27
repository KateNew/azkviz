import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.ResourceBundle;


public class Main {
    /**
     * Makes main frame.
     */
    private static void createAndShowGUI() {
        Locale[] supportedLocales = {Locale.ENGLISH,new Locale("cz", "CZ"),};
        ResourceBundle rs = ResourceBundle.getBundle("LabelsBundle");
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());

        JButton play = new JButton(rs.getString("startGame"));
        SettingPanel setting = new SettingPanel(rs,play);

        // removes panel with setting and add panel with game
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
        javax.swing.SwingUtilities.invokeLater(Main::createAndShowGUI);
    }

}