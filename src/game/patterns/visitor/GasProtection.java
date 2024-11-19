package game.patterns.visitor;

import game.Room;
import game.characters.Character;
import game.items.*;

/**
 * Azon tárgyakat azonosítja amelyek képesek automatikusan megvédeni, egy Karaktert egy
 * gázos szobában. Jelen esetben a Maszkot.
 */
public class GasProtection implements Visitor {
    @Override
    public boolean visit(Mug mug, Room room, Character character) {
        return false;
    }

    /**
     * Meghívja a mask use metódusát, mivel ezt a tárgyat kerestük
     * @param mask a Mask amit felhasználunk
     * @param room a szoba, ahol a maszkot használják
     * @param character a karakter, aki használja a maszkot
     * @return a mask use metódusának visszatérési értéke
     */
    @Override
    public boolean visit(Mask mask, Room room, Character character) {
        return mask.use(room);
    }

    @Override
    public boolean visit(TVSZ tvsz, Room room, Character character) {
        return false;
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
