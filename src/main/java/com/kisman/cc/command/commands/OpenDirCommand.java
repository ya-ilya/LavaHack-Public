package com.kisman.cc.command.commands;

import com.kisman.cc.Kisman;
import com.kisman.cc.command.Command;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.io.File;

public class OpenDirCommand extends Command {
    public OpenDirCommand() {
        super("opendir");
    }

    @Override
    public void runCommand(String s, String[] args) {
        try {
            File file = new File(Minecraft.getMinecraft().gameDir + Kisman.fileName);
            Desktop.getDesktop().open(file);
        } catch (Exception e) {
            error("Usage: " + getSyntax());
        }
    }

    @Override
    public String getDescription() {
        return "opening minecraft's directory";
    }

    @Override
    public String getSyntax() {
        return "opendir";
    }
}
