package game.characters;

import game.Door;
import game.Room;
import game.items.*;
import game.patterns.observer.*;
import game.patterns.visitor.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Egy karaktert reprezentál a játékban
 * A specifikus karakterek a leszármazott osztályban vannak definiálva
 */
public abstract class Character implements Tickable {

    /**
     * Az adott példány változóneve
     */
    protected String name;
    /**
     * A szoba, melyben a character tartózkodik
     */
    protected Room currentRoom;
    /**
     * A character tárgyai
     */
    protected List<Item> items;
    /**
     * A character TickManager-e
     */
    protected TickManager tm;
    /**
     * Még hány tick idejéig fagyott a karakter (ha 0: nem fagyott)
     */
    protected int frozen;

    /**
     * Konstruktor
     * @param name A karakter neve
     */
    protected Character(TickManager tm, String name) {
        items = new ArrayList<>();
        this.name = name;
        this.tm = tm;
        tm.addMember(this);
    }

    /**
     * Ha lehetséges, a character szobát vált egy kiválasztott ajtón át
     * @param door Az ajtó, amin keresztül a character szobát szeretne váltani
     */
    public void changeRoom(Door door) {
        Room newRoom = door.warpCharacter(this, currentRoom);
    }

    /**
     * A character, ha még nem telt be az eszköztára, felvesz egy kiválasztott tárgyat
     * @param item A tárgy, amit a character fel szeretne venni
     */
    public void pickUpItem(Item item) {
        if (currentRoom.getVisitorCounter() > 0 && items.size() < 5) {
            item.pickUp(currentRoom);
            items.add(item);
        }
    }

    /**
     * A character eldobja egy tárgyát
     * @param item A tárgy, amit a character eldob
     */
    public void dropItem(Item item) {
        item.drop(currentRoom);
        items.remove(item);
    }

    /**
     * A character használ, ha ez lehetséges, egy kiválasztott tárgyat
     * Ha egy tárgy elhasználódott, törlődik a character eszköztárából
     * Ha a tárgy egy másodikként használandó transistor, akkor atteszi a charactert a másik szobába,
     * ha ez lehetséges
     * @param item A tárgy, amit a character használni szeretne
     */
    public void useItem(Item item) {
        if (!item.isPaired()) {
            Connection connection = new Connection();
            for (Item i : items) {
                item.accept(connection, i);
                if (item.isPaired()) return;
            }
        }
        
        Teleportation transistorTeleportation = new Teleportation();
        boolean accepted = item.accept(transistorTeleportation, currentRoom, this);

        if (!accepted) {
            item.use(currentRoom);
            if (item.getUsage() == 0) {
                items.remove(item);
            }
        }
    }

    /**
     * ha a character nem tud védekezni, akkor a frozen attribútumát
     * nullánál nagyobb értékre állítja és feliratkoztatja a TickManager listájára az entitást,
     * valamint eldobja minden tárgyát.
     */
    public void gassed() {
        GasProtection gasProtection = new GasProtection();
        boolean used = false;
        List<Item> toRemove = new ArrayList<>();
        for (Item value : items) {
            if (value.accept(gasProtection, currentRoom, this)) {
                used = true;
            }
            if (value.getUsage() == 0) {
                toRemove.add(value);
            }
            if (used) break;
        }
        items.removeAll(toRemove);
        if (!used) {

            if (frozen <= 0) {
                setFrozen(5);
            }
            while (!items.isEmpty()) {
                dropItem(items.get(0));

            }
        }
    }

    /**
     * a művelet, amelyet akkor kell végrehajtani, ha a character hallgatóval találkozik
     * @param s A hallgató, akivel a character találkozik
     * @return alapesetben false értékkel tér vissza, és nem hívódik meg fail()
     */
    public boolean meet(Student s) {
        return false;
    }

    /**
     * a művelet, amelyet akkor kell végrehajtani, ha a character oktatóval találkozik
     * @param t Az oktató, akivel a character találkozik
     * @return alapesetben false értékkel tér vissza, és nem hívódik meg fail()
     */
    public boolean meet(Teacher t) {
        return false;
    }

    /**
     * a művelet, amelyet akkor kell végrehajtani, ha a character takarítóval találkozik
     * @param t A takarító, akivel a character találkozik
     * @return alapesetben false értékkel tér vissza, és nem hívódik meg fail()
     */
    public boolean meet(Cleaner t) {
        return false;
    }

    /**
     * a művelet, amely akkor hajtódik végre, amikor a szobában, ahol a character tartózkodik
     * nedves táblatörlő rongy (sponge) lett használva;
     * csak oktató képes megfagyni ennek hatására
     */
    public void freeze() {
    }

    /**
     * beállítja a character frozen értékét
     * @param i A beállítandó frozen érték
     */
    public void setFrozen(int i) {
        frozen = i;
    }

    /**
     * visszaadja a character frozen értékét
     * @return A character frozen értéke
     */
    public int getFrozen() {
        return frozen;
    }

    /**
     * Csökkenti a character frozen attribútumának értékét.
     */
    public void tickUpdate() {
        if (frozen > 0) {
            frozen--;
        }
    }

    /**
     * beállítja a currentRoom-ot
     */
    public void setCurrentRoom(Room r) {
        currentRoom = r;

    }

    /**
     * Visszaadja a currentRoom-ot
     * @return A currentRoom
     */
    public Room getCurrentRoom() {
        return currentRoom;
    }

    /**
     * hozzáad egy tárgyat az eszköztárhoz
     * @param item A hozzáadandó tárgy
     */
    public void addItem(Item item) {
        items.add(item);
    }

    /**
     * eltávolít egy tárgyat az eszköztárból
     * @param item Az eltávolítandó tárgy
     */
    public void removeItem(Item item) {
        items.remove(item);
    }

    /**
     * A Meeting visitor működéséhez szükséges függvény
     * @param meeting A meeting visitor
     * @return a meeting visitor függvényének visszatérési értéke
     */
    public abstract boolean accept(Meeting meeting, Character c);

    /**
     * @return az adott példány megnevezése
     */
    public String getName() {
        return name;
    }

    /**
     * Stringgé alakítja az osztálypéldány részleteit
     * @return ezen példány részletei
     */
    @Override
    public String toString() {
        String details = this.getName() + ":\n" + "\titems:";
        for(int i = 0; i < items.size(); i++) {
            details = details.concat(" " + items.get(i).getName());
        }
        details = details.concat("\n\tcurrentRoom: " + currentRoom.getName() + "\n");
        details = details.concat("\tfrozen: " + frozen + "\n");
        return details;
    }
    /**
     * Megmondja, a karakternél ott van-e a megadott tárgy
     */
    public boolean hasItem(Item item) {
        if(items.contains(item)) {
            return true;
        }
        return false;
    }

    public void dropOtherItem(Item item) {
    }

    public List<Item> getItems() {
        return items;
    }
}
