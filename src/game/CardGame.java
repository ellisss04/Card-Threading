package game;

import exceptions.InvalidPackException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CardGame implements WinListener{
    private static ArrayList<Player> players = new ArrayList<>();
    private static int numPlayers;
    private static ArrayList<Thread> threadPool = new ArrayList<>();
    public static final ArrayList<Deck> decks = new ArrayList<>();
    private static Pack pack;
    private static final int handSize = 4;
    private static Player winPlayer;
    public CardGame() {
        // getting user inputs to start the game
        // validating pack and number of players
        boolean isValidNumber = false;

        System.out.println("Enter number of players...");
        Scanner myScanner;
        while (!isValidNumber) {
            try {
                myScanner = new Scanner (System.in);
                numPlayers = myScanner.nextInt();
                isValidNumber = true;
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid number");
            }
        }

        myScanner = new Scanner(System.in);
        System.out.println("Enter location of pack...");
        String packLocation = myScanner.nextLine();
        boolean isValidPack = setupPack(packLocation, numPlayers);

        while(!isValidPack){
            System.out.println("\nEnter location of pack...");
            packLocation = myScanner.nextLine();
            isValidPack = setupPack(packLocation, numPlayers);
        }

        setupGame(numPlayers);
    }

    /**
     * Prints out the initial hands, creates n threads for n players and assigns the listeners to the
     *
     * @throws InterruptedException when a player thread gets interrupted
     * @throws IOException when the players aren't initialised properly
     * */
    public void game() throws InterruptedException, IOException {
        System.out.println("-- INITIAL HANDS --");
        for(Player player: players){
            System.out.print(player.initialise());
            threadPool.add(new Thread(player));
            player.addListener(this);
        }
        System.out.println("\n");

        // starting the player threads
        for(Thread playerThread: threadPool){
            playerThread.start();
        }
    }

    /**
     * Finishes the game and writes to the Deck and Player text files
     * */
    private void finish(){
        // finish the game and write the deck and player contents
        try{
            writeToPlayerFiles();
            writeToDeckFiles();
        } catch (IOException e){
            System.out.println("Error when writing to files" + e.getMessage());
        }
        System.out.println("\n" + winPlayer.getName() + " has won!");
        System.out.println(winPlayer.toString());
    }

    /**
     * Writes to each Deck's respective text file
     * @throws IOException when there is an issue writing to the Deck files
     * */
    private void writeToDeckFiles() throws IOException {
        for(Deck deck: decks){
            deck.write("\n" + deck.getName() + " contents: " + deck, deck.getDeckOutputFile());
        }
    }

    /**
     * Writes to each Player's respective text file
     * @throws IOException when there is an issue writing to the Player files
     * */
    private void writeToPlayerFiles() throws IOException {
        for(Player player: players){
            if (player == winPlayer){
                player.write("\n" + player.getName()+" wins", player.getOutputFilePath());
                player.write(("\n" + player.getName()+" exits"), player.getOutputFilePath());
                player.write("\n" + player.getName()+" final hand "+player, player.getOutputFilePath());
            }else{
                player.write("\n" + winPlayer.getName() + " has informed " + player.getName() + " that " + winPlayer.getName() + " has won", player.getOutputFilePath());
                player.write("\n" + player.getName() + " exits", player.getOutputFilePath());
                player.write("\n" + player.getName() + " final hand " + player, player.getOutputFilePath());
            }
        }
    }

    /**
     * Sets up the hands for all players
     */
    private void setupHands() {
        for (int i = 0; i < handSize; i++) {
            for (Player player : players) {
                player.addCard(pack.getTopCard());
            }
        }
    }

    /**
     * Sets up all decks that are used in the game
     */
    private void setupDecks() {
        while (pack.getCards().size() > 0) {
            for (Deck deck : decks) {
                try {
                    deck.initialise();
                } catch (IOException e) {
                    System.out.println("error when writing to text file: " + e.getMessage());
                }
                deck.addCard(pack.getTopCard());
            }
        }
    }

    /**
     * Sets up a pack from the pack text file
     *
     * @param packLocation  absolute path to the pack text file
     * @param numberPlayers the number of players in the game
     */
    private boolean setupPack(String packLocation, int numberPlayers) {
        try {
            pack = new Pack(packLocation, numberPlayers);
        } catch (FileNotFoundException | InvalidPackException e) {
            System.out.println(e.getMessage());
            return false;
        }
        System.out.println("Success! Pack is valid.");
        return true;
    }

    /**
     * Assigns the Draw and Discard pointers to each player
     * */
    private void setUpDeckPointers(){
        // assigning discard and draw decks to players
        for (int i = 0; i < decks.size(); i++) {
            int discard;
            if (i + 1 < decks.size()) {
                discard = i + 1;
            } else {
                discard = 0;
            }

            // assigning the pointers
            players.get(i).setDrawDeckPointer(i);
            players.get(i).setDiscardDeckPointer(discard);
        }
    }
    /**
     * Sets up everything for the game to start.
     *
     * @param numPlayers the number of players in the game
     * */
    private void setupGame(int numPlayers){
        for (int i=1; i < numPlayers+1; i++){
            String name = "Player " + i;
            players.add(new Player(name, i));
            decks.add(new Deck(i));
        }
        setupHands();
        setupDecks();
        setUpDeckPointers();
    }

    @Override
    public void notifyPlayerWon(Player player) {
        // stopping all the other player threads
        for(Player p: players){
            p.stop();
        }
        winPlayer = player;
        finish();
    }
}
