/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package osrswrapper.players;

import java.io.IOException;


public class OSRSTournament extends OSRSUser
{
    
    private static final String url = "http://services.runescape.com/m=hiscore_oldschool_tournament/index_lite.ws?player=";
    
    public OSRSTournament(String userName) throws IOException
    {
        super(userName, url, "Tournament");
    }
}
