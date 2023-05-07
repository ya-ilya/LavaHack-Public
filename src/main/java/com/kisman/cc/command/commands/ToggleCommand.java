package com.kisman.cc.command.commands;

import com.kisman.cc.Kisman;
import com.kisman.cc.command.Command;
import com.kisman.cc.module.Module;

public class ToggleCommand extends Command{
    public ToggleCommand() {
        super("toggle");
    }

    @Override
    public void runCommand(String s, String[] args) {
        if (args.length == 0) {
            error("Usage: " + getSyntax());
            return;
        }

        Module module = Kisman.instance.moduleManager.getModule(args[0]);

        if (module == null) {
            error("Module " + args[0] + " does not exist!");
            return;
        }

        try {
            module.toggle();
            complete("Module " + module + " has been toggled!");
        } catch (Exception e) {
            error("Usage: " + getSyntax());
        }
    }

    @Override
	public String getDescription() {
		return "toggled modules";
	}

	@Override
	public String getSyntax() {
		return "toggle <module>";
	}
}
