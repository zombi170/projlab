# Running Guide

## Compilation
The project needs to be run under JDK 20. 
First extract the files from the downloaded zip archive into a folder. 
Then, open a command prompt and navigate to the "src" folder within the directory where the files were extracted. 
After that, you can compile the project using the following command:

```javac @compile.txt```

## Running
The program can be run via the command prompt. 
First, navigate to the folder where the program was compiled using the command prompt. 
After compilation, you can run the program with the following command:

```java game.Main```

## Specification
In the Logarléc game, players control the students and play against the computer, which represents the teachers. 
The teachers' goal is simple: to prevent the students from finding the slide rule in time. 
They have several ways to hinder the students: they can take objects away from them, slowing them down, and they can even eliminate them from the game.

The students' task is more complex. 
They must navigate through the maze room by room, avoiding the teachers while also paying attention to the obstacles on the map. 
These obstacles might include room types such as a gas chamber, cursed room, one-way doors, or rooms merging and splitting. 
Additionally, the students have a limited time to find the slide rule.

However, they can be assisted by various dropped objects, which they can store in their bags and use at the right time.

The game ends and the teachers win if all players die or if the students fail to find the slide rule in time. 
The students can only win if they find the slide rule before the time runs out.