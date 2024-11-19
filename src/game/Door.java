package game;

import game.characters.Character;

import java.util.ArrayList;
import java.util.List;

/**
 * Ajtót reprezentáló osztály, amely két szobát összeköt, mind a két szobában megjelenik ugyanaz a példány.
 * Lehet egyirányú is, valamint látható és láthatatlan is lehet az elátkozott szobákban.
 * @author Ádám Zsombor (zombi170)
 * @version 2.0.0
 * @since 1.0.0
 */
public class Door {
    /**
     * Az osztály elnevezése
     */
    private final String name;
    /**
     * Lista, amely tartalmazza a szobákat, amiket összeköt az ajtó, ha csak egy szoba található a listában,
     * akkor az ajtó egyirányú, tehát csak abba a szobába tud vinni az ajtó.
     */
    private final List<Room> rooms;
    /**
     * A szoba láthatóságát tárolja. Igaz, ha az ajtó látható, elátkozott szobák esetén lehet hamis is, amikor eltűnik.
     */
    private boolean visibility;

    /**
     * Konstruktor, amely inicializálja a rooms listát és beállítja az
     * isVisible attribútumot false-ra, valamint beállítja az osztály nevét a megadott értékre.
     */
    public Door(String name) {
        this.name = name;
        rooms = new ArrayList<>();
        visibility = true;
    }

    /**
     *  A megadott karaktert átrakja a másik szobába, ha az nincs tele és az ajtó nem egyirányú.
     *  Meg kell adni a függvények azt is, hogy jelenleg, hol tartózkodik az adott karakter.
     *  Továbbá visszaadja a karakternek azt a szobát, ahova érkezett
     *  vagy null-al tér vissza ha sikertelen volt a szobaváltás.
     *  @param character A karakter, aki átmegy az ajtón.
     *  @param room A szoba, ahol a karakter jelenleg tartózkodik.
     *  @return A szoba, ahova a karakter átkerült, vagy null, ha nem sikerült a szobaváltás.
     */
    public Room warpCharacter(Character character, Room room) {
        if (rooms.size() == 1 && rooms.get(0) == room) {
            return null;
        }

        Room destination = rooms.get(0) == room ? rooms.get(1) : rooms.get(0);

        if (destination.isFull()) {
            return null;
        }

        character.setCurrentRoom(destination);

        room.removeCharacter(character);
        destination.addCharacter(character);
        return destination;
    }

    /**
     * Megnézi, hogy benne van-e a rooms listában az adott szoba.
     * @return Igaz, ha benne van, egyébként hamis.
     */
    public boolean contains(Room room) {
        return rooms.contains(room);
    }

    /**
     * Hozzáad egy szobát az ajtóhoz.
     * @param room A szoba, amelyet hozzáadunk az ajtóhoz.
     */
    public void addRoom(Room room) {
        rooms.add(room);
    }

    /**
     * Töröl egy szobát az ajtóból.
     * @param room A szoba, amelyet törlünk az ajtóból.
     */
    public void removeRoom(Room room) {
        rooms.remove(room);
    }

    /**
     * Megváltoztatja az ajtó láthatóságát a jelenlegi ellentettjére.
     */
    public void changeVisibility() {
        visibility = !visibility;
    }

    /**
     * Visszaadja az ajtó láthatóságát.
     * @return Az ajtó láthatósága.
     */
    public boolean isVisible() {
        return visibility;
    }

    /**
     * Beállítja az ajtó láthatóságát.
     * @param visibility Az ajtó láthatósága.
     */
    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
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
        StringBuilder str = new StringBuilder(name + ":\n\trooms: ");

        for (Room room : rooms) {
            str.append(room.getName());
            if (rooms.indexOf(room) != rooms.size() - 1) {
                str.append(", ");
            }
        }

        str.append("\n\tisVisible: ").append(visibility);

        return str.toString();
    }
}
