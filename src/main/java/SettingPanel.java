import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ResourceBundle;

/**
 * Úvodní panel. Umožňuje vybrat soubor s otázkami.
 */
class SettingPanel extends JPanel {
    private File file;
    private ResourceBundle rs;

    SettingPanel(ResourceBundle rs,JButton playButton){
        this.rs = rs;
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        file= new File("QuestExample");
        JLabel title = new JLabel("AZ kvíz");
        title.setFont(new Font("Arial",Font.PLAIN, 50));
        title.setAlignmentX(CENTER_ALIGNMENT);
        this.setLayout(new GridBagLayout());
        Container container = new Container();
        container.setLayout(new BoxLayout(container,BoxLayout.PAGE_AXIS));
        this.add(container);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setAlignmentX(CENTER_ALIGNMENT);
        fileChooser.setSize(new Dimension(this.getWidth(),50));
        playButton.setAlignmentX(CENTER_ALIGNMENT);
        container.add(title);
        container.add(fileChooser);
        container.add(playButton);
    }

    /**
     * Panel na výběr souboru s otázkami.
     */
    private class FileChooser extends JPanel{

        private static final long serialVersionUID = 1L;

        /**
         * Vytvoří a ukáže tlačítko na otevření okna pro výběr souboru
         */
        private FileChooser() {
            JButton button = new JButton(rs.getString("chooseFile"));
            this.add(button);
            button.addActionListener(e -> createFileChooser());
        }

        /**
         * Vytvoří okno pro výběr souboru. Název souboru uloží do proměnné file a vypíše.
         */
        private void createFileChooser(){
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.showOpenDialog(this);
            file=fileChooser.getSelectedFile();
            JLabel label = new JLabel(file.getName());
            this.add(label);
            this.updateUI();
        }
    }

    File GetFile(){
        return file;
    }
}
