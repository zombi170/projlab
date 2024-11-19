package game;



import java.util.*;

/**
 * Az Épületet megvalósító osztály. A szobákat kezeli és tartja nyilván. Az épület képes a szobák
 * összevonására és szétválasztására.
 */
public class Building {
    /**
     * a példány neve, teszteléshez
     */
    public String name;

    /**
     * A szobák listája
     */
    private final List<Room> rooms;
    private final Map<String, Room> newRooms = new HashMap<>();
    private final Map<String, Door> newDoors = new HashMap<>();
    private int index = 1;

    /**
     * konstruktor, amely inicializálja a rooms listát,
     * beállítja a tickManager attribútmnak az értékét tm-re,
     * valamint beállítja az osztály name attribútumát a megadott String-re.
     */
    public Building(String n) {
        name = n;
        rooms = new ArrayList<>();
    }

    /**
     * visszaadja az osztály elnevezését
     * @return name - az osztály elnevezése
     */
    public String getName() {
        return name;
    }

    /**
     * kiírja az osztály attribútumainak értékét tesztelés érdekében
     * @return a kiírandó szöveg
     */
    @Override
    public String toString() {
        StringBuilder roomNames = new StringBuilder();
        for (Room r : rooms) {
            roomNames.append(r.getName());
            if (rooms.indexOf(r) != rooms.size() - 1) {
                roomNames.append(", ");
            }
        }
        return name + ":\n\trooms: " + roomNames;
    }

    /**
     * kis valószínűséggel meghívja a mergeRoom és a splitRoom függvényt véletlenszerűen kiválasztott szobákra
     */
    public void changeMap() {
        Random random = new Random();
        int randomNumber = random.nextInt(10);
        if (randomNumber < 3) {
            int index1 = random.nextInt(rooms.size());
            int index2 = random.nextInt(rooms.size());

            if (index1 != index2) {
                Room room1 = rooms.get(index1);
                Room room2 = rooms.get(index2);

                if(room1.areNeighbours(room2))
                    mergeRooms(room1, room2);
            }

        } else if(randomNumber < 4) {
            splitRoom(rooms.get(random.nextInt(rooms.size())));
        }
    }

    /**
     * létrehoz egy ajtót a paraméterekként megadott két szoba között
     * @param r1 szoba, amit összekötünk egy másik szobával (r2)
     * @param r2 szoba, amit összekötünk egy másik szobával (r1)
     */
    public void doorBetween(Room r1, Room r2) {
        Door door = new Door("door" + index++);
        door.addRoom(r1);
        door.addRoom(r2);
        r1.addDoor(door);
        r2.addDoor(door);
    }

    /**
     * Ha egyik szobában sincsen karakter, összevonja a paraméterként megadott két szobát.
     * Kapacitásuk a nagyobbik kapacitású szobáéval lesz megegyező, és a tulajdonságaik közösek lesznek
     * @param room1 szoba, amivel összevonjuk a room2 szobát
     * @param room2 szoba, amivel összevonjuk a room1 szobát
     */
    public void mergeRooms(Room room1, Room room2) {
        if (room1.getNumberOfCharacters() == 0 && room2.getNumberOfCharacters() == 0) {
            room1.update(room2);
            rooms.remove(room2);
            for(Door d : room1.getDoors()) {
                newDoors.put(d.getName(), d);
            }
            newRooms.put(room1.getName(), room1);
        }
    }

    /**
     * Ha a szobában nincsenek karakterek, kettéválasztja azt,
     * így a szobából két külön szoba lesz, amik osztoznak az eredeti szoba tulajdonságain.
     * A korábbi szomszédok vagy csak az egyik, vagy csak a másik “új” szobának lesznek a szomszédai.
     * @param room a szoba amit kettéválasztunk
     */
    public void splitRoom(Room room) {
        if(room.getNumberOfCharacters() == 0) {
            Room newRoom = room.splitAttributes();
            doorBetween(room, newRoom);
            rooms.add(newRoom);
            newRooms.put(newRoom.getName(), newRoom);
            newRooms.put(room.getName(), room);
            for(Door d : newRoom.getDoors()) {
                newDoors.put(d.getName(), d);
            }
            for(Door d : room.getDoors()) {
                newDoors.put(d.getName(), d);
            }
        }
    }

    public void clearRooms() {
        newRooms.clear();
    }

    public void clearDoors() {
        newDoors.clear();
    }

    /**
     * hozzáad egy szobát a rooms listához
     * @param room a hozzáadandó szoba
     */
    public void addRoom(Room room) {
        rooms.add(room);
    }

    public Map<String, Room> getNewRooms() {
        return newRooms;
    }

    public Map<String, Door> getNewDoors() {
        return newDoors;
    }

    /**
     * visszaad egy random szobát a rooms listából
     * @return a rooms listából egy random szoba
     */
    public Room getRandomRoom() {
        Random random = new Random();
        return rooms.get(random.nextInt(rooms.size()));
    }

    /**
     * beállítja a szobák kapacitását random számokra
     */
    public void setRandomCapacity(){
        for(Room r : rooms){
            r.setCapacity(new Random().nextInt(10) + 1);
        }
    }

    public void  setDemoCapacity() {
        for(Room r : rooms){
            r.setCapacity(4);
        }
    }
}
