package game.patterns.visitor;

import game.characters.Teacher;
import game.items.*;

/**
 * defiens wether or not a teacher can pick up the given item
 * if the item is of type Ruler nothing happens
 * else calls the teachers pickUpItem() function
 */
public class TeacherPickUp {
    public void visit(Teacher teacher, Mug mug) {
        teacher.pickUpItem(mug);
    }

    public void visit(Teacher teacher, Sponge sponge) {
        teacher.pickUpItem(sponge);
    }

    public void visit(Teacher teacher, Camembert camembert) {
        teacher.pickUpItem(camembert);
    }

    public void visit(Teacher teacher, TVSZ tvsz) {
        teacher.pickUpItem(tvsz);
    }

    public void visit(Teacher teacher, Freshener freshener) {
        teacher.pickUpItem(freshener);
    }

    public void visit(Teacher teacher, Transistor transistor) {
        teacher.pickUpItem(transistor);
    }

    public void visit(Teacher teacher, Mask mask) {
        teacher.pickUpItem(mask);
    }

    public void visit(Teacher teacher, Ruler ruler) {}
}
