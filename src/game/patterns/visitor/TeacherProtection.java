package game.patterns.visitor;

import game.Room;
import game.characters.Character;
import game.characters.Student;
import game.items.*;

/**
 * Azon tárgyakat azonosítja amelyek képesek automatikusan megvédeni, egy Hallgatót egy
 * Oktatótól. Jelen esetben a TVSZt és a Söröspoharat.
 */
public class TeacherProtection implements Visitor {
    /**
     * Meghívja a tvsz use metódusát, mivel ezt a tárgyat kerestük
     * @param mug a Söröspohár, amit felhasználunk
     * @param room a szoba, ahol a söröspoharat használják
     * @param character a karakter, aki használja a söröspoharat
     * @return a tvsz use metódusának visszatérési értéke
     */
    @Override
    public boolean visit(Mug mug, Room room, Character character) {
        character.dropOtherItem(mug);
        return mug.use(room);
    }

    @Override
    public boolean visit(Mask mask, Room room, Character character) {
        return false;
    }

    /**
     * Meghívja a tvsz use metódusát, mivel ezt a tárgyat kerestük
     * @param tvsz a TVSZ, amit felhasználunk
     * @param room a szoba, ahol a tvsz-t használják
     * @param character a karakter, aki használja a tvsz-t
     * @return a tvsz use metódusának visszatérési értéke
     */
    @Override
    public boolean visit(TVSZ tvsz, Room room, Character character) {
        return tvsz.use(room);
    }

    @Override
    public boolean visit(Ruler ruler, Room room, Character character) {
        return false;
    }

    @Override
    public boolean visit(Sponge sponge, Room room, Character character) {
        return false;
    }

    @Override
    public boolean visit(Transistor transistor, Room room, Character character) {
        return false;
    }

    @Override
    public boolean visit(Camembert camembert, Room room, Character character) {
        return false;
    }

    @Override
    public boolean visit(Freshener freshener, Room room, Character character) {
        return false;
    }
}
