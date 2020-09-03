
import java.util.List;
import java.util.Collection;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Collections;

/**

Contains data structures, logic, and actions for the 
two-player variant of the game.

**/
public class TwoPlayerBackend {

//	Interface 	//	\\	//	\\	//	\\

public Collection<CommonModels.Biji> getBijiAt(PapanLocation l) {
	return new ArrayList<>(papan.get(l).biji2);
}



public void interactWith(PapanLocation interactionLocation) {
	if (gameState != CommonModels.GameState.WAITING) {
		// Refuse
		return;
	}
	
	switch (interactionLocation) {
		case PEMAIN1_RUMAH: case PEMAIN2_RUMAH:
			// Refuse
			return;
	}
	
	if (currentPlayer != papan.get(interactionLocation).owner) {
		// Refuse but differently
		return;
	}
	
	// Otherwise, the interaction is valid.
	// We'll interpret it as a distribute. Let it roll.
	
	Lubang lubang = papan.get(interactionLocation);
	lastBijiDroppedIn(distribute(lubang));
	
	if (gameIsOver()) {
		gameState = CommonModels.GameState.GAME_OVER;
		return;
	}
	else {
		if (currentPlayer == pemain2) currentPlayer = pemain1;
		else if (currentPlayer == pemain1) currentPlayer = pemain2;
		gameState = CommonModels.GameState.WAITING;
	}
}



//  \\  Interface helpers   \\  //  \\

private Lubang distribute(Lubang lubang) {
	gameState = CommonModels.GameState.DISTRIBUTING;

	// Okay, pick up all the biji from it..
	List<CommonModels.Biji> bijiInHand = new LinkedList<>();
	bijiInHand.addAll(lubang.biji2);
	lubang.biji2.clear();
	
	while (!bijiInHand.isEmpty()) {
		lubang = lubang.nextKampung;
		// Drop off a biji.
		lubang.biji2.add(bijiInHand.remove(0));
	}
	
	return lubang;
}

private void lastBijiDroppedIn(Lubang lubang) {
	gameState = CommonModels.GameState.EVALUATING;
	
	if (lubang.biji2.size() > 1) {
		// Last biji dropped in a non-empty lubang. 
		// Nothing interesting..
		return;
	}
	else {
		// Last biji dropped in an empty lubang.
		assert lubang.biji2.size() == 1;
	
		Lubang oppositeLubang = lubang.oppositeLubang;
		Lubang capturerRumah = null;
		if (lubang.owner == pemain1) {
			capturerRumah = papan.get(PapanLocation.PEMAIN1_RUMAH);
		}
		else if (lubang.owner == pemain2) {
			capturerRumah = papan.get(PapanLocation.PEMAIN2_RUMAH);
		}
		assert capturerRumah != null;
		
		// Capture!
		capturerRumah.biji2.addAll(oppositeLubang.biji2);
		oppositeLubang.biji2.clear();
	}
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



//	Public models 	\\	//	\\	//	\\

public enum PapanLocation {
	PEMAIN1_KAMPUNG1, 
	PEMAIN1_KAMPUNG2, PEMAIN1_KAMPUNG3,
	PEMAIN1_KAMPUNG4, PEMAIN1_KAMPUNG5, PEMAIN1_KAMPUNG6,
	PEMAIN1_KAMPUNG7, PEMAIN1_RUMAH,
	PEMAIN2_KAMPUNG1, PEMAIN2_KAMPUNG2, PEMAIN2_KAMPUNG3,
	PEMAIN2_KAMPUNG4, PEMAIN2_KAMPUNG5, PEMAIN2_KAMPUNG6,
	PEMAIN2_KAMPUNG7, PEMAIN2_RUMAH;
}



//	Structs   	//	\\	//	\\	//	\\

private static class Lubang {
	CommonModels.Pemain owner;
	final Collection<CommonModels.Biji> biji2 = new LinkedList<>();
	Lubang nextLubang;
	Lubang nextKampung;
	Lubang oppositeLubang;
}



//	State 	\\	//	\\	//	\\	//	\\

private CommonModels.GameState gameState;

private CommonModels.Pemain pemain1, pemain2;
private CommonModels.Pemain currentPlayer;

private EnumMap<PapanLocation, Lubang> papan;
// Perfect. How convenient.




//	\\	Constructors  	//	\\	//	\\

public TwoPlayerBackend() {
	gameState = CommonModels.GameState.INITIALISING;
	
	pemain1 = new CommonModels.Pemain();
	pemain2 = new CommonModels.Pemain();
	
	initialisePapan();
	addStartingBiji();
	
	currentPlayer = pemain1;
	gameState = CommonModels.GameState.WAITING;
}



//	Constructor helpers   	\\	//	\\

private void initialisePapan() {
	// Initialise lubangs in the heap as an array..
	// Also, use array math for convenient variable setting.
	Lubang[] lubang2 = new Lubang[(7 + 1) * 2];
	for (int o = 0; o < 8; ++o) {
		lubang2[o] = new Lubang();
		lubang2[o].owner = pemain1;
	}
	for (int o = 8; o < 16; ++o) {
		lubang2[o] = new Lubang();
		lubang2[o].owner = pemain2;
	}
	for (int o = 0; o < 15; ++o) {
		lubang2[o].nextLubang = lubang2[o + 1];
		lubang2[o].nextKampung = lubang2[o + 1];
	}
	
	// I can merge the following block into the above,
	// but I will put it separately here for clarity.
	// Calculate and save 'oppositeLubang'.
	for (int o = 0; o < 16; ++o) {
		/*
		   8   9  10  11  12  13  14
		7                             15
		   6   5   4   3   2   1   0
		   
		f(o) = 15 - (o + 1)
		Working cases: All kampung
		15 - (0 + 1)   =   15 - 1   =   14
		15 - (1 + 1)   =   15 - 2   =   13
		15 - (2 + 1)   =   15 - 3   =   12
		15 - (9 + 1)   =   15 - 10  =   5
		15 - (10 + 1)  =   15 - 11  =   4
		15 - (14 + 1)  =   15 - 15  =   0
		
		Failing cases: Rumah
		15 - (15 + 1)  =   15 - 16  =   -1
		15 - (7 + 1)   =   15 - 8   =   7
		*/
		
		int oo = 15 - (o + 1);
		if (o == 7) oo = 15;
		if (o == 15) oo = 7;
		
		lubang2[o].oppositeLubang = lubang2[oo];
	}
	
	// Now load them into the enum map.	
	papan.put(PapanLocation.PEMAIN1_KAMPUNG1, lubang2[0]);
	papan.put(PapanLocation.PEMAIN1_KAMPUNG2, lubang2[1]);
	papan.put(PapanLocation.PEMAIN1_KAMPUNG3, lubang2[2]);
	papan.put(PapanLocation.PEMAIN1_KAMPUNG4, lubang2[3]);
	papan.put(PapanLocation.PEMAIN1_KAMPUNG5, lubang2[4]);
	papan.put(PapanLocation.PEMAIN1_KAMPUNG6, lubang2[5]);
	papan.put(PapanLocation.PEMAIN1_KAMPUNG7, lubang2[6]);
	papan.put(PapanLocation.PEMAIN1_RUMAH, lubang2[7]);
	papan.put(PapanLocation.PEMAIN2_KAMPUNG1, lubang2[8]);
	papan.put(PapanLocation.PEMAIN2_KAMPUNG2, lubang2[9]);
	papan.put(PapanLocation.PEMAIN2_KAMPUNG3, lubang2[10]);
	papan.put(PapanLocation.PEMAIN2_KAMPUNG4, lubang2[11]);
	papan.put(PapanLocation.PEMAIN2_KAMPUNG5, lubang2[12]);
	papan.put(PapanLocation.PEMAIN2_KAMPUNG6, lubang2[13]);
	papan.put(PapanLocation.PEMAIN2_KAMPUNG7, lubang2[14]);
	papan.put(PapanLocation.PEMAIN2_RUMAH, lubang2[15]);
	
	// Now tie up the ends.
	papan.get(PapanLocation.PEMAIN1_KAMPUNG7)
		.nextKampung = papan.get(PapanLocation.PEMAIN2_KAMPUNG1);
	papan.get(PapanLocation.PEMAIN2_KAMPUNG7)
		.nextKampung = papan.get(PapanLocation.PEMAIN1_KAMPUNG1);
}

private void addStartingBiji() {
	for (PapanLocation l: PapanLocation.values()) {
		if (l == PapanLocation.PEMAIN1_RUMAH) continue;
		if (l == PapanLocation.PEMAIN2_RUMAH) continue;
		// Alternative is to copy array into a list then
		// remove these two locations. The former is annoying.
	
		Collection<CommonModels.Biji> biji2 = papan.get(l).biji2;
		// Initialise 7 biji for each kampung.
		// That's what the rules say.
		for (int i = 7; i > 0; --i) {
			CommonModels.Biji biji = new CommonModels.Biji();
			biji2.add(biji);
		}
	}
	
	// Then, initialise 1 biji for each rumah.
	papan.get(PapanLocation.PEMAIN1_RUMAH).biji2
		.add(new CommonModels.Biji());
	papan.get(PapanLocation.PEMAIN2_RUMAH).biji2
		.add(new CommonModels.Biji());
}

}
