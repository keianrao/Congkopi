
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

public final static int VILLAGE_COUNT = 7;
// Please do not make this very large.


private static class Player {
	String name;
}

private Player perak = new Player();
private Player johor = new Player();
{
	perak.name = "Hang Jebat";
	johor.name = "Hang Tuah";
}
/*
Hard to come up with variable names for symmetrical elements..
*/


private static class Bin {
	private int biji;
	private Player owner;
	void addBiji(int n) { biji += n; }
	void removeBiji(int n) { biji -= n; }
	void setOwner(Player player) { owner = player; }
	Player getOwner() { return owner; }
}
private static class Kampung extends Bin { }
private static class Rumah extends Bin { }

private ArrayList<Bin> bins = new ArrayList<Bin>((VILLAGE_COUNT + 1) * 2);
{
	// Perak.
	Rumah rumah = new Rumah();
	rumah.setOwner(perak);
	bins.add(rumah);
	for (int o = 0; o < VILLAGE_COUNT; ++o) {
		Kampung kampung = new Kampung();
		kampung.setOwner(perak);
		bins.add(kampung);
	}
	
	// Johor.
	rumah = new Rumah();
	rumah.setOwner(johor);
	bins.add(rumah);
	for (int o = 0; o < VILLAGE_COUNT; ++o) {
		Kampung kampung = new Kampung();
		kampung.setOwner(johor);
		bins.add(kampung);
	}
}


public static enum State {
	DISTRIBUTING
}
private State currentState;
private Player currentPlayer;
private boolean distributing;



//  \\  Interface   \\  //  \\  //  \\

State getState() { return currentState; }

Player getPlayerWhoOwns(Bin bin) {
	return bin.getOwner();
}

boolean canDistribute(Bin bin) {
	if (!distributing) return false;
	if (!(bin instanceof Kampung)) return false;
	if (getPlayerWhoOwns(bin) != currentPlayer) return false;
	return true;
}


}
