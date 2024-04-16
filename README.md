# Card-Threading

This project is a Java-based multi-threaded card playing simulation, implementing thread-safe classes for cards, players, and a card game. The simulation follows a ring topology where players and card decks form a circular structure. Players aim to collect four cards of the same value to win the game.
## Features

* Thread-safe implementation of Card class
* Thread-safe implementation of Player class
* Ring topology for players and decks
* Automatic detection of winning condition
* Dynamic distribution of cards and deck filling
* Graceful handling of game progression

## Getting Started

To get started with the simulation, follow these steps:

* Clone this repository to your local machine.
* Ensure you have Java Development Kit (JDK) installed.
* Compile the source files using a Java compiler.
* Run the executable CardGame class to start the simulation.

## Usage

Upon running the CardGame class, the simulation will commence with the specified number of players (n). Each player will be assigned four cards initially, and decks will be filled accordingly.

The game progresses as follows:

* Players take turns picking a card from the deck to their left and discarding one to the deck on their right.
* If a player collects four cards of the same value, they immediately declare victory and exit the game.
* The game ends when a player wins or all cards have been exhausted from the deck.
