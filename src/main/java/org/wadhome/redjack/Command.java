package org.wadhome.redjack;

enum Command {
    playBasic_100k,
    unknown;

    static Command determine(String commandName) {
        Command[] values = values();
        for (Command command : values) {
            if (command.name().equalsIgnoreCase(commandName)) {
                return command;
            }
        }
        return unknown;
    }
}
