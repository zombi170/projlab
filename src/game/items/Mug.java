package game.items;

import game.Room;
import game.characters.Character;
import game.patterns.observer.TickManager;
import game.patterns.observer.Tickable;
import game.patterns.visitor.Connection;
import game.patterns.visitor.Visitor;

public class Mug extends Item implements Tickable {

    /**
     * A tárgy felhasználásának számlálója
     */
    private TickManager tickManager;

    /**
     * konstruktor, amely beállítja a tickManager attribútmnak az értékét tm-re,
     * valamint beállítja az osztály name attribútumát a megadott String-re.
     * Ezen felül beállítja a usage értékét is.
     */
    public Mug(TickManager tm, String name) {
        super(name);
        tickManager = tm;
        usage = 10;
        passive = true;
    }

    /**
     * kiírja az osztály attribútumainak értékét tesztelés érdekében
     * @return a kiírandó szöveg
     */
    @Override
    public String toString() {
        return name + ":\n\tusage: " + usage;
    }

    /**
     * visszaadja a tárgy keresett visitor értékét
     * @param visitor   a keresett visitor
     * @param character A karakter, aki a tárgyat használja
     * @return a keresett visitor értéke a tárgyra
     */
    @Override
    public boolean accept(Visitor visitor, Room room, Character character) {
        return visitor.visit(this,room,character);
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
     * aktiválódik amikor egy hallgató és egy oktató egy szobába kerül,
     * a hallgató immunitást élvez minden oktatóval szemben, amíg életben van a tárgy,
     * elindítja a usage visszaszámlálót
     * @param room a szoba amiben a tárgy felhasználódik
     * @return igazat ad vissza, ha sikerült a tárgyat felhasználni, és hamisat ha nem
     */
    @Override
    public boolean use(Room room) {
        if(usage > 0) {
            tickManager.addMember(this);
            return true;
        }
        return false;
    }

    /**
     * csökkenti a usage attribútumának értékét,
     */
    public void tickUpdate() {
        if(usage > 0) {
            usage--;
        }
    }
}
