package game.items;

import game.Room;

public class FakeMask extends Mask {

    /**
     * konstruktor, amely beállítja az osztály name attribútumát a megadott String-re.
     * @param name - a beállítandó név
     */
    public FakeMask(String name) {
        super(name);
        usage = 1;
        passive = true;
    }

    /**
     * mindenképpen false-al tér vissza
     * @param room - a szoba, ahol a karakter felhasználja a tárgyat
     * @return false
     */
    @Override
    public boolean use(Room room) {
        usage--;
        return false;
    }
}
