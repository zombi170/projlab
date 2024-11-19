package game.patterns.visitor;

import game.Room;
import game.characters.Character;
import game.items.*;

/**
 * Azon tárgyakat azonosítja amelyek képesek teleportálni egy Hallgatót. Csak a teleportálásra kész Transistor esetében próbálja meg teleportálni a hallgatót.
 */
public class Teleportation implements Visitor {
    @Override
    public boolean visit(Mug mug, Room room, Character character) {
        return false;
    }

    @Override
    public boolean visit(Mask mask, Room room, Character character) {
        return false;
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

    /**
     * Meghívja a transistor teleport metódusát, mivel ezt a tárgyat kerestük
     * @param transistor a Tranzisztor
     * @param room a szoba, ahol a tranzisztort használják
     * @param character a karakter, aki használja a tranzisztort
     * @return a transistor teleport metódusának visszatérési értéke
     */
    @Override
    public boolean visit(Transistor transistor, Room room, Character character) {
       return transistor.teleport(room, character);
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
