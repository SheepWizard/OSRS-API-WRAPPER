
package osrswrapper.players;

import java.io.IOException;


public class OSRSPlayer extends OSRSUser
{
    private static final String url = "http://services.runescape.com/m=hiscore_oldschool/index_lite.ws?player=";
    
    public OSRSPlayer(String userName) throws IOException
    {
        super(userName, url);
    }
}
