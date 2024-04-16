package game;

import exceptions.InvalidPackException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;

public class Pack {
    private String packLocation;
    private int numberPlayers;
    private Stack<Card> cards = new Stack<>();

    /**
     * This creates a pack object filled with cards generated from a text file containing the face values. This should
     * contain 8*n values where n is the number of players.
     * @param packLocation the absolute path to the pack location
     * @param numberPlayers the number of players in the game
     * @throws FileNotFoundException when the path provided doesn't exist
     * @throws InvalidPackException when the pack provided is invalid
     * */
    public Pack(String packLocation, int numberPlayers) throws FileNotFoundException, InvalidPackException, IllegalArgumentException {
        this.packLocation = packLocation;
        this.numberPlayers = numberPlayers;
        File file = new File(this.packLocation);
        if (!isValidPack(file)) {
            throw new InvalidPackException("Pack file is not formatted correctly");
        } else{
            this.create(file);
        }
    }

    /**
     * Method checks the pack text file if it's valid or not.
     * @return boolean depending on if the pack is valid
     * @throws FileNotFoundException when the path to the pack file cannot be found
     * */
    private boolean isValidPack(File fileObj) throws FileNotFoundException {
        Scanner myReader = new Scanner(fileObj);
        int count = 0;
        String line;
        while (myReader.hasNextLine()){
            line = myReader.nextLine();
            if(!line.equals("") && Integer.parseInt(line) > 0){
                count++;
            } else{
                return false;
            }
        }
        myReader.close();
        return count == 8 * this.numberPlayers;
    }

    /**
     * Once the pack is deemed valid, the pack is filled with cards generated from the pack text file.
     * @throws FileNotFoundException if the path to the pack text file cannot be found
     * @throws IllegalArgumentException if the face value is less than 0.
     * */
    private void create(File fileObj) throws FileNotFoundException, IllegalArgumentException {
        Scanner myReader = new Scanner(fileObj);

        while (myReader.hasNextLine()){
            int cardValue = myReader.nextInt();
            Card card = new Card(cardValue);
            this.cards.push(card);
        }
        myReader.close();
    }

    /**
     * @return {@link Stack<Card>} of the cards in the pack
     * */
    public Stack<Card> getCards(){
        return this.cards;
    }

    /**
     * @return the top card of the pack
     * */
    public Card getTopCard(){
        return this.cards.pop();
    }
}
