
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

public class TurtleWorld extends JFrame {
	private static final long	serialVersionUID	= 01L;
	// used for updating the turtles
	private ArrayList<Turtle>	turtles;
	// the canvas and the graphics that paint to it
	private BufferedImage		canvas;
	private Graphics			painter;
	// for screen stuff
	private Toolkit				tk					= Toolkit
			.getDefaultToolkit();
	// repainting with just the image causes flashing, but repainting to an
	// image in a JPanel doesn't
	// ImagePanel extends JPanel
	private ImagePanel			imagePanel;

	public TurtleWorld() {
		super("Java Turtles!");
		// make sure everything exits when this window closes
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		// set the window size to the screen size
		setSize(tk.getScreenSize());
		// init the imagePanel
		imagePanel = new ImagePanel(this);
		// add the imagePanel to this
		add(imagePanel);
		// create the image and the Graphics object to paint it with
		canvas = new BufferedImage(getWidth(), getHeight(),
				BufferedImage.TYPE_INT_RGB);
		painter = canvas.createGraphics();
		// fill the canvas with a black background
		painter.setColor(Color.black);
		painter.fillRect(0, 0, getWidth(), getHeight());
		// set the 'look and feel' of the window to match the OS style
		try {
			UIManager.setLookAndFeel(UIManager
					.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// print the error
			System.err.println(e.getMessage());
			// alert to the user that something went wrong with setting the
			// style, and then revert back to java's default
			JOptionPane
					.showMessageDialog(
							this,
							"Something went wrong with creating the theme; Reverting to Java's default ");
		}
		turtles = new ArrayList<Turtle>();
		setVisible(true);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		// something is very wrong if this happens
		if (canvas == null || painter == null) {
			String canvasError = canvas == null ? "null" : "OK";
			String painterError = painter == null ? "null" : "OK";
			System.err
					.println("Whoah there, something isn't right with the graphics: \n\t"
							+ "TurtleCanvas: "
							+ canvasError
							+ "\n\tCanvasPainter: "
							+ painterError
							+ "\nReport this to Curtis Langlitz with the email"
							+ "CurtisLanglitz@gmail.com "
							+ "with the error message"
							+ "\nyou could try to fix it yourself, but on your own head :)");
			System.exit(1);
		} else {
			imagePanel.repaint();
		}
	}

	// get the image painter
	public Graphics getPainter() {
		return painter;
	}

	// get the image for the painter to draw on, used for the turtles
	public BufferedImage getImage() {
		return canvas;
	}

	/***
	 * returns the ArrayList of turtles for referencing
	 * 
	 * @return an ArrayList of every turtle for this world
	 ***/
	public ArrayList<Turtle> getTurtleList() {
		return turtles;
	}

}

class ImagePanel extends JPanel {
	private static final long	serialVersionUID	= 01l;
	private TurtleWorld			world;						// used for

	// referencing

	public ImagePanel(TurtleWorld world) {
		this.world = world;
		setSize(world.getSize());

	}

	@Override
	public void paint(Graphics g) {
		// draw the image within the border, using the window's
		// insets(border size)
		g.drawImage(world.getImage(), getInsets().left, getInsets().top,
				this);
		// draw the triangle for each turtle
		for (int i = 0; i < world.getTurtleList().size(); i++) {
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setColor(world.getTurtleList().get(i).getColor());
			g2.fill(world.getTurtleList().get(i).getTriangle());
			g2.dispose();
		}
	}
}
