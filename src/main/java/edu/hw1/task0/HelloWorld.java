package edu.hw1.task0;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HelloWorld {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void print() {
        LOGGER.info("Привет, мир!");
    }
}
