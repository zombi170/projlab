package game.items;

import game.Room;

public class FakeTVSZ extends TVSZ {

    /**
     * konstruktor, amely beállítja az osztály name attribútumát a megadott String-re.
     * @param name  - beállítandó név
     */
    public FakeTVSZ(String name) {
        super(name);
        usage = 1;
        passive = true;
    }

    /**
     * Mindenképpen false-al tér vissza, hiszen a tárgy hamis, így használatának nincsen hatása
     * @param room - a szoba, ahol a karakter felhasználja a tárgyat
     * @return - false
     */
    @Override
    public boolean use(Room room) {
        usage--;
        return false;
    }
}
