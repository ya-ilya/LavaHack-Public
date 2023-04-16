package com.kisman.cc.module.combat;

import com.kisman.cc.gui.csgo.components.Slider.NumberType;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import com.kisman.cc.util.BlockUtil;
import com.kisman.cc.util.EntityUtil;
import com.kisman.cc.util.PlayerUtil;
import com.kisman.cc.util.process.DynamicTrapUtil;
import com.kisman.cc.util.process.DynamicTrapUtil.RewriteRotateModes;
import com.kisman.cc.util.process.DynamicTrapUtil.RewriteSupportModes;
import com.kisman.cc.util.process.DynamicTrapUtil.RewriteSwitchModes;
import com.kisman.cc.util.process.PacketMineUtil;
import i.gishreloaded.gishcode.utils.TimerUtils;
import i.gishreloaded.gishcode.utils.visual.ChatUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class CevBreaker extends Module {
    //another
    private final Setting range = new Setting("Range", this, 4, 1, 5, false);
    private final Setting targetRange = new Setting("Target Range", this, 15, 1, 30, true);
    private final Setting switch_ = new Setting("Switch Mode", this, RewriteSwitchModes.Silent);

    //crystal
    private final Setting placeDelay = new Setting("Crystal Place Delay", this, 10, 0, 5000, NumberType.TIME);
    private final Setting breakDelay = new Setting("Crystal Break Delay", this, 10, 0, 5000, NumberType.TIME);

    //obby
    private final Setting breakMode = new Setting("Obby Break Mode", this, ObbyBreakMode.Packet); 

    //trap
    private final Setting blocksPerTick = new Setting("Trap Blocks Per Tick", this, 8, 1, 30, true);
    private final Setting antiStep = new Setting("Trap Anti Step", this, false);
    private final Setting surroundPlacing = new Setting("Trap Surround Placing", this, true);
    private final Setting raytrace = new Setting("Trap RayTrace", this, false);
    private final Setting packet = new Setting("Trap Packet Place", this, true);
    private final Setting dynamic = new Setting("Trap Rewrite Dynamic", this, false);
    private final Setting supportBlocks = new Setting("Trap Support Blocks", this, RewriteSupportModes.Dynamic);
    private final Setting rewriteRetries = new Setting("Trap Retries", this, 0, 0, 20, true);
    private final Setting rotateMode = new Setting("Trap Reite Rotate Mode", this, RewriteRotateModes.Silent);

    //packet break mode
    private final Setting packetMineRange = new Setting("Packet Break Range", this, 10, 5, 15, true);
    private final Setting speed = new Setting("Packet Break Speed", this, 0.8f, 0.1f, 1, false);
    public final Setting instant = new Setting("Packet Break Instant", this, true);
    private final Setting strict = new Setting("Packet Break Strict", this, false);
    private final Setting instantAttempts = new Setting("Packet Break Instant Attempts", this, 8, 0, 20, true);
    private final Setting packetSpam = new Setting("Packet Break Packet Spam", this, 1, 1, 10, true);
    private final Setting autoSwitch = new Setting("Auto Switch", this, true);

    private CevBreakerStage stage;

    private DynamicTrapUtil trap = new DynamicTrapUtil();
    private PacketMineUtil mine = new PacketMineUtil();
    private EntityPlayer target;
    private boolean canTopTrap;
    private BlockPos posToUse;

    private BlockPos currentPos;
    private final TimerUtils timer = new TimerUtils();
    private long start;
    private int oldSlot, delay, rebreakCount;
    private boolean swap = false, checked, strictCheck;

    public CevBreaker() {
        super("CevBreaker", Category.COMBAT);

        register(blocksPerTick);
        register(antiStep);
        register(surroundPlacing);
        register(range);
        register(raytrace);
        register(packet);
        register(dynamic);
        register(supportBlocks);
        register(rewriteRetries);
        register(rotateMode);
    }

    public void onEnable() {
        trap = new DynamicTrapUtil();
        mine = new PacketMineUtil();
        stage = CevBreakerStage.Trap;
        canTopTrap = true;
        rebreakCount = 0;
        oldSlot = -1;
        currentPos = null;
        delay = 0;
    }

    public void onDisable() {
        trap = null;
        mine = null;
    }

    public void update() {
        if(mc.player == null || mc.world == null) return;

        target = EntityUtil.getTarget(targetRange.getValFloat());
        if(target == null) return;

        if(trap == null) trap = new DynamicTrapUtil();
        if(stage == CevBreakerStage.Trap){
            trap.update(target, dynamic.getValBoolean(), packet.getValBoolean(), rotateMode.getValString(), supportBlocks.getValString(), surroundPlacing.getValBoolean(), antiStep.getValBoolean(), rewriteRetries.getValInt(), switch_.getValString(), canTopTrap);
            canTopTrap = !canTopTrap;
        } else {
            BlockPos pos = PlayerUtil.getPlayerPos(target).up(2);
            if(!BlockUtil.canBlockBeBroken(pos)) {
                ChatUtils.error("block for break cant be broken :( shutdown!");
                super.setToggled(false);
                return;
            }

            posToUse = pos;

            if(mc.world.getBlockState(pos).getBlock().equals(Blocks.OBSIDIAN) && !canTopTrap) {
                canTopTrap = false;
                stage = CevBreakerStage.PlaceCrystal;
            } else if(!mc.world.getBlockState(pos).getBlock().equals(Blocks.OBSIDIAN) && BlockUtil.canBlockBeBroken(pos)) {
                stage = CevBreakerStage.BreakTopBlock;
            }
        }

        if(stage == CevBreakerStage.PlaceCrystal) {
            placeCrystal();
            stage = CevBreakerStage.BreakTopBlock;
        } else if(stage == CevBreakerStage.BreakTopBlock) {
            //break process :/

            stage = CevBreakerStage.Trap;
        }
    }

    private void placeCrystal() {
        // if(mc.player)
        RayTraceResult result = mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + ( double ) mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(( double ) posToUse.getX() + 0.5, ( double ) posToUse.getY() - 0.5, ( double ) posToUse.getZ() + 0.5));
        EnumFacing facing = result == null || result.sideHit == null ? EnumFacing.UP : result.sideHit;
        mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(posToUse, facing, EnumHand.MAIN_HAND, 0, 0, 0));
        mc.player.connection.sendPacket(new CPacketAnimation());
    }

    public enum ObbyBreakMode {Client, Packet}
    public enum CevBreakerStage {Trap, PlaceObby, PlaceCrystal, BreakTopBlock, DestroyCrystal}
}
