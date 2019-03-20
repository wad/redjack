package org.wadhome.redjack.execution;

import org.wadhome.redjack.casino.Casino;

import java.util.Arrays;

import static java.util.stream.Collectors.joining;

public enum Command {
    demo,
    playOneShoe,
    playBasic,
    playHiLoPerfect,
    playHiLoRealisticBuk10k,
    playHiLoPerfectBuk10M,
    playHiLoRealisticBukBut4k,
    playHiLoPerfectBuk,
    unknown;

    public void execute(String[] args) {
        Long seed = null;
        if (args.length > 1) {
            String seedParameter = args[1];
            try {
                seed = Long.valueOf(seedParameter);
            } catch (NumberFormatException ignored) {
                throw new RuntimeException("I don't know what to do with your second parameter: " + seedParameter);
            }
        }

        Execution execution;
        switch (this) {
            case demo:
                execution = new ExecutionDemo();
                break;
            case playOneShoe:
                execution = new ExecutionOneShoe();
                break;
            case playBasic:
                execution = new ExecutionBasic();
                break;
            case playHiLoPerfect:
                execution = new ExecutionHiLoCounterPerfect();
                break;
            case playHiLoRealisticBuk10k:
                execution = new ExecutionRealisticBuk10k();
                break;
            case playHiLoPerfectBuk10M:
                execution = new ExecutionPerfectBuk10M();
                break;
            case playHiLoRealisticBukBut4k:
                execution = new ExecutionRealisticBukBut4k();
                break;
            case playHiLoPerfectBuk:
                execution = new ExecutionPerfectBuk();
                break;
            case unknown:
                return;
            default:
                throw new IllegalStateException("bug");
        }

        execution.setSeedOverride(seed);
        Casino casino = null;
        try {
            casino = execution.execute();
        } finally {
            if (casino != null) {
                casino.closeCasino();
            }
        }
    }

    public static Command determine(String commandName) {
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

    public static String getListOfValidCommands() {
        return Arrays
                .stream(Command.values())
                .map(Enum::toString)
                .collect(joining(", "));
    }
}
