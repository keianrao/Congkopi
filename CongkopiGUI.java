
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Color;

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
	mainpanel = new JPanel();
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

}
