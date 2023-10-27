package edu.project1;

import java.util.Objects;
import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConsoleHangman {
    protected static final Scanner SCANNER = new Scanner(System.in);
    private final static Logger LOGGER = LogManager.getLogger();
    protected static String hiddenWord;
    private boolean isGameActive = true;
    public GameStatus status = GameStatus.Default;

    public ConsoleHangman() {
    }

    @SuppressWarnings("UncommentedMain")
    public static void main(String[] args) {
        ConsoleHangman hangman = new ConsoleHangman();
        hangman.run();
    }

    public void run() {
        greet();
        while (isGameActive) {
            hiddenWord = Dictionary.choseRandomWord();
            askToPlay();
            if (!isGameActive) {
                break;
            }
            GameExecutor game = new GameExecutor(hiddenWord);
            status = game.play();
            filterResult(status);
        }
        SCANNER.close();
    }

    private void filterResult(GameStatus status) {
        LOGGER.info(status.getOutputResult());
        if (status == GameStatus.Surrendered) {
            isGameActive = false;
        } else if (status == GameStatus.Loser) {
            LOGGER.info(ConsoleOutput.ANSWER + hiddenWord);
        }
    }

    private void greet() {
        LOGGER.info(ConsoleOutput.GREETING);
    }

    private void askToPlay() {
        LOGGER.info(ConsoleOutput.ASK_TO_PLAY);
        String userInput = SCANNER.next();
        isGameActive = !Objects.equals(userInput, ConsoleOutput.QUIT);
    }
}
