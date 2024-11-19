package game;

import game.graphics.MainMenu;
import java.util.Scanner;

/**
 * A Játék belépési pontja.
 * Itt lehet kiválasztani, hogy a felhasználó játszani szeretne vagy pedig tesztelni.
 */
public class Main {

    public static final int DEFAULT_VISITOR_COUNT = 5;

    /**
     * A Játék menüje.
     */
    public static void main(String[] args) {
        MainMenu mainMenu = new MainMenu();
        System.out.println("Demozni vagy játszani szeretnél? (d/j)");
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.next();
        if (choice.equals("d")) {
            mainMenu.setDemo(true);
        }
        new Controller(mainMenu);
        mainMenu.setVisible(true);
    }
}

