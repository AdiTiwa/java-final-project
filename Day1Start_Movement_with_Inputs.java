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
        
        int currentLevel = -1;
                     
                         
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
        
        boolean inDiologue = false;
        boolean inInventory = true;
        
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
        
        public class NPC {
            String[] diologue;
            String name;
            Animation NPCanimation;
            int currentDiologue;
            int[] position;
            int interactionTimer = 0;
            boolean active;
            
            public NPC (Animation NPC, String n, int[] pos, String[] dlque) {
                this.diologue = dlque;
                this.name = n;
                this.position = pos;
                this.NPCanimation = NPC;
                this.active = true;
                
            }
            
            public boolean questbegin() {
                return false;
            }
            
            public void renderNPC(Graphics g) {
                this.NPCanimation.drawImage(this.position, g);
                this.NPCanimation.nextFrame();
            }
            
            public String getCurrentDiologue() {
                if (currentLine > this.getMaxNPCDiologue() - 1) {
                    inDiologue = false;
                    currentNPC = null;
                    currentLine = 0;
                }
                
                return this.diologue[currentLine];
            }
            
            public String getNPCName() {
                return this.name;
            }
            
            public int getMaxNPCDiologue() {
                return diologue.length;
            }
            
            public void updateNPC() {
                if (Methods.collided(playerPos, this.position, playerVel) && this.active) {
                    playerVel = new int[]{-1 * playerVel[0], -1 * playerVel[1]};
                    
                    inDiologue = true;
                    currentNPC = this;
                }
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
        
        HashMap<Integer, NPC[]> npcs = new HashMap<>() {{
            put(0, new NPC[] {});
            put(1, new NPC[] {
                new NPC(new Animation (new String[]{"npc olivia1.png", "npc olivia2.png"}, 2, 20), "Person", new int[]{250, 250, 32, 48}, new String[] {
                    "Hello world!",
                    "Say Hi!",
                }),
            });
        }};
        
        int currentLine = 0;
        NPC currentNPC;
        
        HashMap<String, Integer> inventory = new HashMap<>() {{
            put("Pictures", 0);
        }};
        
        Image[] pictures = new Image[]{
            Toolkit.getDefaultToolkit().getImage("picture frame.png"),
            Toolkit.getDefaultToolkit().getImage("picture2.png"),
            Toolkit.getDefaultToolkit().getImage("picture 3.png"),
            Toolkit.getDefaultToolkit().getImage("picture 4.png"),
            Toolkit.getDefaultToolkit().getImage("picture 5.png"),
        };
        
        int currentNPCtime = 0;
        
        
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
            
            
            if (currentLevel < 0) {
                g.setColor(new Color(0, 0, 0));
                g.fillRect(0, 0, 500, 500);
                g.setFont(new Font("Dialog", Font.PLAIN, 32));
                g.setColor(new Color(100, 100, 100));
                g.drawString("poenitire", 100, 100);
                g.drawString("press space to start", 100, 400); 
                
                if (inputs.get("space")) {
                    currentLevel = 0;
                }
            }
            
            if (currentLevel >= 0) {
                updatePlayer();
            
                drawPlayer(g);
                drawWalls(g);
                drawPickups(g);
                
                for (NPC npc : npcs.get(currentLevel)) {
                    npc.updateNPC();
                    npc.renderNPC(g);
                }
                
                if (inputs.get("K")) {
                    inInventory = true;
                    drawInvHUD(g);
                }
                
                if (inInventory && !inputs.get("K")) {
                    inInventory = false;
                }
                
                if (inDiologue) {
                    drawDiologue(g);
                }
            }
            
            //draws the HUD
            // Methods.HUD(g, mouse, inputs);
        }
        
        /*
         * Updates the player's position.
         */
        public void updatePlayer() {
            // 1) get user input to change player velocity
            // 2) see if with that velocity, player would hit a wall
            // 3) if so, make player velocity 0 in that direction
            // 4) move player based on new velocity
            if (!(inInventory || inDiologue)) {
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
                        inventory.put("Pictures", inventory.get("Pictures") + 1);
                    }
                }
                
                playerPos[1] += playerVel[1];
                playerPos[0] += playerVel[0];
                
                player.nextFrame();
            }
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
            
            
            for (int i = 0; i < inventory.get("Pictures"); i++) {
                g.drawImage(pictures [i], 125 + i * 100, 200, 75, 75, null);
            }
        }
        
        public void drawDiologue (Graphics g) {
            g.setColor(new Color(50, 50, 50));
            g.fillRect(100, 400, 300, 100);
            g.setFont(new Font("Display", Font.PLAIN, 16));
            
            currentNPCtime++;
            boolean canNext = currentNPCtime > 100;
            if (canNext)
                currentNPCtime = 0;
            
            if (inputs.get("J") && canNext) {
                currentLine++;
            }
            
            g.setColor(new Color(200, 200, 200));
            g.drawString(currentNPC.getNPCName(), 125, 415);
            g.drawString(currentNPC.getCurrentDiologue(), 125, 450);
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
        frame.setPreferredSize(new Dimension(500, 515));
        frame.setBackground(new Color(255, 255, 255));
        frame.pack();
        drawPanel = new DrawPanel();
        frame.getContentPane().add(BorderLayout.CENTER, drawPanel);
        frame.setResizable(true);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }
}
