package com.kisman.cc.command.commands;

import com.kisman.cc.Kisman;
import com.kisman.cc.command.Command;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;

public class SliderCommand extends Command{
    public SliderCommand() {
        super("slider");
    }

    @Override
    public void runCommand(String s, String[] args) {
        if (args.length < 3) {
            error("Usage: " + getSyntax());
            return;
        }

        double value;

        Module module = Kisman.instance.moduleManager.getModule(args[0]);
        
        if (module == null) {
            error("Module " + args[0] + " does not exist!");
            return;
        }

        Setting setting = Kisman.instance.settingManager.getSettingByName(module, args[1]);

        if (setting == null) {
            error("Setting " + args[1] + " in module " + args[0] + " does not exist!");
            return;
        }

        try {
            value = Double.parseDouble(args[2]);
        } catch (Exception e) {
            error("Value error! <value> isn't double!");
            return;
        }

        try {
            setting.setValDouble(value);
            message("Value of " + args[1] + " changed to " + value);
        } catch (Exception e) {
            error("Usage: " + getSyntax());
        }
    }

    @Override
	public String getDescription() {
		return "Change slider value from modules setting";
	}

	@Override
	public String getSyntax() {
		return "slider <module> <\"slider name\"> <value>";
	}
}
