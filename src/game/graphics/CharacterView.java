package game.graphics;

import javax.swing.*;

import game.Controller;
import game.characters.Character;

import java.awt.*;

/**
 * A karakterek grafikus kinézetéért felel, egyik fő funkciója, hogy a karaktert megjelenését
 * kezeli fagyott állapotának megfelelően.
 */
public class CharacterView extends JLabel {

    /**
     * konstruktor, benne állítódik be a megjelenés, illetve a
     * paraméterként kapott karakter frozen értéke alapján állítja be a színét.
     * @param c - a karakter
     */
    CharacterView(Character c){
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setPreferredSize(new Dimension(50,100));
        setOpaque(true);
        setBackground(new Color(166, 163, 118));
        setText(c.getName());
        setForeground(Color.BLACK);
        setFont(new Font("Serif", 1,15));
        setHorizontalAlignment(SwingConstants.CENTER);
        if(c.getFrozen() > 0){
            setBackground(new Color(115,151,153));
        }
    }
}
