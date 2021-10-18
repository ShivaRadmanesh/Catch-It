import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.Node;
import java.util.Random;
public class Ball {
    private final double MAX_RADIUS = 75;
    private final double MAX_VELOCITY = 800;
    private int color = 3;
    private Circle circle;
    private double vX;
    private double vY;

    public Ball(double firstRadius, double firstCenterX, double firstCenterY, double firstVX, double firstVY)
    {
        circle = new Circle(firstCenterX, firstCenterY, firstRadius, Color.RED);
        vX = firstVX;
        vY = firstVY;
    }


    public void setCenter(double x, double y)
    {
        circle.setCenterX(x);
        circle.setCenterY(y);
    }

    public double getCenterX()
    {
        return circle.getCenterX();
    }

    public double getCenterY()
    {
        return circle.getCenterY();
    }

    public Node getNode()
    {
        return circle;
    }

    public void setRandomColor() {
        Random rand = new Random();
        int randomColor;
        do {
            randomColor = rand.nextInt(8);
        }while(randomColor == color);

        switch (randomColor) {
            case 0:
                circle.setFill(Color.BLUE);
                break;
            case 1:
                circle.setFill(Color.YELLOW);
                break;
            case 2:
                circle.setFill(Color.GREEN);
                break;
            case 3:
                circle.setFill(Color.RED);
                break;
            case 4:
                circle.setFill(Color.PURPLE);
                break;
            case 5:
                circle.setFill(Color.GRAY);
                break;
            case 6:
                circle.setFill(Color.BLACK);
                break;

        }
    }

    public void setV(double velocityX,double velocityY)
    {
        if(Math.sqrt(Math.pow(velocityX, 2) + Math.pow(velocityY, 2)) <= MAX_VELOCITY){
            vX = velocityX;
            vY = velocityY;
        }
        else{
            vX = MAX_VELOCITY * (vX / Math.sqrt(Math.pow(velocityX, 2) + Math.pow(velocityY, 2)));
            vY = MAX_VELOCITY * (vY / Math.sqrt(Math.pow(velocityX, 2) + Math.pow(velocityY, 2)));
        }
    }

    public double getVX()
    {
        return vX;
    }
    public double getVY()
    {
        return vY;
    }

    public void setRadius(double radius)
    {
            circle.setRadius(radius);
    }

    public double getRadius()
    {
        return circle.getRadius();
    }

}
