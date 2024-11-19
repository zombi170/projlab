package game.characters;

import game.Door;
import game.Room;
import game.patterns.observer.TickManager;
import game.patterns.visitor.Meeting;

public class Cleaner extends Character {
    /**
     * Konstruktor
     */
    public Cleaner(TickManager tickManager, String name) {
        super(tickManager, name);
    }
    /**
     * visszaadja a paraméterként kapott visitor értékét a Cleanerre vonatkozóan
     * @return a visitor érték
     */
    @Override
    public boolean accept(Meeting meeting, Character character) {
        return meeting.meet(this, character);
    }
    /**
     * felülírja a Character ezen függvényét,
     * hogy érkeztével a Cleaner át tudja állítani a szoba
     * gassed attribútumát, ha szükséges a szellőztetés
     */
    @Override
    public void changeRoom(Door door) {
        Room newRoom = door.warpCharacter(this, currentRoom);
        if (newRoom != null) {
            currentRoom.clean(this);
        }
        if (currentRoom.isGassed()) {
            currentRoom.setGassed(false);
        }
    }

    @Override
    public void gassed() {
        //A Takarító ne tudjon elgázosodni
    }
}
