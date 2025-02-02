package com.kisman.cc.module.misc;

import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.util.ChatUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;

public class  VisualRange extends Module {
    private final ArrayList<String> names;
    private final ArrayList<String> newNames;

    public VisualRange() {
        super("VisualRange", Category.MISC);

        this.names = new ArrayList<>();
        this.newNames = new ArrayList<>();
    }

    public void update() {
        this.newNames.clear();
        try {
            for (Entity entity : mc.world.loadedEntityList) if (entity instanceof EntityPlayer && !entity.getName().equalsIgnoreCase(mc.player.getName())) this.newNames.add(entity.getName());
            if (!this.names.equals(this.newNames)) {
                for (String name : this.newNames) if (!this.names.contains(name)) ChatUtil.warning(name + " entered in visual range!");
                for (String name : this.names) if (!this.newNames.contains(name)) ChatUtil.message(name + " left from visual range!");
                this.names.clear();
                this.names.addAll(this.newNames);
            }
        } catch (Exception ignored) {}
    }
}
