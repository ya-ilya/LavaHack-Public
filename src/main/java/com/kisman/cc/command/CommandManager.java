package com.kisman.cc.command;

import com.kisman.cc.Kisman;
import com.kisman.cc.command.commands.*;
import i.gishreloaded.gishcode.utils.visual.ChatUtils;
import i.gishreloaded.gishcode.wrappers.Wrapper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class CommandManager {
    public static ArrayList<Command> commands = new ArrayList<>();
	
	public char cmdPrefix = ';';
	public String cmdPrefixStr = String.valueOf(cmdPrefix);

	public CommandManager()
	{
		addCommands();
	}

	public void addCommands() {
		commands.add(new AntiSpamCommand());
		commands.add(new BindCommand());
		commands.add(new CreditsCommand());
		commands.add(new FriendCommand());
		commands.add(new HelpCommand());
		commands.add(new ConfigCommand());
        commands.add(new SliderCommand());
        commands.add(new OpenDirCommand());
		commands.add(new PeekCommand());
        commands.add(new ToggleCommand());
		commands.add(new TpCommand());
	}

	public void runCommand(String s) {
		String readString = s.trim().substring(Character.toString(cmdPrefix).length()).trim();
		boolean commandResolved = false;
		boolean hasArgs = readString.trim().contains(" ");
		String commandName = hasArgs ? readString.split(" ")[0] : readString.trim();
		String[] args = hasArgs ? readString.substring(commandName.length()).trim().split(" ") : new String[0];

		for(Command command : commands) {
			if(command.getCommand().trim().equalsIgnoreCase(commandName.trim())) {
				command.runCommand(readString, args);
				commandResolved = true;
				break;
			}
		}
		if(!commandResolved) ChatUtils.error("Cannot resolve internal command: \u00a7c" + commandName);
	}

	@SubscribeEvent
	public void onKeyPressed(InputEvent.KeyInputEvent event) {
		if (Wrapper.INSTANCE.mc().currentScreen != null) return;
		for(Command cmd : commands) if(cmd.getKey() == Keyboard.getEventKey()) {
			Kisman.instance.commandManager.runCommand("." + cmd.getExecute());
		}
	}
}
