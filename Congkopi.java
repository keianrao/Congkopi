
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

private static abstract class Bin {
	private int biji;
	private Player owner;
	private int index;
	
	void addBiji(int n) { biji += n; }
	void removeBiji(int n) { biji -= n; }
	int getBiji() { return biji; }
	
	void setOwner(Player player) { owner = player; }
	Player getOwner() { return owner; }
	
	void setIndex(int index) { this.index = index; }
	int getIndex() { return index; }
	
	/*
	A bin - in this game too - maps really cleanly to a struct,
	so it's weird to have an object with getters and setters
	rather than one with open fields.
	
	But anyways this is fine for now. It's semantic anyways.
	*/
}
private static class Kampung extends Bin { }
private static class Rumah extends Bin { }



private final static int VILLAGE_COUNT = 7;
private final static int BIN_COUNT = VILLAGE_COUNT + 1;
private Bin[] board = new Bin[(VILLAGE_COUNT + 1) * 2];



private static class Player {
	String name;
}
private Player[] players;



private int currentPlayerIndex;
private boolean distributing;
public static enum Stage {
	DISTRIBUTING,
	WAITING
}



//	\\	Constructor	\\	//	\\	//	\\

public Congkopi() {
	useDefaultPlayers();
	setupBoard();
}

private void useDefaultPlayers() {
	players = new Player[2];
	
	players[0] = new Player();
	players[0].name = "Hang Jebat";
	
	players[1] = new Player();
	players[1].name = "Hang Tuah";
}

private void setupBoard() {
	for (int playerIndex = 0; playerIndex < players.length; ++playerIndex) {
		Player player = players[playerIndex];
		int startingIndex = playerIndex * BIN_COUNT;
		
		// Add all the villages for this player first.
		int kampungIndex = 0;
		for (; kampungIndex < VILLAGE_COUNT; ++kampungIndex) {
			int index = startingIndex + kampungIndex;
			Kampung kampung = new Kampung();
			kampung.setOwner(player);
			kampung.setIndex(index);
			board[index] = kampung;
		}
		
		// Then add a house.
		int rumahIndex = VILLAGE_COUNT + 1;
		int index = startingIndex + rumahIndex;
		Rumah rumah = new Rumah();
		rumah.setOwner(player);
		rumah.setIndex(index);
		board[index] = rumah;
	}
}



//	\\	Interface 	\\	//	\\	//	\\

Stage getStage() {
	if (distributing) return Stage.DISTRIBUTING;
	else return Stage.WAITING;
}

Bin[] getBoard() {
	return board;
}

Player getCurrentPlayer() {
	return players[currentPlayerIndex];
}

boolean canDistributeNow(Bin bin) {
	if (!distributing) return false;
	if (!(bin instanceof Kampung)) return false;
	if (getCurrentPlayer() != bin.getOwner()) return false;
	return true;
}

void distribute(Bin bin) {
	if (bin == null) return; // Sure?

	int bijiInHand = bin.getBiji();
	bin.removeBiji(bijiInHand);
	int currentIndex = bin.getIndex() + 1;
	
	// Okay, distribute away...
	while (bijiInHand > 0) {
		if (currentIndex == board.length) {
			// Loop back to the start of the board.
			currentIndex = 0;
		}
		
		board[currentIndex].addBiji(1);
		bijiInHand--;
	}
	
	// Finished distributing.
	// We still have currentIndex, so we'll see what we landed on..
	lastBijiLandedOn(board[currentIndex]);
	
	// Switch turns to next player.
	currentPlayerIndex++;
	if (currentPlayerIndex == players.length) currentPlayerIndex = 0;
}


//  \\  Private //  \\  //  \\  //  \\

private void lastBijiLandedOn(Bin bin) {
	// Fun game parts.
	
	if (players.length != 2) {
		// I don't know what to do.
		return;
	}
	
	else {
		if (getCurrentPlayer() == bin.getOwner()) {
			if (bin.getBiji() == 0) {
				// Aha. Time to take opponent's biji.
				int opposingBinIndex = bin.getIndex() + BIN_COUNT;
				Bin opposingBin = board[opposingBinIndex];

				int takenBiji = opposingBin.getBiji();
				opposingBin.removeBiji(takenBiji);
				bin.addBiji(takenBiji);
			}
		
			else {
				// What were the rules for this part again?
				return;
			}
		}
				
		else {
			// Again, not sure of the rules here..
			return;
		}
	}
}

}
