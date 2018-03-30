
package osrswrapper.players;


public class OSRSMinigame 
{
    private final String name;
    private final int rank;
    private final int score;
    
    public OSRSMinigame(String name, int rank, int score)
    {
        this.name = name;
        this.rank = rank;
        this.score = score;
    }
    
    public int getRank()
    {
        return this.rank;
    }
    
    public int getScore()
    {
        return this.score;
    }
    
    public String getName()
    {
        return this.name;
    }
}
