package game.items;

import game.Room;
import game.characters.Character;
import game.patterns.observer.TickManager;
import game.patterns.observer.Tickable;
import game.patterns.visitor.Connection;
import game.patterns.visitor.Visitor;

/**
 * A nedves táblatörlő rongyot megvalósító osztály. Ez egy aktív tárgy, aminek használatakor a
 * hallgatók megfagyaszthatják az oktatókat. A felvételtől számítva adott időn belül fel kell
 * használni, különben eltűnik. Egy alkalommal használható. Oktatók nem használhatják.
 */
public class Sponge extends Item implements Tickable {

    /**
     * reprezentálja, hogy még mennyi idő van hátra a tárgy használhatóságából.
     */
    private int timeLimit;

    /**
     * A tárgy felhasználásának számlálója
     */
    private TickManager tickManager;

    /**
     * kiírja az osztály attribútumainak értékét tesztelés érdekében
     * @return a kiírandó szöveg
     */
    @Override
    public String toString() {
        return super.toString() + "\n\ttimeLimit: " + timeLimit;
    }

    /**
     * konstruktor, amely beállítja a tickManager attribútmnak az értékét tm-re,
     * valamint beállítja az osztály name attribútumát a megadott String-re.
     * Ezen felül beállítja a usage értékét 1-re.
     * @param tm - a beállítandó TickManager
     * @param name - beállítandó név
     */
    public Sponge(TickManager tm, String name) {
        super(name);
        tickManager = tm;
        timeLimit = 10;
        usage = 1;
    }

    /**
     * használatkor megfagyasztja a szobában tartózkodó oktatókat, ha még nem száradt ki
     * @param room a szoba amiben a tárgy felhasználódik
     * @return igazat ad vissza, ha sikerült a tárgyat felhasználni, és hamisat ha nem
     */
    @Override
    public boolean use(Room room) {
        usage--;
        if(timeLimit > 0) {
            room.freezeTeachers();
            return true;
        } else {
            return false;
        }
    }

    /**
     * visszaadja a tárgy keresett visitor értékét
     *
     * @param visitor   a keresett visitor
     * @param character A karakter, aki a tárgyat használja
     * @return a keresett visitor értéke a tárgyra
     */
    @Override
    public boolean accept(Visitor visitor, Room room, Character character) {
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
     * csökkenti a timeLimit attribútumának értékét
     */
    public void tickUpdate() {
        if(timeLimit > 0) {
            --timeLimit;
        }
    }

    /**
     * felvételkor feliratkozik a TickManager listájára és kiveszi magát a paraméterként kapott szobából
     * @param room a szoba, ahonnan fel lett véve
     */
    @Override
    public void pickUp(Room room) {
        tickManager.addMember(this);
        room.removeItem(this);
    }
}