package com.kisman.cc.module.movement;

import com.kisman.cc.event.events.PacketEvent;
import com.kisman.cc.event.events.PlayerUpdateMoveStateEvent;
import com.kisman.cc.gui.ClickGui;
import com.kisman.cc.gui.console.GuiConsole;
import com.kisman.cc.gui.console.rewrite.ConsoleGui;
import com.kisman.cc.mixin.mixins.accessor.AccessorEntityPlayerSP;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import com.kisman.cc.util.MovementUtil;
import com.kisman.cc.util.PlayerUtil;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemShield;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class NoSlow extends Module {
    private final Setting mode = new Setting("Mode", this, Mode.None);

    private final Setting invMove = new Setting("InvMove", this, true);
    private final Setting items = new Setting("Items", this, true);
    private final Setting ncpStrict = new Setting("NCPStrict", this, true);
    private final Setting slimeBlocks = new Setting("SlimeBlocks", this, true);

    private final Setting ignoreChat = new Setting("IgnoreChat", this, true).setVisible(invMove::getValBoolean);
    private final Setting ignoreConsole = new Setting("IgnoreConsole", this, true).setVisible(invMove::getValBoolean);
    private final Setting ignoreClickGui = new Setting("IgnoreClickGui", this, false).setVisible(invMove::getValBoolean);

    public static NoSlow instance;

    public NoSlow() {
        super("NoSlow", "NoSlow", Category.MOVEMENT);

        instance = this;

        register(mode);

        register(invMove);
        register(items);
        register(ncpStrict);
        register(slimeBlocks);

        register(ignoreChat);
        register(ignoreConsole);
        register(ignoreClickGui);
    }

    public void update() {
        if (mc.player == null || mc.world == null) return;

        if (mc.player.isHandActive() && items.getValBoolean()) {
            if (mc.player.getHeldItem(mc.player.getActiveHand()).getItem() instanceof ItemShield) {
                if (mc.player.movementInput.moveStrafe != 0 || mc.player.movementInput.moveForward != 0 && mc.player.getItemInUseMaxCount() >= 8) {
                    mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
                }
            }
        }

        if (slimeBlocks.getValBoolean()) {
            if (mc.player.getRidingEntity() != null) {
                Blocks.SLIME_BLOCK.setDefaultSlipperiness(0.8f);
            } else {
                Blocks.SLIME_BLOCK.setDefaultSlipperiness(0.6f);
            }
        }

        if (mc.player.isHandActive() && !mc.player.isRiding() && mc.player.fallDistance > 0.7 && PlayerUtil.isMoving(mc.player) && mode.getValString().equals("None")) {
            mc.player.motionX *= 0.9;
            mc.player.motionZ *= 0.9;
        }
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingUpdateEvent event) {
        if (mc.player == null || mc.world == null) return;
        if (mc.player.isHandActive() && !mc.player.isRiding() && mode.getValString().equals("Sunrise")) {
            mc.player.movementInput.moveStrafe *= 0.2F;
            mc.player.movementInput.moveForward *= 0.2F;
            ((AccessorEntityPlayerSP) mc.player).setSprintToggleTimer(0);
        }
        if (mc.player.isHandActive() && !mc.player.isRiding()) {
            if (mc.player.ticksExisted % 2 == 0) {
                if (mc.player.onGround) {
                    if (!mc.player.isSprinting()) MovementUtil.setMotion(MovementUtil.WALK_SPEED - 0.2);
                    else MovementUtil.setMotion(MovementUtil.WALK_SPEED - 0.21);
                } else {
                    mc.player.motionX *= 0.9f;
                    mc.player.motionZ *= 0.9f;
                }
            }
        }
    }

    @SuppressWarnings("unused")
    private final Listener<PlayerUpdateMoveStateEvent> updateMoveStateListener = listener(event -> {
        if (invMove.getValBoolean() && mc.currentScreen != null) {
            if (mc.currentScreen instanceof GuiChat && ignoreChat.getValBoolean()) return;
            if ((mc.currentScreen instanceof GuiConsole || mc.currentScreen instanceof ConsoleGui) && ignoreConsole.getValBoolean()) return;
            if (mc.currentScreen instanceof ClickGui && ignoreClickGui.getValBoolean()) return;

            mc.player.movementInput.moveStrafe = 0.0F;
            mc.player.movementInput.moveForward = 0.0F;

            KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode()));

            if (Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode())) {
                ++mc.player.movementInput.moveForward;
                mc.player.movementInput.forwardKeyDown = true;
            } else mc.player.movementInput.forwardKeyDown = false;

            KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode()));
            if (Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode())) {
                --mc.player.movementInput.moveForward;
                mc.player.movementInput.backKeyDown = true;
            } else mc.player.movementInput.backKeyDown = false;

            KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode()));
            if (Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode())) {
                ++mc.player.movementInput.moveStrafe;
                mc.player.movementInput.leftKeyDown = true;
            } else mc.player.movementInput.leftKeyDown = false;

            KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode()));
            if (Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode())) {
                --mc.player.movementInput.moveStrafe;
                mc.player.movementInput.rightKeyDown = true;
            } else mc.player.movementInput.rightKeyDown = false;

            KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode()));
            mc.player.movementInput.jump = Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode());
        }
    });

    @SuppressWarnings("unused")
    private final Listener<PlayerUpdateMoveStateEvent> updateMoveStateListener1 = listener(event -> {
        if (items.getValBoolean() && mc.player.isHandActive() && !mc.player.isRiding()) {
            mc.player.movementInput.moveForward /= 0.2;
            mc.player.movementInput.moveStrafe /= 0.2;
        }
    });

    @SuppressWarnings("unused")
    private final Listener<PacketEvent.PostSend> packetPostSendListener = listener(event -> {
        if (event.getPacket() instanceof CPacketPlayer) {
            if (ncpStrict.getValBoolean()) {
                if (items.getValBoolean() && mc.player.isHandActive() && !mc.player.isRiding()) {
                    mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, PlayerUtil.GetLocalPlayerPosFloored(), EnumFacing.DOWN));
                }
            }
        }});

    public enum Mode {None, Sunrise}
}
