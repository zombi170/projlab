package game.items;

import game.Room;
import game.characters.Character;
import game.patterns.visitor.Connection;
import game.patterns.visitor.Visitor;

/**
 * represents the transistor item in the game
 * extends the item class
 */
public class Transistor extends Item{
    /**
     * reference to the Transistor it's connected with
     */
    private Transistor pair = null;

    /**
     * reference to the room where it was used
     */
    private Room usedRoom = null;

    /**
     * constructor
     */
    public Transistor(String name) {
        super(name);
        usage = 1;
    }

    @Override
    public String toString() {
        return super.toString() +
                "\n\tpair: " + (pair == null ? "null" : pair.getName()) +
                "\n\tusedRoom: " + (usedRoom == null ? "null" : usedRoom.getName());
    }

    /**
     * overrides the default drop function
     * resets the references to their default values and adds the item to the room
     *
     * @param room the room in which we dropped the item
     */
    @Override
    public void drop(Room room) {
        if (pair != null) {
            pair.disconnect();
            pair = null;
        }
        usedRoom = null;
        usage = 1;
        super.drop(room);
    }

    /**
     * disconnects the transistor from its pair
     * if it was already used it also nulls the usedRoom attribute
     * and puts itself in the room it was used in
     */
    public void disconnect() {
        pair = null;
        usage = 1;
        if (usedRoom != null) {
            drop(usedRoom);
            usedRoom = null;
        }
    }

    /**
     * retrieves the Transistor it's paired with
     *
     * @return the value of the pair attribute
     */
    public Transistor getPair() {
        return pair;
    }

    /**
     * sets the pair value of the Transistor
     * @param pair the value to set the pair to
     */
    public void setPair(Transistor pair) {
        this.pair = pair;
    }

    /**
     * returns if the transistor is paired or not
     * @return true if the transistor is paired, false otherwise
     */
    @Override
    public boolean isPaired() {
        return !(pair == null);
    }

    /**
     * retrieves the Room it was used in
     *
     * @return the value of the usedRoom attribute
     */
    public Room getUsedRoom() {
        return usedRoom;
    }

    /**
     * sets the value of the usedRoom attribute
     */
    public void setUsedRoom(Room room) {
        usedRoom = room;
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

    /**
     * if the Transistor is not yet connected, calls the useConnection() function
     * if the Transistor is connected and its pair wasn't used before, calls the useFirst() function
     * if the Transistor is connected and its pair has been used, calls the useSecond() function
     *
     * @param room the room where the character used the item
     * @return true if the item was successfully used; false otherwise
     */
    @Override
    public boolean use(Room room) {
        if (pair == null) return false;
        if (pair.getUsedRoom() == null) useFirst(room);
        else return useSecond();

        return true;
    }

    /**
     * performs the connection between this and the other Transistors
     */
    @Override
    public void connectWith(Transistor t2) {
        if(t2 != this) {
            connect(t2);
            t2.connect(this);
        }
    }

    /**
     * places the Transistor in the room where it was used
     *
     * @param room the room where we put the Transistor
     */
    private void useFirst(Room room) {
        usedRoom = room;
        usage = 0;
    }

    /**
     * checks if the room where the pair was placed is full
     * "drops" the pair in the room where it's located
     *
     * @return true if the room isn't empty; false otherwise
     */
    private boolean useSecond() {
        Room pairRoom = pair.getUsedRoom();
        pair.drop(pairRoom);
        return !pairRoom.isFull();
    }

    /**
     * sets the pair reference to the given Transistor
     *
     * @param transistor the Transistor we wish to connect to this one
     */
    public void connect(Transistor transistor) {
        pair = transistor;
    }

    /**
     * teleports the character to the room where the pair was placed, if the transistor is connected
     * @param room the room where the character used the item
     * @param character the character we wish to teleport
     * @return true if the character was teleported; false otherwise
     */
    public boolean teleport(Room room,Character character){
        if(pair != null && pair.getUsedRoom() != null) {
            Transistor firstUsedTransistor = pair;
            Room tpToRoom = firstUsedTransistor.getUsedRoom();

            boolean used = use(room);
            character.dropItem(this);
            if (used) {
                character.setCurrentRoom(tpToRoom);
                room.removeCharacter(character);
                tpToRoom.addCharacter(character);
            }
            return true;
        }
        return false;
    }
}