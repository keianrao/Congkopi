
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

/**

Contains data structures, logic, and actions for the 
two-player variant of the game.

**/
public class TwoPlayerBackend {

//	\\	State 	//	\\	//	\\	//	\\

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

private Models.Player player1, player2, currentPlayer;
private List<List<Models.Seed>> board;
private GameState currentGameState;


//	\\	Constructors  	//	\\	//	\\

public TwoPlayerBackend() {
	board = new ArrayList<List<Models.Seed>>(2 * BOARD_BREADTH);
	for (int i = 1; i <= 2 * BOARD_BREADTH; ++i) {
		board.add(new LinkedList<Models.Seed>());
	}
	distributeStartingBiji();

	player1 = new Models.Player();
	player1.name = "Hang Tuah";
	player2 = new Models.Player();
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
	* (1) https://tradisionalsports.blogspot.com/2014/05/congkak.html
	* This page recommends 5, 7, 8, 9..
	* (2) https://zikrihusaini.com/cara-untuk-bermain-congkak/
	* This page recommends 7.
	*
	* Ultimately it's up to the players - it's a good parameter to ask.
	* Anyhow, a default of 7 seems reasonable.
	*/
	
	for (int o = 0; o < board.size(); ++o) {
		if (o == PLAYER1_RUMAH_OFFSET) continue;
		if (o == PLAYER2_RUMAH_OFFSET) continue;
		for (int seed = 1; seed <= 7; ++seed)
			board.get(o).add(new Models.Seed());
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
	List<Models.Seed> bijiInHand = new LinkedList<Models.Seed>();	
	bijiInHand.addAll(board.get(offset));
	board.get(offset).clear();
	
	while (!bijiInHand.isEmpty()) {
		offset = offsetOfNextKampungFrom(offset);
		// Drop off a biji.
		board.get(offset).add(bijiInHand.remove(0));
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


public Models.Player getCurrentPlayer() {
	return currentPlayer;
}

public GameState getGameState() {
	return currentGameState;
}



//  \\  Interface helpers   \\  //  \\

private void lastBijiDroppedIn(int offset) {
	if (board.get(offset).size() > 1) {
		// Last biji dropped in a non-empty lubang. 
		// Nothing interesting..
		return;
	}
	else if (board.get(offset).size() == 1) {
		// Last biji dropped in an empty lubang.
		
		int oppositeKampungOffset = offsetAcrossFrom(offset);
		int owningPlayerRumahOffset = offsetOfRumahOfPlayerThatOwns(offset);
		
		// Capture!
		board.get(owningPlayerRumahOffset)
			.addAll(board.get(oppositeKampungOffset));
		board.get(oppositeKampungOffset).clear();
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
