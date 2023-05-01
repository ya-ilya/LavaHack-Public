package com.kisman.cc.gui.hud.hudmodule.render;

import com.kisman.cc.gui.hud.hudmodule.HudCategory;
import com.kisman.cc.gui.hud.hudmodule.HudModule;
import com.kisman.cc.gui.hud.hudmodule.render.packetchat.Log;
import com.kisman.cc.gui.hud.hudmodule.render.packetchat.Message;
import com.kisman.cc.util.Render2DUtil;
import com.kisman.cc.util.customfont.CustomFontUtil;
import com.kisman.cc.util.gish.ColorUtil;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;

public class PacketChat extends HudModule {

    public static PacketChat Instance;

    private final int width = 200;

    private final String header = "PacketChat";
    private final double borderOffset = 5;

    public Log logs = new Log();

    public PacketChat() {
        super("PacketChat", "", HudCategory.PLAYER);
        logs.activeMessages.add(new Message("lol"));
        logs.activeMessages.add(new Message("lmao"));
        Instance = this;
    }

    public void update() {
        setX(3);
        setY(8);
        setW(width + 4);
        setH(borderOffset * 7 + CustomFontUtil.getFontHeight() * 5);
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        drawRewrite();
    }

    private void drawRewrite() {

        if(logs.activeMessages.size()>=30) logs.activeMessages.clear();
        if(logs.passiveMessages.size()>=30) logs.passiveMessages.clear();

        scrollWheelCheck();

        double x = getX();
        double y = getY();
        double width = getW();
        double height = getH();
        double offset = CustomFontUtil.getFontHeight() + borderOffset;
        int count = 0;


        //draw background
        Render2DUtil.drawRect(x + 3, y + 3, x + width + 3, y + height - 3, (ColorUtil.getColor(33, 33, 42)));
        Render2DUtil.drawRect(x + 3, y, x + width + 3, y + height, (ColorUtil.getColor(33, 33, 42)));
        Render2DUtil.drawRect(x + 2, y + 2, x + width + 2, y + height - 2, (ColorUtil.getColor(45, 45, 55)));
        Render2DUtil.drawRect(x + 2, y, x + width + 2, y + height, (ColorUtil.getColor(45, 45, 55)));
        Render2DUtil.drawRect(x + 1, y + 1, x + width + 1, y + height - 1, (ColorUtil.getColor(60, 60, 70)));
        Render2DUtil.drawRect(x + 1, y, x + width + 1, y + height, (ColorUtil.getColor(60, 60, 70)));
        Render2DUtil.drawRect(x - 3, y - 8, x + width + 3, y + height - 3, (ColorUtil.getColor(33, 33, 42)));
        Render2DUtil.drawRect(x - 3, y, x + width + 3, y + height, (ColorUtil.getColor(33, 33, 42)));
        Render2DUtil.drawRect(x - 2, y - 7, x + width + 2, y + height - 2, (ColorUtil.getColor(45, 45, 55)));
        Render2DUtil.drawRect(x - 2, y, x + width + 2, y + height, (ColorUtil.getColor(45, 45, 55)));
        Render2DUtil.drawRect(x - 1, y - 6, x + width + 1, y + height - 1, (ColorUtil.getColor(60, 60, 70)));
        Render2DUtil.drawRect(x - 1, y, x + width + 1, y + height, (ColorUtil.getColor(60, 60, 70)));
        Render2DUtil.drawRect(x, y - 5, x + width, y + height, (ColorUtil.astolfoColors(100, 100)));
        Render2DUtil.drawRect(x - 3, y - 1, x + width + 3, y + height + 3, (ColorUtil.getColor(33, 33, 42)));
        Render2DUtil.drawRect(x - 2, y - 2, x + width + 2, y + height + 2, (ColorUtil.getColor(45, 45, 55)));
        Render2DUtil.drawRect(x - 1, y - 3, x + width + 1, y + height + 1, (ColorUtil.getColor(60, 60, 70)));
        Render2DUtil.drawRect(x, y - 4, x + width, y + height, (ColorUtil.getColor(34, 34, 40)));

        //draw header
        CustomFontUtil.drawCenteredStringWithShadow(header, x + width / 2, y + borderOffset, ColorUtil.astolfoColors(100, 100));

        int pcount = 0;

        //draws messages
        logs.messageIterator = logs.activeMessages.iterator();
        while(logs.messageIterator.hasNext())
        {
            Message message = logs.messageIterator.next();
            pcount++;

            CustomFontUtil.drawStringWithShadow(message.getMessage(), x + borderOffset, y + CustomFontUtil.getFontHeight() + (offset * count), ColorUtil.astolfoColors(100, 100));
            count++;

            if(pcount>=height/borderOffset)
                up();

            if(logs.activeMessages.size()>=30) logs.activeMessages.clear();
            if(logs.passiveMessages.size()>=30) logs.passiveMessages.clear();

            logs.messageIterator = logs.activeMessages.iterator();
        }
    }

    public void up()
    {
        if(!logs.activeMessages.isEmpty()) {
            logs.passiveMessages.add(logs.activeMessages.get(0));
            logs.activeMessages.remove(0);
        }
    }

    public void down()
    {
        if(!logs.passiveMessages.isEmpty())
        {
            logs.activeMessages.add(0, logs.passiveMessages.get(logs.passiveMessages.size()-1));
            logs.passiveMessages.remove(logs.passiveMessages.size()-1);
        }
    }

    public void scrollWheelCheck() {
        int dWheel = Mouse.getDWheel();
        if(dWheel < 0) {
            up();
        } else if(dWheel > 0) {
            down();
        }
    }
}
