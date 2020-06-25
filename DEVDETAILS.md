
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

- The game code can assume that the board will be the long and common 2-player one.

- The number of villages is historically variable, but we will just go with the common '7'.

- Left-aligned as in, each player's house is to the left of them, at an end of the board. Players distribute seeds from right to left, towards their house.

- Capture mode as in, while distributing seeds, if a player lands their last seed on an empty village that they own, they capture all of their opponent's seeds on the other side of the board.

# Developer setup

There is only one developer.

## Keian

- Writes code and documentation in NEdit.
- Compiles code with javac.
- Is reading [Wikipedia Bahasa Melayu's page on Congkak](https://ms.wikipedia.org/wiki/Congkak) for reference.
- Is on Debian Buster (10.2 stable).
- Is on Common Desktop Environment.