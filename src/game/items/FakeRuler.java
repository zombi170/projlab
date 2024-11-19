package game.items;

import game.Controller;
import game.Room;

import javax.swing.*;

public class FakeRuler extends Ruler {

    /**
     * konstruktor, amely beállítja az osztály name attribútumát a megadott String-re.
     * @param name - a beállítandó név
     */
    public FakeRuler(String name, JDialog d) {
        super(name, d);
    }

    /**
     * Mindenképpen false-al tér vissza
     * @param room - a szoba, ahol a karakter felhasználja a tárgyat
     * @return false
     */
    @Override
    public boolean use(Room room) {
        return false;
    }

    /**
     * törli a tárgyat a szobából
     * @param room a szoba, ahonnan felvettük a tárgyat
     */
    @Override
    public void pickUp(Room room) {
        room.removeItem(this);
    }

    /**
     * nem rakja bele a szobába a tárgyat
     * @param room a szoba, ahol a karakter van
     */
    @Override
    public void drop(Room room) {}
}
