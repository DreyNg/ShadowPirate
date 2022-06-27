import bagel.*;
import bagel.util.Colour;
import bagel.util.Point;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Skeleton Code for SWEN20003 Project 1, Semester 1, 2022
 *
 * Please filling your name below
 * @HungLongNguyen
 */
public class ShadowPirate extends AbstractGame {
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static String GAME_TITLE = "ShadowPirate";
    private final Image BACKGROUND_IMAGE = new Image("res/background0.png");
    private final Font HEALTH_POINT = new Font("res/wheaton.otf", 30);
    private final Font MESSAGE = new Font("res/wheaton.otf", 55);

    private final static int MAX_NUM_ENTITIES = 50;
    private final static int INITIALISER = 0;

    private final static int HAS_NOT_STARTED = 0;
    private final static int HAS_STARTED = 1;
    private final static int HAS_WON = 2;
    private final static int HAS_LOST = 3;
    private int gameStatus = HAS_NOT_STARTED;

    private final static Colour GREEN =  new Colour(0, 0.8, 0.2);
    private final static Colour ORANGE = new Colour(0.9, 0.6, 0);
    private final static Colour RED = new Colour(1, 0, 0);

    private final static int RECTANGLE_WIDTH = 40;
    private final static int RECTANGLE_HEIGHT = 40;

    private final static int LADDER_X = 990;
    private final static int LADDER_Y = 630;
    private final static int BORDER_LEFT = 20;
    private final static int BORDER_RIGHT = WINDOW_WIDTH + 20;
    private final static int BORDER_TOP = 40;
    private final static int BORDER_BOTTOM = 690;
    private final static int HEALTH_X = 10;
    private final static int HEALTH_Y = 25;
    private final static int MSG_Y = 402;
    private final static int MSG_Y_NEXT = 402 + 70;
    private final static int HEALTH_THRESHOLD1 = 65;
    private final static int HEALTH_THRESHOLD2 = 35;
    private final static int PERCENTAGE = 100;
    private final static int MIN_HEALTH = 0;

    private final static String WIN_MSG = "CONGRATULATION!";
    private final static String LOSE_MSG = "GAME OVER";
    private final static String START_MSG = "PRESS SPACE TO START";
    private final static String INSTRUCTION_MSG = "USE ARROW KEYS TO FIND LADDER";
    private final static String LOG_MSG = "Block inflicts 10 damage points on Sailor. Sailor's current health: ";
    private final static String DASH = "/";

    private int numberOfBlock = INITIALISER;

    public ShadowPirate() {
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
    }

    /**
     * The entry point for the program.
     */
    private Block[] blocks = new Block[MAX_NUM_ENTITIES];
    private Sailor sailor = new Sailor(INITIALISER,INITIALISER, RECTANGLE_WIDTH, RECTANGLE_HEIGHT);

    public static void main(String[] args) {
        ShadowPirate game = new ShadowPirate();
        game.readCSV("res/level0.csv");
        game.run();
    }

    /**
     * Method used to read file and create objects
     */
    private void readCSV(String fileName){
        String inputLine;
        try{
            BufferedReader br = new BufferedReader(new FileReader(fileName));

            while((inputLine = br.readLine()) != null){
                String[] inputValues = inputLine.split(",");
                if(inputValues[0].equals("Sailor")){
                    this.sailor.relocate(Double.parseDouble(inputValues[1]),
                            Double.parseDouble(inputValues[2]));
                }
                else{
                    blocks[numberOfBlock++] = new Block(Double.parseDouble(inputValues[1]),
                            Double.parseDouble(inputValues[2]),RECTANGLE_WIDTH,RECTANGLE_HEIGHT);
                }
            }
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }


    /**
     * checking player goes beyond border or collide with bock or has the game won
     */
    public void checkSailorBehavior(Sailor sailor, Block[] blocks, Input input){
        if(this.detectWin(sailor)){
            this.gameStatus = HAS_WON;
        }
        this.checkCollision(sailor, blocks, input);
        if(this.checkOutOfBorder(sailor) || sailor.getHealth() <= MIN_HEALTH){
            this.gameStatus = HAS_LOST;

        }
    }

    /**
     * check if player goes beyond boder
     */
    public boolean checkOutOfBorder(Sailor sailor){
        if(sailor.getX() < BORDER_LEFT || sailor.getX() >= BORDER_RIGHT
                || sailor.getY() <= BORDER_TOP || sailor.getY() >= BORDER_BOTTOM){
            return true;
        }
        return false;
    }

    /**
     * Checking if game has won by reaching the ladder
     */
    public boolean detectWin(Sailor sailor){
        if(sailor.getX() >= LADDER_X && sailor.getY() > LADDER_Y){
            return true;
        }
        return false;
    }

    /**
     * Checking if there is a collision
     */
    public void checkCollision(Sailor sailor, Block[] blocks, Input input){
        boolean collided = false;
        //looping each block once
        for(int i = 0; i < numberOfBlock; ++i){

            //check if x of sailor and x or block and y of sailor and y of block collide or not
            if(Math.abs(blocks[i].getX() - sailor.getX()) <= RECTANGLE_WIDTH
                    && Math.abs(blocks[i].getY() - sailor.getY()) <= RECTANGLE_HEIGHT){
                sailor.getAttacked(blocks[i].Attack());
                collided = true;
                String LOG_MESSAGE = String.format(LOG_MSG + sailor.getHealth() + DASH + sailor.getMaxHealth());
                System.out.println(LOG_MESSAGE);
            }
        }
        //undo the move and create the glich effect
        if(collided){
            sailor.undoMove(input);
        }
    }

    /**
     * Clear screen
     */
    public void clearScreen(){
        BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
    }

    /**
     * Changing the health color to corresponding given color
     */
    public void healthColour(int healthPoint){
        int healthDisplay = (int)Math.round((healthPoint/ (double) sailor.getMaxHealth()) * PERCENTAGE);
        String health = String.valueOf((int)Math.round((healthPoint/ (double) sailor.getMaxHealth()) * PERCENTAGE));
        if(healthDisplay< HEALTH_THRESHOLD1 && healthDisplay > HEALTH_THRESHOLD2) {
            HEALTH_POINT.drawString(health + "%", HEALTH_X, HEALTH_Y, new DrawOptions().setBlendColour(ORANGE));

        }
        else if(healthDisplay < HEALTH_THRESHOLD2){
            HEALTH_POINT.drawString(health + "%", HEALTH_X, HEALTH_Y, new DrawOptions().setBlendColour(RED));

        }
        else{
            HEALTH_POINT.drawString(health + "%", HEALTH_X, HEALTH_Y, new DrawOptions().setBlendColour(GREEN));
        }
    }

    /**
     * Centering the message
     */
    public int getMsgX(String text){
        return (int) ((WINDOW_WIDTH - MESSAGE.getWidth(text))/2);
    }

    /**
     * Performs a state update.
     * allows the game to exit when the escape key is pressed.
     */
    @Override
    public void update(Input input) {
        BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);

        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }

        if(this.gameStatus == HAS_NOT_STARTED){
            MESSAGE.drawString(START_MSG, getMsgX(START_MSG), MSG_Y);
            MESSAGE.drawString(INSTRUCTION_MSG, getMsgX(INSTRUCTION_MSG), MSG_Y_NEXT);
            if(input.wasPressed(Keys.SPACE)){
                this.gameStatus = HAS_STARTED;
            }
        }
        else if(this.gameStatus == HAS_WON){
            this.clearScreen();
            MESSAGE.drawString(WIN_MSG, getMsgX(WIN_MSG), MSG_Y);
        }
        else if(this.gameStatus == HAS_LOST){
            this.clearScreen();
            MESSAGE.drawString(LOSE_MSG, getMsgX(LOSE_MSG), MSG_Y);
        }
        else {
            this.healthColour(sailor.getHealth());

            sailor.update(input);

            for (int i = 0; i < numberOfBlock; i++) {
                blocks[i].initialise();
            }
            this.checkSailorBehavior(sailor, blocks, input);
        }
    }
}
