package game.patterns.visitor;

import game.Room;
import game.characters.Character;
import game.items.*;

/**
 * Azon tárgyakat azonosítja amelyeket össze lehet kötni egy másikkal. Csak az összekötésre alkalmas Transistor esetében ad vissza igazat.
 */
public class Connection{
    public void visit(Mug mug, Item item) {

    }
    public void visit(Mask mask, Item item) {

    }
    public void visit(TVSZ tvsz, Item item  ) {
    }
    public void visit(Ruler ruler, Item item   ) {

    }
    public void visit(Sponge sponge, Item item) {
    }

    public void visit(Camembert camembert, Item item) {
    }

    public void visit(Freshener freshener, Item item) {
    }

    /**
     * Meghívja az item connectWith metódusát, mivel ezt a tárgyat kerestük
     * @param transistor a Tranzisztor, amit összekötünk
     * @param item, amit összekötünk
     */
    public void visit(Transistor transistor, Item item) {
        item.connectWith(transistor);
    }

}
