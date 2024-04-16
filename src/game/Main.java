package game;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        CardGame game = new CardGame();
        Thread mainThread = new Thread(() -> {
            try {
                game.game();
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        });
        mainThread.start();
    }
}