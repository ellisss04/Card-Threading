package game;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Deck extends Writer {
    private volatile ArrayList<Card> deck = new ArrayList<>();
    private final String deckOutputFile;
    private final String name;

    /**
     * When created, the deck object needs to be filled with a {@link Pack game.Pack} object.
     * */
    public Deck(int number) {
        this.name = "deck" + number;
        this.deckOutputFile = this.name + "_output.txt";
    }

    /**
     * @return an Array containing all the Card's in the deck.
     * */
    public ArrayList<Card> getDeck(){
        return this.deck;
    }

    @Override
    public String initialise() throws IOException {
        File file = new File(this.deckOutputFile);
        FileWriter writer = new FileWriter(file);

        writer.write("");
        writer.close();
        return null;
    }

    @Override
    public String toString(){
        String deckStr = "";
        for(Card card: this.deck){
            deckStr += card.getFaceValue() + " ";
        }
        return deckStr;
    }

    /**
     * @return file name for the deck
     * */
    public String getDeckOutputFile(){
        return this.deckOutputFile;
    }

    /**
     * Deck names are in the format 'deckn' where n is a positive integer
     * @return the name of the deck
     * */
    public String getName(){
        return this.name;
    }

    /**
     * This appends a card to the deck. This doesn't add it to the bottom. This is used when filling the deck from a
     * {@link Pack game.Pack}.
     * @param card the card to get inserted into the deck
     * */
    public void addCard(Card card){
        this.deck.add(card);
    }

    /**
     * Pops the top card from the deck.
     * @return game.Card object drawn from the top of the deck
     * @implNote This is thread safe
     * */
    public synchronized Card draw() throws InterruptedException {
        Card card = null;
        if (this.deck.size() > 0){
            card = this.deck.get(this.deck.size()-1);
            this.deck.remove(card);
            this.notifyAll();
        } else {
            this.wait(500);
        }
        return card;
    }

    /**
     * Adds a card to the bottom of the deck.
     * @implNote This is thread safe
     * */
    public synchronized void discard(Card card) {
        this.deck.add(0, card);
        this.notifyAll();
    }
}
