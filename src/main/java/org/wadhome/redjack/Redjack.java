package org.wadhome.redjack;

// todo: add multithreading

// todo: log true counts from table

// todo: make log at shoe level

class Redjack {

    public static void main(String... args) {
        if (args.length < 1) {
            System.out.println("Valid commands: " + Command.getListOfValidCommands());
            return;
        }

        Command.determine(args[0]).execute(args);
    }
}
