package com.kisman.cc.module;

import com.kisman.cc.Kisman;
import com.kisman.cc.setting.Setting;
import com.kisman.cc.setting.SettingManager;
import com.kisman.cc.util.ChatUtil;
import me.zero.alpine.listener.EventHook;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Module {
	protected static Minecraft mc = Minecraft.getMinecraft();
	protected static SettingManager settingManager;

	private String name, description, displayInfo;
	private int key;
	private int priority;
	private final Category category;
	public boolean toggled;
	public boolean subscribes;
	public boolean visible = true;
	public boolean hold = false;
	public boolean block = false;
	private Supplier<String> fun = null;
	private final List<Listener<?>> listeners = new ArrayList<>();

	public Module(String name, Category category) {this(name, "", category, 0, true);}
	public Module(String name, Category category, boolean subscribes) {this(name, "", category, 0, subscribes);}
	public Module(String name, String description, Category category) {this(name, description, category, 0, true);}

	public Module(String name, String description, Category category, int key, boolean subscribes) {
		this.name = name;
		this.description = description;
		this.displayInfo = "";
		this.key = key;
		this.category = category;
		this.toggled = false;
		this.subscribes = subscribes;
		this.priority = 1;

		settingManager = Kisman.instance.settingManager;
	}

	public void setToggled(boolean toggled) {
		if(block) return;
		this.toggled = toggled;
		if (Kisman.instance.init && Kisman.instance.moduleManager.getModule("Notification").isToggled()) ChatUtil.message(TextFormatting.GRAY + "Module " + (isToggled() ? TextFormatting.GREEN : TextFormatting.RED) + getName() + TextFormatting.GRAY + " has been " + (isToggled() ? "enabled" : "disabled") + "!");
		if (this.toggled) {
			enable();
		} else {
			disable();
		}
	}

	public void toggle() {
		if(block) return;
		toggled = !toggled;
		if (Kisman.instance.init && Kisman.instance.moduleManager.getModule("Notification").isToggled()) ChatUtil.message(TextFormatting.GRAY + "Module " + (isToggled() ? TextFormatting.GREEN : TextFormatting.RED) + getName() + TextFormatting.GRAY + " has been " + (isToggled() ? "enabled" : "disabled") + "!");
		if (toggled) {
			enable();
		} else {
			disable();
		}
	}

	private void enable() {
		for (Listener<?> listener : listeners) {
			Kisman.EVENT_BUS.subscribe(listener);
		}

		if (subscribes) MinecraftForge.EVENT_BUS.register(this);
		onEnable();
	}

	private void disable() {
		for (Listener<?> listener : listeners) {
			try {
				Kisman.EVENT_BUS.unsubscribe(listener);
			} catch (Exception ignored) {

			}
		}

		if (subscribes) MinecraftForge.EVENT_BUS.unregister(this);
		onDisable();
	}

	public Setting register(Setting setting) {
		settingManager.register(setting);
		return setting;
	}

	public <T> Listener<T> listener(EventHook<T> hook) {
		Listener<T> listener = new Listener<>(hook);
		listeners.add(listener);
		return listener;
	}

	public String getDescription() {return description;}
	public void setDescription(String description) {this.description = description;}
	public int getKey() {return key;}
	public int getPriority() {return priority;}
	public void setPriority(int priority) {this.priority = priority;}
	public void setKey(int key) {this.key = key;}
	public boolean isToggled() {return toggled;}
	public void onEnable() { }
	public void onDisable() { }
	public String getName() {return this.name;}
	public Category getCategory() {return this.category;}
	public String getDisplayInfo() {return fun == null ? displayInfo : fun.get();}
	public void setDisplayInfo(String displayInfo) {this.displayInfo = displayInfo;}
	public void setDisplayInfo(Supplier<String> fun) {this.fun = fun;}
	public void update(){}
	public void render(){}
	public void key() {}
	public void key(int key) {}
	public void key(char typedChar, int key) {}
	@Override public String toString() {return getName();}
	public boolean isVisible() {return true;}
	public boolean isBeta() {return false;}
}
