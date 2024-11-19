package game.patterns.observer;

import game.Room;

import java.util.ArrayList;
import java.util.List;

/**
 * Observer listát tároló osztály, amely minden tick-re meghívja a listájára feliratkozott
 * példányok tickUpdate() függvényét.
 * @author Ádám Zsombor (zombi170)
 * @version 2.0.0
 * @since 1.0.0
 */
public class TickManager {
    /**
     * Lista, amely tartalmazza azokat az osztályokat, akik fel vannak iratkozva a tick-enkénti frissítésre.
     */
    private final List<Tickable> tickList;

    /**
     * Konstruktor, amely inicializálja a tickList listát és
     * beállítja az osztály nevét a megadott értékre.
     */
    public TickManager() {
        tickList = new ArrayList<>();
    }

    /**
     * Hozzáad egy példányt a listához, ha az még nem szerepel benne.
     * @param tickable A példány, amelyet hozzáadunk a listához.
     */
    public void addMember(Tickable tickable) {
        if (!tickList.contains(tickable)) {
            tickList.add(tickable);
        }
    }

    /**
     * Hozzáad egy szobát a lista elejére, ha az még nem szerepel benne.
     * @param room A szoba, amelyet hozzáadunk a listához.
     */
    public void addRoom(Room room) {
        if (!tickList.contains(room)) {
            tickList.add(0, room);
        }
    }

    /**
     * Eltávolít egy példányt a listából, ha az szerepel benne.
     * @param tickable A példány, amelyet eltávolítunk a listából.
     */
    public void removeMember(Tickable tickable) {
        tickList.remove(tickable);
    }

    /**
     * A tickList listában lévő összes példánynak meghívja a tickUpdate() függvényét.
     * Ezzel értesítve őket, hogy frissíteniük kell magukat.
     * A példányoknak a tickUpdate() függvényben kell implementálniuk a frissítési logikát.
     */
    public void notifyMembers() {
        for (Tickable tickable : tickList) {
            tickable.tickUpdate();
        }
    }
}
