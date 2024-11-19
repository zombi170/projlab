package game.characters;

import game.items.Item;
import game.patterns.observer.TickManager;
import game.patterns.visitor.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Egy hallgató típusú karaktert reprezentál a játékban
 */
public class Student extends Character {
    /**
     * A hallgató maradék immunitásának tick száma (ha 0: nem immunis)
     */
    private int immunity;
    /**
     * Konstruktor
     * @param tickManager A tickManager
     * @param name Az osztály neve
     */
    public Student(TickManager tickManager, String name) {
        super(tickManager, name);
        immunity = 0;
    }
    /**
     * a hallgató megbukik vagy kivédheti a bukást valamilyen tárggyal
     */
    public void fail() {
        TeacherProtection teacherProtection = new TeacherProtection();
        List<Item> toRemove = new ArrayList<>();
        boolean used = false;
        for (Item item : items) {
            if (item.accept(teacherProtection, currentRoom, this)) {
                used = true;
            }
            if(item.getUsage() == 0) {
                toRemove.add(item);
            }
            if(used) break;
        }
        if (!used) {
            currentRoom = null;
        }
        items.removeAll(toRemove);
    }
    /**
     * A karakter összeköti a transzisztorokat, ha nincs kettő transzisztor a tárgyai között, akkor nem történik semmi
     * @param item A transzisztor, amit a karakter összeköt
     */
    public void connectTransistors(Item item){
        Connection connection = new Connection();
        for (Item i : items) {
            item.accept(connection, i);
        }
    }
    /**
     * csökkenti a hallgató frozen attribútumának értékét,
     * csökkenti a hallgató immunitását,
     */
    @Override
     public void tickUpdate() {
        if (frozen > 0)
            frozen--;
        if (immunity > 0)
            immunity--;

        items.removeIf(item -> item.getUsage() <= 0);
    }
    /**
     * a művelet, amelyet akkor kell végrehajtani, ha a hallgató oktatóval találkozik
     * @param t Az oktató, akivel a hallgató találkozik
     * @return False értékkel tér vissza, hogy a hallgató minden karakterrel találkozzon
     */
    @Override
    public boolean meet(Teacher t) {
        this.fail();
        return false;
    }
    /**
     * Meghívja a Meeting visitor meet() függvényét
     * @param meeting A meeting visitor
     * @param character A karakter, akivel a hallgató találkozik
     * @return a meeting meet() függvényének visszatérési értéke
     */
    @Override
    public boolean accept(Meeting meeting, Character character) {
        return meeting.meet(this, character);
    }

    /**
     * Stringgé alakítja az osztálypéldány részleteit
     * @return ezen Student példány részletei
     */
    @Override
    public String toString() {
        String details = this.getName() + ":\n" + "\titems:";
        for(int i = 0; i < items.size(); i++) {
            details = details.concat(" " + items.get(i).getName());
        }
        if(currentRoom != null) {
            details = details.concat("\n\tcurrentRoom: " + currentRoom.getName() + "\n");
        } else {
            details = details.concat("\n\tcurrentRoom:\n");
        }
        details = details.concat("\tfrozen: " + frozen + "\n");
        details = details.concat("\timmunity: " + immunity + "\n");
        return details;
    }

    public void setImmunity(int imm) {
        immunity = imm;
    }

    public int getImmunity() {
        return immunity;
    }
    /**
     * eldobat egy a paraméterrel nem egyezző tárgyat
     */
    public void dropOtherItem(Item item) {
        for (Item i : items) {
            if (!i.equals(item)) {
                setImmunity(3);
                dropItem(i);
                break;
            }
        }
    }

}
