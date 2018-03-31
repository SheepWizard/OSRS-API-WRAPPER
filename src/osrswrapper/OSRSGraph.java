package osrswrapper;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.imageio.ImageIO;
import osrswrapper.players.OSRSStat;
import osrswrapper.players.OSRSUser;


public class OSRSGraph
{
    private BufferedImage graph;
    private final List<OSRSUser> users = new ArrayList<>();
    private final Color[] colours = new Color[]{
        Color.BLUE,
        Color.RED,
        Color.GREEN,
        Color.PINK,
        Color.YELLOW,
        Color.ORANGE,
        Color.MAGENTA,
        Color.CYAN,
        Color.GRAY,
    };
    public OSRSGraph()
    {
        
    }
    
    public boolean addPlayer(OSRSUser player)
    {
        if(users.size() >= 9)
            return false;
        if(player.getValid())
        {
            users.add(player);
            return true;
        }
        return false;
    }
    
    public void drawGraph()
    {
        if(users.isEmpty())
            return;
        
        graph = new BufferedImage(1280, 720,  BufferedImage.TYPE_INT_ARGB);
        
        
        Graphics2D g = graph.createGraphics();
        
        //draw white background
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, graph.getWidth(), graph.getHeight());
        
        int yBarSize = 640;
        int xBarSize = 1140;
        
        g.setStroke(new BasicStroke(9));
        g.setColor(Color.BLACK);
        
        //y bar
        g.drawLine(60, 10, 60, 650);
        //x bar
        g.drawLine(60, 650, 1200, 650);
        
        
        Font myFont = new Font("Serif", Font.PLAIN, 15);
        g.setFont(myFont);
        
        //y numbers
        g.setStroke(new BasicStroke(3));
        int yPos = 650;
        float yoffset = (float) 640/ (100/5);
        for(int i=0; i<100; i+=5)
        {
            g.drawString(Integer.toString(i), 35, yPos);
            g.drawLine(50, yPos-5, 60, yPos-5);
            yPos -= yoffset;
        }
        g.drawString("99", 35, 12);
        g.drawLine(50, 12, 60, 12);
        
        //x skills
        String[] skills = users.get(0).getSkillNames();
        
        float xoffset = xBarSize / skills.length-1;
        
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.rotate(Math.toRadians(45), 0, 0);
        Font font = new Font(null, Font.PLAIN, 15);    
        Font rotatedFont = font.deriveFont(affineTransform);
        g.setFont(rotatedFont);
        for(int i = 1; i<skills.length; i++)
        {
            g.drawString(skills[i], 65 + (xoffset*i), 670);
            g.drawLine( 75 + (int)(xoffset*i), 660, 75 + (int)(xoffset*i), 650);
        }
        
        int yoffset2 = 640/ 99;
        g.setStroke(new BasicStroke(2));
        g.setFont(new Font(null, Font.PLAIN, 15));
        for(int i = 0; i<users.size(); i++)
        {
            HashMap<String, OSRSStat> userSkills = users.get(i).getAllSkills();
            g.setColor(colours[i]);
            for(int z = 1; z<skills.length; z++)
            {
                int level = userSkills.get(skills[z]).getLevel();
                g.drawOval(73 + (int)(xoffset*z) , 620 - (int)yoffset2*level, 4, 4);
                if(z != 1)
                {
                    int oldlevel = userSkills.get(skills[z-1]).getLevel();
                    g.drawLine(73 + (int)(xoffset*z), 620 - (int)yoffset2*level, 73 + (int)(xoffset*(z-1)), 620 - (int)yoffset2*oldlevel);
                }
            }
            
            g.drawString(users.get(i).getUserName(), 1160, (i+1)*15);
        }
        
        g.dispose();
        
        File newImage = new File("newImage.png");
        try
        {
            ImageIO.write(graph, "PNG", newImage);
            
        }
        catch(IOException e){}
    }
}
