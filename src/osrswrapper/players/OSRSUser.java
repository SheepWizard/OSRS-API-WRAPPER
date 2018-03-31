
package osrswrapper.players;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

public class OSRSUser
{
    private String userName = "";
    private final HashMap<String, OSRSStat> playerStats = new HashMap<>();
    private final HashMap<String, OSRSMinigame> playerMinigames = new HashMap<>();
    private final String RSURL;
    private boolean valid = false;
    
    private final String[] statNames = new String[]{
        "overall",
        "attack",
        "defence",
        "strength",
        "hitpoints",
        "ranged",
        "prayer",
        "magic",
        "cooking",
        "woodcutting",
        "fletching",
        "fishing",
        "firemaking",
        "crafting",
        "smithing",
        "mining",
        "herblore",
        "agility",
        "thieving",
        "slayer",
        "farming",
        "runecrafting",
        "hunter",
        "construction",
        "combat"
    };
    
    private final String[] minigameNames = new String[]{
        "easyclue",
        "mediumclue",
        "allclue",
        "bountyhunterrouge",
        "bountyhunter",
        "hardclue",
        "lms",
        "eliteclue",
        "masterclue"
    };
    
    /**
     * Create a OSRS player object to access their skills and clue scrolls
     * @param userName Players username
     * @throws IOException Error if player is not found
     */
    public OSRSUser(String userName, String url) throws IOException
    {
        this.userName = userName;
        this.RSURL = url;
        createStats();
        
    }
    
    private void createStats() throws IOException
    {
        URL request;
        URLConnection conn;
        BufferedReader in;
        try
        {
            request = new URL(RSURL+userName.toLowerCase());
        }
        catch(MalformedURLException e)
        {
            System.out.println("URL Malformed");
            return;
        }

        conn = request.openConnection();
        in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        String inputLine;
        int index = 0;
        int minigameIndex = 0;

        while( (inputLine = in.readLine()) != null)
        {
            String[] tkn = inputLine.split(",");  
            //saves rank level xp for skills
            if(index < statNames.length)
            {
                OSRSStat stat;
                if(statNames[index].equals("combat"))
                {
                    int combatlevel = getCombatLevel(playerStats.get("defence").getLevel(), playerStats.get("hitpoints").getLevel(), playerStats.get("prayer").getLevel(), playerStats.get("attack").getLevel(), playerStats.get("strength").getLevel(), playerStats.get("ranged").getLevel(), playerStats.get("magic").getLevel());
                    stat = new OSRSStat(statNames[index], 0, combatlevel, 0);
                }
                else
                {
                    stat = new OSRSStat(statNames[index], Integer.parseInt(tkn[0]), Integer.parseInt(tkn[1]), Integer.parseInt(tkn[2]));
                }
                playerStats.put(statNames[index], stat);
            }

            //clue scrolls
            if(tkn.length == 2)
            {
                OSRSMinigame clue = new OSRSMinigame(minigameNames[minigameIndex], Integer.parseInt(tkn[0]), Integer.parseInt(tkn[1]));
                playerMinigames.put(minigameNames[minigameIndex], clue);
                minigameIndex++;
            }

            index++;
        }
        in.close();
        valid = true;
 
    }
    
    /**
     * Gets the skill object a given skill
     * @param skill The skill you are looking for
     * @return OSRSStat object
     */
    public OSRSStat getSkill(String skill)
    {
        if(playerStats.containsKey(skill.toLowerCase()))
            return playerStats.get(skill.toLowerCase());
        return new OSRSStat("Null", -1, -1, -1);
    }
    
    /**
     * gets the clue object for a given clue scroll type
     * @param clue
     * @return 
     */
    public OSRSMinigame getMinigame(String clue)
    {
        if(playerMinigames.containsKey(clue.toLowerCase()))
            return playerMinigames.get(clue.toLowerCase());
        return new OSRSMinigame("Null", -1, -1);
    }
    
    public HashMap<String, OSRSMinigame> getAllMinigames()
    {
        return playerMinigames;
    }
    
    public HashMap<String, OSRSStat> getAllSkills()
    {
        return playerStats;
    }
    
    public String[] getSkillNames()
    {
        return statNames;
    }
    
    public String[] getMinigameNames()
    {
        return minigameNames;
    }
    
    public void changePlayer(String userName) throws IOException
    {
        valid = false;
        playerStats.clear();
        playerMinigames.clear();
        this.userName = userName;
        createStats();
    }
    
    public boolean getValid()
    {
        return this.valid;
    }
    
    private int getCombatLevel(int defc, int hit, int prayer, int att, int stre, int rang, int mag)
    {
        double base = 0.25 * (defc+hit+Math.floor(prayer/2));
        double melee = 0.325 * (att+stre);
        double range = 0.325 * (Math.floor(rang/2)+rang);
        double mage = 0.325 * (Math.floor(mag/2)+mag);
        
        double top = melee;
        if(range>top)
            top = range;
        if(mage > top)
            top = mage;
        double combatLvl = Math.floor(base+top);
        
        return (int)combatLvl;  
    }
 
}
