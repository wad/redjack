package org.wadhome.redjack;

// todo: add multithreading

// todo: log true counts from table

// todo: make log at shoe level

// todo: test insurance with card counting

// todo: look at two other hi-lo card-counting basic strategy deviation charts, compare with the one used here

// todo: code up basic strategy tables according to table rule variation. Also update card counting deviations from those.

import org.wadhome.redjack.execution.Command;

class Redjack {

    public static void main(String... args) {
        if (args.length < 1) {
            System.out.println("Valid commands: " + Command.getListOfValidCommands());
            return;
        }

        Command.determine(args[0]).execute(args);
    }
}
