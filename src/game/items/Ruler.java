package game.items;

import game.Controller;
import game.Room;
import game.characters.Character;
import game.patterns.visitor.Connection;
import game.patterns.visitor.Visitor;

import javax.swing.*;

/**
 * represents the ruler item in the game
 * extends the item class
 */
public class Ruler extends Item{
    JDialog dialog;

    /**
     * constructor
     */
    public Ruler(String name, JDialog d) {
        super(name);
        usage = 1;
        this.dialog = d;
    }

    /**
     * notifies the controller about the game ending and students winning
     *
     * @param room the room from which we picked up the item
     */
    @Override
    public void pickUp(Room room) {
        dialog.setVisible(true);
    }

    /**
     * this function provides no functionality since the ruler ends the game when it's picked up
     *
     * @param room the room where the character used the item
     * @return true
     */
    @Override
    public boolean use(Room room) {
        return true;
    }

    /**
     * checks if the item is compatible with the visitor
     *
     * @param visitor   the visitor to accept
     * @param character
     * @return true if the visitor was accepted; false otherwise
     */
    @Override
    public boolean accept(Visitor visitor, Room room, Character character) {
        return false;
    }

    @Override
    public void accept(Connection connection, Item item) {
        connection.visit(this, item);
    }
}
