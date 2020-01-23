
/**

Contains the game data structures, logic and actions.

**/
public class Congkopi {

Congkopi runtime;
CongkopiGUI gui;

public static void main(String.. args) {
	if (args.length != 0) {
		System.err.println(
			"Sorry, this game doesn't accept command-line options yet.. " +
			"There won't be any user configuration through flags - " +
			"the source code has to be tweaked and recompiled."
		);
		System.exit(2);
	}
	// To-do: Craft a quick version of getopt

	runtime = new Congkopi();
	gui = new CongkopiGUI(gui);
}


//  \\  //  \\  //  \\


public final int VILLAGE_COUNT = 7;
// Please do not make this '1000'.

int[]
	hangTuahVillages = new int[VILLAGE_COUNT + 1],
	hangJebatVillages = new int[VILLAGE_COUNT + 1];
/*
These are int's because they represent the number of seeds in them.

The arrays go like this: villages[0] is a player's house.
villages[1] to villages[VILLAGE_COUNT] is a player's villages.
So villages are one-indexed.
*/
/*
I have two symmetrical players, don't know what to name them.
So I named them after the two characters of Hikayat Hang Tuah.
*/


emptyHangTuahVillage(int number) {
	if (number < 0 || number > hangTuahVillages.length) {
		throw new IndexOutOfBoundsException(
			"emptyHangTuahVillage: Tried to empty non-existent village!"
		);
	}
	else if (number == 0) {
		throw new IndexOutOfBoundsException(
			"emptyHangTuahVillage: #0 is the player's house, not a village!"
		);
	}
		
	
	int seedsInHand = hangTuahVillages[number];
	hangTuahVillages[number] = 0;
	
	
	int currentVillage = number;
	while (seedsInHand > 1) {
		--currentVillage;
				
		if (currentVillage >= 0) {
			++hangTuahVillages[currentVillage];
		}
		else if (currentVillage >= -7) {
			++hangJebatVillages[currentVillage + (VILLAGE_COUNT + 1)];
			/*
			For example, assuming VILLAGE_COUNT = 7:
			currentVillage =~ -1 -> ++hangJebatVillages[7]
			currentVillage == -4 -> ++hangJebatVillages[4]
			currentVillage = -7 -> ++hangJebatVillages[1]
			*/
		}
		else {
			/*
			There were so many seeds that we went one full circle.
			Keep seeds in hand. Skip over Hang Jebat's house, and
			reset back to furthest village for Hang Tuah.
			*/
			currentVillage = 7;
		}
	}
	
	// Okay, we are on our last seed.

	if (currentVillage >= 0) {
		++hangTuahVillages[currentVillage];
		if (hangTuahVillages[currentVillage] == 1) {
			// We had just landed on an empty house. Capture!
			hangTuahVillages[0] 
				+= hangJebatVillages[currentVillage + (VILLAGE_COUNT + 1)];
			hangJebatVillages[currentVillage + (VILLAGE_COUNT + 1)] 
				= 0;
		}
	}
	/*
	To-do:
	Do nothing if landed in Hang Jebat's house.
	What to do if landed in Hang Jebat's village?
	How do we implement going another turn
	when Hang Tuah landed in their own house?
	*/
}


emptyHangJebatVillage(int number) {
	if (number < 0 || number > hangJebatVillages.length)
		throw new IndexOutOfBoundsException(
			"Tried to empty non-existent village " +
			"on Hang Jebat's side!"
		);
	
}


}
