package com.kisman.cc.event.events.client.settings;

import com.kisman.cc.event.Event;
import com.kisman.cc.setting.Setting;

public class SettingChangeEvent extends Event {
    private final Setting setting;
    private final Type type;
    private final Action action;

    public SettingChangeEvent(Setting setting, Type type, Action action) {
        this.setting = setting;
        this.type = type;
        this.action = action;
    }

    public Setting getSetting() {
        return setting;
    }

    public Type getType() {
        return type;
    }

    public Action getAction() {
        return action;
    }

    public static class BooleanSetting extends SettingChangeEvent {
        public BooleanSetting(Setting setting) {
            super(setting, Type.Boolean, Action.Default);
        }
    }

    public static class ModeSetting extends SettingChangeEvent {
        public ModeSetting(Setting setting) {
            super(setting, Type.Boolean, Action.Default);
        }
    }

    public static class ColorSetting extends SettingChangeEvent {
        public ColorSetting(Setting setting) {
            super(setting, Type.Boolean, Action.EndFocused);
        }
    }

    public static class NumberSetting extends SettingChangeEvent {
        public NumberSetting(Setting setting) {
            super(setting, Type.Boolean, Action.EndFocused);
        }
    }

    public enum Type {Boolean, Mode, Color, Number}
    public enum Action {Default, EndFocused, EveryTime}
}
