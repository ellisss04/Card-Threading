package game;

public class Card {
    private final int faceValue;

    /**
     * Creates a card object with a given face value
     * @param faceValue value displayed on the front of the card
     * @throws IllegalArgumentException when the face value is less than 0
     * */
    public Card(int faceValue) throws IllegalArgumentException{
        if (faceValue > 0) {
            this.faceValue = faceValue;
        } else {
            throw new IllegalArgumentException("Face value must be greater than 0.");
        }
    }

    /**
     * @return face value of the card
     * */
    public int getFaceValue(){
        return this.faceValue;
    }
}
