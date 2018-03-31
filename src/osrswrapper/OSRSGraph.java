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
    
    
    /**
     * Create a graph object
     */
    public OSRSGraph()
    {
        
    }
    /**
     * Add a player to be displayed on graph
     * @param player OSRSUser object
     * @return Returns false if too many players have been added of player is not valid. 
     * You can use OSRSUser.getValid() to check if player is valid beforehand.
     */
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
    
    /**
     * Creates a graph of players skills
     * @return A BufferedImage of the graph. Save graph as a .PNG.
     * If BufferedImage is null then you have not added any users.
     */
    public BufferedImage drawGraph()
    {
        if(users.isEmpty())
            return null;
        
        final Font myFont = new Font("Serif", Font.PLAIN, 15);
        final int imageWidth = 1280;
        final int imageHeight = 720;
        
        //x-axis contains list of skills
        //minus 2, we dont want the first or the second skill
        final int xAxisItems = users.get(0).getAllSkills().size()-3;
        //y-axis contains 0 - 99
        final int yAxisItems = 99;
        
        final String[] skillList = users.get(0).getSkillNames();
        
        //the offset of the graph from the start of the image
        final int xOffsetFromImage = 60;
        final int yOffsetFromImage = 10;
        
        //x/y axis bar lengths
        final int xBarLength = 1000;
        final int yBarLength = 600;
        
        //spacing between each item on the axis
        final int xAxisSpacing = xBarLength / xAxisItems;
        final int yAxisSpacing = yBarLength / yAxisItems;
        
        final int yTextPosition = xOffsetFromImage - 35;
        final int xTextPosition = yOffsetFromImage+yBarLength + 35;
        
        final int ovalSize = 4;
        final int ovalOffset = ovalSize/2;
        
        final int userTextOffset = 20;
           
        graph = new BufferedImage(imageWidth, imageHeight,  BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = graph.createGraphics();
        
        //fill rectangle with white
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, graph.getWidth(), graph.getHeight());
        
        //draw axis bars
        g.setStroke(new BasicStroke(2));
        g.setColor(Color.BLACK);
        g.drawLine(xOffsetFromImage, yOffsetFromImage, xOffsetFromImage, yOffsetFromImage + yBarLength);
        g.drawLine(xOffsetFromImage, yOffsetFromImage + yBarLength, xOffsetFromImage + xBarLength,  yOffsetFromImage + yBarLength);

        g.setFont(myFont);
        
        //draw number of yAxis
        for(int i = 0; i<=yAxisItems; i++)
        {
            if(i%5 == 0 || i == 99)
            {
                int ySpacing = (yBarLength + yOffsetFromImage) - yAxisSpacing*i;
                
                g.setColor(new Color(0,0,0,255));
                //draw text
                g.drawString(Integer.toString(i), yTextPosition, ySpacing );
                //draw small line
                g.drawLine(xOffsetFromImage-20, ySpacing, xOffsetFromImage, ySpacing);
                //draw line across graph
                g.setColor(new Color(0,0,0,50));
                g.drawLine(xOffsetFromImage, ySpacing, xOffsetFromImage + xBarLength, ySpacing);
            }
        }
        
        //rotate text
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.rotate(Math.toRadians(45), 0, 0);
        Font font = new Font(null, Font.PLAIN, 15);    
        Font rotatedFont = font.deriveFont(affineTransform);
        g.setFont(rotatedFont);
        
        for(int i=0; i<=xAxisItems; i++)
        {
            int xSpacing = xOffsetFromImage + xAxisSpacing*i;
            
            g.setColor(new Color(0,0,0,255));
            //draw text
            g.drawString(skillList[i+1], xSpacing, xTextPosition);
            //draw small line
            g.drawLine(xSpacing, yOffsetFromImage + yBarLength, xSpacing, yOffsetFromImage + yBarLength + 20);
            //draw line across graph
            g.setColor(new Color(0,0,0,50));
            g.drawLine(xSpacing, yOffsetFromImage + yBarLength, xSpacing, yOffsetFromImage);
        }
        
        g.setFont(myFont);
        //Draw lines on graph
        for(int i = 0; i<users.size(); i++)
        {
            HashMap<String, OSRSStat> userSkills = users.get(i).getAllSkills();
            g.setColor(colours[i]);
            int oldLevel = 0;
            
            for(int z = 0; z<=xAxisItems; z++)
            {
                int level = userSkills.get(skillList[z+1]).getLevel();
                //draw dots
                int xSpacing = xOffsetFromImage + xAxisSpacing*z;
                int ySpacing = (yBarLength + yOffsetFromImage) - yAxisSpacing*level;
                g.drawOval(xSpacing - ovalOffset, ySpacing - ovalOffset, ovalSize, ovalSize);
                //draw lines
                if(z != 0)
                {
                    int x = xOffsetFromImage + xAxisSpacing* (z-1);
                    int y = (yBarLength + yOffsetFromImage) - yAxisSpacing*oldLevel;
                    g.drawLine(xSpacing, ySpacing, x, y);
                }
                oldLevel = level;
            }
            //draw player names
            g.drawString(users.get(i).getUserName() + " (" + users.get(i).getAccountType()+")", xBarLength + xOffsetFromImage + userTextOffset, (i+2)*15);
        }
        
        return graph;
    }

    
    /*
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
*/
}
