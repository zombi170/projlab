package game.graphics;

import game.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.text.NumberFormat;

/**
 * A játék "kezdőképernyője". Innen lehet elindítani a játékot, illetve kilépni belőle.
 * Indítást követően sorban bekéri a szükséges paramétereket (játékosok száma, nevük), majd inicializálja a GameView-t.
 */
public class MainMenu extends JFrame {

    /**
     * a kezdőképernyőn ezzel léphetünk át a játék
     */
    private final JButton newGameButton = new JButton("új játék");

    /**
     * a játékból való kilépésre szolgál
     */
    private JButton exitButton = new JButton("kilépés");

    /**
     * - a játékosszám megadásakor továbblép a játékosok neveinek megadására
     * - a játékosok neveinek megadásakor továbblép a következőre
     * - utolsó játékos esetén elindítja a játékot
     */
    private final JButton nextButton = new JButton("következő");

    /**
     * a játékosok ide írhatják be a nevüket
     */

  private final TextField nameTextField  = new TextField();

    /**
     * a játék neve
     */
    private final JLabel title = new JLabel("A Logarléc");

    /**
     * a játékosok ide írhatják be, hogy hányan vannak
     */
    private final JFormattedTextField playerCount = new JFormattedTextField(NumberFormat.getIntegerInstance());

    /**
     * nyomon követi, hogy még hány játékos nem írta be a nevét
     */
    private long playersLeft;

    /**
     * a belső panel, amin elhelyezkezdnek az elemek
     */
    private final JPanel panel = new JPanel(new GridLayout(5, 1, 5, 10));

    /**
     * egy label, ami megjeleníti a kiirandó szöveget
     */
    private final JLabel label = new JLabel("Játékosok száma:");

    /**
     * a külső panel, amin elhelyezkezdik a belső panel
     */
    private final JPanel containerPanel = new JPanel(new GridBagLayout());

    /**
     * a játék demo módban van-e
     */
    private boolean demo = false;

    /**
     * konstruktor, felépíti a kezdeti nézetét, kettő gombbal
     */
    public MainMenu(){
        super("MainMenu test");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);


        newGameButton.addActionListener(e -> displayPlayerNumberSelection());
        exitButton.addActionListener(e -> System.exit(0));
        panel.add(title);
        panel.add(newGameButton);
        panel.add(exitButton);
        panel.setPreferredSize(new Dimension(300, 300));

        containerPanel.add(panel);

        add(containerPanel, BorderLayout.CENTER);
        createLook();
    }

    /**
     * a kinézetet állítja be
     */
    public void createLook() {
        panel.setBackground(new Color(28, 38, 31));
        containerPanel.setBackground(new Color(28, 38, 31));
        title.setFont(new Font("Arial", Font.BOLD, 26));
        label.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        label.setForeground(Color.WHITE);
        newGameButton.setBackground(new Color(93, 115, 107));
        newGameButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        nextButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        exitButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        nextButton.setBackground(new Color(93, 115, 107));
        exitButton.setBackground(new Color(93, 115, 107));
        exitButton.setForeground(Color.WHITE);
        nextButton.setForeground(Color.WHITE);
        newGameButton.setForeground(Color.WHITE);
        newGameButton.setFont(new Font("Arial", Font.BOLD, 18));
        nextButton.setFont(new Font("Arial", Font.BOLD, 18));
        exitButton.setFont(new Font("Arial", Font.BOLD, 18));
        playerCount.setFont(new Font("Arial", Font.BOLD, 18));
        nameTextField.setFont(new Font("Arial", Font.BOLD, 18));
    }

    /**
     * átalakítja az ablakot, hogy megadható legyen a játékosszám
     */
    public void displayPlayerNumberSelection() {
        panel.remove(newGameButton);
        panel.add(label, 1);
        panel.add(playerCount, 2);
        nextButton.addActionListener(e -> displayPlayerNameSelection());
        panel.add(nextButton, 3);
        setContentPane(containerPanel);
    }

    /**
     * átalakítja az ablakot, hogy az első játékos megadhassa a nevét
     */
    public void displayPlayerNameSelection() {
        if (playerCount.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Hiba! Add meg a játékosok számát!");
            return;
        }
        playersLeft = (Long)playerCount.getValue();

        label.setText("A játékos neve:");
        label.setForeground(Color.WHITE);
        panel.remove(playerCount);

        nameTextField.addTextListener(e -> nextButton.setEnabled(!nameTextField.getText().isEmpty()));
        nextButton.setEnabled(!nameTextField.getText().isEmpty());

        panel.add(nameTextField, 2);
        nextButton.addActionListener(e -> updatePlayerNameSelection());
        panel.add(nextButton, 3);
    }

    /**
     * frissíti az ablakot, hogy a következő játékos megadhassa a nevét
     */
    public void updatePlayerNameSelection() {
        String playerName = nameTextField.getText();
        if (!playerName.isEmpty()) {
            new Controller.GameListener("new Student " + playerName).actionPerformed(null);
        }

        nameTextField.setText("");
        if(--playersLeft == 0) {
            if(demo){
                new Controller.GameListener("newDemo").actionPerformed(null);

            } else {
                new Controller.GameListener("newGame").actionPerformed(null);

            }
            closeMainMenu();
        }
    }

    /**
     * bezárja a MainMenu-t
     */
    private void closeMainMenu(){
        setVisible(false);
        dispose();
    }

    public void setDemo(boolean value){
        demo = value;
    }

    public boolean getDemo(){
        return demo;
    }
}
