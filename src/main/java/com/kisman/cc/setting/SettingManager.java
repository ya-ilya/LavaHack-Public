package com.kisman.cc.setting;

import com.kisman.cc.module.Module;

import java.util.ArrayList;

/**
 *  Made by HeroCode
 *  it's free to use
 *  but you have to credit him
 *
 *  @author HeroCode
 */
public class SettingManager {
	
	private final ArrayList<Setting> settings;

	public SettingManager(){
		this.settings = new ArrayList<>();
	}

	public void rSetting(Setting in){
		this.settings.add(in);
	}

	public ArrayList<Setting> getSettings(){
		return this.settings;
	}
	
	public ArrayList<Setting> getSettingsByMod(Module mod) {
		ArrayList<Setting> out = new ArrayList<>();
		for(Setting s : getSettings()) if(s.getParentMod() == mod) out.add(s);
		if(out.isEmpty()) return null;
		return out;
	}

	public Setting getSettingByName(Module mod, String name){
		for(Setting set : getSettings()) {
			if(set.isHud()) return null;
			if(set.getName().equalsIgnoreCase(name) && set.getParentMod() == mod) return set;
		}
		return null;
	}
}