package org.wadhome.redjack;

enum Command {
    playBasic_100k,
    strategyCompare_basic_vs_highLow,
    strategyCompare_basic_vs_highLow_one_shoe,
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
