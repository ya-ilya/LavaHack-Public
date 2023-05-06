package com.kisman.cc.command.commands;

import com.kisman.cc.Kisman;
import com.kisman.cc.command.Command;
import com.kisman.cc.setting.Setting;

public class SliderCommand extends Command{
    public SliderCommand() {
        super("slider");
    }

    @Override
    public void runCommand(String s, String[] args) {
        String module;
        String name;
        double value;

        try {
            module = args[0];
            name = args[1];
        } catch (Exception e) {
            error("Usage: " + getSyntax());
            return;
        }

        try {
            Kisman.instance.moduleManager.getModule(module);
        } catch (Exception e) {
            error("Module " + module + " does not exist!");
            return;
        }

        try {
            Kisman.instance.settingManager.getSettingByName(Kisman.instance.moduleManager.getModule(module), name).getValDouble();
        } catch (Exception e) {
            String oldName = name;
            for (Setting set : Kisman.instance.settingManager.getSettingsByMod(Kisman.instance.moduleManager.getModule(module))) {
                String updString = set.getName().replace(" ", "");
                if (updString.equalsIgnoreCase(name)) {
                    name = updString;
                    break;
                }
            }
            if (!oldName.equalsIgnoreCase(name)) {
                error("Setting " + name + " in module " + module + " does not exist!");
                return;
            }
        }

        try {
            value = Double.parseDouble(args[2]);
        } catch (Exception e) {
            error("Value error! <value> isnt double!");
            return;
        }

        try {
            if (Kisman.instance.settingManager.getSettingByName(Kisman.instance.moduleManager.getModule(module), name) != null){
                Kisman.instance.settingManager.getSettingByName(Kisman.instance.moduleManager.getModule(module), name).setValDouble(value);
                message("Value " + name + " changed to " + value);
            } else {
                String parsedName = name.replace('_', ' ');
                if (Kisman.instance.settingManager.getSettingByName(Kisman.instance.moduleManager.getModule(module), parsedName) != null){
                    Kisman.instance.settingManager.getSettingByName(Kisman.instance.moduleManager.getModule(module), parsedName).setValDouble(value);
                    message("Value " + parsedName + " changed to " + value);
                }
            }
        } catch (Exception e) {error("Usage: " + getSyntax());}
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
