package game.patterns.observer;

/**
 * Observer interfész, amelyet azok az osztályok implementálnak, amiknek bizonyos állapota függhet az eltelt időtől.
 * @author Ádám Zsombor (zombi170)
 * @version 2.0.0
 * @since 1.0.0
 */
public interface Tickable {

    /**
     * Az osztályok frissítésére szolgáló függvény.
     * A példányoknak ebben a függvényben kell implementálniuk a frissítési logikát.
     */
    void tickUpdate();

    /**
     * Visszaadja az osztály nevét.
     * @return Az osztály neve.
     */
    String getName();
}
