
import java.util.ArrayList;

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

	Congkopi runtime = new Congkopi();
	CongkopiGUI gui = new CongkopiGUI(runtime);
}



//	\\	State 	//	\\	//	\\	//	\\

private final static int KAMPUNG_PER_PLAYER = 7;
private final static int LUBANG_PER_PLAYER = KAMPUNG_PER_PLAYER + 1;
private final static int BOARD_BREADTH = LUBANG_PER_PLAYER;

private class Player {
	String name;
}

private enum BoardState {
	DISTRIBUTING,
	EVALUATING,
	WAITING
}

private class Board {
	Player player1, player2;
	Player currentPlayer;
	
	int[] lubang = new int[2 * LUBANG_PER_PLAYER];
	
	final int PLAYER1_RUMAH_OFFSET 
		= LUBANG_PER_PLAYER - 1; // 8th = lubang[7]
	final int PLAYER2_RUMAH_OFFSET
		= PLAYER1_RUMAH_OFFSET + BOARD_BREADTH;
	
	void distribute(int offset) {
		if (offset == PLAYER1_RUMAH ||
		 	offset == PLAYER2_RUMAH) {
			crashAndBurn();
		}
		
		// Okay, pick up all the seeds from that lubang..
		int bijiInHand = lubang[offset];
		lubang[offset] = 0;
		
		while (bijiInHand > 0) {
			/*
			* Move to the next *kampung*. So skip over 
			* the player rumah, and wrap around the board 
			* if necessary.
			*/
			++offset;
			if (offset == PLAYER1_RUMAH) ++offset;
			else if (offset == PLAYER2_RUMAH) offset = 0;
			
			// Drop off a biji.
			--bijiInHand; ++lubang[offset];
		}
		
		// Okay, so we just dropped off our last biji.
		// 'offset' is still that of the kampung we dropped it in.
		// See if we can execute those exciting game moves.
		evaluate(offset);
		
		// We're done, so switch over now.
		switchPlayers();
	}
	
	void crashAndBurn() {
		System.err.println("Tried to distribute a player's rumah!");
		System.exit(1);
	}
	
	void evaluate(int offset) {
		if (lubang[offset] > 1) {
			// Last biji dropped in a non-empty lubang.
			// Never anything interesting.
			return;
		}
		
		else if (lubang[offset] == 1) {
			// Last biji dropped in an empty lubang.
			// Time to grab!
			
			// First, determine the offset of the opposite side..
			/*
			* Consider this example board, of breadth 6.
			*
			*      o6   o7   o8   o9   o10
			*  o5						    o11
			*      o4   o3   o2   o1   o0
			*
			* Player1's lubang are lubang[0] to lubang[5].
			* Player2's lubang are lubang[6] to lubang[11];
			*
			* Suppose the last biji landed on 3.
			* Its offset from the start of Player1's lubang2 is 3.
			* The offset of the opposing side, 7 -
			* its offset from the end of Player2's lubang2 is 3.
			*/
			
			// I'll just do the lazy way of getting this..
			int currentPlayerRumahOffset, opposingPlayerRumahOffset;
			if (currentPlayer == player1) {
				currentPlayerRumahOffset = PLAYER1_RUMAH_OFFSET;
				opposingPlayerRumahOffset = PLAYER2_RUMAH_OFFSET;
			}
			else {
				currentPlayerRumahOffset = PLAYER2_RUMAH_OFFSET;
				opposingPlayerRumahOffset = PLAYER1_RUMAH_OFFSET;
			}
			
			// Okay, so the opposing player's rumah is the end.
			int opposingOffset = opposingPlayerRumahOffset - offset;
			
			/*
			* The way grab works is, you grab all biji from the
			* opposing side, and put it into your own rumah.
			*/
			lubang[currentPlayerRumahOffset] += lubang[opposingOffset];
			lubang[opposingOffset] = 0;
			
			/*
			* Why are we confusing our calculations?
			* We can write this method agnostic of "current player".
			* We can determine who owns the lubang of any offset.
			* Then we should distribute the opposing lubang's biji
			* to the rumah of whoever that owner is.
			*
			* Please decide whether you want to be stateful or not.
			*/
		}
	}
	
	void switchPlayers() {
		if (currentPlayer == player2) currentPlayer = player1;
		else currentPlayer = player1;
	}
}



//	\\	Constructor	\\	//	\\	//	\\

public Congkopi() {
	board = new Board();
	board.player1 = new Player("Hang Tuah");
	board.player2 = new Player("Hang Jebat");
}



//	\\	Interface 	\\	//	\\	//	\\



//  \\  Private //  \\  //  \\  //  \\

}
