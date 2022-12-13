import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.HashMap;

public class Methods
{
    /**********************************************
     ******** Methods to use in your code *********
     **********************************************/
    
    /*
     * Parameters: 
     * int[] p --> player array (x,y,w,h) 
     * int[] w --> wall array (x,y,w,h)
     * int[] v --> player velocity array (x,y)
     * 
     * Returns:
     * - true if player and wall overlap in the next frame
     * - false otherwise
     * 
     * Interpretation:
     * This would mean that there is a collision
     */
    public static boolean collided(int[] p,int[] w,int[] v)
    {
        
        /* PLAYER       pTop
         *           _________
         *           |        |
         * pLeft --> |        | <-- pRight
         *           |________|
         *           
         *            pBottom
         */
        double pLeft = p[0]+v[0]; 
        double pRight = p[0]+p[2]+v[0];
        double pTop = p[1]+v[1]; 
        double pBottom = p[1]+p[3]+v[1];
        
        /* WALL         wTop
         *           _________
         *           |        |
         * wLeft --> |        | <-- wRight
         *           |________|
         *           
         *            wBottom
         */
        double wLeft = w[0]; 
        double wRight = w[0]+w[2];
        double wTop = w[1]; 
        double wBottom = w[1]+w[3];
        
        //check to see if player and wall are overlapping with current velocity added

        if(!(pLeft>wRight || pRight<wLeft) && !(pTop>wBottom || pBottom< wTop))
        {
            return true;
        }
        else
        {
             return false;
        }
    }
    
    //same as the above methods, but with player coordinates and velocity as an array of doubles
    public boolean collided(double[] p,int[] w,double[] v)
    {
        double pLeft = p[0]+v[0]; 
        double pRight = p[0]+p[2]+v[0];
        double pTop = p[1]+v[1]; 
        double pBottom = p[1]+p[3]+v[1];
        double wLeft = w[0]; 
        double wRight = w[0]+w[2];
        double wTop = w[1]; 
        double wBottom = w[1]+w[3];
        
        //check to see if player and wall are overlapping with current velocity added
        if(!(pLeft>wRight || pRight<wLeft) && !(pTop>wBottom || pBottom< wTop))
        {
            return true;
        }
        else
        {
             return false;
        }
    }
     
    /*
     * Parameters: 
     * int[] p --> player array (x,y,w,h) 
     * int[] v --> player velocity array (x,y)
     * Polygon[] walls --> array of walls that are polygons
     * 
     * Returns:
     * - true if the player and a wall overlap in the next frame
     * - false otherwise
     * 
     * Interpretation:
     * This would mean that there is a collision with one of the walls.
     */
    public boolean collided(int[] p, int[] v, Polygon[] walls)
    {
        //boolean col = false;
        int[] px = {p[0]+v[0],p[0]+p[2]+v[0]}; //players 2 x values of rec after vel was added
        int[] py = {p[1]+v[1],p[1]+p[3]+v[1]}; //players 2 y values of rec after vel was added
        for(int object = 0; object < walls.length; object++)
            for(int i = 0;i<2;i++) //both player x values
            {
                for(int j = 0;j<2;j++)//both player y values
                {
                    if(walls[object].contains(px[i],py[j]))
                    {
                        //col = true;
                        return true;
                    }
                }
            }   
        //return col;
        return false;
    }
    
    
    /*
     *  Draws the HUD using the user inputs.
     */
    public static void HUD(Graphics g, int[] mouse, HashMap<String, Boolean> inputs)
    {
        g.setColor(new Color(40,40,40));
        g.fillRect(0,500,500,125);
        g.setColor(Color.GRAY);
        g.fillRect(50,520,20,20);
        g.fillRect(50,560,20,20);
        g.fillRect(30,540,20,20);
        g.fillRect(70,540,20,20);
        g.fillRect(30,585,60,10);
        g.fillOval(110,580,15,15);
        g.fillOval(110,530,15,15);
        g.fillOval(130,530,15,15);
        g.setColor(Color.YELLOW);
        if(inputs.get("up"))
            g.fillRect(50,520,20,20);
        if(inputs.get("down"))
            g.fillRect(50,560,20,20);
        if(inputs.get("left"))
            g.fillRect(30,540,20,20);
        if(inputs.get("right"))
            g.fillRect(70,540,20,20);
        if(inputs.get("space"))
            g.fillRect(30,585,60,10);
        if(inputs.get("J"))
            g.fillOval(110,530,15,15);
        if(inputs.get("K"))
            g.fillOval(130,530,15,15);
        if(inputs.get("mouse"))
            g.fillOval(110,580,15,15);
        String display1 = "DIRECTIONS:";
        String display2 = "up,down,left,and right = move square";
        String display3 = "space bar = change color of square";
        String display4 = "X:" + mouse[0] + " Y:" + mouse[1];
        g.drawString(display1, 250, 540);
        g.drawString(display2, 250, 560);
        g.drawString(display3, 250, 580);
        g.drawString(display4, 135, 593); 
        g.drawString("J", 117, 525); 
        g.drawString("K", 134, 525); 
    }
}
