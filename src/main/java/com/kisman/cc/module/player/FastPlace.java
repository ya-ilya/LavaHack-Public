package com.kisman.cc.module.player;

import com.kisman.cc.event.Event;
import com.kisman.cc.event.events.PlayerMotionUpdateEvent;
import com.kisman.cc.mixin.mixins.accessor.AccessorMinecraft;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import com.kisman.cc.util.RotationUtils;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

public class FastPlace extends Module {
    public static FastPlace instance;

    private final Setting all = new Setting("All", this, false);

    private final Setting obby = new Setting("Obby", this, false);
    private final Setting enderChest = new Setting("EnderChest", this, false);
    private final Setting crystal = new Setting("Crystal", this, true);
    private final Setting exp = new Setting("Exp", this, true);
    private final Setting minecart = new Setting("Minecart", this, false);
    private final Setting feetExp = new Setting("FeetExp", this, false);
    private final Setting fastCrystal = new Setting("PacketCrystal", this, false);

    private BlockPos mousePos = null;

    public FastPlace() {
        super("FastPlace", Category.PLAYER);

        instance = this;

        register(all);
        register(obby);
        register(enderChest);
        register(crystal);
        register(exp);
        register(minecart);
        register(feetExp);
        register(fastCrystal);
    }

    public void update() {
        if (mc.player == null || mc.world == null) return;

        try {
            if (mc.player.inventory.getStackInSlot(mc.player.inventory.currentItem).getItem().equals(Items.EXPERIENCE_BOTTLE) && this.exp.getValBoolean()) setRightClickDelayTimerZero();
            if (mc.player.inventory.getStackInSlot(mc.player.inventory.currentItem).getItem().equals(Blocks.OBSIDIAN) && this.obby.getValBoolean()) setRightClickDelayTimerZero();
            if (mc.player.inventory.getStackInSlot(mc.player.inventory.currentItem).getItem().equals(Blocks.ENDER_CHEST) && this.enderChest.getValBoolean()) setRightClickDelayTimerZero();
            if (mc.player.inventory.getStackInSlot(mc.player.inventory.currentItem).getItem().equals(Items.MINECART) && this.minecart.getValBoolean()) setRightClickDelayTimerZero();
            if (mc.player.inventory.getStackInSlot(mc.player.inventory.currentItem).getItem().equals(Items.END_CRYSTAL) && (this.crystal.getValBoolean() || this.all.getValBoolean())) setRightClickDelayTimerZero();
            if (this.all.getValBoolean()) setRightClickDelayTimerZero();
        } catch (ArrayIndexOutOfBoundsException ignored) {}

        if (this.fastCrystal.getValBoolean() && mc.gameSettings.keyBindUseItem.isKeyDown()) {
            boolean offhand = (mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL);
            if (offhand || mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) {
                RayTraceResult result = mc.objectMouseOver;
                if (result == null) {
                    return;
                }
                switch (result.typeOfHit) {
                    case MISS: {
                        this.mousePos = null;
                        break;
                    }
                    case BLOCK: {
                        this.mousePos = mc.objectMouseOver.getBlockPos();
                        break;
                    }
                    case ENTITY: {
                        final Entity entity;
                        if (this.mousePos == null || (entity = result.entityHit) == null) {
                            break;
                        }
                        if (!this.mousePos.equals(new BlockPos(entity.posX, entity.posY - 1.0, entity.posZ))) {
                            break;
                        }
                        mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(this.mousePos, EnumFacing.DOWN, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                        break;
                    }
                }
            }
        }
    }
    
    private static void setRightClickDelayTimerZero() {
        ((AccessorMinecraft) mc).setRightClickDelayTimer(0);
    }
    

    @SuppressWarnings("unused")
    private final Listener<PlayerMotionUpdateEvent> motionUpdateListener = listener(event -> {
        if (event.getEra().equals(Event.Era.PRE) && this.feetExp.getValBoolean()) {
            boolean mainHand = mc.player.getHeldItemMainhand().getItem() == Items.EXPERIENCE_BOTTLE;
            boolean offHand = (mc.player.getHeldItemOffhand().getItem() == Items.EXPERIENCE_BOTTLE);
            if (mc.gameSettings.keyBindUseItem.isKeyDown() && ((mc.player.getActiveHand() == EnumHand.MAIN_HAND && mainHand) || (mc.player.getActiveHand() == EnumHand.OFF_HAND && offHand))) {
                RotationUtils.lookAtVec3d(mc.player.getPositionVector());
            }
        }
    });
}
