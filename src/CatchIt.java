import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.io.File;
import java.util.Random;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.scene.image.Image;



public class CatchIt extends Application
{
    AnimationTimer animator = new AnimationTimer() {
        long previousT;
        boolean isFirstCall = true;

        @Override
        public void handle(long now) {
            if(isFirstCall) {
                isFirstCall = false;
            } else {
                double dt = (double)(now - previousT) / 1_000_000_000.0;
                // the code to be executed
                nextFrame(dt);
            }  // end if-else block

            previousT = now;
        } // end method handle
    } ; // end of anonymous inner class

    Ball ball;
    MediaPlayer tap;
    MediaPlayer tickTock;
    MediaPlayer ring;
    private final double WIDTH = 1000;
    private final double HEIGHT = 500;
    private final double FIRST_RADIUS = 50;
    private final double MAX_RADIUS = 75;
    private int scoreNum = 0;
    private boolean isFinished = false;



    public void start(Stage window)
    {
        boolean isFirstClick = true;
        Group root = new Group();
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        ball = new Ball(FIRST_RADIUS, WIDTH / 2, HEIGHT / 2, 50, 50);

        String tapPath = ("tap.mp3");
        Media tapSound = new Media(new File(tapPath).toURI().toString());
        tap = new MediaPlayer(tapSound);

        String tickTockPath = ("tickTock.mp3");
        Media tickTockSound = new Media(new File(tickTockPath).toURI().toString());
        tickTock = new MediaPlayer(tickTockSound);

        String ringPath = ("ring.mp3");
        Media ringSound = new Media(new File(ringPath).toURI().toString());
        ring = new MediaPlayer(ringSound);


        String imagePath =("file:background.jpg");
        Image image = new Image(imagePath); //setting the image background
        Canvas canvas = new Canvas(WIDTH, HEIGHT);   //setting the background including the red area, the yellow area, the blue area and the green area
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.drawImage(image, 0, 0, WIDTH, HEIGHT);
        gc.setFill(Color.GREEN);
        gc.fillRect(400, 490, 200, 10); // green area, on the bottom wall
        gc.setFill(Color.YELLOW);
        gc.fillRect(400, 0, 200, 10); // yellow area,on the upper wall
        gc.setFill(Color.RED);
        gc.fillRect(990, 175, 10, 150); //red area, on the right wall
        gc.setFill(Color.BLUE);
        gc.fillRect(0, 175, 10, 150); //blue area, on the left wall
        root.getChildren().add(canvas);

        Text description = new Text(); //for showing the score on the screen
        Font font = new Font(40);
        description.setText("Score : " + scoreNum);
        description.setLayoutX(10);
        description.setLayoutY(HEIGHT - 10);
        description.setFont(font.font(40));
        description.setFill(Color.DARKCYAN);

        root.getChildren().add(description);

        ball.getNode().setOnMouseClicked(new EventHandler<MouseEvent>() {
            boolean isFirstClick = true;
            @Override
            public void handle(MouseEvent event) {

                MouseButton mouseButton = event.getButton();
                if(isFirstClick) //starting the game by the first click
                {
                        animator.start();
                        isFirstClick = false;

                    Timeline stopGame = new Timeline();  // for the time limitation
                    KeyFrame stopAnimationTimer = new KeyFrame(Duration.seconds(120), new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            animator.stop();
                            tickTock.stop();
                            ring.play();
                            isFinished = true;
                            if(isFinished == true)  // show the final score if the game is finished
                            {
                                Text finalScore = new Text("Your score  \n      " + scoreNum);
                                finalScore.setFill(Color.DARKBLUE);
                                finalScore.setFont(font.font(80));
                                finalScore.setLayoutX((WIDTH / 2) - 200);
                                finalScore.setLayoutY((HEIGHT / 2) - 100);
                                root.getChildren().add(finalScore);
                            }
                        }
                    });
                    KeyFrame timer = new KeyFrame(Duration.seconds(110), new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                           tickTock.play();
                        }
                    });
                    stopGame.getKeyFrames().add(timer);
                    stopGame.getKeyFrames().add(stopAnimationTimer);
                    stopGame.play();





                }
                if(isFinished == false) {
                    if (mouseButton == MouseButton.PRIMARY)   //if right clicked
                        ball.setV(1.1 * ball.getVX(), 1.1 * ball.getVY());
                    else if (mouseButton == MouseButton.SECONDARY)   //if left clicked
                        ball.setV(0.9 * ball.getVX(), 0.9 * ball.getVY());
                    scoreNum = scoreNum + (int) (Math.sqrt(Math.pow(ball.getVX(), 2) + Math.pow(ball.getVY(), 2)) / 10);
                    description.setText("Score : " + scoreNum);
                }
            }
        });
        root.getChildren().add(ball.getNode());

        window.setScene(scene);
        window.show();
    }


    public void nextFrame(double dt)
    {
        ball.setCenter(ball.getCenterX() + (ball.getVX() * dt), ball.getCenterY() + (ball.getVY() * dt));
        if(ball.getCenterY() + ball.getRadius() >= HEIGHT)
        {
            if(ball.getCenterX() >= 400 && ball.getCenterX() <= 600) //the green area
            {
                tap.stop();
                tap.play();
                ball.setRandomColor();
            }
            ball.setV(ball.getVX(), -ball.getVY()); //reflection of the bottom wall
            ball.setCenter(ball.getCenterX(), HEIGHT - ball.getRadius());
        }
        if(ball.getCenterY() - ball.getRadius() <= 0)
        {
            if(ball.getCenterX() >= 400 && ball.getCenterX() <= 600)  //the yellow area
            {
                tap.stop(); //first stop a playing sound, then start the sound
                tap.play();
                double randomAngle = getRandomAngle();
                double velocity = Math.sqrt(Math.pow(ball.getVX(), 2) + Math.pow(ball.getVY(), 2));
                ball.setV(Math.abs(velocity) * Math.cos(randomAngle),
                        Math.abs(velocity) * Math.sin(randomAngle));
            }
            ball.setV(ball.getVX(), -ball.getVY()); //reflection of the upper wall
            ball.setCenter(ball.getCenterX(), ball.getRadius());
        }
        if(ball.getCenterX() + ball.getRadius() >= WIDTH)
        {
            if(ball.getCenterY() >= 175 && ball.getCenterY() <= 325)  //the red area
            {
                tap.stop();
                tap.play();
                ball.setRadius(FIRST_RADIUS);
            }
            ball.setV(-ball.getVX(), ball.getVY()); //reflection of the right wall
            ball.setCenter(WIDTH - ball.getRadius(), ball.getCenterY());
        }
        if(ball.getCenterX() - ball.getRadius() <= 0)
        {
            if(ball.getCenterY() >= 175 && ball.getCenterY() <= 325)  //the blue area
            {
                tap.stop();
                tap.play();
                ball.setRadius(MAX_RADIUS);
            }
            ball.setV(-ball.getVX(), ball.getVY());   //reflection of the left wall
            ball.setCenter(ball.getRadius(), ball.getCenterY());
        }
    }

    private double getRandomAngle()
    {
        double randomAngle;
        Random rand = new Random();
        randomAngle =(double) rand.nextFloat() * 180;
        return randomAngle;
    }

    public static void main(String[] args){
        Application.launch(args);
    }
}

