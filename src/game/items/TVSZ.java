package game.items;

import game.Room;
import game.characters.Character;
import game.patterns.visitor.Connection;
import game.patterns.visitor.Visitor;


/**
 * A TVSZ denevérbőrre nyomtatott példánya tárgyat megvalósító osztály. Amikor egy hallgató
 * és egy oktató egy szobába kerülnek, megmenti a hallgatót a haláltól. Három alkalommal
 * használható. Oktatók nem használhatják.
 */
public class TVSZ extends Item {

    /**
     * a tárgy konstruktora, létrehozza a tárgyat és beállítja a usage értéket 3-ra.
     * Valamint beállítja az osztály name attribútumát a megadott String-re.
     */
    public TVSZ(String name) {
        super(name);
        usage = 3;
        passive = true;
    }

    /**
     * kifejti a hatását automatikusan, amikor egy hallgató és egy oktató egy szobába kerül, a hallgató megmenekül a haláltól
     * csökkenti a usage értékét eggyel
     * @param room - a szoba, ahol a karakter felhasználja a tárgyat
     * @return igazat ad vissza, ha sikerült a tárgyat felhasználni, és hamisat ha nem
     */
    @Override
    public boolean use(Room room) {
        if (usage > 0) {
            --usage;
            return true;
        }
        return false;
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
        return visitor.visit(this, room,character );
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
}
