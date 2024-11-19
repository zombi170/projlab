package game.characters;

import game.patterns.observer.TickManager;
import game.patterns.visitor.Meeting;

import java.util.Random;

/**
 * Egy oktató típusú karaktert reprezentál a játékban
 */
public class Teacher extends Character {
    /**
     * Konstruktor
     */
    public Teacher(TickManager tickManager, String name) {
        super(tickManager, name);
    }
    /** 
     * a művelet, ami akkor hajtódik végre, mikor az oktató hallgatóval találkozik, mikor a szobába belép
     * @param s A hallgató, akivel az oktató találkozik
     * @return True értékkel tér vissza, így jelzi, hogy a hallgató már találkozott oktatóval,
     * tehát vagy megbukott, vagy megvédte magát a szoba összes oktatójával szemben, nem kell több
     * karakterrel összetalálkoztatni
     */
    @Override
    public boolean meet(Student s) {
        s.fail();
        return true;
    }
    /**
     * Az oktató megfagy sponge használatakor
     */
    @Override
    public void freeze() {
        frozen = 5;
    }
    /**
     * Meghívja a Meeting visitor meet() függvényét
     * @param meeting A meeting visitor
     * @param character A karakter, akivel az oktató találkozik
     * @return a meeting meet() függvényének visszatérési értéke
     */
    @Override
    public boolean accept(Meeting meeting, Character character) {
        return meeting.meet(this, character);
    }

    /**
     * meghívja egy véletlenszerű tárgyára a dropItem függvényét
     * @param rand a random példány amivel kiválasztjuk a véletlenszerű tárgyat
     */
    public void dropRandomItem(Random rand) {
        if (!items.isEmpty()) {
            int itemInd = rand.nextInt(items.size());
            dropItem(items.get(itemInd));
        }
    }
}
