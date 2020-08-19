
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;
import java.io.IOException;

class SeedDemo extends JPanel {

private static final Color FOREGROUND = Color.GRAY;
private static final Color ACCENT = FOREGROUND.darker();
private static final Color BACKGROUND = Color.BLACK;

private final List<Seed> seeds = new ArrayList<Seed>(32);

//  \\  //  \\  //  \\  //  \\  //  \\  //

public SeedDemo() {
	setBackground(BACKGROUND);
	setForeground(FOREGROUND);
}

public static void main(String... args) throws IOException {
	JFrame mainframe = new JFrame("Seed");
	mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	SeedDemo instance = new SeedDemo();

	mainframe.setContentPane(instance);
	mainframe.setSize(256, 256);

	mainframe.setVisible(true);

	System.out.print("Hit return at any time to generate an extra seed: ");
	System.out.flush();
	while (System.in.read() != -1) {
		System.in.skip(System.in.available());
		instance.generateSeed();
	}
}

//  \\  //  \\  //  \\  //  \\  //  \\  //

public void generateSeed() {
	Seed seed = new Seed(
		128, 32,
		Math.random() * (2 * Math.PI)
	);

	SwingUtilities.invokeLater(new Runnable() {
		public void run() {
			seeds.add(seed);
			repaint();
			Toolkit.getDefaultToolkit().sync();
		}
	});
}

//  \\  //  \\  //  \\  //  \\  //  \\  //

public void paintComponent(Graphics g) {
	if (!(g instanceof Graphics2D)) return;
	Graphics2D g2d = (Graphics2D)g;

	// Fill with background..
	g2d.setColor(BACKGROUND);
	g2d.fillRect(0, 0, getWidth(), getHeight());

	// Now render seeds.
	AffineTransform moveToCentre = 
		AffineTransform.getTranslateInstance(
			getWidth() / 2,
			getHeight() / 2
		);
	for (Seed seed: seeds) {
		Shape renderedShape = 
			moveToCentre.createTransformedShape(seed.rotatedShape);
		g2d.setColor(FOREGROUND);
		g2d.fill(renderedShape);
		g2d.setColor(ACCENT);
		g2d.draw(renderedShape);
	}
}

//  \\  //  \\  //  \\  //  \\  //  \\  //

static class Seed {
	// int centreX, centreY;
	/*
	* Firstly, these should be separate from unrotatedShape so that
	* rotation won't mess up (classic problem, try it).
	* Secondly, we won't use it for now, we'll always repaint seeds
	* at the centre of the window, for this demo.
	*/
	Shape unrotatedShape;
	Shape rotatedShape;

	Seed(int length, int width, double rotationInRadians) {
		unrotatedShape = new Ellipse2D.Double(
			-width / 2, -length / 2, 
			width, length
		);
		rotatedShape = 
			AffineTransform.getRotateInstance(-rotationInRadians)
				.createTransformedShape(unrotatedShape);
		/*
		* [1] We assume rotation given is clockwise, but anticlockwise is
		* assumed by AffineTransform#getRotateInstance(double).
		* I think simply negating the value will swap directions like so.
		*
		* Also. This looks completely wrong when the program is run
		* (as in, either the Javadoc is wrong or we shouldn't negate??).
		* I think it's actually because of GUI-style y-positioning,
		* where positive y is towards the bottom. So the 'top left corner'
		* in our Shape actually becomes the bottom left corner on the screen.
		*
		* In that light, the code is working correctly. If there is a
		* positive angle, the *bottom* end of the seed rotates clockwise.
		*
		* To "fix" things, we can try to invert y on the screen, we can
		* incorrectly edit the angle to look like the argument given
		* on the screen.. The former is a typical solution, but I'm
		* not sure how to do it using matrix-based graphics. Lug around a
		* "camera" matrix, just like many OpenGL programs?
		*/
	}
}

}