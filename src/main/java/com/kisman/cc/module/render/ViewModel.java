package com.kisman.cc.module.render;

import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author _kisman_(Value & HandModel & Item Alpha)
 * @author NekoPvP(Item FOV)
 */

public class ViewModel extends Module {
    public static ViewModel instance;

    public final Setting customEating = new Setting("Custom Eating", this, false);
    public final Setting translate = new Setting("Translate", this, true);

    //item FOV
    public final Setting itemFOV = new Setting("ItemFOV", this, false);
    public final Setting fov = new Setting("FOV", this, 130, 70, 200, true).setVisible(itemFOV::getValBoolean);

    //scale
    public final Setting scaleRightX = new Setting("ScaleRigthX", this, 1, -2, 2, false);
    public final Setting scaleRightY = new Setting("ScaleRigthY", this, 1, -2, 2, false);
    public final Setting scaleRightZ = new Setting("ScaleRigthZ", this, 1, -2, 2, false);
    public final Setting scaleLeftX = new Setting("ScaleLeftX", this, 1, -2, 2, false);
    public final Setting scaleLeftY = new Setting("ScaleLeftY", this, 1, -2, 2, false);
    public final Setting scaleLeftZ = new Setting("ScaleLeftZ", this, 1, -2, 2, false);

    //auto rotate
    public final Setting autoRotateRigthX = new Setting("AutoRotateRigthX", this, false);
    public final Setting autoRotateRigthY = new Setting("AutoRotateRigthY", this, false);
    public final Setting autoRotateRigthZ = new Setting("AutoRotateRigthZ", this, false);
    public final Setting autoRotateLeftX = new Setting("AutoRotateLeftX", this, false);
    public final Setting autoRotateLeftY = new Setting("AutoRotateLeftY", this, false);
    public final Setting autoRotateLeftZ = new Setting("AutoRotateLeftZ", this, false);

    //hand pos modifier
    private final Setting handLine = new Setting("HandLine", this, "Hand");
    public final Setting hands = new Setting("Hands", this, false);
    public final Setting handRightX = new Setting("HandRightX", this, 0, -4, 4, false).setVisible(hands::getValBoolean);
    public final Setting handRightY = new Setting("HandRightY", this, 0, -4, 4, false).setVisible(hands::getValBoolean);
    public final Setting handRightZ = new Setting("HandRightZ", this, 0, -4, 4, false).setVisible(hands::getValBoolean);
    public final Setting handRightRotateX = new Setting("HandRotateRightX", this, 0, 0, 360, false).setVisible(hands::getValBoolean);
    public final Setting handRightRotateY = new Setting("HandRotateRightY", this, 0, 0, 360, false).setVisible(hands::getValBoolean);
    public final Setting handRightRotateZ = new Setting("HandRotateRightZ", this, 0, 0, 360, false).setVisible(hands::getValBoolean);
    public final Setting handRightScaleX = new Setting("HandScaleRightX", this, 0, -2, 2, false).setVisible(hands::getValBoolean);
    public final Setting handRightScaleY = new Setting("HandScaleRightY", this, 0, -2, 2, false).setVisible(hands::getValBoolean);
    public final Setting handRightScaleZ = new Setting("HandScaleRightZ", this, 0, -2, 2, false).setVisible(hands::getValBoolean);
    public final Setting handLeftX = new Setting("HandLeftX", this, 0, -4, 4, false).setVisible(hands::getValBoolean);
    public final Setting handLeftY = new Setting("HandLeftY", this, 0, -4, 4, false).setVisible(hands::getValBoolean);
    public final Setting handLeftZ = new Setting("HandLeftZ", this, 0, -4, 4, false).setVisible(hands::getValBoolean);
    public final Setting handLeftRotateX = new Setting("HandRotateLeftX", this, 0, 0, 360, false).setVisible(hands::getValBoolean);
    public final Setting handLeftRotateY = new Setting("HandRotateLeftY", this, 0, 0, 360, false).setVisible(hands::getValBoolean);
    public final Setting handLeftRotateZ = new Setting("HandRotateLeftZ", this, 0, 0, 360, false).setVisible(hands::getValBoolean);
    public final Setting handLeftScaleX = new Setting("HandScaleLeftX", this, 0, -2, 2, false).setVisible(hands::getValBoolean);
    public final Setting handLeftScaleY = new Setting("HandScaleLeftY", this, 0, -2, 2, false).setVisible(hands::getValBoolean);
    public final Setting handLeftScaleZ = new Setting("HandScaleLeftZ", this, 0, -2, 2, false).setVisible(hands::getValBoolean);

    //custom items alpha
    private final Setting itemLine = new Setting("ItemLine", this, "Item");
    public final Setting useAlpha = new Setting("Use Custom Alpha", this, false);
    public final Setting alpha = new Setting("Alpha", this, 255, 0, 255, true).setVisible(useAlpha::getValBoolean);


    public ViewModel() {
        super("ViewModel", "modeL vieM", Category.RENDER);
        instance = this;

        settingManager.register(customEating);
        settingManager.register(translate);

        settingManager.register(itemFOV);
        settingManager.register(fov);

        settingManager.register(new Setting("RightX", this, 0, -2, 2, false).setVisible(translate::getValBoolean));
        settingManager.register(new Setting("RightY", this, 0, -2, 2, false).setVisible(translate::getValBoolean));
        settingManager.register(new Setting("RightZ", this, 0, -2, 2, false).setVisible(translate::getValBoolean));
        settingManager.register(new Setting("RotateRightX", this, 0, 0, 360, false));
        settingManager.register(new Setting("RotateRightY", this, 0, 0, 360, false));
        settingManager.register(new Setting("RotateRightZ", this, 0, 0, 360, false));
        settingManager.register(autoRotateRigthX);
        settingManager.register(autoRotateRigthY);
        settingManager.register(autoRotateRigthZ);
        settingManager.register(scaleRightX);
        settingManager.register(scaleRightY);
        settingManager.register(scaleRightZ);

        settingManager.register(new Setting("LeftX", this, 0, -2, 2, false).setVisible(translate::getValBoolean));
        settingManager.register(new Setting("LeftY", this, 0, -2, 2, false).setVisible(translate::getValBoolean));
        settingManager.register(new Setting("LeftZ", this, 0, -2, 2, false).setVisible(translate::getValBoolean));
        settingManager.register(new Setting("RotateLeftX", this, 0, 0, 360, false));
        settingManager.register(new Setting("RotateLeftY", this, 0, 0, 360, false));
        settingManager.register(new Setting("RotateLeftZ", this, 0, 0, 360, false));
        settingManager.register(autoRotateLeftX);
        settingManager.register(autoRotateLeftY);
        settingManager.register(autoRotateLeftZ);
        settingManager.register(scaleLeftX);
        settingManager.register(scaleLeftY);
        settingManager.register(scaleLeftZ);

        settingManager.register(handLine);
        settingManager.register(hands);
        settingManager.register(handRightX);
        settingManager.register(handRightY);
        settingManager.register(handRightZ);
        settingManager.register(handRightRotateX);
        settingManager.register(handRightRotateY);
        settingManager.register(handRightRotateZ);
        settingManager.register(handRightScaleX);
        settingManager.register(handRightScaleY);
        settingManager.register(handRightScaleZ);
        settingManager.register(handLeftX);
        settingManager.register(handLeftY);
        settingManager.register(handLeftZ);
        settingManager.register(handLeftRotateX);
        settingManager.register(handLeftRotateY);
        settingManager.register(handLeftRotateZ);
        settingManager.register(handLeftScaleX);
        settingManager.register(handLeftScaleY);
        settingManager.register(handLeftScaleZ);

        settingManager.register(itemLine);
        settingManager.register(useAlpha);
        settingManager.register(alpha);
    }

    public void hand(EnumHandSide side) {
        switch (side) {
            case RIGHT: {
                {
                    glTranslated(handRightX.getValDouble(), handRightY.getValDouble(), handRightZ.getValDouble());
                    glRotated(handRightRotateX.getValDouble(), 1, 0, 0);
                    glRotated(handRightRotateY.getValDouble(), 0, 1, 0);
                    glRotated(handRightRotateZ.getValDouble(), 0, 0, 1);
                    glScaled(handRightScaleX.getValDouble(), handRightScaleY.getValDouble(), handRightScaleZ.getValDouble());
                }
                break;
            }
            case LEFT: {
                {
                    glTranslated(handLeftX.getValDouble(), handLeftY.getValDouble(), handLeftZ.getValDouble());
                    glRotated(handLeftRotateX.getValDouble(), 1, 0, 0);
                    glRotated(handLeftRotateY.getValDouble(), 0, 1, 0);
                    glRotated(handLeftRotateZ.getValDouble(), 0, 0, 1);
                    glScaled(handLeftScaleX.getValDouble(), handLeftScaleY.getValDouble(), handLeftScaleZ.getValDouble());
                }
                break;
            }
        }
    }

    @SubscribeEvent public void onItemFOV(EntityViewRenderEvent.FOVModifier event) {if (itemFOV.getValBoolean()) event.setFOV(fov.getValFloat());}
}
