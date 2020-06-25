
import java.awt.Shape;

/**

This class contains object classes that are shared between the
frontend and the backend. They still start off created in the
backend, but they have some fields that are then set by the frontend -
or set in the backend but the backend doesn't use them.

**/
public class Models {

//  \\  Subclasses  \\  //  \\  //  \\

public static class Player {
	String name;
}
	
public static class Seed {
	Shape shape;
}

}
