package game;


import game.graphics.GameView;
import game.graphics.MainMenu;
import game.items.*;
import game.patterns.observer.TickManager;
import game.characters.*;
import game.characters.Character;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * A játék vezérlője, a játékmenetet irányítja.
 */
public class Controller {
    /**
     * Beolvasásra szolgáló Scanner a Controllerben.
     */
    private final Scanner sc = new Scanner(System.in);
    /**
     * A tanárokat tároló lista.
     */
    private static final List<Teacher> teachers = new ArrayList<>();
    /**
     * A takarítókat tároló lista.
     */
    private static final List<Cleaner> cleaners = new ArrayList<>();
    /**
     * A diákokat tároló lista.
     */
    private static final List<Student> students = new ArrayList<>();
    /**
     * Az éppen aktív karakter.
     */
    private static Character activeCharacter;
    /**
     * A hátralévő körök száma.
     */
    private static int turnsLeft = 40;
    /**
     * A TickManager példánya.
     */
    private static final TickManager tickManager = new TickManager();
    /**
     * Az objektumokat tároló map.
     */
    private static final Map<String, Object> objects = new HashMap<>();
    /**
     * A random generátor.
     */
    private static final Random rand = new Random();
    /**
     * A játék végét jelző változó.
     */
    private static boolean gameOver = false;
    /**
     * Az aktív játékos körének végét jelző változó.
     */
    private static boolean isTurnOver = false;
    /**
     * A fájlba írásra szolgáló BufferedWriter.
     */
    private static BufferedWriter writer = new BufferedWriter(new BufferedWriter(new PrintWriter(System.out)));
    /**
     * a játék megjelenítéséért felelős osztály
     */
    private static GameView gameView;
    /**
     * a főmenü megjelenítéséért felelős osztály
     */
    private static MainMenu mainMenu;
    /**
     * popup, amikor a játékosok nyertek
     */
    private static JDialog studentsWonPopUp = new JDialog(gameView, "winning popup", true);
    /**
     * popup, amikor a tanárok nyertek
     */
    private static JDialog teachersWonPopUp = new JDialog(gameView, "loseing popup", true);


    public Controller(MainMenu mM) {
        mainMenu = mM;
    }

    /**
     * A kapott sort kiírja a konzolra és/vagy fájlba
     * @param line a kiírandó sor
     */
    public static void writeLine(String line) {
        try {
            writer.write(line + "\n");
            writer.flush();
        } catch (FileNotFoundException e) {
            System.err.println("Fajl nem talalhato: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Fajl irasa soran hiba tortent: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ismeretlen hiba: " + e.getMessage());
        }
    }

    /**
     * Feldolgozza a felhasználó által beírt parancssort
     * és meghívja a megfelelő parancsfüggvényt
     * @param command A parancssor stringje
     */
    public static void commandCalls(String command){
      String[] cmd = command.split(" ");
      switch(cmd[0]) {
        case "load":
          load(cmd[1]);
          break;
        case "new":
          newObj(cmd[1], cmd[2]);
          break;
        case "setCharacter":
          setCharacter(cmd[1]);
          break;
        case "setAttribute":
          setAttribute(cmd[1], cmd[2], cmd[3], cmd[4]);
          break;
        case "changeRoom":
          Door d = (Door)objects.get(cmd[1]);
          isTurnOver = true;
          try {
              Room rBefore = activeCharacter.getCurrentRoom();
              activeCharacter.changeRoom(d);
              if(activeCharacter.getCurrentRoom() != rBefore) {
                writeLine("A " + activeCharacter.getName() + " sikeresen atment a " + d.getName() + " ajton.");
                  gameView.updateView(activeCharacter, turnsLeft);
              } else {
                writeLine("A " + activeCharacter.getName() + " nem tudott átmenni a " + d.getName() + " ajton.");
                  gameView.updateView(activeCharacter, turnsLeft);
              }
              
          } catch(Exception e) {
              writeLine("A karakter nem tudott szobat valtani.");
          }
          break;
        case "useItem":
          Item it1 = (Item)objects.get(cmd[1]);
          try {
              activeCharacter.useItem(it1);
              if(!activeCharacter.hasItem(it1)) {
                writeLine("A " + activeCharacter.getName() + " felhasznalta a " + it1.getName() + " targyat.");
                  gameView.updateView(activeCharacter, turnsLeft);
              } 
              else if(it1.isPaired()) {
                writeLine("A " + activeCharacter.getName() + " sikeresen osszekotott ket tranzisztort.");
                  gameView.updateView(activeCharacter, turnsLeft);
              }
              else {
                writeLine("A " + activeCharacter.getName() + " nem tudta felhasznalni a " + it1.getName() + " targyat.");
                  gameView.updateView(activeCharacter, turnsLeft);
              }
          } catch(Exception e) {
              writeLine("A targy hasznalata sikertelen volt.");
              gameView.updateView(activeCharacter, turnsLeft);
          }
          break;
        case "splitRoom":
          Room r = (Room)objects.get(cmd[1]);
          Building b = (Building)objects.get("building");
            try {
                if(r.getNumberOfCharacters() == 0) {
                    writeLine("A " + r.getName() + " szoba ketteoszodott.");
                    gameView.updateView(activeCharacter, turnsLeft);
                } else {
                    writeLine("Nem osztodott kette a szoba.");

                }
                b.splitRoom(r);
          } catch(Exception e) {
              writeLine("Nem osztodott kette a szoba.");
          }
          break;
        case "mergeRooms":
          Room r1 = (Room)objects.get(cmd[1]);
          Room r2 = (Room)objects.get(cmd[2]);
          Building b2 = (Building)objects.get("building");
            try {
                if(r1.areNeighbours(r2) && r1.getNumberOfCharacters() == 0 && r2.getNumberOfCharacters() == 0) {
                    b2.mergeRooms(r1, r2);
                    writeLine("A " + r1.getName() + " es a " + r2.getName() + " szobak egyesultek.");
                    gameView.updateView(activeCharacter, turnsLeft);
                } else {
                    writeLine("Nem egyesultek a szobak.");
                }
          } catch(Exception e) {
              writeLine("Nem egyesultek a szobak.");
          }
          break;
        case "pickUpItem":
          Item it2 = (Item)objects.get(cmd[1]);
          try {
              activeCharacter.pickUpItem(it2);
              if(activeCharacter.hasItem(it2)) {
                writeLine("A " + activeCharacter.getName() + " sikeresen felvette a " + it2.getName() + " targyat.");

                  gameView.updateView(activeCharacter, turnsLeft);
              } else {
                writeLine("A " + activeCharacter.getName() + " nem tudta felvenni a " + it2.getName() + " targyat.");
                  gameView.updateView(activeCharacter, turnsLeft);
              }
          } catch(Exception e) {
              writeLine("A jatekos nem tudta felvenni a " + it2.getName() + " targyat.");
          }
          break;
        case "dropItem":
          Item it3 = (Item)objects.get(cmd[1]);
          try {
              activeCharacter.dropItem(it3);
              writeLine("A " + activeCharacter.getName() + " sikeresen eldobta a " + it3.getName() + " targyat.");
              gameView.updateView(activeCharacter, turnsLeft);
          } catch(Exception e) {
              writeLine("A jatekos nem tudta eldobni a " + it3.getName() + " targyat.");
          }
          break;
        case "tick":
          int time = Integer.parseInt(cmd[1]);
          tick(time);
          break;
        case "endTurn":
          isTurnOver = true;
          break;
        case "ok":
          // literally does everything lmao
          break;
        case "newGame":
          newGame();
          break;
        case "exit":
          System.exit(0);
          break;
        case "win":
             studentsWon();
            break;
        case "lose":
             teachersWon();
            break;
        case "newDemo":
            newDemo();
            break;
        default:
          System.out.println("Rossz parancs!");
      }

        if (isTurnOver && !gameOver) {
            int currentIndex = students.indexOf(activeCharacter);
            int nextIndex = (++currentIndex) % students.size();
            activeCharacter = students.get(nextIndex);

            if (nextIndex == 0) {
                turn();
                if (!gameOver && activeCharacter.getCurrentRoom() == null) activeCharacter = students.get(0);
            }

            if (activeCharacter.getFrozen() == 0) isTurnOver = false;
        }

        if (gameView != null) {
            gameView.updateView(activeCharacter, turnsLeft);
            if (activeCharacter.getFrozen() > 0 && activeCharacter.getCurrentRoom() != null) gameView.frozenPopUp();
        }
    }



    /**
     * a paraméterként megadott fájlban lévő parancsokat sorban lefuttatja.
     * @param file - a fájl, amiben a parancsok vannak
     */
    public static void load(String file) {
        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                commandCalls(line);
            }
        } catch (Exception e) {
            writeLine("A betoltes sikertelen volt.");
        }
    }

    /**
     * Az aktív karakter beállítása
     * @param name Az aktív karakter neve
     */
    public static void setCharacter(String name){
      activeCharacter = (Character)objects.get(name);
      if(activeCharacter.getName().equals(name)) {
        writeLine("A " + name + " az aktiv karakter.");
      } else {
        writeLine("Az aktiv karakter beallitasa sikertelen volt.");
      }
    }

    /**
     * Új objektum létrehozása
     * @param type Az objektum típusa
     * @param name Az objektum neve
     */
    public static void newObj(String type, String name) {
        String s = "Letrejott a "+ name +" nevu "+ type +" objektum.";
       switch(type){
              case "Teacher":
                Teacher teacher = new Teacher(tickManager, name);
                teachers.add(teacher);
                objects.put(name, teacher);
                  writeLine(s);
                break;
              case "Cleaner":
                Cleaner cleaner = new Cleaner(tickManager, name);
                cleaners.add(cleaner);
                objects.put(name, cleaner);
                  writeLine(s);
                break;
              case "Student":
                Student student = new Student(tickManager, name);
                students.add(student);
                objects.put(name, student);
                  writeLine(s);
                break;
              case "Room":
                Room room = new Room(tickManager, name);
                objects.put(name, room);
                  writeLine(s);
                break;
              case "Building":
                Building building = new Building(name);
                objects.put(name, building);
                  writeLine(s);
                break;
              case "Door":
                Door door = new Door(name);
                objects.put(name, door);
                  writeLine(s);
                break;
              case "Camembert":
                Camembert camembert = new Camembert(name);
                objects.put(name, camembert);
                  writeLine(s);
                break;
              case "Transistor":
                Transistor transistor = new Transistor(name);
                objects.put(name, transistor);
                  writeLine(s);
                break;
              case "Mask":
                Mask mask = new Mask(name);
                objects.put(name, mask);
                  writeLine(s);
                break;
              case "Mug":
                Mug mug = new Mug(tickManager, name);
                objects.put(name, mug);
                  writeLine(s);
                break;
              case "Ruler":
                Ruler ruler = new Ruler(name, studentsWonPopUp);
                objects.put(name, ruler);
                  writeLine(s);
                break;
              case "Sponge":
                Sponge sponge = new Sponge(tickManager, name);
                objects.put(name, sponge);
                  writeLine(s);
                break;
              case "TVSZ":
                TVSZ tvsz = new TVSZ(name);
                objects.put(name, tvsz);
                  writeLine(s);
                break;
              case "FakeMask":
                FakeMask fakeMask = new FakeMask(name);
                objects.put(name, fakeMask);
                  writeLine(s);
                break;
              case "FakeRuler":
                FakeRuler fakeRuler = new FakeRuler(name, studentsWonPopUp);
                objects.put(name, fakeRuler);
                  writeLine(s);
                break;
              case "FakeTVSZ":
                FakeTVSZ fakeTVSZ = new FakeTVSZ(name);
                objects.put(name, fakeTVSZ);
                  writeLine(s);
                break;
              case "Freshener":
                Freshener freshener = new Freshener(name);
                objects.put(name, freshener);
                  writeLine(s);
                break;
              default:
                  writeLine("Nem jott letre uj objektum.");
                  break;

       }
    }

    /**
     * Beállítja az objektum attribútumát a megadott értékre
     * @param objName Az objektum neve
     * @param attrName Az attribútum neve
     * @param value Az érték, amire a keresett attribútumot beállítjuk
     * @param type Az objektum típusa
     */
    public static void setAttribute(String type, String objName, String attrName, String value) {
        Object obj = null;
        for(String n : objects.keySet()){
            if(n.equals(objName)){
                obj = objects.get(n);
                break;
            }
        }
        if(obj == null){
            writeLine("Az objektum valtozatlan maradt.");
            return;
        }
        String s = "A "+ objName +" nevu objektum "+ attrName +" nevu attributuma "+ value +" ertekre allitodott.";
        switch(attrName){
            case "currentRoom":
                ((Character)obj).setCurrentRoom((Room)objects.get(value));
                writeLine(s);
                break;
            case "frozen":
                ((Character)obj).setFrozen(Integer.parseInt(value));
                writeLine(s);
                break;
            case "immunity":
                ((Student)obj).setImmunity(Integer.parseInt(value));
                writeLine(s);
                break;
            case "usage":
                ((Item)obj).setUsage(Integer.parseInt(value));
                writeLine(s);
                break;
            case "capacity":
                ((Room)obj).setCapacity(Integer.parseInt(value));
                writeLine(s);
                break;
            case "characters":
                ((Room)obj).addCharacter((Character)objects.get(value));
                writeLine(s);
                break;
            case "doors":
                ((Room)obj).addDoor((Door)objects.get(value));
                writeLine(s);
                break;
            case "gassed":
                ((Room)obj).setGassed(Boolean.parseBoolean(value));
                writeLine(s);
                break;
            case "cursed":
                ((Room)obj).setCursed(Boolean.parseBoolean(value));
                writeLine(s);
                break;
            case "visitorCounter":
                ((Room)obj).setVisitorCounter(Integer.parseInt(value));
                writeLine(s);
                break;
            case "visibility":
                ((Door)obj).setVisibility(Boolean.parseBoolean(value));
                writeLine(s);
                break;
            case "items":
                if(type.equals("Room")){
                    ((Room)obj).addItem((Item)objects.get(value));
                    writeLine(s);
                }
                if(type.equals("Student")){
                    ((Student)obj).addItem((Item)objects.get(value));
                    writeLine(s);
                }
                if(type.equals("Teacher")){
                    ((Teacher)obj).addItem((Item)objects.get(value));
                    writeLine(s);
                }
                break;
            case "rooms":
                if(type.equals("Building")){
                    ((Building)obj).addRoom((Room)objects.get(value));
                    writeLine(s);
                }
                if(type.equals("Door")){
                    ((Door)obj).addRoom((Room)objects.get(value));
                    writeLine(s);
                }
                break;
            case "pair":
                ((Transistor)obj).setPair((Transistor)objects.get(value));
                writeLine(s);
                break;
            case "usedRoom":
                ((Transistor)obj).setUsedRoom((Room)objects.get(value));
                writeLine(s);
                break;
            default:
                writeLine("Az objektum valtozatlan maradt.");
                break;
        }
    }

    /**
     * Az aktuális játékmenetet lépteti előre a megadott idővel
     * @param time Az idő, amennyivel előre léptetjük a játékmenetet
     */
    public static void tick(int time) {
        int ogtime = time;
        try {
            while (time-- > 0) {
                tickManager.notifyMembers();
            }
        } catch (Exception e) {
            writeLine("Varatlanul megallt az ido mulasa.");
        }
        writeLine("A jatek lepett " + ogtime + " tick-et az idoben.");
    }

    /**
     * Új játék indítása, a játékmező betöltése, a játékosok és tanárok létrehozása. A tanárok és takarítók, valamint a játékosok elhelyezése a játékmezőn. A játék elindítása.
     */
    public static void newGame(){
        load("field_init.txt");
        Building building = (Building) objects.get("building");
        building.setRandomCapacity();

        for(int i = 0; i < students.size() - 2; i++){
            Teacher teacher = new Teacher(tickManager, "teacher" + i);
            teachers.add(teacher);
            objects.put("teacher"+i, teacher);
        }
        if(teachers.isEmpty()){
            Teacher teacher = new Teacher(tickManager, "teacher0");
            teachers.add(teacher);
            objects.put("teacher0", teacher);
        }

            Cleaner cleaner1 = new Cleaner(tickManager, "cleaner1");
            cleaners.add(cleaner1);
            objects.put("cleaner1", cleaner1);
            Room rC1 = building.getRandomRoom();
            cleaner1.setCurrentRoom(rC1);
            rC1.addCharacter(cleaner1);

            Cleaner cleaner2 = new Cleaner(tickManager, "cleaner2");
            cleaners.add(cleaner2);
            objects.put("cleaner2", cleaner2);
            Room rC2 = building.getRandomRoom();
            cleaner2.setCurrentRoom(rC2);
            rC2.addCharacter(cleaner2);

        Room tRoom = building.getRandomRoom();
        for(Teacher t : teachers){
            t.setCurrentRoom(tRoom);
            tRoom.addCharacter(t);
        }

        for(Student s : students){
            while(s.getCurrentRoom() == null) {
                Room r = building.getRandomRoom();
                if (r != tRoom && r != rC1 && r != rC2) {
                    s.setCurrentRoom(r);
                    r.addCharacter(s);
                }
            }
        }

        activeCharacter = students.get(0);
        setSPopUp();
        setTPopUp();
        gameView = new GameView(activeCharacter);
        gameView.setVisible(true);
    }

    private static void newDemo() {
        load("demo_field.txt");
        Building building = (Building) objects.get("building");
        building.setDemoCapacity();

        Teacher teacher = new Teacher(tickManager, "teacher");
        teachers.add(teacher);
        objects.put("teacher", teacher);
        Room tRoom = (Room)objects.get("room3");
        teacher.setCurrentRoom(tRoom);
        tRoom.addCharacter(teacher);

        Cleaner cleaner = new Cleaner(tickManager, "cleaner");
        cleaners.add(cleaner);
        objects.put("cleaner", cleaner);
        Room cRoom = (Room)objects.get("room4");
        cleaner.setCurrentRoom(cRoom);
        cRoom.addCharacter(cleaner);

        for(Student s : students){
            while(s.getCurrentRoom() == null) {
                Room r = (Room)objects.get("room1");
                s.setCurrentRoom(r);
                r.addCharacter(s);
            }
        }
        if(!students.isEmpty()) {
            activeCharacter = students.get(0);
        }
        setSPopUp();
        setTPopUp();
        gameView = new GameView(activeCharacter);
        gameView.setVisible(true);
    }

    /**
     * notifies the users that they've lost the game
     * returns to the main menu
     */
    public static void teachersWon(){
        System.out.println("You've lost the game!");
        gameOver = true;

        gameOver();

    }

    /**
     * notifies the users that they've won the game
     * returns to the main menu
     */
    public static void studentsWon(){
        System.out.println("Congrtatulations, you've won the game!");
        gameOver = true;
        gameOver();

    }

    /**
     * performs the controlling of a cleaner
     * tries to move the cleaner to a neighboring room
     * @param cleaner the cleaner to move
     */
    public static void moveCleaner(Cleaner cleaner){
        Room currentRoom = cleaner.getCurrentRoom();
        if(mainMenu.getDemo()) {
            Door d = (Door)objects.get("d0405");
            cleaner.changeRoom(d);
        }else {
            Door randDoor = currentRoom.getRandomDoor(rand);
            if (randDoor != null) cleaner.changeRoom(randDoor);
        }
    }

    /**
     * performs the controlling of a teacher
     * tries to move the teacher to a neighboring room
     * with a random chance tries to pick an item up with the teacher
     * with a random chance tries to drop a random item with the teacher
     * @param teacher the teacher to control
     */
    public static void moveTeacher(Teacher teacher){
        if(mainMenu.getDemo()){
            return;
        }
        Room currentRoom = teacher.getCurrentRoom();
        Door randDoor = currentRoom.getRandomDoor(rand);
        Item randItem = currentRoom.getRandomItem(rand);
        double chance = rand.nextDouble();

        if (randDoor != null) teacher.changeRoom(randDoor);
        if (randItem != null && chance < 0.1) teacher.pickUpItem(randItem);
        if (chance < 0.1) teacher.dropRandomItem(rand);
    }

    private void printFirstUserInfo() {
        System.out.println("""
                a kovetkezo parancsok kozul valaszthatsz:
                \tpickUpItem <item>
                \tdropItem <item>
                \tuseItem <item>
                \tchangeRoom <door>
                tartsd eszben, hogy a korod addig tart, amig meg nem probalsz hasznalni egy ajtot (changeRoom), vagy nem zarod le (end)
                """);
    }

    private void printCurrentUserInfo(Student student) {
        System.out.print("a jelenlegi informaciok a szobarol:\n" +
                indentString(student.getCurrentRoom().toStringForUser()) +
                "\na jelenlegi informaciok rolad:\n" +
                indentString(student.toString()) +
                "\ninput: "
        );
    }

    private String indentString(String string) {
        return "\t" + String.join("\n\t", string.split("\n"));
    }

    /**
     * receives and performs commands in the name of the active character
     * terminates after the first changeRoom command
     */
    public void studentTurn() {
        if (activeCharacter.getFrozen() > 0) {
            System.out.println(activeCharacter.getName() + " meg " + activeCharacter.getFrozen() + " korig nem mozoghat");
            return;
        }

        String command = "";

        printFirstUserInfo();
        printCurrentUserInfo((Student)activeCharacter);
        while (!(command = sc.nextLine()).startsWith("changeRoom")) {
            if (command.equals("end")) return;
            commandCalls(command);
            if (gameOver) return;
            printCurrentUserInfo((Student)activeCharacter);
        }

        commandCalls(command);
    }

    /**
     * handles the rundown of a turn in the game
     *  ends the game with the teachers winning if there are no more turns or students left
     *  handles the ticks of turn-based objects
     *  performs a turn with each student
     *  performs a turn with each teacher
     *  performs a turn with each cleaner
     *  decrements the turns left
     *  calls itself
     */
    public static void turn() {

        if (turnsLeft <= 0) {
            teachersWonPopUp.setVisible(true);

            return;
        }

        for (Teacher teacher : teachers) {
            if (teacher.getFrozen() == 0) moveTeacher(teacher);
        }

        for (Cleaner cleaner : cleaners) {
            moveCleaner(cleaner);
        }
        
        List<Student> removeStudents = new ArrayList<>();
        for (Student student : students) {
            if (student.getCurrentRoom() == null) {
                removeStudents.add(student);
                gameView.deathPopUp(student.getName());
                System.out.println("a " + student.getName() + " karakter meghalt lmao");
            }
        }
        students.removeAll(removeStudents);

        if (students.isEmpty()) {
            teachersWonPopUp.setVisible(true);
            return;
        }
        
        --turnsLeft;
        if(!mainMenu.getDemo()) {
            ((Building) objects.get("building")).changeMap();
            objects.putAll(((Building) objects.get("building")).getNewRooms());
            objects.putAll(((Building) objects.get("building")).getNewDoors());

            ((Building) objects.get("building")).clearRooms();
            ((Building) objects.get("building")).clearDoors();
        }
        tickManager.notifyMembers();
    }

    /**
     * visszaadja az aktív játékost
     * @return - aktív játékos
     */
    public Character getCharacter() {
        return activeCharacter;
    }



    /**
     * A felületen megjelenő különböző bal egérgomb-nyomásokat kezeli le, meghívja a megfelelő
     * metódusokat.
     */

    public static class GameListener implements ActionListener{
        /**
         * a konstruktorban kapott parancs, amit majd az actionPerformed()
         * függvény használ fel
         */
        private final String command;

        /**
         * konstruktor
         * @param command - parancs
         */
        public GameListener(String command){
            this.command = command;

        }

        /**
         * meghívja a Controller osztály commandCalls(command) függvényét, ami elvégzi a megfelelő
         * metódusok meghívását.
         * @param e the event to be processed
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            commandCalls(command);
        }
    }

    /**
     * a tárgyak eldobásáért felelős osztály
     */
    public static class DropitemListener extends MouseAdapter {
        private final Item item;

        /**
         * konstruktor, amely beállítja az item-et a paraméterként kapott tárgyra
         * @param i - a tárgy
         */
        public DropitemListener(Item i) {
            item = i;
        }

        /**
         * meghívja a tárgyak eldobásáért felelős függvényt, ha a jobb egérgomb volt lenyomva
         * @param e the event to be processed
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getButton() == MouseEvent.BUTTON3){
                try {
                   activeCharacter.dropItem(item);
                   if(!activeCharacter.hasItem(item)) {
                        gameView.updateView(activeCharacter, turnsLeft);
                   }
                } catch(Exception ex) {}
            }
        }
    }

    /**
     * a hallgatók nyertek popup-ot állítja be
     */
    public static void setSPopUp(){
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setPreferredSize(new Dimension(200,80));

        JLabel label = new JLabel("Gratulalok, a Hallgatok nyertek!");
        label.setPreferredSize(new Dimension(200, 30));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label);

        JButton okButton = new JButton("OK");
        GameListener okListener = new GameListener("win");
        okButton.addActionListener(okListener);
        okButton.addActionListener(a -> studentsWonPopUp.dispose());
        okButton.setPreferredSize(new Dimension(55, 30));
        panel.add(okButton);

        studentsWonPopUp.add(panel);
        studentsWonPopUp.pack();
        studentsWonPopUp.setLocationRelativeTo(gameView);

    }

    /**
     * az oktatók nyertek popup-ot állítja be
     */
    public static void setTPopUp(){
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setPreferredSize(new Dimension(200,80));

        JLabel label = new JLabel("Az Oktatok nyertek!");
        label.setPreferredSize(new Dimension(200, 30));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);

        JButton okButton = new JButton("OK");
        GameListener okListener = new GameListener("lose");
        okButton.addActionListener(okListener);
        okButton.addActionListener(a -> teachersWonPopUp.dispose());
        okButton.setPreferredSize(new Dimension(55, 30));
        panel.add(okButton);

        teachersWonPopUp.add(panel);
        teachersWonPopUp.pack();
        teachersWonPopUp.setLocationRelativeTo(gameView);
    }

    /**
     * játék végéért felelős függvény, törli a listákat, és megjeleníti a MainView-t
     */

    public static void gameOver(){
        objects.clear();
        students.clear();
        teachers.clear();
        cleaners.clear();
        gameView.dispatchEvent(new WindowEvent(gameView, WindowEvent.WINDOW_CLOSING));
        gameView.dispose();
        System.exit(0);
    }
}
