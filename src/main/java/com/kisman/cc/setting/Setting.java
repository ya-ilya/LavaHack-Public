package com.kisman.cc.setting;

import com.kisman.cc.gui.csgo.components.Slider;
import com.kisman.cc.gui.hud.hudmodule.HudModule;
import com.kisman.cc.module.Module;
import com.kisman.cc.util.Colour;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 *  Made by HeroCode
 *  it's free to use
 *  but you have to credit him
 *
 *  @author HeroCode
 */
public class Setting {
	private Supplier<Boolean> visibleSupplier = () -> true;
	private Colour colour;

	private Entity entity;

	private int index = 0;
	private int key = Keyboard.KEY_NONE;
	
	private String name;
	private Module parent;
	private Setting settingParent;
	private HudModule hudParent;
	private final String mode;

	private String title;

	private String stringValue;
	private String defaultStringValue;
	private ArrayList<String> options;
	private Enum<?> optionEnum;
	
	public boolean booleanValue;
	private boolean rainbow;
	private boolean hud = false;
	private boolean opening;
	private boolean onlyOneWord;
	private boolean onlyNumbers;
	private boolean minus;
	private boolean enumCombo = false;

	public double doubleValue;
	private double minValue;
	private double maxValue;

	private ItemStack[] items;

	private int r, g, b, a;

	private float red, green, blue, alpha;

	private boolean onlyInt = false;

	private Slider.NumberType numberType = Slider.NumberType.DECIMAL;

	public Setting(String type) {
		mode = type;
	}

	public Setting(String name, Module parent, int key) {
		this.name = name;
		this.parent = parent;
		this.key = key;
		this.mode = "Bind";
	}

	public Setting(String name, Module parent, Setting settingParent, String title) {
		this.name = name;
		this.parent = parent;
		this.settingParent = settingParent;
		this.title = title;
		this.mode = "CategoryLine";
	}

	public Setting(String name, Module parent, String title, boolean open) {
		this.name = name;
		this.parent = parent;
		this.title = title;
		this.opening = open;
		this.mode = "Category";
	}

	public Setting(String name, Module parent, String stringValue, String defaultStringValue, boolean opening) {
		this.name = name;
		this.parent = parent;
		this.stringValue = stringValue;
		this.defaultStringValue = defaultStringValue;
		this.opening = opening;
		this.onlyOneWord = false;
		this.minus = true;
		this.onlyNumbers = false;
		this.mode = "String";
	}

	public Setting(String name, Module parent, String stringValue, String defaultStringValue, boolean opening, boolean onlyOneWord) {
		this.name = name;
		this.parent = parent;
		this.stringValue = stringValue;
		this.defaultStringValue = defaultStringValue;
		this.opening = opening;
		this.onlyOneWord = onlyOneWord;
		this.minus = true;
		this.onlyNumbers = false;
		this.mode = "String";
	}

	public Setting(String name, Module parent, String title) {
		this.name = name;
		this.title = title;
		this.parent = parent;
		this.mode = "Line";
	}

	public Setting(String name, Module parent, String stringValue, ArrayList<String> options){
		this.name = name;
		this.parent = parent;
		this.stringValue = stringValue;
		this.options = options;
		this.optionEnum = null;
		this.mode = "Combo";
	}

	public Setting(String name, Module parent, String stringValue, List<String> options){
		this.name = name;
		this.parent = parent;
		this.stringValue = stringValue;
		this.options = new ArrayList<>(options);
		this.optionEnum = null;
		this.mode = "Combo";
	}

	public Setting(String name, Module parent, Enum<?> options){
		this.name = name;
		this.parent = parent;
		this.stringValue = options.name();
		this.options = null;
		this.optionEnum = options;
		this.enumCombo = true;
		this.mode = "Combo";
	}
	
	public Setting(String name, Module parent, boolean booleanValue){
		this.name = name;
		this.parent = parent;
		this.booleanValue = booleanValue;
		this.mode = "Check";
	}

	public Setting(String name, HudModule parent, boolean booleanValue) {
		this.name = name;
		this.hudParent = parent;
		this.booleanValue = booleanValue;
		this.mode = "CheckHud";
		this.hud = true;
	}

	public Setting(String name, Module parent, float red, float green, float blue, float alpha) {
		this.name = name;
		this.parent = parent;
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
		this.mode = "ExampleColor";
	}

	public Setting(String name, Module parent, double doubleValue, double minValue, double maxValue, Slider.NumberType numberType){
		this.name = name;
		this.parent = parent;
		this.doubleValue = doubleValue;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.onlyInt = numberType.equals(Slider.NumberType.INTEGER);
		this.mode = "Slider";
		this.numberType = numberType;
	}

	public Setting(String name, Module parent, double doubleValue, double minValue, double maxValue, boolean onlyInt){
		this.name = name;
		this.parent = parent;
		this.doubleValue = doubleValue;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.onlyInt = onlyInt;
		this.mode = "Slider";
		this.numberType = onlyInt ? Slider.NumberType.INTEGER : Slider.NumberType.DECIMAL;
	}

	public Setting(String name, Module parent, String title, Colour colour) {
		this.name = name;
		this.parent = parent;
		this.title = title;
		this.colour = colour;
		this.r = colour.r;
		this.g = colour.g;
		this.b = colour.b;
		this.a = colour.a;
		this.mode = "ColorPicker";
	}

	public Setting(String name, Module parent, String title, Entity entity) {
		this.name = name;
		this.parent = parent;
		this.title = title;
		this.entity = entity;
		this.mode = "Preview";
	}

	public Setting(String name, Module parent, String title, ItemStack[] items) {
		this.name = name;
		this.parent = parent;
		this.title = title;
		this.items = items;
		this.mode = "Items";
	}

	public Enum<?> getEnumByName() {
		if (optionEnum == null) return null;
		Enum<?> enumVal = optionEnum;
		String[] values = Arrays.stream(enumVal.getClass().getEnumConstants()).map(Enum::name).toArray(String[]::new);
		return Enum.valueOf(enumVal.getDeclaringClass(), values[index]);
	}

	@Override
	public boolean equals(Object obj) {
		if (isCombo()) return stringValue.equals(obj);
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(colour, entity, index, key, name, parent, settingParent, hudParent, mode, title, stringValue, defaultStringValue, options, optionEnum, booleanValue, rainbow, hud, opening, onlyOneWord, onlyNumbers, minus, enumCombo, doubleValue, r, g, b, a, red, green, blue, alpha, onlyInt);
		result = 31 * result + Arrays.hashCode(items);
		return result;
	}

	@Override
	public String toString() {
		if (isCombo()) return getValString();
		if (isCheck()) return String.valueOf(getValBoolean());
		if (isSlider()) return String.valueOf(onlyInt ? getValInt() : getValDouble());
		if (isString()) return getValString();
		if (isColorPicker()) return get32BitString((colour != null) ? colour.getRGB() : new Color(1f, 1f, 1f).getRGB()) + "-" + rainbow;
		return super.toString();
	}

	/**
	 * Returns the Integers value as a full 32bit hex string:
	 * <p>
	 * <p>get32BitString(-1) -> "FFFFFFFF"
	 * <p>get32BitString(0) -> "00000000"
	 * <p>get32BitString(128) -> "00000080"
	 * <p>...
	 *
	 * @param value the integer to get the 32bit string from.
	 * @return a 32bit string representing the integers value.
	 */
	private static String get32BitString(int value) {
		StringBuilder r = new StringBuilder(Integer.toHexString(value));
		while (r.length() < 8) r.insert(0, 0);
		return r.toString().toUpperCase();
	}

	public boolean isNoneKey() {return key == Keyboard.KEY_NONE;}

	public boolean checkValString(String str) {
		return stringValue.equalsIgnoreCase(str);
	}

	public boolean isVisible() {
		return visibleSupplier.get();
	}

	public Setting setVisible(Supplier<Boolean> supplier) {
		visibleSupplier = supplier;

		return this;
	}

	public Setting setVisible(boolean visible) {
		visibleSupplier = () -> visible;
		return this;
	}

	public String[] getStringValues() {
		if (!enumCombo) return options.toArray(new String[0]);
		else return Arrays.stream(optionEnum.getClass().getEnumConstants()).map(Enum::name).toArray(String[]::new);
	}

	public String getStringFromIndex(int index) {
		if (index != -1) return getStringValues()[index];
		else return "";
	}

	public int getSelectedIndex() {
		String[] modes = getStringValues();
		int object = 0;

		for (int i = 0; i < modes.length; i++) {
			String mode = modes[i];

			if (mode.equalsIgnoreCase(stringValue)) object = i;
		}

		return object;
	}

	public boolean isOnlyInt() {
		return onlyInt;
	}


	public Slider.NumberType getNumberType() {
		return numberType;
	}

	public float getRed() {
		return red;
	}

	public void setRed(float red) {
		this.red = red;
	}

	public float getGreen() {
		return green;
	}

	public void setGreen(float green) {
		this.green = green;
	}

	public float getBlue() {
		return blue;
	}

	public void setBlue(float blue) {
		this.blue = blue;
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	public boolean isOnlyNumbers() {
		return onlyNumbers;
	}

	public ItemStack[] getItems() {
		return items;
	}

	public void setItems(ItemStack[] items) {
		this.items = items;
	}

	public Entity getEntity() {
		return entity;
	}

	public int getValInt() {
		return (int) this.doubleValue;
	}

	public boolean isOnlyOneWord() {
		return onlyOneWord;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public Colour getColour() {
		return colour;
	}

	public void setColour(Colour colour) {
		this.colour = colour;
	}

	public Enum<?> getValEnum() {
		return Enum.valueOf(optionEnum.getDeclaringClass(), stringValue);
	}

	public String getDefaultStringValue() {
		return defaultStringValue;
	}

	public boolean isHud() {
		return this.hud;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getName(){
		return name;
	}

	public Setting setName(String name) {
		this.name = name;
		return this;
	}
	
	public Module getParentMod(){
		return parent;
	}
	
	public String getValString(){
		return this.stringValue;
	}
	
	public void setValString(String in){
		this.stringValue = in;
	}
	
	public ArrayList<String> getOptions(){
		return this.options;
	}
	
	public boolean getValBoolean(){
		return this.booleanValue;
	}
	
	public void setValBoolean(boolean in){
		this.booleanValue = in;
	}
	
	public double getValDouble(){
		if (this.onlyInt){
			this.doubleValue = (int) doubleValue;
		}
		return this.doubleValue;
	}

	public float getValFloat() {
		if (onlyInt) {
			doubleValue = (int) doubleValue;
		}

		return (float) doubleValue;
	}

	public long getValLong() {
		if (onlyInt) {
			doubleValue = (int) doubleValue;
		}

		return (long) doubleValue;
	}

	public void setValDouble(double in){
		this.doubleValue = in;
	}
	
	public double getMinValue(){
		return this.minValue;
	}
	
	public double getMaxValue(){
		return this.maxValue;
	}

	public boolean isRainbow() {
		return this.rainbow;
	}

	public void setRainbow(boolean rainbow) {
		this.rainbow = rainbow;
	}

	public boolean isItems() { return mode.equalsIgnoreCase("Items"); }

	public boolean isPreview() { return mode.equalsIgnoreCase("Preview"); }

	public boolean isBind() { return mode.equalsIgnoreCase("Bind"); }

	public boolean isCategory() { return this.mode.equalsIgnoreCase("Category"); }

	public boolean isString() { return this.mode.equalsIgnoreCase("String"); }

	public boolean isVoid() { return this.mode.equalsIgnoreCase("Void"); }
	
	public boolean isCombo(){
		return this.mode.equalsIgnoreCase("Combo");
	}
	
	public boolean isCheck(){
		return this.mode.equalsIgnoreCase("Check");
	}

	public boolean isSlider(){
		return this.mode.equalsIgnoreCase("Slider");
	}

	public boolean isLine() {
		return this.mode.equalsIgnoreCase("Line");
	}

	public boolean isColorPicker() {
		return this.mode.equalsIgnoreCase("ColorPicker");
	}
}