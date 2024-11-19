package game.graphics;

import game.items.Item;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionListener;


/**
 * a tárgyak grafikus megjelenítéséért felel
 */
public class ItemView extends JButton {
    /**
     * a megjelenítendő tárgy
     */
    Item item;

    /**
     * konstruktor
     * @param i
     */
    ItemView(Item i){
        item = i;
        draw();
    }

    /**
     * ActionListener hozzáadása a gombhoz
     * @param actionlistener - az ActionListener, amit hozzáadunk
     */
    public void addListener(ActionListener actionlistener) {
        this.addActionListener(actionlistener);
    }

    /**
     * a gomb kinézetéért felelős
     */
    public void draw() {
        setPreferredSize((new Dimension(60,60)));
        String text = getFirstTwoletters(item.getName());
        setText("<html>" + text + "<br>" + item.getUsage() + "</html>");
        setForeground(Color.WHITE);
        setFont(new Font("Serif", 1,15));
    }

    /**
     * visszaadja a megadott stringnek az első két betűjét
     * @param name - aminek keressük az első két betűjét
     * @return - az első két betű
     */
    public String getFirstTwoletters(String name) {
        return name.substring(0, Math.min(name.length(), 2)) + "\n";
    }
}
