import java.awt.*;

public class Main {
	public static void main(String[] args) {
		TurtleWorld world = new TurtleWorld();
		Turtle turtle = new Turtle(100, 100, 97, Color.RED, world);
		turtle.moveForward(20);
		turtle.setAnimationSpeed(1);
		turtle.setRotation(270);
		turtle.moveForward(100);
	}
}
