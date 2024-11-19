package game.items;

import game.Room;
import game.characters.Character;
import game.patterns.visitor.Connection;
import game.patterns.visitor.Visitor;

/**
 * represents an item in the game
 * the specific items are defined in its subclasses
 */
public abstract class Item {
    /**
     * Csak a teszteléshez kell, hogy ki lehessen írni az osztály nevét!
     */
    public String name;
    protected int usage;
    protected boolean passive = false;

    /**
     * Csak a teszteléshez kell, konstruktor
     */
    public Item (String name) {
        this.name = name;
    }

    /**
     * returns the name attribute
     * @return name attribute
     */
    public String getName() { return name; }

    /**
     * returns a formatted string containing the data of the item
     * @return string containing the name and usage
     */
    public String toString() {
        return name + ":\n\tusage: " + usage;
    }

    /**
     * picks up this item from the specified room
     * the item is removed from the room
     *
     * @param room the room from which we picked up the item
     */
    public void pickUp(Room room) {
        room.removeItem(this);
    }

    /**
     * drops this item to the specified room
     * the item is added to the room
     *
     * @param room the room in which we dropped the item
     */
    public void drop(Room room) {
        room.addItem(this);
    }

    /**
     * returns the usage attribute
     * @return usage attribute
     */
    public int getUsage() { return usage; }

    /**
     * sets the usage attribute
     * @param usage the value to set the usage to
     */
    public void setUsage(int usage) { this.usage = usage; }

    /**
     * uses the item, the item performs its "ability"
     *
     * @param room the room where the character used the item
     * @return true if the item was successfully used; false otherwise
     */
    public abstract boolean use(Room room);

    /**
     * has no effect
     * @param t2 the transistor
     */
    public void connectWith(Transistor t2) { }

    /**
     * return if the item is paired
     * used in the transistor
     * @return always false
     */
    public boolean isPaired() {
        return false;
    }

    public boolean isPassive() {
        return passive;
    }

    /**
     * checks if the item is compatible with the visitor
     *
     * @param visitor   the visitor to accept
     * @param character
     * @return true if the visitor was accepted; false otherwise
     */
    public abstract boolean accept(Visitor visitor, Room room, Character character);
    public abstract void accept(Connection connection,Item item);
}
