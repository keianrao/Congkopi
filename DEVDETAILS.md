
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

### 7 villages, 2 player houses, capture mode, left-aligned

- 'Villages' as in Malay 'kampung' - the name for the smaller holes - and 'Houses' as in Malay 'rumah' - the name for the players' seed banks.
- Capture mode as in, a player landing their last seed on an empty village on their sode of the board results in capturing all of the opponents' seeds on the opposite side of the board.
- The number of villages is historically variable, but we will just go with the common '7'.
- Left-aligned as in, each player's house is on the left side of the board from their perspective, and they move their seeds from right to left.

