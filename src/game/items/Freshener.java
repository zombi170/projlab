package game.items;

import game.Room;
import game.characters.Character;
import game.patterns.visitor.Connection;
import game.patterns.visitor.Visitor;

public class Freshener extends Item {
    /**
     * constructor
     */
    public Freshener(String name) {
        super(name);
        usage = 1;
    }

    /**
     * invokes the ability of the freshener, clearing the room of the toxic gas
     *
     * @param room the room where the character used the item
     * @return true if it was able to perform the ability (usage was at least 1); false otherwise
     */
    @Override
    public boolean use(Room room) {
        room.setGassed(false);
        return --usage >= 0;
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
        return visitor.visit(this, room, character);
    }

    @Override
    public void accept(Connection connection, Item item) {
        connection.visit(this, item);
    }
}
