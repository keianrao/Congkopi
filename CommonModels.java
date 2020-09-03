
import java.awt.Shape;

/**

This class contains structs, and interfaces, shared between
the frontend and backend, and across all board types.

**/
public class CommonModels {

//	Structs	  	//	\\	//	\\	//	\\

public static class Pemain {
	String name;
}

public static class Biji {
	Shape shape;
}



//	Enums 	\\	//	\\	//	\\	//	\\

public enum GameState {
	INITIALISING,
	DISTRIBUTING, EVALUATING, 
	WAITING, GAME_OVER
}

}
