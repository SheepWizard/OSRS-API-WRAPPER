
package osrswrapper.players;

public class OSRSStat
{
    private final String name;
    private final int rank;
    private final int level;
    private final int xp;
    
    public OSRSStat(String name, int rank, int level, int xp)
    {
        this.name = name;
        this.rank = rank;
        this.level = level;
        this.xp = xp;
    }
    
    public int getRank()
    {
        return this.rank;
    }
    
    public int getLevel()
    {
        return this.level;
    }
    
    public int getXp()
    {
        return this.xp;
    }
    
    public String getName()
    {
        return this.name;
    }
}
