package com.kisman.cc.command.commands;

import com.kisman.cc.command.Command;
import com.kisman.cc.module.chat.AntiSpammer;

public class AntiSpamCommand extends Command {
    public AntiSpamCommand() {
        super("antispam");
    }

    @Override
    public void runCommand(String s, String[] args) {
        try {
            if(args[0].equalsIgnoreCase("add")) {
                AntiSpammer.instance.illegalWords.add(args[1]);
                complete(args[1] + " added to AntiSpam list");
            } else if(args[0].equalsIgnoreCase("remove")) {
                AntiSpammer.instance.illegalWords.remove(args[1]);
                complete(args[1] + " removed from AntiSpam list");
            } else if(args[0].equalsIgnoreCase("clear")) {
                AntiSpammer.instance.illegalWords.clear();
                complete("AntiSpam list has been cleared");
            } else if(args[0].equalsIgnoreCase("list")) {
                print("AntiSpam list:");
                for(String str : AntiSpammer.instance.illegalWords) print(str);
            }
        } catch (Exception e) {
            error("Usage:" + getDescription());
        }
    }

    @Override
    public String getDescription() {
        return "null";
    }

    @Override
    public String getSyntax() {
        return "antispam <add/remove/list>";
    }
}
