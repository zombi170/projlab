package game.patterns.visitor;

import game.Room;
import game.characters.Character;
import game.characters.Student;
import game.items.*;

/**
 *Egy interfész, amelyet három osztály is megvalósít (TeacherProtection, GasProtection,
 * Teleportation). A Protection interfész segítségével lesz képes a Hallgató azonosítani
 * a táskájában lévő olyan passzív tárgyakat amelyek segíthetik őt a védekezésben.
 */

public interface Visitor {
    /**
     * a Söröspohár azonosításához szükséges visit függvény.
     *
     * @param mug a Söröspohár
     * @param room a szoba, ahol a söröspoharat használják
     * @param character a karakter, aki használja a söröspoharat
     * @return true, ha a Söröspohár a felhasznált tárgy, false egyébként, vagy ha nem sikerült használni
     */
     boolean visit(Mug mug, Room room, Character character);

    /**
     * a Mask azonosításához szükséges visit függvény.
     *
     * @param mask      a Mask
     * @param character a karakter, aki a tárgyat használja
     * @return true, ha a Mask a felhasznált tárgy, false egyébként vagy ha nem sikerült használni
     */
     boolean visit(Mask mask, Room room, Character character);

    /**
     * a TVSZ azonosításához szükséges visit függvény.
     *
     * @param tvsz      a TVSZ
     * @param character a karakter, aki a tárgyat használja
     * @return true, ha a TVSZ a felhasznált tárgy, false egyébként vagy ha nem sikerült használni
     */
     boolean visit(TVSZ tvsz, Room room, Character character);

    /**
     * a Ruler azonosításához szükséges visit függvény.
     *
     * @param ruler     a Ruler
     * @param character a karakter, aki a tárgyat használja
     * @return true, ha a Ruler a felhasznált tárgy, false egyébként vagy ha nem sikerült használni
     */
     boolean visit(Ruler ruler, Room room, Character character);

    /**
     * a Sponge azonosításához szükséges visit függvény.
     *
     * @param sponge    a Sponge
     * @param character a karakter, aki a tárgyat használja
     * @return true, ha a Sponge a felhasznált tárgy, false egyébként vagy ha nem sikerült használni
     */
     boolean visit(Sponge sponge, Room room, Character character);

    /**
     * a Tranzisztor azonosításához szükséges visit függvény.
     *
     * @param transistor a Tranzisztor
     * @param character a karakter, aki a tárgyat használja
     * @return true, ha a Tranzisztor a felhasznált tárgy, false egyébként vagy ha nem sikerült használni
     */
     boolean visit(Transistor transistor, Room room, Character character);

    /**
     * a Camembert azonosításához szükséges visit függvény.
     *
     * @param camembert a Camembert
     * @param character a karakter, aki a tárgyat használja
     * @return true, ha a Camembert a felhasznált tárgy, false egyébként vagy ha nem sikerült használni
     */
     boolean visit(Camembert camembert, Room room, Character character);

    /**
     * a Freshener azonosításához szükséges visit függvény.
     *
     * @param freshener a Camembert
     * @param character a karakter, aki a tárgyat használja
     * @return true, ha a Freshener a felhasznált tárgy, false egyébként vagy ha nem sikerült használni
     */
     boolean visit(Freshener freshener, Room room, Character character);
}
