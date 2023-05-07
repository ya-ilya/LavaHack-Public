package com.kisman.cc.command.commands;

import com.kisman.cc.command.Command;
import com.kisman.cc.command.CommandManager;

public class HelpCommand extends Command {
    public HelpCommand() {
        super("help");
    }

    public void runCommand(String s, String[] args) {
        message("Commands:");
        for (Command command : CommandManager.commands) {
            message(command.getSyntax());
        }
    }

    public String getDescription() {
        return "help of commands";
    }

    public String getSyntax() {
        return "help";
    }
}
