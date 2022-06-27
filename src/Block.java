import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Block extends Rectangle {
    private final Image BLOCK_IMAGE = new Image("res/block.png");
    private static final int DAMAGE_POINT = 10;

    /**
     * constructor
     */
    public Block(double blockX, double blockY, double blockW, double blockH){super(blockX, blockY, blockW, blockH);
    }


    /**
     * creating getters
     */
    public Point getPoint(){
        return new Point(super.centre().x, super.centre().y);
    }
    public double getX(){
        return super.centre().x;
    }

    public double getY(){
        return super.centre().y;
    }

    /**
     * block attack damage
     */
    public int Attack(){
        return DAMAGE_POINT;
    }

    /**
     * inisialising block position base on given x and y
     */
    public void initialise(){
        BLOCK_IMAGE.draw(super.topLeft().x, super.topLeft().y);
    }
}
