package game.graphics;

import game.Controller.DropitemListener;
import game.Controller.GameListener;
import game.Door;
import game.Room;
import game.characters.Character;
import game.items.Item;

import javax.swing.*;
import java.awt.*;

/**
 * A játék kinézetéért felelős osztály
 */
public class GameView  extends JFrame {
    /**
     * gomb, amivel ki lehet lépni a játékból
     */
    public static JButton exitButton = new JButton("Kilépés");

    /**
     * gomb, amivel a soron lévő játékos be tudja fejezni a körét
     */
    public static JButton endTurnButton = new JButton("Kör vége");

    /**
     * a jelenlegi játékos nevének megjelenítése
     */
    public JLabel playerLabel = new JLabel("");

    /**
     * a panel, amelyen a szobában lévő ajtók jelennek meg
     */
    public JPanel doors = new JPanel();

    /**
     * annak a szobának a megjelenítése, ahol a játékos
     * tartózkodik
     */
    public RoomView roomView;

    /**
     * a panel, amelyen a játékos táskájában lévő tárgyak jelennek meg
     */
    public JPanel items = new JPanel();

    /**
     * kitöltő panel
     */
    public JPanel filler = new JPanel();

    /**
     * a hátralévő körök számának megjelenítése
     */
    public JLabel turnsLeftLabel = new JLabel("");

    /**
     * az éppen soron lévő játékos
     */
    private Character activeCharacter;

    /**
     * hátralévő körök száma
     */
    private int turnsLeft = 0;

    /**
     * konstruktor
     * @param c - aktív játékos
     */
    public GameView(Character c) {
        super("Game Window");
        setSize(800, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocation(300,100);

        setLayout(new FlowLayout());
        createLook();

        activeCharacter = c;
        updateView(activeCharacter, turnsLeft);
        exitButton.addActionListener(e -> {
            setVisible(false);
            dispose();
            System.exit(0);
        });

        endTurnButton.addActionListener(new GameListener("endTurn"));
    }

    /**
     * a megadott játékos szerint
     * frissíti a pályát, és a hátralévő körök számát
     * @param c - aktív játékos
     * @param t - hátralévő körök száma
     */
    public void updateView(Character c, int t){
        activeCharacter = c;
        if(activeCharacter == null || getRoom() == null) return;
        turnsLeft = t;
        if (roomView != null) {
            roomView.removeAll();
            remove(roomView);

        }
        if (items != null) {
            items.removeAll();
            remove(items);
        }
        if (doors != null) {
            doors.removeAll();
            remove(doors);
        }
        playerLabel.setText("<html>Aktív játékos: " + activeCharacter.getName() + "</html>");
        playerLabel.setForeground(Color.WHITE);
        playerLabel.setFont(new Font("Serif", 1,15));

        turnsLeftLabel.setText("Hátralévő körök: " + turnsLeft);
        turnsLeftLabel.setForeground(Color.WHITE);
        turnsLeftLabel.setFont(new Font("Serif", 1,15));
        filler.add(turnsLeftLabel);
        System.out.println(activeCharacter.getName());
        roomView = new RoomView(getRoom());
        createBag();
        createDoor();
        add(playerLabel);
        add(filler);
        add(endTurnButton);
        add(exitButton);
        add(doors);
        add(roomView);
        add(items);
        revalidate();
        repaint();
    }

    /**
     * játékos táskájának a megjelenítése
     */
    public void createBag(){
        for(Item item : activeCharacter.getItems()){
            ItemView itemView = new ItemView(item);
            itemView.setBackground(new Color(38,83,89));
            GameListener gameListener = new GameListener("useItem " + item.getName());
            DropitemListener dropitemListener = new DropitemListener(item);
            itemView.addMouseListener(dropitemListener);
            if(!item.isPassive()){
                itemView.addListener(gameListener);
            }
            items.add(itemView);
            revalidate();
            repaint();
        }
    }

    /**
     * ajtók megjelenítése
     */
    public void createDoor(){
        for(Door door : getRoom().getDoors()){
            if(door.isVisible()) {
                JButton doorView = new JButton("D");
                doorView.setBackground(new Color(112, 71, 43));
                doorView.setForeground(Color.WHITE);
                doorView.setFont(new Font("Serif", 1,15));
                GameListener gameListener = new GameListener("changeRoom " + door.getName());
                doorView.addActionListener(gameListener);
                doors.add(doorView);
            }
        }
    }

    /**
     * visszaadja az aktív játékos jelenlegi szobáját
     * @return - szoba
     */
    private Room getRoom(){
        return activeCharacter.getCurrentRoom();
    }

    /**
     * a GameView kinézetét állítja be
     */
    private void createLook(){
        getContentPane().setBackground(new Color(38, 38, 38));
        doors.setBackground(new Color(61,40,29));
        doors.setPreferredSize(new Dimension(705,50));
        doors.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        filler.setPreferredSize(new Dimension(300,50));
        filler.setBackground(new Color(38,38,38));
        items.setBackground(new Color(93, 115, 107));
        items.setPreferredSize(new Dimension(70,400));
        items.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        endTurnButton.setBackground(new Color(93, 115, 107));
        exitButton.setBackground(new Color(93, 115, 107));
    }

    /**
     * megjelenít egy popupot
     */
    public void frozenPopUp() {
        JDialog dialog = new JDialog(this, "frozen popup", true);
        dialog.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setPreferredSize(new Dimension(200,80));
        JLabel label = new JLabel("meg vagy fagyva meg " + activeCharacter.getFrozen() + " korig!");
        label.setPreferredSize(new Dimension(200, 30));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label);
        
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new GameListener("ok"));
        okButton.addActionListener(e -> dialog.dispose());
        okButton.setPreferredSize(new Dimension(55, 30));
        panel.add(okButton);

        dialog.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    public void deathPopUp(String name) {
        JDialog dialog = new JDialog(this, "death popup", true);
        dialog.setLayout(new BorderLayout());
        
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setPreferredSize(new Dimension(200,80));
        JLabel label = new JLabel(name + " meghalt :D");
        label.setPreferredSize(new Dimension(200, 30));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label);
        
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> dialog.dispose());
        okButton.setPreferredSize(new Dimension(55, 30));
        panel.add(okButton);
        
        dialog.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

}
