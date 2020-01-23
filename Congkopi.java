
/**

Contains the game data structures, logic and actions.

**/
public class Congkopi {

Congkopi runtime;
CongkopiGUI gui;

public static void main(String.. args) {
	// To-do: Craft a quick version of getopt

	runtime = new Congkopi();
	gui = new CongkopiGUI(gui);
}


//  \\  //  \\  //  \\


public final int VILLAGE_COUNT = 7;

int[]
	hangTuahVillages = new int[VILLAGE_COUNT],
	hangJebatVillages = new int[VILLAGE_COUNT];
int
	hangTuahHouse,
	hangJebatHouse;
/*
I have two symmetrical players, don't know what to name them.
So I named them after the two characters of Hikayat Hang Tuah.
*/


emptyHangTuahVillage(int number) {
	if (number < 0 || number > hangTuahVillages.length)
		throw new IndexOutOfBoundsException(
			"Tried to empty non-existent village " +
			"on Hang Tuah's side!"
		);
		
	int seedsInHand = hangTuahVillages[number];
	hangTuahVillages[number] = 0;
	
	int currentVillage = number + 1;
	while (seedsInHand != 0) {
		
	}
	// Be right back, commiting this file.
	// Which order are seeds travelling? That tells us whether
	// currentVillage = hangTuahVillages.length or currentVillage = 0
	// means we've reached the player's house.
}

emptyHangJebatVillage(int number) {
	if (number < 0 || number > hangJebatVillages.length)
		throw new IndexOutOfBoundsException(
			"Tried to empty non-existent village " +
			"on Hang Jebat's side!"
		);
	
}


}
