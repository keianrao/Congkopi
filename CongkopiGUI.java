
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**

Provides a Swing/AWT-based client for {@link Congkopi}.

**/
class CongkopiGUI {

//	\\	State 	//	\\  //  \\  //  \\

private Congkopi game;

private JFrame mainframe;
private JPanel mainpanel;



//	\\	Constructors    //  \\  //  \\

CongkopiGUI(Congkopi game) {
	this.game = game;
	
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

}
