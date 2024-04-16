package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import game.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

public class DeckTest {
    private static Deck deck;

    @BeforeEach
    public void setUp(){
        deck = new Deck(1);
        for(int i = 1; i < 6; i ++) {
            Card card = mock(Card.class);
            when(card.getFaceValue()).thenReturn(i);
            deck.addCard(card);
        }
    }
    @Test
    public void drawTest() {
        assertDoesNotThrow(() -> {
            assertEquals(deck.draw().getFaceValue(), 5);
        });
        assertDoesNotThrow(() -> {
            deck = new Deck(1);
            assertNull(deck.draw());
        });
    }
    @Test
    public void addCardTest() {
        Card card = mock(Card.class);
        when(card.getFaceValue()).thenReturn(6);

        assertDoesNotThrow(() -> {
            deck.addCard(card);
            assertEquals(deck.draw().getFaceValue(), 6);
        });
    }
    @Test
    public void addTest() {
        assertDoesNotThrow(() -> {
            Card card = mock(Card.class);
            when(card.getFaceValue()).thenReturn(7);

            deck.discard(card);
            for (int i=0; i<5; i++){
                deck.draw();
            }
            assertEquals(deck.draw().getFaceValue(), 7);
        });
    }
    @Test
    public void getNameTest(){
        assertEquals(deck.getName(), "deck1");
    }
    @Test
    public void discardTest(){
        Card card = mock(Card.class);
        when(card.getFaceValue()).thenReturn(1);

        assertDoesNotThrow(() -> {
            deck.discard(card);
        });
    }
    @Test
    public void getOutputFileNameTest(){
        assertEquals(deck.getDeckOutputFile(), "deck1_output.txt");
    }
    @Test
    public void toStringTest(){
        assertEquals(deck.toString(),"1 2 3 4 5 ");
    }
}