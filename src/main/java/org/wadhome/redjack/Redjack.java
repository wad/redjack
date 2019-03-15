package org.wadhome.redjack;

import java.util.Arrays;

import static java.util.stream.Collectors.joining;

// todo: add multithreading

// todo: log true counts from table

// todo: make log at shoe level

class Redjack {

    public static void main(String... args) {
        if (args.length != 1) {
            String listOfCommands = Arrays
                    .stream(Command.values())
                    .map(Enum::toString)
                    .collect(joining(", "));
            System.out.println("Supply a command. One of " + listOfCommands);
            return;
        }

        Command.determine(args[0]).execute();
    }
}
