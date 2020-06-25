
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.AffineTransform;

/**

Provides a Swing/AWT-based client for {@link Congkopi}.

**/
class CongkopiGUI {

//	\\	Main  	//	\\	//	\\	//	\\

public static void main(String... args) {
	if (args.length != 0) {
		System.err.println(
			"Sorry, this game doesn't accept command-line options yet.. " +
			"There won't be any user configuration through flags - " +
			"the source code has to be tweaked and recompiled."
		);
		System.exit(2);
	}
	// To-do: Craft a quick version of getopt

	TwoPlayerBackend backend = new TwoPlayerBackend();
	CongkopiGUI gui = new CongkopiGUI(backend);
}



//	\\	State 	//	\\  //  \\  //  \\

private TwoPlayerBackend backend;

private JFrame mainframe;
private JPanel mainpanel;



//	\\	Constructors    //  \\  //  \\

CongkopiGUI(TwoPlayerBackend backend) {
	this.backend = backend;
	
	// Initialise mainframe.
	mainframe = new JFrame("Congkopi");
	mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	// Initialise mainpanel.
	/*
	We actually need to create a subclass of JPanel
	for custom drawing, but I'll pass on doing that right now.
	It's the hard part.
	The one below's a stub.
	*/
	mainpanel = new CongkopiPanel();
	mainpanel.setPreferredSize(new Dimension(640, 480));
	/*
	Should we be hardcoding values?
	It would be marvellous if we have the thing scale to window size.
	*/
	mainframe.setContentPane(mainpanel);
	mainframe.pack();
	
	
	// Alright, roll the curtains.
	mainframe.setVisible(true);
}

class CongkopiPanel extends JPanel {
	BufferedImage skin = null;

	public void paintComponent(Graphics g) {
		if (skin != null) {
			g.drawImage(skin, 0, 0, getWidth(), getHeight(), this);
		}
		else {
			// Draw something with AWT graphics.
			
			// Draw a background first..
			g.setColor(new Color(97, 142, 41));
			g.fillRect(0, 0, getWidth(), getHeight());
			// Draw herringbone for kicks.
			g.setColor(new Color(255, 255, 255, 25));
			for (int y = 1; y < getWidth(); y += 3) {
				g.fillRect(0, y, getWidth(), 1);
			}
			
			
			// Let's do things guide style..
			int
				xCentre = getWidth() / 2,
				yCentre = getHeight() / 2;
			int
				lubangMargin = 16,
				kampungRadius = 32, rumahRadius = 64;
			int
				kampung2Breadth =
					(kampungRadius * 7) + (lubangMargin * 6),
				rumahBreadth =
					rumahRadius + (2 * lubangMargin),
				boardBreadth =
					kampung2Breadth + (2 * rumahBreadth);
			int
				kampung2LeftEdgeX = xCentre - (kampung2Breadth / 2),
				kampung2RightEdgeX = getWidth() - kampung2LeftEdgeX,
				boardLeftEdgeX = kampung2LeftEdgeX - rumahBreadth,
				boardTopEdgeY = yCentre - (rumahBreadth / 2);
			
			
			g.setColor(new Color(93, 44, 34));
				
			// Draw rumah2 sections of the board.
			g.fillOval(
				boardLeftEdgeX, boardTopEdgeY,
				rumahBreadth, rumahBreadth
			);
			g.fillOval(
				kampung2RightEdgeX, boardTopEdgeY,
				rumahBreadth, rumahBreadth
			);
			
			// Draw kampung2 sections of the board.
			g.fillRect(
				kampung2LeftEdgeX - (rumahBreadth / 2), boardTopEdgeY,
				kampung2Breadth + rumahBreadth, rumahBreadth
			);
			
			
			g.setColor(new Color(15, 2, 1));
			
			// Okay, draw rumah2 themselves.
			
			// Then draw kampung2 themselves. Be careful of margins!
		}
	}

}



//	\\	Private helpers //  \\  //  \\

private static Shape generateSeedShape() {
	// Generate random X and Y.
	// We want the seed in a circular bowl, so use circle math.
	double angle = Math.random() * 2 * Math.PI;
	double radius = Math.random() * (32 - 8);
	// (Radius matches kampungRadius, but we're hardcoding for now.
	// Making it a bit shorter so it's squarely within the circle)
	double x = Math.abs(Math.cos(angle) * radius);
	double y = Math.abs(Math.sin(angle) * radius);
	
	// Width and height will be the same for each seed.
	double w = 4;
	double h = 1;

	Shape seedShape = new Ellipse2D.Double(x, y, w, h);
	
	// Okay, now here's the absurd part. We'll also rotate the seed
	// randomly, so that it looks naturally placed. We'll use
	// java.awt.geom.AffineTransform for this, which is happy to
	// work with java.awt.Shape.
	
	// For now we'll generate a new AffineTransform everytime,
	// but it is rather costly, so if it starts to lag, just pick
	// one of several predefined angles, and get an already-initialised
	// AffineTransform for it.
	AffineTransform rotationTransform = new AffineTransform();
	rotationTransform.rotate(Math.random() * 2 * Math.PI);
	
	// Okay! We're done.
	seedShape = rotationTransform.createTransformedShape(seedShape);
	return seedShape;
}

private static void generateAndAddSeedShapeIfNeeded(Models.Seed seed) {
	assert seed != null;
	if (seed.shape == null) {
		seed.shape = generateSeedShape();
	}
}

}
