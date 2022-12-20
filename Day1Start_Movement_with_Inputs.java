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
                         {200, 400, 500, 100} // bottom
                         /* {300, 200, 500, 100},
                         {100, 100, 100, 200} */};
        
        int[][] wallsLvl2 = {{0, 0, 200, 100}, {300, 0, 200, 100}, {0, 0, 100, 300}, {0, 400, 500, 100}, {300, 240, 80, 20}};
        
        int[][] wallsLvl3 = {{0, 0, 500, 100}, {0, 400, 500, 100}, {400, 0, 100, 400}, {240, 160, 120, 20}, {210, 80, 400 - 210, 20}};
        
        int[][] wallsLvl4 = {{0, 0, 500, 100}, {0, 0, 100, 300}, {0, 400, 500, 100}, {400, 200, 100, 300}};

        int[][] wallsLvl5 = {{0, 0, 500, 100}, {0, 0, 100, 500}, {0, 400, 500, 100}, {400, 200, 100, 300}};

        int[][] wallsLvl6 = { { 0, 0, 500, 100 }, { 0, 0, 100, 500 }, { 0, 400, 500, 100 }, { 400, 200, 100, 300 } };

        int[][] wallsLvl7 = { { 0, 0, 500, 100 }, { 0, 0, 100, 300 }, { 0, 400, 500, 100 }, { 400, 200, 100, 300 } };

        int[][] wallsLvl8 = { { 0, 0, 500, 100 }, { 0, 400, 500, 100 }, { 400, 0, 100, 400 }, { 240, 160, 120, 20 },
                { 210, 80, 400 - 210, 20 } };

        int[][] wallsLvl9 = { { 0, 0, 200, 100 }, { 300, 0, 200, 100 }, { 0, 0, 100, 300 }, { 0, 400, 500, 100 },};

        int[][] wallsLvl10 = { { 0, 0, 200, 100 }, {300, 0, 200, 100},
                { 0, 100, 100, 400 }, // left
                { 400, 100, 100, 400 }, // right
                { 200, 400, 500, 100 },};
        int[][] wallsLvl11 = { { 0, 0, 200, 100 }, { 300, 0, 200, 100 },
                { 0, 100, 100, 400 }, // left
                { 400, 100, 100, 400 }, // right
                { 200, 400, 500, 100 }, };
        int[][] wallsLvl12 = { { 0, 0, 200, 100 }, { 300, 0, 200, 100 },
                { 0, 100, 100, 400 }, // left
                { 400, 100, 100, 400 }, // right
                { 200, 400, 500, 100 }, };

        int[][] wallsLvl13 = { {0, 0, 500, 100}, {0, 0, 100, 500}, {0, 400, 500, 100}, {400, 0, 100, 500} };
        
        int[][][] levels = {wallsLvl1, wallsLvl2, wallsLvl3, wallsLvl4, wallsLvl5, wallsLvl6, wallsLvl7, wallsLvl8, wallsLvl9, wallsLvl10, wallsLvl11, wallsLvl12, wallsLvl13};
        
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
        boolean inQuiz = false;
        boolean inInventory = false;
        boolean inInternal = false;

        String[] internalDiologue = new String[] {
            "How did I get here?",
            "Who is he?",
            "What is this world?",
            "This isn't real.",
            "THIS ISN'T REAL THIS ISN'T REAL THIS ISN'T REAL THIS ISN'T REAL"
        };

        boolean internalFinished = false;

        int internalDiologueCount = 0;
        
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
            DisplaySprite poly;
            Color color;
            boolean active;
            
            public PickUp(int[] transitionRect, DisplaySprite display, Color col) {
                this.transRect = transitionRect;
                this.poly = display;
                this.color = col;
                active = true;
            }
            
            public int[] getRect() {
                return this.transRect;
            } 
            
            public void drawDisplay(Graphics g) {
                if (this.active) {
                    this.poly.display(g);
                }
            }
            
            public boolean isActive() {
                return this.active;
            }
            
            public void notActive() {
                this.active = false;
            }
            
            public void onPickup() {
            
            }
        }
        
        public class Picture extends PickUp {
            public Picture(int[] transitionRect, DisplaySprite display, Color col) {
                super(transitionRect, display, col);
            }
            
            public void onPickup() {
                inventory.put("Pictures", inventory.get("Pictures") + 1);
            }
        }

        public class FinalPicture extends PickUp {
            public FinalPicture(int[] transitionRect, DisplaySprite display, Color col) {
                super(transitionRect, display, col);
            }

            public void onPickup() {
                currentLevel += 1;
            }
        }
        
        public class ItemPickup extends PickUp {
            String pickup;
            
            public ItemPickup(int[] transitionRect, DisplaySprite display, Color col, String pickup) {
                super(transitionRect, display, col);
                this.pickup = pickup;
            }
            
            public void onPickup() {
                String[] newItems = new String[items.length + 1];
                
                for (int i = 0; i < items.length;i++) {
                    newItems[i] = items[i];
                }
                
                newItems[items.length] = this.pickup;
                
                items = newItems;
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
            Quest quest = null;
            Quiz quiz = null;
            
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
                    if (currentNPC.getNPCName() == "Olivia") {
                        if (quest == null) {
                            this.quest = new Quest("coffee", new String[]{
                                "Oh thank you!",
                                "Did you enjoy the walk there,",
                                " its kinda extreme, but its fun!",
                                "*Sip* This coffee is kinda bitter...",
                                "DID YOU FORGET THE SUGAR!!!!",
                                "I can't trust you with anything...",
                                "Anyway, when you were out,",
                                "I found something that you might like!",
                                "A picture of the two of you before he...",
                                "yeah...",
                                "When are you coming back, we miss you...",
                                "Manu, he wouldn't have wanted this",
                            });
                        }
                    } 

                    if (currentNPC.getNPCName() == "Gloop") {
                        if (quiz == null) {
                            this.quiz = new Quiz("Who's my friend?", new String[] {"Abby", "Yolotli", "Amir"}, 0, new String[] {"GLOOOP", "GLOOP GLOOP", "(Good Job!)"});
                        }
                    }

                    if (currentNPC.getNPCName() == "Nurul") {
                        currentLevel += 1;
                    }
                    
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
                    
                    if (quiz != null) {
                        if (quiz.isSolved()) {
                            inDiologue = true;
                            currentNPC = this;

                            this.diologue = this.quiz.getDStrings();

                            this.quiz = null;

                            if (this.getNPCName() == "Gloop") {
                                this.active = false;
                            }

                            return;
                        }

                        inQuiz = true;
                        currentNPC = this;

                        return;
                    }

                    inDiologue = true;
                    currentNPC = this;
                    
                    if (quest != null) {
                        if (quest.checkQuest()) {
                            this.diologue = quest.getDiologue();
                            
                            if (this.name == "Olivia") {
                                inventory.put("Pictures", inventory.get("Pictures") + 1);
                            }
                        }
                    }
                }
            }

            public Quiz getQuiz() {
                return this.quiz;
            }
        }
        
        public class Quest {
            String questItem;
            String[] extraDiologue;
            boolean completed;
            
            public Quest(String qItem, String[] eD) {
                this.questItem = qItem;
                this.extraDiologue = eD;
                this.completed = false;
            }
            
            public boolean checkQuest() {
                for (String item : items) {
                    if (item.equals(this.questItem)) {
                        return true;
                    }
                }
                return false;
            }
            
            public String[] getDiologue() {
                return this.extraDiologue;
            }

        }
        
        public class DisplaySprite {
            Image image;
            int[] pos;
            
            public DisplaySprite(String filename, int[] position) {
                this.image = Toolkit.getDefaultToolkit().getImage(filename);
                this.pos = position;
            }
            
            public void display(Graphics g) {
                g.drawImage(this.image, this.pos[0], this.pos[1], this.pos[2], this.pos[3], null);
            }
        }
        
        public class Quiz {
            String question;
            String[] answers;
            int answer;
            int currentSelection = 0;
            int triviaIntent = 0;
            int triviaFrameTimer = 0;
            String[] newDiologue;
            boolean solved = false;
            
            public Quiz(String q, String[] choices, int a, String[] nDlque) {
                this.question = q;
                this.answers = choices;
                this.answer = a;
                this.newDiologue = nDlque;
            }

            public void display(Graphics g) {
                g.setColor(new Color(50, 50, 50));
                g.fillRect(100, 100, 300, 300);
                g.setColor(new Color(150, 150, 150));
                g.setFont(new Font("display", Font.PLAIN, 24));
                g.drawString(this.question, 125, 155);
                for (int i = 0; i < this.answers.length; i++) {
                    if (this.currentSelection == i)
                        g.setColor(new Color(225, 255, 0));
                    else
                        g.setColor(new Color(150, 150, 150));
                    g.drawString(this.answers[i], 125, 175 + i * 50);
                }

                if (inputs.get("up"))
                    this.triviaIntent = -1;
                else if (inputs.get("down"))
                    this.triviaIntent = 1;
                

                this.triviaFrameTimer++;

                if (this.triviaFrameTimer > 10) {
                    this.triviaFrameTimer = 0;

                    this.currentSelection += triviaIntent;

                    this.triviaIntent = 0;

                    if (this.currentSelection >= this.answers.length)
                        this.currentSelection = 0;
                    
                    if (this.currentSelection < 0)
                    	this.currentSelection = this.answers.length - 1;
                }

                if (inputs.get("space")) {
                    if (this.currentSelection == this.answer) {
                        inQuiz = false;
                        this.solved = true;
                    }
                }
            }

            public String[] getDStrings() {
                return this.newDiologue;
            }

            public boolean isSolved() {
            	return this.solved;
            }
        }


        
        Animation player = new Animation(new String[]{"player0.png", "player1.png", "player2.png", "player3.png"}, 4, 10);
        
        HashMap<Integer, Transition[]> transitions = new HashMap<>() {{
            put(0, new Transition[]{
                new Transition(new int[]{100, 475, 100, 25}, 1, new int[]{250, 30}), // bottom to 2
            });
            put(1, new Transition[]{
                new Transition(new int[]{200, 0, 100, 25}, 0, new int[]{135, 425}), // top to 1
                new Transition(new int[]{475, 0, 25, 500}, 2, new int[] {50, 250}),
                new Transition(new int[]{0, 300, 25, 100}, 3, new int[] {400, 125}),
            });
            put(2, new Transition[] {
                new Transition(new int[]{0, 0, 25, 500}, 1, new int[] {425, 250})
            });
            put(3, new Transition[] {
                new Transition(new int[]{475, 0, 25, 500}, 1, new int[] {75, 350}),
                new Transition(new int[] { 0, 300, 25, 100 }, 4, new int[] { 400, 125 }),
            });
            put(4, new Transition[] {
                new Transition(new int[] { 475, 0, 25, 500 }, 3, new int[] { 75, 350 }),
            });
            put(5, new Transition[] {
                    new Transition(new int[] { 475, 0, 25, 500 }, 6, new int[] { 75, 350 }),
            });
            put(6, new Transition[] {
                    new Transition(new int[] { 475, 0, 25, 500 }, 8, new int[] { 75, 350 }),
                    new Transition(new int[] { 0, 300, 25, 100 }, 5, new int[] { 400, 125 }),
            });
            put(7, new Transition[] {
                    new Transition(new int[] { 0, 0, 25, 500 }, 8, new int[] { 425, 250 })
            });
            put(8, new Transition[] {
                    new Transition(new int[] { 200, 0, 100, 25 }, 9, new int[] { 135, 425 }), // top to 1
                    new Transition(new int[] { 475, 0, 25, 500 }, 7, new int[] { 50, 250 }),
                    new Transition(new int[] { 0, 300, 25, 100 }, 6, new int[] { 400, 125 }),
            });
            put(9, new Transition[] {
                new Transition(new int[] {0, 0, 500, 25}, 10, new int[] {125, 400}),
            });
            put(10,new Transition[]{new Transition(new int[]{0,0,500,25},11,new int[]{125,400}),});
            put(11,new Transition[]{new Transition(new int[]{0,0,500,25},12,new int[]{125,400}),});
            put(12, new Transition[] {});
        }};
        
        HashMap<Integer, PickUp[]> pickups = new HashMap<>() {{
            put(0, new PickUp[]{
                new Picture(new int[]{220, 220, 50, 50}, new DisplaySprite("pickup1.png", new int[]{240, 240, 20, 20}), new Color(0, 0, 255)),
            });
            put(1, new PickUp[]{});
            put(2, new PickUp[] {
                new ItemPickup(new int[]{250, 150, 40, 40}, new DisplaySprite("coffee.png", new int[] {260, 160, 20, 20}), new Color(0, 0, 255), "coffee"),
            });
            put(3, new PickUp[]{});
            put(4, new PickUp[]{
                new Picture(new int[]{100, 225, 120, 40}, new DisplaySprite("pickup1.png", new int[] {100, 225, 20, 20}), new Color(0, 0, 255)),
                new Picture(new int[]{190, 80, 40, 40}, new DisplaySprite("pickup1.png", new int[] {200, 90, 20, 20 }), new Color(0, 0, 255)),
            });
            put(5, new PickUp[] {});
            put(6, new PickUp[] {});
            put(7, new PickUp[] {});
            put(8, new PickUp[] {});
            put(9, new PickUp[] {
                new Picture(new int[] { 215, 215, 40, 40 },
                        new DisplaySprite("pickup1.png", new int[] { 225, 225, 20, 20 }), new Color(0, 0, 255)),
            });
            put(10, new PickUp[] {
                        new Picture(new int[] { 215, 215, 40, 40 },
                                new DisplaySprite("pickup1.png", new int[] { 225, 225, 20, 20 }), new Color(0, 0, 255)),
            });
            put(11, new PickUp[] {
                new FinalPicture(new int[] { 215, 215, 40, 40 },
                        new DisplaySprite("pickup1.png", new int[] { 225, 225, 20, 20 }), new Color(0, 0, 255)),
                    });
            put(12, new PickUp[] {});
        }};
        
        HashMap<Integer, NPC[]> npcs = new HashMap<>() {{
            put(0, new NPC[] {});
            put(1, new NPC[] {
                new NPC(new Animation (new String[]{"npc olivia1.png", "npc olivia2.png"}, 2, 20), "Olivia", new int[]{250, 250, 32, 48}, new String[] {
                    "Hey! How are you Manu!",
                    "You finally woke up, its been a while!",
                    "You look tired too...",
                    "did you get enough sleep???",
                    "I swear if you pulled another all-nighter...",
                    "It looks like you could use a coffee...",
                    "Thinking of it, I could prolly get one too",
                    "Could you get me a coffee?",
                    "It should just be the room over I think.",
                }),
            });
            put(2, new NPC[] {});
            put(3, new NPC[] {
                new NPC(new Animation(new String[]{
                    "goopy1.png",
                    "goopy2.png",
                    "goopy3.png",
                    "goopy4.png",
                    "goopy5.png",
                    "goopy6.png",
                    "goopy7.png",
                }, 7, 5), "Gloop", new int[]{100, 350, 32, 48}, new String[] {
                    "GLOOP",
                    "GLOOP GLOOP GLOOP",
                    "(who's my best friend, ask the NPCs)",
                }),
                new NPC(new Animation(new String[] {"npc abby1.png", "npc abby2.png"}, 2, 20), "Abby", 
                    new int[]{150, 150, 32, 48}, new String[] {
                        "Hey ho traveler!",
                        "People think they are Gloop's friend",
                        "But I can tell you",
                        "I definetely am not!",
                    }),
                new NPC(new Animation(new String[] { "npc yolotli1.png", "npc yolotli2.png" }, 2, 20), "Yolotli",
                    new int[] { 250, 150, 32, 48 }, new String[] {
                        "Hello friend!",
                        "I can't tell if gloop's my friend...",
                        "All I know that 1 of us is telling the truth!",
                    }),
                new NPC(new Animation(new String[] { "npc amir1.png", "npc amir2.png" }, 2, 20), "Amir",
                    new int[] { 150, 225, 32, 48 }, new String[] {
                        "Dig the hairdo?",
                        "I'm sure you do!",
                        "I think both Abby and Yolotli are good people",
                        "They liked the hair!",
                        "They must be telling the truth...",
                        "Abby probably isn't friends with Gloop..."
                    }),
            });
            put(4, new NPC[] {});
            put(5, new NPC[] {});
            put(6, new NPC[] {
                new NPC(new Animation(new String[] { "npc corrupt_abby.png"}, 1, 20), "Corrupt Abby",
                        new int[] { 150, 150, 32, 48 }, new String[] {
                                "Given up already?",
                                "You thought this worked huh...",
                                "Clearly not,",
                                "YOU'RE WEAK YOU'RE WEAK YOU'RE WEAK YOU'RE WEAK YOU'RE WEAK"
                        }),
            });
            put(7, new NPC[] {});
            put(8, new NPC[] {
                        new NPC(new Animation(new String[] { "npc corrupt_olivia.png" }, 1, 20), "Corrupt Olivia",
                                new int[] { 150, 150, 32, 48 }, new String[] {
                                        "YOU KILLED HIM,",
                                        "YOU KILLED HIM,",
                                        "YOU SAID YOU CARED,",
                                        "YOU KILLED HIM.",
                                }),});
            put(9, new NPC[] {});
            put(10, new NPC[] {});
            put(11, new NPC[] {});
            put(12, new NPC[] {
                new NPC(new Animation(new String[] { "npc nurul1.png", "npc nurul2.png" }, 2, 20), "Nurul", new int[]{150, 150, 32, 48}, new String[] {
                    "Hey Manu...",
                    "Been a while...",
                    "I think we have to check in",
                    "Man I miss you... but this isn't it",
                    "You gotta let go!",
                    "I forgive you... it was OUR mistake",
                    "not just YOURS.",
                    "Anyway, what can we do",
                    "I'm dead now.",
                    "That's it, just let it go.",
                    "You can't take it all on yourself.",
                    "Live like you always wanted to...",
                    "Hell! Like WE wanted to.",
                    "Free.",
                    "From everything.",
                    "See ya buddy...",
                    "Though I hope it won't be soon.",
                })
            });
        }};
        
        Image[] rooms = new Image[] {
                    Toolkit.getDefaultToolkit().getImage("room0.png"),
                    Toolkit.getDefaultToolkit().getImage("room1.png"),
                    Toolkit.getDefaultToolkit().getImage("room2.png"),
                    Toolkit.getDefaultToolkit().getImage("room3.png"),
                    Toolkit.getDefaultToolkit().getImage("room4.png"),
                    Toolkit.getDefaultToolkit().getImage("room5.png"),
                    Toolkit.getDefaultToolkit().getImage("room6.png"),
                    Toolkit.getDefaultToolkit().getImage("room7.png"),
                    Toolkit.getDefaultToolkit().getImage("room8.png"),
                    Toolkit.getDefaultToolkit().getImage("room9.png"),
                    Toolkit.getDefaultToolkit().getImage("room10.png"),
                    Toolkit.getDefaultToolkit().getImage("room11.png"),
                    Toolkit.getDefaultToolkit().getImage("room12.png"),
        };
        
        int currentLine = 0;
        NPC currentNPC;
        
        HashMap<String, Integer> inventory = new HashMap<>() {{
            put("Pictures", 0);
        }};
        
        String[] items = new String[]{};
        
        Image[] pictures = new Image[]{
            Toolkit.getDefaultToolkit().getImage("picture frame.png"),
            Toolkit.getDefaultToolkit().getImage("picture2.png"),
            Toolkit.getDefaultToolkit().getImage("picture 3.png"),
            Toolkit.getDefaultToolkit().getImage("picture 4.png"),
            Toolkit.getDefaultToolkit().getImage("picture 5.png"),
            Toolkit.getDefaultToolkit().getImage("picture 6.png"),
            Toolkit.getDefaultToolkit().getImage("picture 7.png"),
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
            
            if (currentLevel >= 0 && currentLevel <= 12) {
                updatePlayer();
                
                drawWalls(g);
                drawPickups(g);
                drawPlayer(g);
                
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

                if (inQuiz) {
                    currentNPC.getQuiz().display(g);
                }

                if (inInternal) {
                    drawInternalDiologue(g);
                }
            }
            
            if (currentLevel == 13) {
                g.drawImage(pictures[6], 0, 0, 500, 500, null);
                g.setColor(new Color(255, 255, 255));
                g.setFont(new Font("Display", Font.PLAIN, 32));
                g.drawString("press space", 100, 400);

                if (inputs.get("space")) {
                    currentLevel += 1;
                }

            }

            if (currentLevel == 14) {
                g.setColor(new Color(0, 0, 0));
                g.fillRect(0, 0, 500, 500);
                g.setFont(new Font("Dialog", Font.PLAIN, 32));
                g.setColor(new Color(100, 100, 100));
                g.drawString("Furthest Wilds", 100, 100);
                g.drawString("Press Space to Start", 100, 400);
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
            if (inventory.get("Pictures") == 4 && !internalFinished) {
                inInternal = true;
                playerSpeed = 6;
            }

            if (currentLevel == 9) {
                playerSpeed = 1;
            }

            if (!(inInventory || inDiologue || inQuiz || inInternal)) {
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
                        pickup.onPickup();
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
            if (currentLevel < rooms.length) {
                g.drawImage(rooms[currentLevel], 0, 0, 500, 500, null);
            } else {
                for (int[] walls : levels[currentLevel]) {
                    g.setColor(new Color(0, 0, 0));
                    g.fillRect(walls[0], walls[1], walls[2], walls[3]);
                }
            }
        }
        
        public void drawInvHUD (Graphics g) {
            g.setColor(new Color(50, 50, 50));
            g.fillRect(100, 175, 300, 250);
            
            for (int i = 0; i < inventory.get("Pictures"); i++) {
                int col = 0;
                int row = i;

                if (i > 2) {
                    col = 1;
                    row -= 3;
                }

                g.drawImage(pictures[i], 125 + row * 86, 200 + col * 86, 64, 64, null);
            }
        }
        
        public void drawDiologue (Graphics g) {
            g.setColor(new Color(50, 50, 50));
            g.fillRect(100, 400, 300, 100);
            g.setFont(new Font("Display", Font.PLAIN, 16));
            
            currentNPCtime++;
            boolean canNext = currentNPCtime > 25;
            if (canNext)
                currentNPCtime = 0;
            
            if (inputs.get("J") && canNext) {
                currentLine++;
            }
            
            g.setColor(new Color(200, 200, 200));
            g.drawString(currentNPC.getNPCName(), 125, 415);
            g.drawString(currentNPC.getCurrentDiologue(), 125, 450);
        }

        public void drawInternalDiologue(Graphics g) {
            g.setColor(new Color(50, 50, 50));
            g.fillRect(100, 400, 300, 100);
            g.setFont(new Font("Display", Font.PLAIN, 16));

            currentNPCtime++;
            boolean canNext = currentNPCtime > 25;
            if (canNext)
                currentNPCtime = 0;

            if (inputs.get("J") && canNext) {
                internalDiologueCount++;
                if (internalDiologueCount > 4) {
                    currentLevel = 5;
                    inInternal = false;
                    internalFinished = true;
                    return;
                }
            }

            g.setColor(new Color(200, 200, 200));
            g.drawString("Self", 125, 415);
            g.drawString(internalDiologue[internalDiologueCount], 125, 450);
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
        frame = new JFrame("Furthest Wilds");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(500, 615));
        frame.setBackground(new Color(255, 255, 255));
        frame.pack();
        drawPanel = new DrawPanel();
        frame.getContentPane().add(BorderLayout.CENTER, drawPanel);
        frame.setResizable(true);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }
}
