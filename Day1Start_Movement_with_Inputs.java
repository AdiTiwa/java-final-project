import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent; 
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.SwingUtilities;
import java.util.HashMap;

public class Day1Start_Movement_with_Inputs {
    class DrawPanel extends JPanel {
        //Global Variables go here
        
        //player coordinates and size
        int[] playerPos = {230, 230, 32, 48};
        
        //player's velocity
        int[] playerVel = {0, 0};
        
        //player's speed
        int playerSpeed = 2;
        
        
        //2D array of walls --> each wall has (x, y, w, h)
        int[][] wallsLvl1 = {{0, 0, 500, 100}, // top
                         {0, 100, 100, 400}, // left
                         {400, 100, 100, 400}, // right
                         {200, 400, 500, 100}, // bottom
                         /* {300, 200, 500, 100},
                         {100, 100, 100, 200} */};
        
        int[][] wallsLvl2 = {{0, 0, 200, 100}, {300, 0, 200, 100}, {0, 0, 100, 300}, {0, 400, 500, 100}};
                         
        int[][][] levels = {wallsLvl1, wallsLvl2};
        
        int currentLevel = 0;
                     
                         
        int[][] coins = {{230, 230, 40, 40}};
   
        //booleans for user input
        HashMap<String, Boolean> inputs = new HashMap<>() {{
            put("up", false);
            put("down", false);
            put("right", false);
            put("left", false);
            put("space", false);
            put("mouse", false);
            put("J", false);
            put("K", false);
        }};
        
        //keeps track of mouse coordinates
        int[] mouse = {0,0};
        
        public class Transition {
            int[] transRect;
            int[] reset;
            int levelNum;
            
            public Transition(int[] transitionRect, int levelSwitch, int[] resetPosition) {
                this.transRect = transitionRect;
                this.reset = resetPosition;
                this.levelNum = levelSwitch;
            }
            
            public int[] getRect() {
                return this.transRect;
            }
            
            public int[] getReset() {
                return this.reset;
            }
            
            public int getNextRoom() {
                return this.levelNum;
            }
        }
        
        public class PickUp {
            int[] transRect;
            int pickupNum;
            Polygon poly;
            Color color;
            boolean active;
            
            public PickUp(int[] transitionRect, int pickup, Polygon display, Color col) {
                this.transRect = transitionRect;
                this.pickupNum = pickup;
                this.poly = display;
                this.color = col;
                active = true;
            }
            
            public int[] getRect() {
                return this.transRect;
            } 
            
            public void drawDisplay(Graphics g) {
                if (this.active) {
                    g.setColor(new Color(0, 0, 0));
                    g.drawRect(this.getRect()[0], this.getRect()[1], this.getRect()[2], this.getRect()[3]);
                    
                    g.setColor(this.color);
                    g.fillPolygon(this.poly);
                }
            }
            
            public boolean isActive() {
                return this.active;
            }
            
            public void notActive() {
                this.active = false;
            }
        }
        
        public class Animation {
            Image[] images;
            int totalFrames;
            int currentFrame;
            boolean flipped;
            int delay;
            int currentFrameTimer;
            
            public Animation(String[] animations, int frames, int d) {
                this.images = new Image[frames];
                this.totalFrames = frames;
                this.currentFrame = 1;
                this.flipped = false;
                this.delay = d;
                this.currentFrameTimer = 0;
                
                for (int i = 0; i<frames ; i++) {
                    this.images[i] = Toolkit.getDefaultToolkit().getImage(animations[i]);
                }
            }
            
            public void nextFrame() {
                this.currentFrameTimer++;
                
                if (this.currentFrameTimer > this.delay) {
                    this.currentFrame++;
                    if (currentFrame > totalFrames) { this.currentFrame = 1; }
                    this.currentFrameTimer = 0;
                }
            }
            
            public void drawImage(int[] rect, Graphics g) {
                if (!flipped)
                    g.drawImage(images[currentFrame -1], rect[0], rect[1], rect[2], rect[3], null);
                else
                    g.drawImage(images[currentFrame - 1], rect[0] + rect[2], rect[1], -rect[2], rect[3], null);
            }
            
            public void setFlipped(boolean flip) {
                this.flipped = flip;
            }
        }
        
        public class ProjectileSpawner {
            int rockTimer = 0;
            int rockDelay;
            int[] spawnPosition;
            int[] velocity;
            
            public ProjectileSpawner (int rDelay, int[] position, int[] rockVelocity) {
                this.rockDelay = rockDelay;
                this.spawnPosition = position;
                this.velocity = rockVelocity;
            }
        }

        
        Animation player = new Animation(new String[]{"player0.png", "player1.png", "player2.png", "player3.png"}, 4, 10);
        
        HashMap<Integer, Transition[]> transitions = new HashMap<>() {{
            put(0, new Transition[]{
                new Transition(new int[]{100, 475, 100, 25}, 1, new int[]{250, 30}), // bottom
            });
            put(1, new Transition[]{
                new Transition(new int[]{200, 0, 100, 25}, 0, new int[]{135, 425}),
            });
        }};
        
        HashMap<Integer, PickUp[]> pickups = new HashMap<>() {{
            put(0, new PickUp[]{
                new PickUp(new int[]{225, 225, 50, 50}, 0, new Polygon(new int[]{250, 260, 260, 250}, new int[]{250, 250, 260, 260}, 4), new Color(0, 0, 255)),
            });
            put(1, new PickUp[]{});
        }};
        
        HashMap<String, Integer> inventory = new HashMap<>() {{
            put("Pictures", 0);
        }};
        
        Image[] pictures = new Image[]{
            Toolkit.getDefaultToolkit.getImage(""),
            
        };
        
        
        public void paintComponent(Graphics g) {
            //This code redraw the scene.  Don't change this
            try {
                Thread.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            }
            frame.repaint();
            //This code finds the mouse location and stores it in the mouse array
            Point p = MouseInfo.getPointerInfo().getLocation();
            SwingUtilities.convertPointFromScreen(p, frame);
            mouse[0] = p.x;
            mouse[1] = p.y-30;
            
            
            
            //Your code goes here
            
            
            if (currentLevel >= 0) {
                updatePlayer();
            
                drawPlayer(g);
                drawWalls(g);
                drawPickups(g);
                
                if (inputs.get("K")) {
                    drawInvHUD(g);
                }
            }
            
            //draws the HUD
            Methods.HUD(g, mouse, inputs);
        }
        
        /*
         * Updates the player's position.
         */
        public void updatePlayer() {
            // 1) get user input to change player velocity
            // 2) see if with that velocity, player would hit a wall
            // 3) if so, make player velocity 0 in that direction
            // 4) move player based on new velocity
            
            if (inputs.get("up")) {
                playerVel[1] = -playerSpeed;
            } else if (inputs.get("down")) {
                playerVel[1] = playerSpeed;
            } else {
                playerVel[1] = 0;
            }
            
            if (inputs.get("left")) {
                playerVel[0] = -playerSpeed;
                player.setFlipped(true);
            } else if (inputs.get("right")) {
                playerVel[0] = playerSpeed;
                player.setFlipped(false);
            } else {
                playerVel[0] = 0;
            }
            
            for (int[] wall : levels[currentLevel]) {
                if (Methods.collided(playerPos, wall, playerVel)) {
                    playerVel[1] -= playerVel[1];
                    playerVel[0] -= playerVel[0];
                }
            }
            
            for (Transition trans : transitions.get(currentLevel)) {
                if (Methods.collided(playerPos, trans.getRect(), playerVel)) {
                    currentLevel = trans.getNextRoom();
                    playerPos[0] = trans.getReset()[0];
                    playerPos[1] = trans.getReset()[1];
                }
            }
            
            for (PickUp pickup : pickups.get(currentLevel)) {
                if (Methods.collided(playerPos, pickup.getRect(), playerVel) && pickup.isActive() && inputs.get("J")) {
                    pickup.notActive();
                }
            }
            
            playerPos[1] += playerVel[1];
            playerPos[0] += playerVel[0];
            
            player.nextFrame();

        }
        
        /*
         * Draws the player.
         */
        public void drawPlayer(Graphics g) {
            player.drawImage(playerPos, g);
        }
        
        public void drawCoins (Graphics g) {
            g.setColor(Color.YELLOW);
            for (int[] coin : coins) {
                g.fillRect(coin[0], coin[1], coin[2], coin[3]);
            }
        }
        
        public void drawPickups (Graphics g) {
            for (PickUp pickup : pickups.get(currentLevel)) {
                pickup.drawDisplay(g);
            }
        }
        
        /*
         * Draws all of the walls.
         */
        public void drawWalls(Graphics g) {
            g.setColor(new Color(0, 0, 0));
            for (int i = 0; i < levels[currentLevel].length; i++) {
                g.fillRect(levels[currentLevel][i][0], levels[currentLevel][i][1], levels[currentLevel][i][2], levels[currentLevel][i][3]);
            }
        }
        
        public void drawInvHUD (Graphics g) {
            g.setColor(new Color(50, 50, 50));
            g.fillRect(100, 175, 300, 250);
            
            for (int i = 0; i < inventory.get("pictures"); i++) {
                
            }
        }
        
        /*********************************************************
         ********* DO NOT TOUCH ANYTHING BELOW HERE!!! ***********
         *********************************************************/
 
        public DrawPanel() {
            KeyListener listener = new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    //System.out.println("keyPressed=" + KeyEvent.getKeyText(e.getKeyCode()));
                    int key = e.getKeyCode();
                    
                    if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W ) {
                        inputs.put("up", true);
                        inputs.put("down", false);
                    } else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
                        inputs.put("up", false);
                        inputs.put("down", true);
                    }
                    if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
                        inputs.put("right", true);
                        inputs.put("left", false);
                    } else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
                        inputs.put("right", false);
                        inputs.put("left", true);
                    }
                    if (key == KeyEvent.VK_J) {
                        inputs.put("J", true);
                    }
                    if (key == KeyEvent.VK_K) {
                        inputs.put("K", true);
                    }
                    if (key == KeyEvent.VK_SPACE) {
                        inputs.put("space", true);
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    int key = e.getKeyCode();
                    
                    if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
                        inputs.put("up", false);
                    } else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
                        inputs.put("down", false);
                    }
                    if (key == KeyEvent.VK_RIGHT|| key == KeyEvent.VK_D) {
                        inputs.put("right", false);
                    } else if (key == KeyEvent.VK_LEFT|| key == KeyEvent.VK_A) {
                        inputs.put("left", false);
                    }
                    if (key == KeyEvent.VK_J) {
                        inputs.put("J", false);
                    }
                    if (key == KeyEvent.VK_K) {
                        inputs.put("K", false);
                    }
                    if (key == KeyEvent.VK_SPACE) {
                        inputs.put("space", false);
                    }
                }
            };
            
            addKeyListener(listener);
            setFocusable(true);            
            
            MouseListener mListener = new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {}

                @Override
                public void mousePressed(MouseEvent e) {
                     inputs.put("mouse", true);
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                     inputs.put("mouse", false);
                }
                
                @Override
                public void mouseEntered(MouseEvent e) {}
                
                @Override
                public void mouseExited(MouseEvent e) {}
            };
            addMouseListener(mListener);
            setFocusable(true);
        }
    }
    
    JFrame frame;
    DrawPanel drawPanel;
    public static void main(String... args) {
        new Day1Start_Movement_with_Inputs().go();
    }
    private void go() {
        frame = new JFrame("Final Project");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(500, 640));
        frame.setBackground(new Color(255, 255, 255));
        frame.pack();
        drawPanel = new DrawPanel();
        frame.getContentPane().add(BorderLayout.CENTER, drawPanel);
        frame.setResizable(true);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }
}
