
/**

Contains the game data structures, logic and actions.

**/
public class Congkopi {

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



//  \\  //  \\  //  \\



public final static int VILLAGE_COUNT = 7;
// Please do not make this '1000'.


private static final class Player {
	String name;
	final int[] villages = new int[VILLAGE_COUNT];
	int house = 0;
	
	Player(String name) { this.name = name; }
}

Player
	hangTuah = new Player("Hang Tuah"), 
	hangJebat = new Player("Hang Jebat");
/*
I have two symmetrical players, don't know what to name them.
So I named them after the two characters of Hikayat Hang Tuah.
*/

Player playerForThisTurn = hangTuah;

/*
Note: These are package-visible, and are used 
directly by CongkopiGUI.
*/



void emptyVillage(Player emptier, Player opponent, int villageIndex) {
	// Check that villageIndex is a proper value.
	if (villageIndex < 0 || villageIndex > emptier.villages.length) {
		throw new IndexOutOfBoundsException(
			"Tried to empty non-existent village for " +
			emptier.name + "!"
		);
	}
	
	// Okay, empty that village, and put seeds in hand.
	int seedsInHand = emptier.villages[villageIndex];
	emptier.villages[villageIndex] = 0;
	
	// Start emptying our hand.
	int currentVillageIndex = villageIndex;
	boolean onOpposingSide = false;
	Player playerForNextTurn = opponent;
	
	while (seedsInHand > 1) {
		--currentVillageIndex;
		
		if (!onOpposingSide) {
			if (currentVillageIndex >= 0) {
				// Usual scenario. Just drop a seed.
				++emptier.villages[currentVillageIndex];
				--seedsInHand;
			}
			else if (currentVillageIndex == -1) {
				// We've reached emptier's house.
				++emptier.house;
				--seedsInHand;
				// We still have seeds, so let's switch over.
				onOpposingSide = true;
				currentVillageIndex = opponent.villages.length;
			}
		}
		else {
			if (currentVillageIndex >= 0) {
				// Giving seeds to opponent...
				++opponent.villages[currentVillageIndex];
				--seedsInHand;
			}
			else if (currentVillageIndex == -1) {
				// We've reached opponent's house.
				++opponent.house;
				--seedsInHand;
				// We still have even more seeds, so 
				// let's switch over again.
				onOpposingSide = false;
				currentVillageIndex = emptier.villages.length;
			}
		}
	}
	
	
	// Okay, we're on our last seed.
	if (!onOpposingSide) {
		if (currentVillageIndex >= 0) {
			// Putting last seed in one of emptier's villages.
			++emptier.villages[currentVillageIndex];
			
			if (emptier.villages[currentVillageIndex] == 1) {
				/*
				We just dropped our last seed in
				an empty village. Capture opponents' seeds!
				*/
				int opposingVillageIndex =
					opponent.villages.length - 1 - currentVillageIndex;
				
				emptier.house += opponent.villages[opposingVillageIndex];
				opponent.villages[opposingVillageIndex] = 0;			
			}
		}
		else if (currentVillageIndex == 0) {
			// Putting last seed in emptier's house!
			++emptier.house;
			playerForNextTurn = emptier;
		}
	}
	else {
		if (currentVillageIndex > 0) {
			++opponent.villages[currentVillageIndex];
			// Do we still follow capture rule if we are
			// on opposing side?
		}
		else {
			// Putting last seed in opponent's house.
			++opponent.house;
		}
	}
	
	
	// Okay, update 'playerForThisTurn' for next turn.
	playerForThisTurn = playerForNextTurn;
}


// Okay, we either have a game method, or the GUI literally just
// uses #emptyVillage and playerForThisTurn to play the game.


}
