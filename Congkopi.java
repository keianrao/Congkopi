
/**

Contains the game data structures, logic and actions.

**/
public class Congkopi {

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

	Congkopi game = new Congkopi();
	CongkopiGUI gui = new CongkopiGUI(game);
}



//	\\	State 	//	\\	//	\\	//	\\

// Reminder: All of these are hardcoded for a 2-player board.
private final static int
	KAMPUNG_PER_PLAYER = 7,
	PLAYER1_RUMAH_OFFSET = 8 - 1,
	PLAYER2_RUMAH_OFFSET = 16 - 1,
	BOARD_BREADTH = KAMPUNG_PER_PLAYER + 1;

private enum GameState {
	DISTRIBUTING,
	EVALUATING,
	WAITING,
	GAME_OVER
}

private class Player {
	String name;
}


private Player player1, player2, currentPlayer;
private int[] board;
private GameState currentGameState;
/*
* Instead of directly messing with an array, we can try to break Board 
* out into its own class. However! Game mechanics vary quite a lot
* between the different types of boards. So if we break Board out into
* a new class here, the interface is still going to have to end up
* hyperspecific.
*
* If we depend that much on specific properties of the Board,
* we might as well just become the board.
*/


//	\\	Constructors  	//	\\	//	\\

public Congkopi() {
	board = new int[2 * BOARD_BREADTH];
	distributeStartingBiji();

	player1 = new Player();
	player1.name = "Hang Tuah";
	player2 = new Player();
	player2.name = "Hang Jebat";
	currentPlayer = player1;
}



//	\\	Constructor helpers	\\	//	\\

private void distributeStartingBiji() {
	/*
	* The rumah2 are to start off empty, while the kampung2 are to be
	* filled evenly.
	*
	* Surprisingly, there's not much reference as to how many seeds.
	* [1] https://tradisionalsports.blogspot.com/2014/05/congkak.html
	* This page recommends 5, 7, 8, 9..
	* [2] https://zikrihusaini.com/cara-untuk-bermain-congkak/
	* This page recommends 7.
	*
	* Ultimately it's up to the players - it's a good parameter to ask.
	* Anyhow, a default of 7 seems reasonable.
	*/
	
	for (int o = 0; o < board.length; ++o) {
		if (o == PLAYER1_RUMAH_OFFSET) continue;
		if (o == PLAYER2_RUMAH_OFFSET) continue;
		board[o] = 7;
	}
	
	/*
	* If for some reason animations are added into this game,
	* this procedure should be modified to instead distribute in a
	* repeating, circular fashion, like distributing in the game.
	*/
}



//	\\	Interface 	\\	//	\\	//	\\

void distribute(int offset) {
	currentGameState = GameState.DISTRIBUTING;

	if (offset == PLAYER1_RUMAH_OFFSET ||
	 	offset == PLAYER2_RUMAH_OFFSET) {
		return; // Refuse quietly for now
	}

	// Okay, pick up all the seeds from that board..
	int bijiInHand = board[offset];
	board[offset] = 0;
	
	while (bijiInHand > 0) {
		offset = offsetOfNextKampungFrom(offset);
		// Drop off a biji.
		--bijiInHand; ++board[offset];
	}
	
	
	currentGameState = GameState.EVALUATING;
	
	/*
	* bijiInHand became 0, so we just dropped off the last biji.
	* 'offset' is still that of that last kampung we dropped it in.
	* Check if there's game events like a capture.
	*/
	lastBijiDroppedIn(offset);
	
	if (gameIsOver()) {
		currentGameState = GameState.GAME_OVER;
	}
	else {
		switchToNextPlayer();
		currentGameState = GameState.WAITING;
	}
}


public Player getCurrentPlayer() {
	return currentPlayer;
}

public GameState getGameState() {
	return currentGameState;
}



//  \\  Interface helpers   \\  //  \\

private void lastBijiDroppedIn(int offset) {
	if (board[offset] > 1) {
		// Last biji dropped in a non-empty lubang. 
		// Nothing interesting..
		return;
	}
	else if (board[offset] == 1) {
		// Last biji dropped in an empty lubang.
		
		int oppositeKampungOffset = offsetAcrossFrom(offset);
		int owningPlayerRumahOffset = offsetOfRumahOfPlayerThatOwns(offset);
		
		// Capture!
		board[owningPlayerRumahOffset] += board[oppositeKampungOffset];
		board[oppositeKampungOffset] = 0;
	}
}

private void switchToNextPlayer() {
	if (currentPlayer == player2) currentPlayer = player1;
	else currentPlayer = player1;
}

private int offsetOfNextKampungFrom(int offset) {
	if (offset + 1 == PLAYER1_RUMAH_OFFSET) return offset + 2;
	else if (offset + 1 == PLAYER2_RUMAH_OFFSET) return 0;
	else return offset + 1;
}

private int offsetAcrossFrom(int offset) {
	if (offset + 1 == PLAYER1_RUMAH_OFFSET) return offset + 2;
	else if (offset + 1 == PLAYER2_RUMAH_OFFSET) return 0;
	else return offset + 1;
}

private int offsetOfRumahOfPlayerThatOwns(int offset) {
	if (offset <= PLAYER1_RUMAH_OFFSET) return PLAYER1_RUMAH_OFFSET;
	else return PLAYER2_RUMAH_OFFSET;
}

private boolean gameIsOver() {
	/*
	* I'm not 100% sure of the rules of how this goes. I think an
	* important part is when a player has no more biji to distribute -
	* like a checkmate.
	*
	* Maybe the game just loops back to the other player that
	* has seeds, and the charade goes until there's one seed left
	* crawling to the next rumah?
	*
	* Or maybe if a player falls into a checkmate, the other player
	* gets to collect all the remaining seeds into their rumah?
	*/
	return false;
}

}
