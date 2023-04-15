package com.kisman.cc.command.commands;

import com.kisman.cc.Kisman;
import com.kisman.cc.command.Command;
import com.kisman.cc.config.ConfigLoader;

public class ConfigCommand extends Command {
    public ConfigCommand() {
        super("config");
    }

    @Override
    public void runCommand(String s, String[] args) {
        if (args.length == 0) {
            error("Usage: " + getSyntax());
            return;
        }

        if (args[0].equals("load")) {
            try {
                warning("Start loading configs!");
                ConfigLoader.init();
                message("Loaded config!");
            } catch (Exception e) {
                error("Loaded config is failed!");
                e.printStackTrace();
            }
        } else if (args[0].equals("save")) {
            try {
                warning("Start saving configs!");
                Kisman.instance.configManager.getSaver().init();
                message("Saved config!");
            } catch (Exception e) {
                error("Saving config is failed!");
                e.printStackTrace();
            }
        } else {
            error("Usage: " + getSyntax());
        }
    }

    @Override
    public String getDescription() {
        return "Saving/loading config";
    }

    @Override
    public String getSyntax() {
        return "config <save/load>";
    }
}
