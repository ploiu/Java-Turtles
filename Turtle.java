
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Turtle {
	// the turtle's position
	private int					posX, posY;
	private final int			origX, origY;					// original
	// direction in radians, don't worry, the input can be in degrees
	private double				direction;
	private final double		origDirection;					// original
	// the Window with the image to draw to
	private TurtleWorld			turtleWorld			= null;
	// the turtle id used for referencing
	private final int			ID;
	// the default shape that shows the turtle's direction
	private Shape				triangle;
	// do not mess with this, it ensures that the pointer rotates correctly
	private AffineTransform		transform;
	// whether or not animations should be enabled
	public static boolean		enableAnimations	= false;
	// the speed at which to animate, seperate for each turtle; 100 is
	// normal speed
	private int					animateSpeed;
	// the original animationSpeed
	private final int			origSpeed			= 100;
	// the original Color
	private final Color			origColor;
	// controls if the turtle draws when it moves
	private boolean				shouldDraw;

	/*
	 * the image to draw on from the TurtleWorld class, as well as Graphics
	 * to paint on it
	 */
	public static BufferedImage	world;
	private Graphics			painter;

	/***
	 * Creates a turtle object
	 *
	 * @param posX the turtle's x position
	 * @param posY the turtle's y position
	 * @param direction the direction the turtle is facing in Degrees; 90
	 *        is North
	 * @param color the starting color for the turtle to paint with
	 * @param turtleWorld you must give the turtle an already constructed
	 *        world
	 * ***/
	public Turtle(int posX, int posY, double direction, Color color,
			TurtleWorld turtleWorld) {
		// since origX,origY,origColor and origDirection are final, they
		// MUST be initialized in the constructor
		// this is for when any of the reset() methods are called
		origX = posX;
		origY = posY;
		origDirection = Math.toRadians(direction);
		origColor = color;
		animateSpeed = 100;
		// now make sure that the world isn't null, and alert the user if
		// it is
		if (turtleWorld == null) {
			System.err
			.println("ERROR: Couldn't get world for the turtles!"
					+ " Did you create a Turtle World?");
			System.exit(1);
		} else {
			// set the world for the turtle to draw in
			this.turtleWorld = turtleWorld;
			// create a reference to the image
			world = this.turtleWorld.getImage();

			// set the graphics to draw with
			painter = world.createGraphics();
			// set the color the graphics should draw with
			painter.setColor(color);
			// set position and rotation
			this.posX = posX;
			this.posY = posY;
			// convert the direction to radians
			this.direction = Math.toRadians(direction);
			if (Math.toDegrees(direction) < 0)
				this.direction = Math.toRadians(360 + direction);
			// add this turtle to the list of turtles
			turtleWorld.getTurtleList().add(this);
			// set shouldDraw to true
			shouldDraw = true;
		}
		// set this turtle's id to its index in the TurtleWorld list
		// this will only happen if the program doesn't fail, but it must
		// be placed outside the else block because the compiler flips out
		// if it's in there
		ID = turtleWorld.getTurtleList().size();
	}

	/***
	 * Rotates the turtle's direction <strong>left</strong> the specified
	 * amount in degrees
	 * 
	 * @param amount the amount in degrees the turtle should turn
	 ***/
	public void turnLeft(double amount) {
		// animated version
		if (Turtle.enableAnimations) {
			if (amount > 0) {
				direction += Math.toRadians(1);
				try {
					Thread.sleep(1000 / animateSpeed);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				turtleWorld.repaint();
				turnLeft(amount - 1);
			} else {
				direction += Math.toRadians(amount);
				turtleWorld.repaint();
			}
		} else {
			// non-animation version
			direction += Math.toRadians(amount);
			turtleWorld.repaint();
		}

	}

	/***
	 * Rotates the turtle's direction <strong>right</strong> the specified
	 * amount in degrees
	 * 
	 * @param amount the amount in degrees the turtle should turn
	 ***/
	public void turnRight(double amount) {
		// animated version
		if (Turtle.enableAnimations) {
			if (amount > 0) {
				direction -= Math.toRadians(1);
				try {
					Thread.sleep(1000 / animateSpeed);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				turtleWorld.repaint();
				turnRight(amount - 1);
			} else {
				direction -= Math.toRadians(amount);
				turtleWorld.repaint();
			}
		} else {
			// non-animation version
			direction -= Math.toRadians(amount);
			turtleWorld.repaint();
		}

	}

	/***
	 * Moves the turtle forward the specified amount
	 *
	 * @param amount the distance the turtle should move
	 ***/
	public void moveForward(int amount) {
		int newX, newY;
		/*
		 * set the new x and y coordinates of this turtle and round them at
		 * the same time
		 */
		// animated version
		if (Turtle.enableAnimations) {
			if (amount > 0) {
				newX = round(posX + 1 * Math.cos(direction));
				newY = round(posY + 1 * Math.sin(-direction));
				if (shouldDraw)
					painter.drawLine(posX, posY, newX, newY);
				posX = newX;
				posY = newY;
				try {
					Thread.sleep(1000 / animateSpeed);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				turtleWorld.repaint();
				moveForward(amount - 1);
			} else {
				newX = round(posX + 1 * Math.cos(direction));
				newY = round(posY + 1 * Math.sin(-direction));
				if (shouldDraw)
					painter.drawLine(posX, posY, newX, newY);
				posX = newX;
				posY = newY;
				turtleWorld.repaint();
			}
		} else {
			// non-animated version
			newX = round(posX + amount * Math.cos(direction));
			newY = round(posY + amount * Math.sin(-direction));
			if (shouldDraw)
				painter.drawLine(posX, posY, newX, newY);
			posX = newX;
			posY = newY;
			turtleWorld.repaint();
		}

	}

	/***
	 * Move the turtle without drawing
	 * 
	 * @param x the x position the turtle should go to
	 * @param y the y position the turtle should go to
	 * ***/
	public void setPosition(int x, int y) {
		posX = x;
		posY = y;
	}

	/***
	 * set the rotation in degrees the turtle should face
	 * 
	 * @param direction the direction in degrees the turtle should point
	 *        towards
	 * ***/
	public void setRotation(double direction) {
		this.direction = Math.toRadians(direction);
	}

	/***
	 * Handles the rotation of the turtle to ensure that<br/>
	 * <code>direction >=0 && direction < 360</code>
	 * 
	 * @return the turtle's direction in Radians
	 ***/
	public double getLookDirection() {
		// handle direction being > 360 and < 0
		// if the direction > 360, make negative and repeatedly add 180
		double d = direction;
		if (Math.toDegrees(d) > 360)
			d = -d;
		while (Math.toDegrees(d) < 0)
			d += Math.toRadians(180);
		return d;
	}

	/***
	 * Returns the Color the turtle currently draws in
	 * 
	 * @return the turtle's current Color
	 ***/
	public Color getColor() {
		return painter.getColor();
	}

	/***
	 * sets the turtle's color to the one specified. The turtle will now
	 * draw in this color
	 * 
	 * @param c the Color the turtle should now draw in
	 ***/
	public void setColor(Color c) {
		painter.setColor(c);

	}

	/*** Returns the turtle's position as a Point object ***/
	public Point getPoint() {
		return new Point(posX, posY);
	}

	/***
	 * Draws a String at the location and angle the turtle is. draws the
	 * string even if <code>shouldDraw</code> = false
	 * 
	 * @param message the message for the turtle to write
	 * @param move whether or not the turtle moves as the string is typed
	 ***/
	public void drawString(String message, boolean move) {
		// fix the string in terms of \t
		message = message.replaceAll("\t", "    ");
		// animated version
		if (Turtle.enableAnimations) {
			drawString(message, posX, move);
		} else {
			((Graphics2D) painter).rotate(-direction, posX, posY);
			painter.drawString(message, posX, posY);
			((Graphics2D) painter).rotate(direction, posX, posY);
			if (move) {
				// calculate the spacing of each character and add to posX
				char[] chars = message.toCharArray();
				int totalSpacing = 0;
				// calc the spacing
				for (int i = 0; i < chars.length; i++) {
					totalSpacing += painter.getFontMetrics().getWidths()[chars[i]];
				}
				posX = round(posX + totalSpacing * Math.cos(direction));
				posY = round(posY + totalSpacing * Math.sin(-direction));
			}

			turtleWorld.repaint();
		}
	}

	// used for animations
	private void drawString(String message, int x, boolean move) {
		if (message.length() > 1) {
			char[] c = message.toCharArray();
			((Graphics2D) painter).rotate(-direction, posX, posY);
			painter.drawChars(c, 0, 1, posX, posY);
			((Graphics2D) painter).rotate(direction, posX, posY);
			if (move) {
				if (round(Math.toDegrees(direction)) == 90
						|| round(Math.toDegrees(direction)) == 270) {
					posY = round(posY
							+ painter.getFontMetrics().getWidths()[c[0]]
									* Math.sin(-direction));
				} else {
					int space = painter.getFontMetrics().getWidths()[c[0]];
					posX = round(posX + space * Math.cos(direction));
					posY = round(posY + space * Math.sin(-direction));
				}
			}
			try {
				Thread.sleep(1000 / animateSpeed);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			message = message.substring(1, message.length());

			turtleWorld.repaint();
			drawString(message, x, move);
		} else {
			((Graphics2D) painter).rotate(-direction, posX, posY);
			painter.drawString("" + message.charAt(0), posX, posY);
			((Graphics2D) painter).rotate(direction, posX, posY);
			if (move)
				if (round(Math.toDegrees(direction)) == 90
				|| round(Math.toDegrees(direction)) == 270)
					posY = round(posY
							+ painter.getFontMetrics().getWidths()[0]
									* Math.sin(-direction));
				else
					posX = round(posX
							+ painter.getFontMetrics().getWidths()[0]
									* Math.cos(direction));
			turtleWorld.repaint();
		}
	}

	@Override
	public String toString() {
		String info = "TurtleID: " + ID + "\n\tPosition: " + posX + ","
				+ posY + "\n\tDirection: "
				+ Math.abs(round(Math.toDegrees(getLookDirection())))
				+ " degrees\n\tColor: " + painter.getColor();
		return info;
	}

	private int round(double par1) {
		int x = (int) (par1 + .5);
		return x;
	}

	public Shape getTriangle() {
		// edit the xpts and ypts locations
		int[] xpts = { posX, posX - 5, posX + 5 };
		int[] ypts = { posY, posY + 12, posY + 12 };
		triangle = new Polygon(xpts, ypts, 3);
		// get the centerX and centerY coords for the triangle
		int centerX, centerY;
		centerX = round(triangle.getBounds2D().getCenterX());
		centerY = round(triangle.getBounds2D().getCenterY());
		// create the transform object
		transform = AffineTransform.getRotateInstance(
				-direction + Math.toRadians(90), centerX, centerY);
		// transform.preConcatenate(AffineTransform.getTranslateInstance(10,
		// 40));
		return transform.createTransformedShape(triangle);
	}

	public TurtleWorld getWorld() {
		return turtleWorld;
	}

	public void setAnimationSpeed(int speed) {
		animateSpeed = speed;
	}

	public int getAnimationSpeed() {
		return animateSpeed;
	}

	/***
	 * Changes the shouldDraw boolean which controls whether or not the
	 * turtle draws when it moves
	 * 
	 * @param shouldDraw whether or not the turtle should draw when moving
	 ***/
	public void setShouldDraw(boolean shouldDraw) {
		this.shouldDraw = shouldDraw;
	}

	/*** Puts the turtle back at the position and rotation it started at ***/
	public void resetPosition() {
		posX = origX;
		posY = origY;
	}

	/*** Sets the angle back to the turtle's starting angle ***/
	public void resetAngle() {
		direction = origDirection;
	}

	/*** Sets the turtle's color to what it started as ***/
	public void resetColor() {
		painter.setColor(origColor);
	}

	/*** Sets the turtle's animation speed to what it was in the beginning ***/
	public void resetAnimationSpeed() {
		animateSpeed = origSpeed;
	}

	/*** resets the turtle's position, rotation, color, and animation speed ***/
	public void reset() {
		resetPosition();
		resetAngle();
		resetColor();
		resetAnimationSpeed();
	}

}
