import bagel.util.Rectangle;
import bagel.*;
import bagel.util.Point;

public class Sailor extends Rectangle {
    private Image sailorImage = new Image("res/sailorRight.png");
    private static final double STEP_SIZE = 20;
    private final int MAX_HEALTH = 100;
    private final int DAMAGE_POINT = 25;
    private int healthPoint = MAX_HEALTH;

    /**
     * constructor
     */
    public Sailor(double sailorX, double sailorY, double sailorW, double sailorH){
        super(sailorX, sailorY, sailorW, sailorH);
    }

    /**
     * sailor getting attacked and lose health
     */
    public void getAttacked(int damage){
        this.healthPoint -= damage;
    }
    /**
     * moving sailor to different position
     */
    public void relocate(double sailorX, double sailorY){
        super.moveTo(new Point(sailorX,sailorY));
        sailorImage.draw(sailorX, sailorY);
    }

    /**
     * sailor attack damage
     */
    public int Attack(){
        return this.DAMAGE_POINT;
    }

    /**
     * creating getters
     */
    public double getX(){
        return super.centre().x;
    }

    public double getY(){
        return super.centre().y;
    }

    public int getHealth(){
        return this.healthPoint;
    }

    public int getMaxHealth(){
        return this.MAX_HEALTH;
    }

    /**
     * update position of sailor
     */
    public void update(Input input){
        if(input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }
        double sailorX = super.topLeft().x;
        double sailorY = super.topLeft().y;

        if (input.wasPressed(Keys.LEFT)) {
            sailorX = super.topLeft().x - STEP_SIZE;
            sailorImage = new Image("res/sailorLeft.png");
        }
        if (input.wasPressed(Keys.RIGHT)) {
            sailorX = super.topLeft().x + STEP_SIZE;
            sailorImage = new Image("res/sailorRight.png");
        }
        if (input.wasPressed(Keys.UP)) {
            sailorY = super.topLeft().y - STEP_SIZE;
        }
        if (input.wasPressed(Keys.DOWN)) {
            sailorY = super.topLeft().y + STEP_SIZE;
        }
        this.relocate(sailorX,sailorY);
    }

    /**
     * undo the move and create the glich effect
     */
    public void undoMove(Input input){
        double sailorX = super.topLeft().x;
        double sailorY = super.topLeft().y;

        if (input.wasPressed(Keys.LEFT)) {
            sailorX = super.topLeft().x + STEP_SIZE;
        }
        if (input.wasPressed(Keys.RIGHT)) {
            sailorX = super.topLeft().x - STEP_SIZE;
        }
        if (input.wasPressed(Keys.UP)) {
            sailorY = super.topLeft().y + STEP_SIZE;
        }
        if (input.wasPressed(Keys.DOWN)) {
            sailorY = super.topLeft().y - STEP_SIZE;
        }
        this.relocate(sailorX,sailorY);
    }




}
