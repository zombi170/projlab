package game.items;

import game.Room;
import game.characters.Character;
import game.patterns.visitor.Connection;
import game.patterns.visitor.Visitor;

/**
 * Egy camembert típusú tárgyat reprezentál a játékban
 */
public class Camembert extends Item {

    /**
     * Konstruktor
     */
    public Camembert(String name) {
        super(name);
        usage = 1;
    }
    /**
     * visszaadja az adott visitor értékét a tárgyra
     *
     * @param visitor   Az adott visitor
     * @param character A karakter, aki a tárgyat használja
     * @return a visitor értéke a tárgyra
     */
    @Override
    public boolean accept(Visitor visitor, Room room, Character character){
        return visitor.visit(this, room, character);
    }

    /**
     * A connection visit metódusát hívja meg, a transistorok használatához szükséges
     * @param connection az összekötés lehetőségét ellenőrzi
     * @param item az item amit össze akarunk kötni
     */
    @Override
    public void accept(Connection connection, Item item) {
        connection.visit(this, item);
    }

    /**
     * elgázosítja az adott szobát
     * @param room A szoba, amit elgázosít
     * @return True értékkel tér vissza
     */
    @Override
    public boolean use(Room room) {
        room.setGassed(true);
        usage--;
        return true;
    }
}