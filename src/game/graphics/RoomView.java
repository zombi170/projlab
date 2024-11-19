package game.graphics;

import game.Room;
import game.characters.Character;
import game.items.Item;
import game.Controller.GameListener;


import javax.swing.*;
import java.awt.*;

/**
 * Az aktuális szobát megjelenítő osztály. A szobában jelennek meg a benne tartózkodó
 * játékososk továbbá az ott felvehető tárgyak.
 */
public class RoomView extends JPanel  {
    /**
     * a szobában lévő tárgyakat megjelenítő panel.
     */
    private final JPanel items = new JPanel();

    /**
     * a szobában lévő játékosokat megjelenítő panel.
     */
    private final JPanel characters = new JPanel();

    /**
     * konstruktor
     * @param r - szoba, amit megjelenít
     */
    RoomView(Room r){
        if(r == null){
            return;
        }
        createLook(r);
        getData(r);
        add(items);
        add(characters);
        this.setBorder(BorderFactory.createLineBorder(Color.WHITE));
    }

    /**
     * a kapott szoba adatai alapján feltölti a panelt, karakterekkel és tárgyakkal.
     * @param room - szoba
     */
    private void getData(Room room){
        if(!room.getItems().isEmpty()) {
            for (Item item : room.getItems()) {
                if(item != null) {
                    ItemView itemView = new ItemView(item);
                    itemView.setBackground(new Color(38, 83, 89));
                    GameListener gameListener = new GameListener("pickUpItem " + item.getName());
                    itemView.addListener(gameListener);
                    items.add(itemView);
                }

            }
        }
        if(!room.getCharacters().isEmpty()) {
            for (Character character : room.getCharacters()) {
                CharacterView characterView = new CharacterView(character);
                characters.add(characterView);
                revalidate();
            }
        }
    }

    /**
     * a panel megjelenítéséhez tartozó paramétereket állítja be
     * @param room - a szoba
     */
    private void createLook(Room room){
        setLayout(new GridLayout(2,1));
        setPreferredSize(new Dimension(630, 400));
        if (room.isGassed()) {
            items.setBackground(new Color(28, 38, 31));
            characters.setBackground(new Color(28, 38, 31));
        } else {
            items.setBackground(new Color(21, 36, 40));
            characters.setBackground(new Color(21, 36, 40));
        }

        characters.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
    }
}
