package org.wadhome.redjack;

import java.util.Arrays;

import static java.util.stream.Collectors.joining;

enum Command {
    playOneShoe,
    playBasic,
    play100k,
    unknown;

    void execute() {
        Execution execution;
        switch (this) {
            case playOneShoe:
                execution = new ExecutionOneShoe();
                break;
            case playBasic:
                execution = new ExecutionBasic();
                break;
            case play100k:
                execution = new Execution100k();
                break;
            case unknown:
                return;
            default:
                throw new IllegalStateException("bug");
        }

        Casino casino = null;
        try {
            casino = execution.execute();
        } finally {
            if (casino != null) {
                casino.closeCasino();
            }
        }
    }

    static Command determine(String commandName) {
        Command[] values = values();
        for (Command command : values) {
            if (command.name().equalsIgnoreCase(commandName)) {
                return command;
            }
        }
        System.out.println("Unknown command received: '" + commandName + "'");
        System.out.println("Valid commands: " + getListOfValidCommands());
        return unknown;
    }

    static String getListOfValidCommands() {
        return Arrays
                .stream(Command.values())
                .map(Enum::toString)
                .collect(joining(", "));
    }
}
