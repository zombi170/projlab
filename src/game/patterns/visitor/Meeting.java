package game.patterns.visitor;

import game.characters.Cleaner;
import game.characters.Student;
import game.characters.Teacher;
import game.characters.Character;

/**
* Azon osztály, amely a karakterek közötti találkozásokat kezeli.
* @author Ádám Zsombor (zombi170)
* @version 1.0
* @since 1.0
*/
public class Meeting {

    /**
     * Bemutatja a Hallgatót a megadott karakternek.
     * @param student A Hallgató, aki belépett a szobába.
     * @param character A karakter, akivel találkozik.
     * @return A karakter meet() függvényének visszatérési értéke.
     */
    public boolean meet(Student student, Character character) {
        if (character.getFrozen() == 0) {
            return character.meet(student);
        }
        return false;
    }

    /**
     * Bemutatja az Oktatót a megadott karakternek.
     * @param teacher Az Oktató, aki belépett a szobába.
     * @param character A karakter, akivel találkozik.
     * @return A karakter meet() függvényének visszatérési értéke.
     */
    public boolean meet(Teacher teacher, Character character) {
        if (character.getFrozen() == 0) {
            return character.meet(teacher);
        }
        return false;
    }

    public boolean meet(Cleaner cleaner, Character character) {
        if (character.getFrozen() == 0) {
            return character.meet(cleaner);
        }
        return false;
    }
}
