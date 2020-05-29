
# Specifications

## Engine

### Game must be light/simple

There is no requirement on architecture, features beyond the minimum, etc.
The target is UNIX-style 'worse is better'.

### Compilation must be light/simple

No external libraries, no external compilation tools.
Makefile is provided as convenience - the commands in it should work fine.


## Gameplay

### Plain but customisable graphics

Start with solid colour shape graphics, then/or switch to image graphics.
Colours should be user-customisable.

### 2 players, 7 villages, capture mode, left-aligned

- Firstly, the board is made out of two types of holes that hold seeds. In Malay, holes are called 'lubang', so it's an apt general descriptor.

- Villages are the smaller kind of holes in the board - each player owns the row closest to them. In Malay, they are called 'kampung' (village) or 'rumah anak' (childrens' houses). The number of villages is historically variable, but we will just go with the common '7'.

- Houses are the larger kinds of holes in the board, one for each player - the player owns the one closest to them. Houses serve as the players' seed banks, and the player with the most seeds in their house at the end of the game wins. In Malay, they are called 'rumah' (house) or 'rumah ibu' (mother's house).

- A 2-player board thus is long and thin, with houses on either end and rows of villages in between. Players sit at the sides, not the ends. 

- Left-aligned as in, each player's house is to the left of them, at an end of the board. Players distribute seeds from right to left, towards their house.

- Capture mode as in, while distributing seeds, if a player lands their last seed on an empty village that they own, they capture all of their opponent's seeds on the other side of the board.

# Developer setup

There is only one developer.

## Keian

- Writes code in NEdit.
- Compiles code with javac.
- Writes documentation (e.g. this) in Vi IMproved.
- Is reading [Wikipedia Bahasa Melayu's page on Congkak](https://ms.wikipedia.org/wiki/Congkak) for reference.
- Is on Debian Buster (10.2).
- Is on Common Desktop Environment.

