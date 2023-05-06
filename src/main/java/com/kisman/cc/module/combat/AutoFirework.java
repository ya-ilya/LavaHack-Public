package com.kisman.cc.module.combat;

import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import com.kisman.cc.util.*;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;

import java.util.*;

public class AutoFirework extends Module {
    public static AutoFirework instance;
    
    private final Setting targetRange = new Setting("Target Range", this, 10, 1, 20, true);

    private final Setting delayLine = new Setting("DLine", this, "Delays");

    private final Setting delay = new Setting("Delay", this, 1, 0, 20, true);
    private final Setting trapDelay = new Setting("PlaceDelay", this, 1000, 1, 10000, true);


    private final Setting placeLine = new Setting("PlaceLine", this, "Place");

    private final Setting placeMode = new Setting("PlaceMode", this, "Normal", new ArrayList<>(Arrays.asList("Normal", "Packet")));
    private final Setting rotate = new Setting("Rotate", this, true);
    private final Setting blocksPerTick = new Setting("BlocksPerTick", this, 8, 1, 30, true);
    private final Setting antiScaffold = new Setting("AntiScaffold", this, false);
    private final Setting antiStep = new Setting("AntiStep", this, false);
    private final Setting surroundPlacing = new Setting("SurroundPlacing", this, true);
    private final Setting range = new Setting("Range", this, 4, 1, 5, false);
    private final Setting raytrace = new Setting("RayTrace", this, false);


    private final Setting switchLine = new Setting("SwitchLine", this, "Switch");

    private final Setting switchMode = new Setting("SwitchMode", this, InventoryUtil.Switch.NORMAL);
    private final Setting switchObbyReturn = new Setting("SwitchReturnObby", this, true);
    private final Setting switchFireReturn = new Setting("SwitchReturnFirework", this, true);


    private final Setting pauseLine = new Setting("PauseLine", this, "Pause");

    private final Setting minHealthPause = new Setting("MinHealthPause", this, false);
    private final Setting requiredHealth = new Setting("RequiredHealth", this, 11, 0, 36, true);
    private final Setting pauseWhileEating = new Setting("PauseWhileEating", this, false);
    private final Setting pauseIfHittingBlock = new Setting("PauseIfHittingBlock", this, false);


    private final Setting handLine = new Setting("HandLine", this, "Hand");
    private final Setting fireHand = new Setting("FireworkHand", this, "Default", new ArrayList<>(Arrays.asList("Default", "MainHand", "OffHand")));

    private final TimerUtil trapTimer = new TimerUtil();
    private final TimerUtil delayTimer = new TimerUtil();

    private final Map<BlockPos, Integer> retries = new HashMap<>();
    private final TimerUtil retryTimer = new TimerUtil();
    private boolean didPlace = false;
    private boolean isSneaking;
    private int lastHotbarSlot;
    private int placements = 0;
    private boolean smartRotate = false;
    private BlockPos startPos = null;

    private final AimBot aimBot;
    public EntityPlayer target = null;

    public AutoFirework() {
        super("AutoFirework", Category.COMBAT);

        aimBot = AimBot.instance;
        instance = this;

        register(targetRange);

        register(delayLine);
        register(delay);
        register(trapDelay);

        register(placeLine);
        register(placeMode);
        register(rotate);
        register(blocksPerTick);
        register(antiScaffold);
        register(antiStep);
        register(surroundPlacing);
        register(range);
        register(raytrace);

        register(switchLine);
        register(switchMode);
        register(switchObbyReturn);
        register(switchFireReturn);

        register(pauseLine);
        register(minHealthPause);
        register(requiredHealth);
        register(pauseWhileEating);
        register(pauseIfHittingBlock);

        register(handLine);
        register(fireHand);
    }

    public void onEnable() {
        if (!aimBot.isToggled()) aimBot.setToggled(true);
        aimBot.rotationSpoof = null;
        startPos = EntityUtil.getRoundedBlockPos(mc.player);
        lastHotbarSlot = mc.player.inventory.currentItem;
        retries.clear();
    }

    public void onDisable() {
        aimBot.rotationSpoof = null;
        isSneaking = EntityUtil.stopSneaking(isSneaking);
    }

    public void update() {
        if (mc.player == null || mc.world == null) return;

        if (target != null) super.setDisplayInfo("[" +  target.getDisplayName().getFormattedText() + TextFormatting.GRAY + "]");
        else super.setDisplayInfo("");

        if (needPause()) return;
        if (target != null) {
            BlockPos playerPos = target.getPosition();

            //place trap
            smartRotate = false;
            doTrap();

            {
                //place firework

                if (Math.sqrt(mc.player.getDistanceSq(playerPos.getX(), playerPos.getY(), playerPos.getZ())) <= range.getValDouble()) {
                    //switch
                    final int oldSlot = mc.player.inventory.getBestHotbarSlot();

                    //rotate
                    if (rotate.getValBoolean()) {
                        final double[] pos = EntityUtil.calculateLookAt(target.posX + 0.5, target.posY - 0.5, target.posZ + 0.5, mc.player);

                        aimBot.rotationSpoof = new RotationSpoof((float) pos[0], (float) pos[1]);

                        Random rand = new Random(2);

                        aimBot.rotationSpoof.yaw += (rand.nextFloat() / 100);
                        aimBot.rotationSpoof.pitch += (rand.nextFloat() / 100);
                    }

                    //place
                    EnumFacing facing = null;

                    if (raytrace.getValBoolean()) {
                        RayTraceResult result = mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(target.posX + 0.5, target.posY - 0.5, target.posZ + 0.5));
                        if (result == null || result.sideHit == null) facing = EnumFacing.UP;
                        else facing = result.sideHit;
                    }

                    mc.getConnection().sendPacket(new CPacketPlayerTryUseItemOnBlock(playerPos, facing, fireHand.getValString().equalsIgnoreCase("Default") ? mc.player.getHeldItemOffhand().getItem() == Items.FIREWORKS ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND : fireHand.getValString().equalsIgnoreCase("MainHand") ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND, 0, 0, 0));

                    //switch return
                    if (switchFireReturn.getValBoolean())
                        InventoryUtil.switchToSlot(oldSlot, (InventoryUtil.Switch) switchMode.getValEnum());

                    //reset timer
                    delayTimer.reset();
                    trapTimer.reset();
                }
            }
        } else target = EntityUtil.getTarget(targetRange.getValFloat());
    }

    private boolean needPause() {
        if (pauseWhileEating.getValBoolean() && PlayerUtil.IsEating()) return true;
        if (minHealthPause.getValBoolean() && mc.player.getHealth() + mc.player.getAbsorptionAmount() < requiredHealth.getValDouble()) return true;
        return pauseIfHittingBlock.getValBoolean() && mc.playerController.isHittingBlock && mc.player.getHeldItemMainhand().getItem() instanceof ItemTool;
    }

    private void doTrap() {
        if (check()) return;

        doStaticTrap();

        if (didPlace) trapTimer.reset();
    }

    private void doStaticTrap() {
        final List<Vec3d> placeTargets = BlockUtil.targets(target.getPositionVector(), antiScaffold.getValBoolean(), antiStep.getValBoolean(), surroundPlacing.getValBoolean(), false, false, this.raytrace.getValBoolean());
        placeList(placeTargets);
    }

    private void placeList(final List<Vec3d> list) {
        list.sort((vec3d, vec3d2) -> Double.compare(mc.player.getDistanceSq(vec3d2.x, vec3d2.y, vec3d2.z), mc.player.getDistanceSq(vec3d.x, vec3d.y, vec3d.z)));
        list.sort(Comparator.comparingDouble(vec3d -> vec3d.y));
        for (final Vec3d vec3d3 : list) {
            final BlockPos position = new BlockPos(vec3d3);
            final int placeability = BlockUtil.isPositionPlaceable(position, this.raytrace.getValBoolean());
            if (placeability == 1 && (this.retries.get(position) == null || this.retries.get(position) < 4)) {
                this.placeBlock(position);
                this.retries.put(position, (this.retries.get(position) == null) ? 1 : (this.retries.get(position) + 1));
                this.retryTimer.reset();
            } else {
                if (placeability != 3) continue;
                this.placeBlock(position);
            }
        }
    }

    private boolean check() {
        if (mc.player == null || startPos == null) return false;
        didPlace = false;
        placements = 0;
        final int obbySlot2 = InventoryUtil.findBlock(Blocks.OBSIDIAN, 0, 9);
        if (obbySlot2 == -1) {
            setToggled(false);
            return true;
        }
        final int obbySlot3 = InventoryUtil.findBlock(Blocks.OBSIDIAN, 0, 9);
        if (!super.isToggled()) return true;
        if (!startPos.equals(EntityUtil.getRoundedBlockPos(mc.player))) {
            setToggled(false);
            return true;
        }
        if (retryTimer.passedMillis(2000L)) {
            retries.clear();
            retryTimer.reset();
        }
        if (obbySlot3 == -1) {
            ChatUtil.error(ChatFormatting.RED + "No Obsidian in hotbar, AutoTrap disabling...");
            setToggled(false);
            return true;
        }
        if (mc.player.inventory.currentItem != this.lastHotbarSlot && mc.player.inventory.currentItem != obbySlot3) {
            this.lastHotbarSlot = mc.player.inventory.currentItem;
        }
        isSneaking = EntityUtil.stopSneaking(this.isSneaking);
        target = EntityUtil.getTarget(targetRange.getValFloat());
        return target == null || !trapTimer.passedMillis(trapDelay.getValInt());
    }

    private void placeBlock(final BlockPos pos) {
        if (this.placements < this.blocksPerTick.getValInt() && AutoTrap.mc.player.getDistanceSq(pos) <= MathUtil.square(5.0)) {
            final int originalSlot = AutoTrap.mc.player.inventory.currentItem;
            final int obbySlot = InventoryUtil.findBlock(Blocks.OBSIDIAN, 0, 9);
            final int eChestSot = InventoryUtil.findBlock(Blocks.ENDER_CHEST, 0, 9);

            if (obbySlot == -1 && eChestSot == -1) this.toggle();

            mc.player.inventory.currentItem = ((obbySlot == -1) ? eChestSot : obbySlot);
            mc.playerController.updateController();
            if (this.smartRotate) {
                isSneaking = BlockUtil.placeBlockSmartRotate(pos, EnumHand.MAIN_HAND, true, true, this.isSneaking);
            } else {
                isSneaking = BlockUtil.placeBlockSmartRotate(pos, EnumHand.MAIN_HAND, this.rotate.getValBoolean(), true, this.isSneaking);
            }
            mc.player.inventory.currentItem = originalSlot;
            mc.playerController.updateController();

            this.didPlace = true;
            ++this.placements;
        }
    }
}
