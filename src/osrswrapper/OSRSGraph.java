package osrswrapper;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import osrswrapper.players.OSRSUser;


public class OSRSGraph
{
    private BufferedImage graph;
    private List<OSRSUser> users = new ArrayList<>();
    
    public OSRSGraph()
    {
        
    }
    
    public boolean addPlayer(OSRSUser player)
    {
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
        
        int xBarSize = 640;
        //y bar
        g.setStroke(new BasicStroke(9));
        
        g.setColor(Color.BLACK);
        g.drawLine(60, 10, 60, 650);
        Font myFont = new Font("Serif", Font.PLAIN, 15);
        g.setFont(myFont);
        
        //y numbers
        g.setStroke(new BasicStroke(3));
        int yPos = 650;
        float offset = (float) 640/ (100/5);
        for(int i=0; i<100; i+=5)
        {
            g.drawString(Integer.toString(i), 35, yPos);
            g.drawLine(50, yPos, 60, yPos);
            yPos -= offset;
        }
        g.drawString("99", 35, 12);
        g.drawLine(50, 12, 60, 12);
        
        
        
        g.dispose();
        
        File newImage = new File("newImage.png");
        try
        {
            ImageIO.write(graph, "PNG", newImage);
            
        }
        catch(IOException e){}
    }
}
