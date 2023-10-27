package edu.project1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameExecutorTest {

    @Test
    @DisplayName("Testing game, all symbols are guessed, duplicate symbols included, expected GameStatus = Winner")
    public void testShouldReturnWinner_1() {
        GameExecutor game = new GameExecutor("kitten");
        game.guess('k');
        game.guess('i');
        game.guess('e');
        game.guess('n');
        game.guess('t');
        GameStatus result = game.status;
        assertEquals(GameStatus.Winner, result);
    }

    @Test
    @DisplayName("Testing game, all symbols are guessed, expected GameStatus = Winner")
    public void testShouldReturnWinner_2() {
        GameExecutor game = new GameExecutor("house");
        game.guess('h');
        game.guess('i');
        game.guess('o');
        game.guess('u');
        game.guess('s');
        game.guess('e');
        GameStatus result = game.status;
        assertEquals(GameStatus.Winner, result);
    }

    @Test
    @DisplayName("Testing game, 5 mistakes, expected GameStatus = Loser")
    public void testShouldReturnLoser() {
        GameExecutor game = new GameExecutor("kitten");
        game.guess('a');
        game.guess('b');
        game.guess('a');
        game.guess('c');
        game.guess('r');
        game.guess('y');
        GameStatus result = game.status;
        assertEquals(GameStatus.Loser, result);
    }

}

