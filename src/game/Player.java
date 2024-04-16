package game;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.SortedMap;

import static game.CardGame.decks;

public class Player extends Writer implements Runnable{
    private final String name;
    private final int number;
    private int drawDeckPointer;
    private int discardDeckPointer;
    private ArrayList<Card> hand = new ArrayList<>();
    private final ArrayList<WinListener> listeners = new ArrayList<>();
    private final String outputFilePath;
    private volatile boolean isRunning = true;

    /**
     * @param name name of the player
     * @param number players associated number
     * */
    public Player(String name, int number){
        this.name = name;
        this.number = number;
        this.outputFilePath = this.name + "_output.txt";
    }

    @Override
    public String initialise() throws IOException {
        File file = new File(outputFilePath);
        FileWriter writer = new FileWriter(file);

        writer.write("");

        String strHand = "";
        for (Card card: this.hand){
            strHand += card.getFaceValue() + " ";
        }
        String outputLine = "\n" + this.name + ": initial hand " + strHand;
        writer.write(outputLine);
        writer.close();
        return outputLine;
    }

    @Override
    public String toString(){
        String strHand = "";
        for (Card card: this.hand){
            strHand += card.getFaceValue() + " ";
        }
        return strHand;
    }

    @Override
    public void run() {
        while (!isWin()){
            if(isRunning){
                System.out.println(this.name + ": " + this);
                int discardChoice = this.pickCard();
                try {
                    this.turn(discardChoice);
                } catch (InterruptedException ignored) {}
            } else{
                break;
            }
        }
    }

    /**
     * Picks an index from the players hand to be discarded
     * @return index of card from players hand to be discarded
     * */
    private int pickCard(){
        int number = this.getNumber();
        ArrayList<Integer> handIndex = new ArrayList<>();
        for(Card card: this.getHand()){
            if(card.getFaceValue() != number){
                handIndex.add(this.getHand().indexOf(card));
            }
        }
        Random random = new Random();
        return handIndex.get(random.nextInt(handIndex.size()));
    }

    /**
     * After each turn the player makes, the object will check its Hand to see if all the values are the same.
     *
     * @return Boolean depending on if the player has won
     * */
    private boolean isWin() {
        List<Integer> valueList = new ArrayList<>();
        for (Card card : this.getHand()) {
            valueList.add(card.getFaceValue());
        }
        boolean isSame = valueList.stream().allMatch(valueList.get(0)::equals);
        if (isSame) {
            this.notifyWinListener();
            return true;
        }
        return false;
    }

    /**
     * Adds a card to the players hand
     * @param card the card to be added to the players hand
     * */
    public void addCard(Card card){
        this.hand.add(card);
    }

    /**
     * Sets an integer value pointer which corresponds to the deck the player draws to
     * @param pointer integer pointer to deck
     * */
    public void setDrawDeckPointer(int pointer){
        this.drawDeckPointer = pointer;
    }

    /**
     * Sets an integer value pointer which corresponds to the deck the player discards to
     * @param pointer integer point to deck
     * */
    public void setDiscardDeckPointer(int pointer){
        this.discardDeckPointer = pointer;
    }

    /**
     * @return players current hand
     * */
    public ArrayList<Card> getHand(){
        return this.hand;
    }

    /**
     * @return the players player number, which is a positive integer
     * */
    public int getNumber(){
        return this.number;
    }

    /**
     * Player names are in the format 'playern' where n is a positive integer
     * @return the name of the player
     * */
    public String getName(){
        return this.name;
    }

    /**
     * @return output file path
     * */
    public String getOutputFilePath(){
        return this.outputFilePath;
    }


    private void turn(int discardChoice) throws InterruptedException {
        synchronized (decks){
            // drawing a card
            Card drawCard = decks.get(this.drawDeckPointer).draw();
            if(drawCard != null){
                this.hand.add(drawCard);
                // discarding a card
                Card discardCard = this.hand.get(discardChoice);
                this.hand.remove(discardCard);
                decks.get(this.discardDeckPointer).discard(discardCard);

                try {
                    this.write("\n" + this.name+" draws a "+drawCard.getFaceValue()+" from deck "+(this.drawDeckPointer + 1), this.outputFilePath);
                    this.write("\n" + this.name+" discards a "+discardCard.getFaceValue()+" to deck "+(this.discardDeckPointer + 1), this.outputFilePath);
                    this.write ("\n" + this.name+" current hand is "+this, this.outputFilePath);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                decks.notifyAll();
            } else{
                // if the draw deck is empty, wait on this resource
                decks.wait(500);
            }
        }
    }

    public void stop(){
        this.isRunning = false;
    }

    private void notifyWinListener(){
        for(WinListener listener: listeners){
            listener.notifyPlayerWon(this);
        }
    }

    public final void addListener(WinListener listener) {
        listeners.add(listener);
    }

    public final void removeListener(final WinListener listener) {
        listeners.remove(listener);
    }
}
