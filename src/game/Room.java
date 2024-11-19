package game;

import game.characters.Character;
import game.items.Item;
import game.patterns.observer.TickManager;
import game.patterns.observer.Tickable;
import game.patterns.visitor.Meeting;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static game.Main.DEFAULT_VISITOR_COUNT;

/**
 * A szobát megvalósító osztály. A pálya alapeleme, minden karakter szobáról szobára menve
 * próbálja elérni a saját célját. A szobákat ajtó kötik össze. Minden szobában lehetnek tárgyak.
 * Minden szobában fix mennyiségű karakter tartózkodhat egyszerre.
 * Lehetnek elátkozott szobák is, amelyek ajtajai néha eltűnnek és néha előtűnnek.
 * Továbbá lehet egy szoba gázos is, ide belépve a karakterek megbénulnak és eldobják tárgyaikat.
 * @author Ádám Zsombor (zombi170)
 * @version 2.0.0
 * @since 1.0.0
 */
public class Room implements Tickable {

    /**
     * Az osztály elnevezése
     */
    private final String name;
    /**
     * Lista, amely tartalmazza a szobában található tárgyakat.
     */
    private final List<Item> items;
    /**
     * Lista, amely tartalmazza a szobához tartozó ajtókat.
     */
    private final List<Door> doors;
    /**
     * Lista, amely tartalmazza a szobában található karaktereket.
     */
    private final List<Character> characters;
    /**
     * A szoba kapacitása, azaz, hogy hány karakter tartózkodhat egyszerre a szobában.
     */
    private int capacity;
    /**
     * A szoba gázos-e.
     */
    private boolean gassed;
    /**
     * A szoba elátkozott-e.
     */
    private boolean cursed;
    /**
     * Az osztály frissítéséért felelős menedzser osztály.
     */
    private final TickManager tickManager;
    /**
     * Értéke alapesetben egy nullánál nagyobb szám, amely minden
     * egyes karakter szobába lépésével csökken, és ha eléri a nullát, akkor a szoba
     * ragacsossá válik és nem lehet tárgyakat felvenni belőle.
     */
    private int visitorCounter;

    private int index = 1;

    /**
     * Konstruktor, amely inicializálja characters, items és doors listákat és beállítja a tickManager
     * attribútumának az értékét a megadott TickManager-re, valamint beállítja az osztály
     * name attribútumát a megadott String-re. További alapértékek, amiket beállít:
     * capacity = 10, cursed = false, gassed = false, visitorCounter = 100.
     */
    public Room(TickManager tickManager, String name) {
        this.name = name;
        items = new ArrayList<>();
        doors = new ArrayList<>();
        characters = new ArrayList<>();
        this.tickManager = tickManager;
        tickManager.addRoom(this);
        capacity = 10;
        cursed = false;
        gassed = false;
        visitorCounter = DEFAULT_VISITOR_COUNT;
    }

    /**
     * A szobában található ajtók láthatóságát változtatja meg.
     */
    public void cursing() {
        for (Door d : doors) {
            d.changeVisibility();
        }
    }

    /**
     * Meghívja a szobában lévő karakterekre a freeze() függvényt.
     */
    public void freezeTeachers() {
        for (Character c : characters) {
            c.freeze();
        }
    }

    /**
     * Végigmegy a karakterek listáján és elgázosítja a szobában lévő karaktereket.
     */
    public void gassingCharacters() {
        for (Character c : characters) {
            c.gassed();
        }
    }

    /**
     * A paraméterként megadott szoba alapján kibővíti a szoba tulajdonságait.
     * Kapacitásnak a két érték közül a nagyobbat állítja be.
     * @param room A szoba, amely alapján kibővítjük a szoba tulajdonságait.
     */
    public void update(Room room) {
        capacity = Math.max(capacity, room.getCapacity());
        cursed = cursed || room.cursed;
        gassed = gassed || room.gassed;
        visitorCounter = Math.max(visitorCounter, room.getVisitorCounter());

        Door door = room.popDoor();
        while (door != null) {
            if (doors.contains(door)) {
                doors.remove(door);
                door.removeRoom(this);
            } else if (door.contains(this)) {
                doors.add(door);
            } else {
                doors.add(door);
                door.addRoom(this);
            }
            door = room.popDoor();
        }
    }

    /**
     * Felosztja a szobának a tulajdonságait, a felét megtartja, a másik felével egy új szobát hoz létre.
     * @return Az új szoba, amely a felosztás eredményeként jön létre.
     */
    public Room splitAttributes() {
        Room newRoom = new Room(tickManager, "newRoom" + index++);
        newRoom.setCapacity(capacity);
        newRoom.setCursed(cursed);
        newRoom.setGassed(gassed);
        newRoom.setVisitorCounter(visitorCounter);

        List<Door> moveDoors = new ArrayList<>();
        for (int i = 0; i < doors.size()/2; ++i) {
            moveDoors.add(doors.get(i));
        }

        for (Door d : moveDoors) {
            removeDoor(d);
            d.removeRoom(this);
            d.addRoom(newRoom);
            newRoom.addDoor(d);
        }

        return newRoom;
    }

    /**
     * Kitessékeli a mozogni képes karaktereket egy szomszédos szobába.
     * @param cleaner A takarító aki éppen takarítja ezt a szobát.
     */
    public void clean(Character cleaner) {
        gassed = false;
        List<Character> toMove = new ArrayList<>();

        for (Character c : characters) {
            if (c.getFrozen() == 0 && c != cleaner) {
                toMove.add(c);
            }
        }

        for (Character c : toMove) {
            int headcount = characters.size();
            for (Door d : doors) {
                c.changeRoom(d);
                if (characters.size() < headcount) {
                    break;
                }
            }
        }

        visitorCounter = DEFAULT_VISITOR_COUNT;
    }

    /**
     * A szoba frissítéséért felelős függvény, amely meghívja a gassingCharacters() és a cursing() függvényeket.
     */
    public void tickUpdate() {
        if (gassed) {
            gassingCharacters();
        }

        if (cursed) {
            cursing();
        }
    }

    /**
     * Visszaad egy ajtót a doors listájából, amit egyből ki is töröl a
     * listából, valamint az adott ajtóból is törli a szobát.
     * @return A listából kivett ajtó, null ha üres a lista.
     */
    public Door popDoor() {
        if (doors.isEmpty()) {
            return null;
        }

        Door door = doors.get(0);
        doors.remove(door);
        door.removeRoom(this);
        return door;
    }

    /**
     * Hozzáad egy tárgyat a szobához.
     * @param item A tárgy, amelyet hozzáadunk a szobához.
     */
    public void addItem(Item item) {
        items.add(item);
    }

    /**
     * Töröl egy tárgyat a szobából.
     * @param item A tárgy, amelyet törlünk a szobából.
     */
    public void removeItem(Item item) {
        items.remove(item);
    }

    /**
     * Hozzáad egy ajtót a szobához.
     * @param door Az ajtó, amelyet hozzáadunk a szobához.
     */
    public void addDoor(Door door) {
        doors.add(door);
    }

    /**
     * Töröl egy ajtót a szobából.
     * @param door Az ajtó, amelyet törlünk a szobából.
     */
    public void removeDoor(Door door) {
        doors.remove(door);
    }

    /**
     * Hozzáad egy karaktert a szobához és bemutatja a szobában lévő karaktereknek.
     * Ha a karakter megbukott, akkor kiveszi a szobából.
     * @param character A karakter, amelyet hozzáadunk a szobához.
     */
    public void addCharacter(Character character) {
        characters.add(character);
        Meeting meeting = new Meeting();

        for (Character c : characters) {
            if (c != character && (character.accept(meeting, c))) {
                break;
            }
        }

        characters.removeIf(c -> c.getCurrentRoom() == null);
        visitorCounter--;
    }

    /**
     * Töröl egy karaktert a szobából.
     * @param character A karakter, amelyet törlünk a szobából.
     */
    public void removeCharacter(Character character) {
        characters.remove(character);
    }

    /**
     * Visszaadja, hogy a szoba tele van-e.
     * @return Igaz, ha a szoba tele van, egyébként hamis.
     */
    public boolean isFull() {
        return characters.size() >= capacity;
    }

    /**
     * Visszaadja a szobában található karakterek számát.
     * @return A szobában található karakterek száma.
     */
    public int getNumberOfCharacters() {
        return characters.size();
    }

    /**
     * Visszaadja a szoba kapacitását.
     * @return A szoba kapacitása.
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Beállítja a szoba kapacitását.
     * @param capacity A szoba kapacitása.
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Visszaadja a szoba cursed értékét.
     * @return A szoba cursed értéke.
     */
    public boolean isCursed() {
        return cursed;
    }

    /**
     * Beállítja a szoba cursed értékét.
     * @param cursed A szoba cursed értéke.
     */
    public void setCursed(boolean cursed) {
        this.cursed = cursed;
    }

    /**
     * Visszaadja a szoba gázos tulajdonságát.
     * @return A szoba gázos tulajdonsága. (Igaz, ha a szoba gázos, egyébként hamis.)
     */
    public boolean isGassed() {
        return gassed;
    }

    /**
     * Beállítja a szoba gázos tulajdonságát.
     * @param gassed A szoba gázos tulajdonsága.
     */
    public void setGassed(boolean gassed) {
        this.gassed = gassed;
    }

    /**
     * Visszaadja a szoba visitorCounter attribútumának az értékét.
     * @return A szoba visitorCounter attribútuma.
     */
    public int getVisitorCounter() {
        return visitorCounter;
    }

    /**
     * Beállítja a szoba visitorCounter attribútumának az értékét.
     * @param visitorCounter A visitorCounter értéke.
     */
    public void setVisitorCounter(int visitorCounter) {
        this.visitorCounter = visitorCounter;
    }

    /**
     * Megnézi, hogy a szomszédos-e a szoba a paraméterként megadott szobával
     * @param room - szoba, amivel megnézzük, hogy szomszédos-e
     * @return true - ha szomszédosak, false - ha nem
     */
    public boolean areNeighbours(Room room) {
        for (Door d : doors) {
            if (d.contains(room)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Visszaadja az osztály nevét.
     * @return Az osztály neve.
     */
    public String getName() {
        return name;
    }

    /**
     * Visszaadja az osztály attribútumainak értékét a tesztelés érdekében.
     * @return Az osztály attribútumai.
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder(name + ":\n\titems: ");

        for (Item item : items) {
            str.append(item.getName());
            if (items.indexOf(item) != items.size() - 1) {
                str.append(", ");
            }
        }

        str.append("\n\tdoors: ");
        for (Door door : doors) {
            str.append(door.getName());
            if (doors.indexOf(door) != doors.size() - 1) {
                str.append(", ");
            }
        }

        str.append("\n\tcharacters: ");
        for (Character character : characters) {
            str.append(character.getName());
            if (characters.indexOf(character) != characters.size() - 1) {
                str.append(", ");
            }
        }

        str.append("\n\tgassed: ").append(gassed);
        str.append("\n\tcursed: ").append(cursed);
        str.append("\n\tcapacity: ").append(capacity);
        str.append("\n\tvisitorCounter: ").append(visitorCounter);

        return str.toString();
    }

    /**
     * A játék során a jelenlegi játékosnak visszaadja az osztály attribútumainak értékét a láthatatlan ajtókat kivéve.
     * @return Az osztály attribútumai, a láthatatlan ajtókat kivéve
     */
    public String toStringForUser() {
        StringBuilder str = new StringBuilder(name + ":\n\titems: ");

        for (Item item : items) {
            str.append(item.getName());
            if (items.indexOf(item) != items.size() - 1) {
                str.append(", ");
            }
        }

        str.append("\n\tdoors: ");
        for (Door door : doors) {
            if (door.isVisible()) {
                str.append(door.getName());
                if (doors.indexOf(door) != doors.size() - 1) {
                    str.append(", ");
                }
            }
        }

        str.append("\n\tcharacters: ");
        for (Character character : characters) {
            str.append(character.getName());
            if (characters.indexOf(character) != characters.size() - 1) {
                str.append(", ");
            }
        }

        str.append("\n\tcursed: ").append(cursed);
        str.append("\n\tgassed: ").append(gassed);

        return str.toString();
    }

    /**
     * visszatér egy véletlenszerű ajtóval a szobában lévő ajtók közül
     * @param rand a random példány amivel kiválasztjuk a véletlenszerű ajtót
     * @return null, ha nincs ajtó a szobában, különben a véletlenszerűen lekért ajtó
     */
    public Door getRandomDoor(Random rand) {
        if (doors.isEmpty()) return null;

        int doorInd = rand.nextInt(doors.size());
        return doors.get(doorInd).isVisible() ? doors.get(doorInd) : null;
    }

    /**
     * visszatér egy véletlenszerű tárggyal a szobából
     * @param rand a random példány amivel kiválasztjuk a véletlenszerű tárgyat
     * @return null, ha nincs tárgy a szobában, különben a véletlenszerűen lekért tárgy
     */
    public Item getRandomItem(Random rand) {
        if (items.isEmpty()) return null;

        int itemInd = rand.nextInt(items.size());
        return items.get(itemInd);
    }
    /**
     * Megmondja, bent van-e a szobában az adott tárgy
     */
    public boolean hasItem(Item item) {
        if(items.contains(item)) {
            return true;
        }
        return false;
    }
    /**
     * Megmondja, bent van-e a szobában az adott karakter
     */
    public boolean hasCharacter(Character ch) {
        if(characters.contains(ch)) {
            return true;
        }
        return false;
    }

    public List<Door> getDoors() {
        return  doors;
    }

    public List<Item> getItems() {
        return items;
    }

    public List<Character> getCharacters() {
        return characters;
    }


}
