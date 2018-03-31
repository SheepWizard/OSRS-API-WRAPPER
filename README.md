# OSRS player API Wrapper

This us a simple wrapper for Old School Runescape player API. This allows you to get the level, rank and xp from a players skills. This includes skills from all gametypes such as deadman, ironman etc...

You can also create a graph with up to 9 players showing skills.

# Getting player skills
First create a OSRSUser object
```java
OSRSUser p1;
```

Then assign the dynamic type for which gamemode you want to get the skills for and give the players user name as the parameter. You will need to use try/catch with IOException. This will create an error if a player is not found.
```java
try
{
    p1 = new OSRSPlayer("sheepwizard2");
}catch(IOException e)
{
    System.out.println("Player not found");
    return;
}
```
All types
```java
new OSRSPlayer("sheepwizard2");
new OSRSDeadMan("sheepwizard2");
new OSRSHardCoreIronMan("sheepwizard2");
new OSRSIronMan("sheepwizard2");
new OSRSSeasonal("sheepwizard2");
new OSRSTournament("sheepwizard2");
new OSRSUltimateIronMan("sheepwizard2");
```

To get information about a specific skill you can get a OSRSStat object from the player with the skill as the parameter.

```java
//returns a OSRSStat object. Set the paramater as the skill you want to get.
p1.getSkill("magic");
//Information you can get from the OSRSStat object. Each returns an int.
p1.getSkill("magic").getLevel();
p1.getSkill("magic").getRank();
p1.getSkill("magic").getXp();
```
You can also get a hashmap of all the player skills, and a list of all skills available.
```java
//returns hashmap
HashMap<String, OSRSStat> stats = p1.getAllSkills();
//get string array of all skills
String[] skillNames = p1.getSkillNames();
```

# Getting users Minigame scores
Create the OSRSUser object as before.
Then you can get the OSRSMinigame object
```java
//returns OSRSMinigame object
p1.getMinigame("easyclue");
//gets player rank from the object
p1.getMinigame("easyclue").getRank();
//gets player score from the object
p1.getMinigame("lms").getScore();
```
You can also get a hashmap off all the minigames and a list of all minigame names
```java
//returns a hashmap of OSRSMinigame
HashMap<String, OSRSMinigame> miniGames = p1.getAllMinigames();
//returns string array of all mingame names
String[] miniGameNames = p1.getMinigameNames();
```
# How to create a graph
Firstly create a OSRSGraph object
```java
OSRSGraph x = new OSRSGraph();
```

Then add a OSRSUser object to the graph.
```java
//This will return false if OSRSUser object does not contain a valid user or too many players have been added
x.addPlayer(p1);
x.addPlayer(p2);
```
Then create the graph once you have added all the player (9 players max)
This will return a BufferedImage.
If you have not added any players to the graph it will return null.
```java
BufferedImage graph = x.drawGraph(); 
```
Now you just need to save the image as a .PNG
```java
File newImage = new File("newImage.png");
try
{
    ImageIO.write(graph, "PNG", newImage);
    System.out.println("graph created");
}
catch(IOException e){}
```
