
package osrswrapper;

import osrswrapper.players.*;
import java.io.IOException;


public class OsrsWrapper {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        OSRSUser x;
        try
        {
            x = new OSRSSeasonal("sheepwizard2");
        }
        catch(IOException e)
        {
            System.out.println("Player not found");
            return;
        }
        
        System.out.println(x.getSkill("magic").getLevel());

    }
    
}
