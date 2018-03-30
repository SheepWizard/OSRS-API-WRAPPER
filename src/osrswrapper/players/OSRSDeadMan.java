
package osrswrapper.players;

import java.io.IOException;


public class OSRSDeadMan extends OSRSUser
{
    
    private static final String url = "http://services.runescape.com/m=hiscore_oldschool_deadman/index_lite.ws?player=";
    
    public OSRSDeadMan(String userName) throws IOException
    {
        super(userName, url);
    }
}
