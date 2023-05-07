package com.kisman.cc.module.combat;

import com.kisman.cc.event.events.PacketEvent;
import com.kisman.cc.event.events.PlayerMotionUpdateEvent;
import com.kisman.cc.gui.csgo.components.Slider;
import com.kisman.cc.manager.managers.FriendManager;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.module.client.Config;
import com.kisman.cc.module.combat.autorer.AutoRerUtil;
import com.kisman.cc.module.combat.autorer.PlaceInfo;
import com.kisman.cc.module.combat.autorer.render.AutoRerRenderer;
import com.kisman.cc.module.render.shader.FramebufferShader;
import com.kisman.cc.module.render.shader.shaders.GlowShader;
import com.kisman.cc.module.render.shader.shaders.GradientOutlineShader;
import com.kisman.cc.module.render.shader.shaders.ItemShader;
import com.kisman.cc.module.render.shader.shaders.OutlineShader;
import com.kisman.cc.setting.Setting;
import com.kisman.cc.setting.util.RenderingRewritePattern;
import com.kisman.cc.util.*;
import com.kisman.cc.util.bypasses.SilentSwitchBypass;
import com.kisman.cc.util.enums.ShaderModes;
import me.zero.alpine.listener.Listener;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.*;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author _kisman_(Logic, Renderer logic), Cubic(Renderer)
 */
public class AutoRer extends Module {
    public final Setting lagProtect = new Setting("Lag Protect", this, false);
    public final Setting placeRange = new Setting("Place Range", this, 6, 0, 6, false);
    private final Setting recalcPlaceRange = new Setting("Recalc Place Range", this, 4, 0, 6, false);
    private final Setting placeWallRange = new Setting("Place Wall Range", this, 4.5f, 0, 6, false);
    private final Setting breakRange = new Setting("Break Range", this, 6, 0, 6, false);
    private final Setting breakWallRange = new Setting("Break Wall Range", this, 4.5f, 0, 6, false);
    private final Setting targetRange = new Setting("Target Range", this, 9, 0, 20, false);
    private final Setting logic = new Setting("Logic", this, LogicMode.PlaceBreak);
    public final Setting terrain = new Setting("Terrain", this, false);
    private final Setting switch_ = new Setting("Switch", this, SwitchMode.None);
    private final Setting fastCalc = new Setting("Fast Calc", this, true);
    private final Setting recalc = new Setting("ReCalc", this, false);
    private final Setting motionCrystal = new Setting("Motion Crystal", this, false);
    private final Setting motionCalc = new Setting("Motion Calc", this, false).setVisible(motionCrystal::getValBoolean);
    private final Setting swing = new Setting("Swing", this, SwingMode.PacketSwing);
    private final Setting instant = new Setting("Instant", this, true);
    private final Setting instantCalc = new Setting("Instant Calc", this, true).setVisible(instant::getValBoolean);
    private final Setting instantRotate = new Setting("Instant Rotate", this, true).setVisible(instant::getValBoolean);
    private final Setting inhibit = new Setting("Inhibit", this, true);
    private final Setting sound = new Setting("Sound", this, true);
    public final Setting syns = new Setting("Syns", this, true);
    private final Setting rotate = new Setting("Rotate", this, Rotate.Place);
    private final Setting rotateMode = new Setting("Rotate Mode", this, RotateMode.Silent).setVisible(() -> !rotate.checkValString("None"));
    private final Setting calcDistSort = new Setting("Calc Dist Sort", this, false);

    private final Setting placeLine = new Setting("PlaceLine", this, "Place");
    private final Setting place = new Setting("Place", this, true);
    public final Setting secondCheck = new Setting("Second Check", this, false);
    private final Setting thirdCheck = new Setting("Third Check", this, false);
    public final Setting armorBreaker = new Setting("Armor Breaker", this, 100, 0, 100, Slider.NumberType.PERCENT);
    private final Setting multiPlace = new Setting("Multi Place", this, false);
    private final Setting firePlace = new Setting("Fire Place", this, false);

    private final Setting breakLine = new Setting("BreakLine", this, "Break");
    private final Setting break_ = new Setting("Break", this, true);
    private final Setting breakPriority = new Setting("Break Priority", this, BreakPriority.Damage);
    private final Setting friend_ = new Setting("Friend", this, FriendMode.AntiTotemPop);
    private final Setting clientSide = new Setting("Client Side", this, false);
    private final Setting manualBreaker = new Setting("Manual Breaker", this, false);
    private final Setting removeAfterAttack = new Setting("Remove After Attack", this, false);
    private final Setting antiCevBreakerMode = new Setting("Anti Cev Breaker", this, AntiCevBreakerMode.None);

    private final Setting delayLine = new Setting("DelayLine", this, "Delay");
    private final Setting placeDelay = new Setting("Place Delay", this, 0, 0, 2000, Slider.NumberType.TIME);
    private final Setting breakDelay = new Setting("Break Delay", this, 0, 0, 2000, Slider.NumberType.TIME);
    private final Setting calcDelay = new Setting("Calc Delay", this, 0, 0, 20000, Slider.NumberType.TIME);
    private final Setting clearDelay = new Setting("Clear Delay", this, 500, 0, 2000, Slider.NumberType.TIME);
    private final Setting multiplication = new Setting("Multiplication", this, 1, 1, 10, true);

    private final Setting dmgLine = new Setting("DMGLine", this, "Damage");
    public final Setting minDMG = new Setting("Min DMG", this, 6, 0, 37, true);
    public final Setting maxSelfDMG = new Setting("Max Self DMG", this, 18, 0, 37, true);
    private final Setting maxFriendDMG = new Setting("Max Friend DMG", this, 10, 0, 37, true);
    public final Setting lethalMult = new Setting("Lethal Mult", this, 0, 0, 6, false);

    private final Setting threadLine = new Setting("ThreadLine", this, "Thread");
    private final Setting threadMode = new Setting("Thread Mode", this, ThreadMode.None);
    private final Setting threadDelay = new Setting("Thread Delay", this, 50, 1, 1000, Slider.NumberType.TIME).setVisible(() -> !threadMode.getValString().equalsIgnoreCase(ThreadMode.None.name()));
    private final Setting threadSyns = new Setting("Thread Syns", this, true).setVisible(() -> !threadMode.getValString().equalsIgnoreCase(ThreadMode.None.name()));
    private final Setting threadSynsValue = new Setting("Thread Syns Value", this, 1000, 1, 10000, Slider.NumberType.TIME).setVisible(() -> !threadMode.getValString().equalsIgnoreCase(ThreadMode.None.name()));
    private final Setting threadPacketRots = new Setting("Thread Packet Rots", this, false).setVisible(() -> !threadMode.getValString().equalsIgnoreCase(ThreadMode.None.name()) && !rotate.checkValString(Rotate.Off.name()));
    private final Setting threadSoundPlayer = new Setting("Thread Sound Player", this, 6, 0, 12, true).setVisible(() -> threadMode.checkValString("Sound"));
    private final Setting threadCalc = new Setting("Thread Calc", this, true).setVisible(() -> !threadMode.checkValString("None"));

    private final Setting renderLine = new Setting("RenderLine", this, "Render");
    private final Setting render = new Setting("Render", this, true);
    private final Setting movingLength = new Setting("Moving Length", this, 400, 0, 1000, true).setVisible(render::getValBoolean);
    private final Setting fadeLength = new Setting("Fade Length", this, 200, 0, 1000, true).setVisible(render::getValBoolean);

    private final Setting text = new Setting("Text", this, true);

    private final Setting targetCharmsLine = new Setting("Target Charms", this, "Target Shader Charms");
    private final Setting targetCharms = new Setting("Target Charms", this, false);
    private final Setting targetCharmsShader = new Setting("TC Shader", this, ShaderModes.SMOKE);

    private final Setting targetCharmsAnimationSpeed = new Setting("Animation Speed", this, 0, 1, 10, true).setVisible(() -> !targetCharmsShader.checkValString("GRADIENT") && targetCharms.getValBoolean());

    private final Setting targetCharmsBlur = new Setting("Blur", this, true).setVisible(() -> targetCharmsShader.checkValString("ITEMGLOW") && targetCharms.getValBoolean());
    private final Setting targetCharmsRadius = new Setting("Radius", this, 2, 0.1f, 10, false).setVisible(() -> (targetCharmsShader.checkValString("ITEMGLOW") || targetCharmsShader.checkValString("GLOW") || targetCharmsShader.checkValString("OUTLINE") || targetCharmsShader.checkValString("GRADIENT")) && targetCharms.getValBoolean());
    private final Setting targetCharmsMix = new Setting("Mix", this, 1, 0, 1, false).setVisible(() -> targetCharmsShader.checkValString("ITEMGLOW") && targetCharms.getValBoolean());
    private final Setting targetCharmsColor = new Setting("TC Color", this, "TC Color", new Colour(255, 255, 255)).setVisible(() -> (targetCharmsShader.checkValString("ITEMGLOW") || targetCharmsShader.checkValString("GLOW") || targetCharmsShader.checkValString("OUTLINE")) && targetCharms.getValBoolean());

    private final Setting targetCharmsQuality = new Setting("Quality", this, 1, 0, 20, false).setVisible(() -> (targetCharmsShader.checkValString("GRADIENT") || targetCharmsShader.checkValString("ITEMGLOW") || targetCharmsShader.checkValString("GLOW") || targetCharmsShader.checkValString("OUTLINE")) && targetCharms.getValBoolean());
    private final Setting targetCharmsGradientAlpha = new Setting("Gradient Alpha", this, false).setVisible(() -> targetCharmsShader.checkValString("GRADIENT") && targetCharms.getValBoolean());
    private final Setting targetCharmsAlphaGradient = new Setting("Alpha Gradient Value", this, 255, 0, 255, true).setVisible(() -> targetCharmsShader.checkValString("GRADIENT") && targetCharms.getValBoolean());
    private final Setting targetCharmsDuplicateOutline = new Setting("Duplicate Outline", this, 1, 0, 20, false).setVisible(() -> targetCharmsShader.checkValString("GRADIENT") && targetCharms.getValBoolean());
    private final Setting targetCharmsMoreGradientOutline = new Setting("More Gradient", this, 1, 0, 10, false).setVisible(() -> targetCharmsShader.checkValString("GRADIENT") && targetCharms.getValBoolean());
    private final Setting targetCharmsCreepyOutline = new Setting("Creepy", this, 1, 0, 20, false).setVisible(() -> targetCharmsShader.checkValString("GRADIENT") && targetCharms.getValBoolean());
    private final Setting targetCharmsAlpha = new Setting("Alpha", this, 1, 0, 1, false).setVisible(() -> targetCharmsShader.checkValString("GRADIENT") && targetCharms.getValBoolean());
    private final Setting targetCharmsNumOctavesOutline = new Setting("Num Octaves", this, 5, 1, 30, true).setVisible(() -> targetCharmsShader.checkValString("GRADIENT") && targetCharms.getValBoolean());
    private final Setting targetCharmsSpeedOutline = new Setting("Speed", this, 0.1, 0.001, 0.1, false).setVisible(() -> targetCharmsShader.checkValString("GRADIENT") && targetCharms.getValBoolean());

    public static AutoRer instance;

    public final List<PlaceInfo> placedList = new ArrayList<>();
    private final TimerUtil placeTimer = new TimerUtil();
    private final TimerUtil breakTimer = new TimerUtil();
    private final TimerUtil calcTimer = new TimerUtil();
    private final TimerUtil renderTimer = new TimerUtil();
    private final TimerUtil predictTimer = new TimerUtil();
    private final TimerUtil manualTimer = new TimerUtil();
    private final TimerUtil synsTimer = new TimerUtil();
    private ScheduledExecutorService executor;
    private final AtomicBoolean shouldInterrupt = new AtomicBoolean(false);
    private final AtomicBoolean threadOngoing = new AtomicBoolean(false);
    public static EntityPlayer currentTarget;
    private Thread thread;
    public PlaceInfo placePos, renderPos;
    private Entity lastHitEntity = null;
    public boolean rotating;
    private String lastThreadMode = threadMode.getValString();
    private final AutoRerRenderer renderer = new AutoRerRenderer();
    private final RenderingRewritePattern renderer_;

    public AutoRer() {
        super("AutoRer", Category.COMBAT);

        instance = this;

        register(lagProtect);

        register(placeRange);
        register(recalcPlaceRange.setVisible(recalc::getValBoolean));
        register(placeWallRange);
        register(breakRange);
        register(breakWallRange);
        register(targetRange);
        register(logic);
        register(terrain);
        register(switch_);
        register(fastCalc);
        register(recalc);
        register(motionCrystal);
        register(motionCalc);
        register(swing);
        register(instant);
        register(instantCalc);
        register(instantRotate);
        register(inhibit);
        register(sound);
        register(syns);
        register(rotate);
        register(rotateMode);
        register(calcDistSort);

        register(placeLine);
        register(place);
        register(secondCheck.setVisible(place::getValBoolean));
        register(thirdCheck.setVisible(place::getValBoolean));
        register(armorBreaker.setVisible(place::getValBoolean));
        register(multiPlace.setVisible(place::getValBoolean));
        register(firePlace.setVisible(place::getValBoolean));

        register(breakLine);
        register(break_);
        register(breakPriority.setVisible(break_::getValBoolean));
        register(friend_.setVisible(break_::getValBoolean));
        register(clientSide.setVisible(break_::getValBoolean));
        register(manualBreaker.setVisible(break_::getValBoolean));
        register(removeAfterAttack.setVisible(break_::getValBoolean));
        register(antiCevBreakerMode.setVisible(break_::getValBoolean));

        register(delayLine);
        register(placeDelay.setVisible(place::getValBoolean));
        register(breakDelay.setVisible(break_::getValBoolean));
        register(calcDelay);
        register(clearDelay);
        register(multiplication);

        register(dmgLine);
        register(minDMG);
        register(maxSelfDMG);
        register(maxFriendDMG);
        register(lethalMult);

        register(threadLine);
        register(threadMode);
        register(threadDelay);
        register(threadSyns);
        register(threadSynsValue);
        register(threadPacketRots);
        register(threadSoundPlayer);
        register(threadCalc);

        register(renderLine);
        register(render);
        //New renderer
        renderer_ = new RenderingRewritePattern(this, render::getValBoolean);
        renderer_.init();
        register(movingLength);
        register(fadeLength);
        register(text);

        register(targetCharmsLine);
        register(targetCharms);
        register(targetCharmsShader);
        register(targetCharmsAnimationSpeed);
        register(targetCharmsBlur);
        register(targetCharmsRadius);
        register(targetCharmsMix);
        register(targetCharmsColor);
        register(targetCharmsQuality);
        register(targetCharmsGradientAlpha);
        register(targetCharmsAlphaGradient);
        register(targetCharmsDuplicateOutline);
        register(targetCharmsMoreGradientOutline);
        register(targetCharmsCreepyOutline);
        register(targetCharmsAlpha);
        register(targetCharmsNumOctavesOutline);
        register(targetCharmsSpeedOutline);
    }

    public void onEnable() {
        renderer.reset();
        placedList.clear();
        breakTimer.reset();
        placeTimer.reset();
        renderTimer.reset();
        predictTimer.reset();
        manualTimer.reset();
        currentTarget = null;
        rotating = false;
        renderPos = null;

        if (!threadMode.getValString().equalsIgnoreCase("None")) {
            processMultiThreading();
        }
    }

    public void onDisable() {
        if (thread != null) shouldInterrupt.set(false);
        if (executor != null) executor.shutdown();
        placedList.clear();
        breakTimer.reset();
        placeTimer.reset();
        renderTimer.reset();
        predictTimer.reset();
        manualTimer.reset();
        currentTarget = null;
        rotating = false;
        renderPos = null;
        renderer.reset();
    }

    private void processMultiThreading() {
        switch (threadMode.getValString()) {
            case "None":
                break;
            case "While":
                handleWhile();
                break;
            default:
                handlePool(false);
                break;
        }
    }

    private ScheduledExecutorService createExecutorService() {
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(RAutoRer.getInstance(this), 0L, this.threadDelay.getValLong(), TimeUnit.MILLISECONDS);
        return service;
    }

    private void handleWhile() {
        if (isThreadAlive()) {
            return;
        }

        if (thread == null) {
            thread = new Thread(RAutoRer.getInstance(this));
        } else if (checkThreadSync() && !shouldInterrupt.get()) {
            shouldInterrupt.set(true);
            synsTimer.reset();
            return;
        }

        if (thread != null && (thread.isInterrupted() || !thread.isAlive())) {
            thread = new Thread(RAutoRer.getInstance(this));

            try {
                thread.start();
            } catch (Exception ignored) {

            }

            synsTimer.reset();
        }
    }

    private boolean isThreadAlive() {
        if (thread == null) return false;
        if (thread.isInterrupted()) return false;
        if (checkThreadSync()) return false;
        return thread.isAlive();
    }

    private boolean checkThreadSync() {
        return threadSyns.getValBoolean() && synsTimer.passedMillis(threadSynsValue.getValLong());
    }

    private void handlePool(boolean justDoIt) {
        if (!justDoIt && executor != null && !executor.isTerminated() && !executor.isShutdown() && (!synsTimer.passedMillis(threadSynsValue.getValLong()) || !threadSyns.getValBoolean())) {
            return;
        }

        if (executor != null) {
            executor.shutdown();
        }

        executor = createExecutorService();
        synsTimer.reset();
    }

    public void update() {
        if (mc.player == null || mc.world == null || mc.isGamePaused()) return;

        updateRenderTimer();

        currentTarget = EntityUtil.getTarget(targetRange.getValFloat());

        updateThreads();
        updateDisplayInfo();
        updateCalculation();
        updateStart();
    }

    private void updateRenderTimer() {
        if (renderTimer.passedMillis(clearDelay.getValLong())) {
            placedList.clear();
            renderTimer.reset();
            renderPos = null;
        }
    }

    private void updateThreads() {
        if (!lastThreadMode.equalsIgnoreCase(threadMode.getValString())) {
            if (this.executor != null) {
                this.executor.shutdown();
            }

            if (this.thread != null) {
                this.shouldInterrupt.set(true);
            }

            lastThreadMode = threadMode.getValString();
        }
    }

    private void updateDisplayInfo() {
        if (currentTarget != null) {
            super.setDisplayInfo("[" + currentTarget.getName() + " | Thread: " + threadMode.getValString() + "]");
        }
    }

    private void updateCalculation() {
        if (fastCalc.getValBoolean() && calcTimer.passedMillis(calcDelay.getValLong())) {
            if (threadCalc.getValBoolean() && !threadMode.getValString().equalsIgnoreCase("None")) {
                return;
            }
            doCalculatePlace();

            if (placePos != null) {
                if (!mc.world.getBlockState(placePos.getBlockPos()).getBlock().equals(Blocks.OBSIDIAN) && !mc.world.getBlockState(placePos.getBlockPos()).getBlock().equals(Blocks.BEDROCK)) {
                    placePos = null;
                }
            }

            calcTimer.reset();
        }
    }

    private void updateStart() {
        if (threadMode.getValString().equals("None")) {
            if (manualBreaker.getValBoolean()) {
                manualBreaker();
            }

            if (motionCrystal.getValBoolean()) {
                return;
            }

            if (motionCalc.getValBoolean() && fastCalc.getValBoolean()) {
                return;
            }

            for (int i = 0; i < multiplication.getValInt(); i++) {
                doAutoRerLogic(null, false);
            }
        } else {
            processMultiThreading();
        }
    }

    public synchronized void doAutoRerForThread() {
        if (mc.player == null || mc.world == null || mc.isGamePaused()) return;
        if (manualBreaker.getValBoolean()) manualBreaker();
        if (fastCalc.getValBoolean() && calcTimer.passedMillis(calcDelay.getValLong())) {
            doCalculatePlace();
            calcTimer.reset();
        }

        for (int i = 0; i < multiplication.getValInt(); i++) {
            doAutoRerLogic(null, true);
        }
    }

    private void manualBreaker() {
        RayTraceResult result = mc.objectMouseOver;
        if (manualTimer.passedMillis(200) && mc.gameSettings.keyBindUseItem.isKeyDown() && mc.player.getHeldItemOffhand().getItem() != Items.GOLDEN_APPLE && mc.player.inventory.getCurrentItem().getItem() != Items.GOLDEN_APPLE && mc.player.inventory.getCurrentItem().getItem() != Items.BOW && mc.player.inventory.getCurrentItem().getItem() != Items.EXPERIENCE_BOTTLE && result != null) {
            if (result.typeOfHit.equals(RayTraceResult.Type.ENTITY) && result.entityHit instanceof EntityEnderCrystal) {
                mc.player.connection.sendPacket(new CPacketUseEntity(result.entityHit));
                manualTimer.reset();
            } else if (result.typeOfHit.equals(RayTraceResult.Type.BLOCK)) {
                for (Entity target : mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(new BlockPos(mc.objectMouseOver.getBlockPos().getX(), mc.objectMouseOver.getBlockPos().getY() + 1.0, mc.objectMouseOver.getBlockPos().getZ())))) {
                    if (!(target instanceof EntityEnderCrystal)) continue;
                    mc.player.connection.sendPacket(new CPacketUseEntity(target));
                    manualTimer.reset();
                }
            }
        }
    }

    @SuppressWarnings("unused")
    private final Listener<PlayerMotionUpdateEvent> motionUpdateListener = listener(event -> {
        if (!motionCrystal.getValBoolean() || currentTarget == null) return;
        if (motionCalc.getValBoolean() && fastCalc.getValBoolean() && calcTimer.passedMillis(calcDelay.getValLong())) {
            doCalculatePlace();
            calcTimer.reset();
        }

        for (int i = 0; i < multiplication.getValInt(); i++) {
            doAutoRerLogic(event, false);
        }
    });

    private void doAutoRerLogic(PlayerMotionUpdateEvent event, boolean thread) {
        if (mc.isGamePaused) return;

        if (logic.getValString().equalsIgnoreCase("PlaceBreak")) {
            doPlace(event, thread);
            if (placePos != null) doBreak();
        } else {
            doBreak();
            doPlace(event, thread);
        }
    }

    private void attackCrystalPredict(int entityID, BlockPos pos) {
        if (instantRotate.getValBoolean() && !motionCrystal.getValBoolean() && (rotate.getValString().equalsIgnoreCase("Break") || rotate.getValString().equalsIgnoreCase("All"))) {
            float[] rots = RotationUtils.calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d((pos.getX() + 0.5f), (pos.getY() - 0.5f), (pos.getZ() + 0.5f)));
            mc.player.rotationYaw = rots[0];
            mc.player.rotationPitch = rots[1];
        }
        CPacketUseEntity packet = new CPacketUseEntity();
        packet.entityId = entityID;
        packet.action = CPacketUseEntity.Action.ATTACK;
        mc.player.connection.sendPacket(packet);
        breakTimer.reset();
        predictTimer.reset();
    }

    @SuppressWarnings("unused")
    private final Listener<PacketEvent.Receive> packetReceiveListener = listener(event -> {
        if (event.getPacket() instanceof SPacketSpawnObject && instant.getValBoolean()) {
            SPacketSpawnObject packet =  (SPacketSpawnObject) event.getPacket();
            if (packet.getType() == 51) {
                if (!(mc.world.getEntityByID(packet.getEntityID()) instanceof EntityEnderCrystal)) return;
                PlaceInfo toRemove = null;
                for (PlaceInfo placeInfo : placedList) {
                    BlockPos pos = placeInfo.getBlockPos();
                    boolean canSee = EntityUtil.canSee(pos);
                    if (mc.player.getDistance(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5) >= (canSee ? breakRange.getValDouble() : breakWallRange.getValDouble())) break;

                    if (instantCalc.getValBoolean() && currentTarget != null) {
                        float targetDamage = calculateCrystalDamage(pos, currentTarget);

                        if (checkTargetDamage(targetDamage)) {
                            float selfDamage = calculateCrystalDamage(pos, mc.player);

                            if (checkSelfDamage(selfDamage, targetDamage)) {
                                toRemove = placeInfo;
                                if (inhibit.getValBoolean()) try {
                                    lastHitEntity = mc.world.getEntityByID(packet.getEntityID());
                                } catch (Exception ignored) {

                                }
                                attackCrystalPredict(packet.getEntityID(), pos);
                                swing();
                            }
                        }
                    } else {
                        toRemove = placeInfo;
                        if (inhibit.getValBoolean()) try {lastHitEntity = mc.world.getEntityByID(packet.getEntityID());} catch (Exception ignored) {}
                        attackCrystalPredict(packet.getEntityID(), pos);
                        swing();
                    }

                    break;
                }
                if (toRemove != null) placedList.remove(toRemove);
            }
        }

        if (event.getPacket() instanceof SPacketSoundEffect && ((inhibit.getValBoolean() && lastHitEntity != null) || (sound.getValBoolean()))) {
            SPacketSoundEffect packet = (SPacketSoundEffect) event.getPacket();
            if (packet.getCategory() == SoundCategory.BLOCKS && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) if (lastHitEntity.getDistance(packet.getX(), packet.getY(), packet.getZ()) <= 6.0f) lastHitEntity.setDead();
            if (threadMode.checkValString(ThreadMode.Sound.name()) && isRightThread() && mc.player != null && mc.player.getDistanceSq(new BlockPos(packet.getX(), packet.getY(), packet.getZ())) < MathUtil.square(threadSoundPlayer.getValInt())) handlePool(true);
        }
    });

    @SuppressWarnings("unused")
    private final Listener<PacketEvent.Send> packetSendListener = listener(event -> {
        if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock && mc.player.getHeldItem(((CPacketPlayerTryUseItemOnBlock) event.getPacket()).getHand()).getItem() == Items.END_CRYSTAL) try {
            PlaceInfo info = AutoRerUtil.getPlaceInfo(((CPacketPlayerTryUseItemOnBlock) event.getPacket()).getPos(), currentTarget, terrain.getValBoolean());
            placedList.add(info);
        } catch (Exception ignored) {}
        if (removeAfterAttack.getValBoolean() && event.getPacket() instanceof CPacketUseEntity) {
            CPacketUseEntity packet = (CPacketUseEntity) event.getPacket();
            if (packet.getAction().equals(CPacketUseEntity.Action.ATTACK) && packet.getEntityFromWorld(mc.world) instanceof EntityEnderCrystal) {
                Objects.requireNonNull(packet.getEntityFromWorld(mc.world)).setDead();
                try {
                    mc.world.removeEntityFromWorld(packet.entityId);
                } catch (Exception ignored) {

                }
            }
        }
    });

    private boolean isRightThread() {
        return mc.isCallingFromMinecraftThread() || (!this.threadOngoing.get());
    }

    private void doCalculatePlace() {
        try {
            calculatePlace();
            if (recalc.getValBoolean() && placePos == null) recalculatePlace();
        } catch (Exception exception) {
            lagProtect(exception);
        }
    }

    private void recalculatePlace() {
        List<BlockPos> sphere = CrystalUtils.getSphere(recalcPlaceRange.getValFloat(), true, false);
        List<BlockPos> validPos = new ArrayList<>();

        sphere.stream()
                .filter(pos -> CrystalUtils.calculateDamage(mc.world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, currentTarget, terrain.getValBoolean()) > minDMG.getValInt() || CrystalUtils.calculateDamage(mc.world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, currentTarget, terrain.getValBoolean()) * lethalMult.getValDouble() > currentTarget.getHealth() + currentTarget.getAbsorptionAmount() || InventoryUtil.isArmorUnderPercent(currentTarget, armorBreaker.getValInt()))
                .filter(pos -> CrystalUtils.calculateDamage(mc.world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, mc.player, terrain.getValBoolean()) <= maxSelfDMG.getValInt())
                .filter(pos -> CrystalUtils.calculateDamage(mc.world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, mc.player, terrain.getValBoolean()) + 2 < mc.player.getHealth() + mc.player.getAbsorptionAmount())
                .filter(pos -> CrystalUtils.calculateDamage(mc.world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, mc.player, terrain.getValBoolean()) < CrystalUtils.calculateDamage(mc.world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, currentTarget, terrain.getValBoolean()))
                .forEach(validPos::add);

        validPos.sort((first, second) -> (int) (mc.player.getDistanceSqToCenter(first) - mc.player.getDistanceSqToCenter(second)));

        double[] maxDamage = {0.5};
        BlockPos[] placePos = {null};

        validPos.forEach(pos -> {
            if (CrystalUtils.calculateDamage(mc.world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, currentTarget, terrain.getValBoolean()) > maxDamage[0]) {
                maxDamage[0] = calculateCrystalDamage(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, currentTarget);
                placePos[0] = pos;
            }
        });

        this.placePos = placePos[0] == null ? null : AutoRerUtil.getPlaceInfo(placePos[0], currentTarget, terrain.getValBoolean());
    }

    private void calculatePlace() {
        double maxDamage = 0.5;
        BlockPos placePos = null;
        List<BlockPos> sphere = CrystalUtils.getSphere(placeRange.getValFloat(), true, false);

        if (calcDistSort.getValBoolean()) {
            Collections.sort(sphere);
            Collections.reverse(sphere);
        }

        for (BlockPos pos : sphere) {
            if (thirdCheck.getValBoolean() && !isPosValid(pos)) {
                continue;
            }

            if (!CrystalUtils.canPlaceCrystal(pos, secondCheck.getValBoolean(), true, multiPlace.getValBoolean(), firePlace.getValBoolean())) {
                continue;
            }

            float targetDamage = calculateCrystalDamage(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, currentTarget);

            if (!checkTargetDamage(targetDamage)) {
                continue;
            }

            float selfDamage = calculateCrystalDamage(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, mc.player);

            if (!checkSelfDamage(selfDamage, targetDamage)) {
                continue;
            }

            if (maxDamage <= targetDamage) {
                maxDamage = targetDamage;
                placePos = pos;
            }
        }

        this.placePos = placePos == null ? null : AutoRerUtil.getPlaceInfo(placePos, currentTarget, terrain.getValBoolean());
    }

    private boolean isPosValid(BlockPos pos) {
        return mc.player.getDistance(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5) <= (EntityUtil.canSee(pos) ?  placeRange.getValDouble() : placeWallRange.getValDouble());
    }

    private void doPlace(PlayerMotionUpdateEvent event, boolean thread) {
        if (!placeCheck(thread)) {
            return;
        }

        SilentSwitchBypass bypass = new SilentSwitchBypass(Items.END_CRYSTAL);
        EnumHand hand = null;
        boolean offhand = mc.player.getHeldItemOffhand().getItem().equals(Items.END_CRYSTAL);
        boolean silentBypass = switch_.getValString().equals("SilentBypass");
        int oldSlot = mc.player.inventory.currentItem;

        placeSwitch(bypass, silentBypass, offhand);

        if (mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL && mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
            return;
        }

        if (mc.player.isHandActive()) {
            hand = mc.player.getActiveHand();
        }

        float[] oldRotations = placeRotate(event, thread);

        placePlaceCrystal(offhand);
        placeRotatePost(oldRotations);
        placeSwingPost(bypass, silentBypass, hand, oldSlot);
    }

    private boolean placeCheck(boolean thread) {
        if (!place.getValBoolean() || !placeTimer.passedMillis(placeDelay.getValLong())) {
            return false;
        }

        if (placePos == null && fastCalc.getValBoolean()) {
            return false;
        }

        if (!fastCalc.getValBoolean() || (thread && threadCalc.getValBoolean())) {
            doCalculatePlace();
        }

        if (placePos != null) {
            Block placeBlock = mc.world.getBlockState(placePos.getBlockPos()).getBlock();

            if (placeBlock != Blocks.OBSIDIAN && placeBlock != Blocks.BEDROCK) {
                return false;
            }
        }

        return !syns.getValBoolean() || !placedList.contains(placePos);
    }

    private void placeSwitch(SilentSwitchBypass bypass, boolean silentBypass, boolean offhand) {
        if (switch_.getValString().equals("None")) {
            return;
        }

        int crystalSlot = InventoryUtil.findItem(Items.END_CRYSTAL, 0, 9);

        if (crystalSlot == -1 && !silentBypass && !offhand) return;

        if (mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL && !offhand) {
            switch (switch_.getValString()) {
                case "Normal":
                    InventoryUtil.switchToSlot(crystalSlot, false);
                    break;
                case "Silent":
                    InventoryUtil.switchToSlot(crystalSlot, true);
                    break;
                default:
                    if (silentBypass) bypass.doSwitch();
                    break;
            }
        }
    }

    private void placeSwingPost(SilentSwitchBypass bypass, boolean silentBypass, EnumHand hand, int oldSlot) {
        if (hand != null) {
            mc.player.setActiveHand(hand);
        }

        if (oldSlot != -1 && !silentBypass) {
            if (switch_.getValString().equals(SwitchMode.Silent.name())) InventoryUtil.switchToSlot(oldSlot, true);
        } else if (silentBypass) {
            bypass.doSwitch();
        }
    }

    private float[] placeRotate(PlayerMotionUpdateEvent event, boolean thread) {
        float[] oldRotations = new float[] {mc.player.rotationYaw, mc.player.rotationPitch};

        if (currentTarget == null) return oldRotations;

        if (rotate.getValString().equalsIgnoreCase("Place") || rotate.getValString().equalsIgnoreCase("All")) {
            try {
                float[] rotations = RotationUtils.calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d((placePos.getBlockPos().getX() + 0.5f), (placePos.getBlockPos().getY() - 0.5f), (placePos.getBlockPos().getZ() + 0.5f)));
                if (!thread) {
                    if (!motionCrystal.getValBoolean()) {
                        mc.player.rotationYaw = rotations[0];
                        mc.player.rotationPitch = rotations[1];
                    } else if (event != null) {
                        event.setYaw(rotations[0]);
                        event.setPitch(rotations[1]);
                    }
                } else if (threadPacketRots.getValBoolean()) {
                    mc.player.connection.sendPacket(new CPacketPlayer.Rotation(rotations[0], rotations[1], mc.player.onGround));
                }
            } catch (Exception ignored) {

            }
        }

        return oldRotations;
    }

    private void placeRotatePost(float[] rotations) {
        if ((rotate.getValString().equalsIgnoreCase("Place") || rotate.getValString().equalsIgnoreCase("All")) && rotateMode.getValString().equalsIgnoreCase("Silent")) {
            mc.player.rotationYaw = rotations[0];
            mc.player.rotationPitch = rotations[1];
        }
    }

    private void placePlaceCrystal(boolean offhand) {
        RayTraceResult result = mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + ( double ) mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(( double ) placePos.getBlockPos().getX() + 0.5, ( double ) placePos.getBlockPos().getY() - 0.5, ( double ) placePos.getBlockPos().getZ() + 0.5));
        EnumFacing facing = result == null || result.sideHit == null ? EnumFacing.UP : result.sideHit;
        mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(placePos.getBlockPos(), facing, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0, 0, 0));
        if (!swing.checkValString(SwingMode.None.name())) mc.player.connection.sendPacket(new CPacketAnimation(swing.getValString().equals(SwingMode.MainHand.name()) ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND));
        placeTimer.reset();
        renderPos = placePos;
    }

    private Entity findCrystalForAntiCevBreaker() {
        Entity crystal = null;

        switch (antiCevBreakerMode.getValString()) {
            case "Both":
                crystal = findBothCrystal();
                break;
            case "Cev":
                crystal = findCevCrystal();
                break;
            case "Civ":
                crystal = findCivCrystal();
                break;
        }

        return crystal;
    }

    private Entity findBothCrystal() {
        Entity crystal = findCevCrystal();
        return crystal == null ? findCivCrystal() : crystal;
    }

    private Entity findCevCrystal() {
        Entity crystal = null;

        for (Vec3i vec : AntiCevBreakerVectors.Cev.vectors) {
            BlockPos pos = mc.player.getPosition().add(vec);
            for (Entity entity : mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos))) {
                if (entity instanceof EntityEnderCrystal) {
                    crystal = entity;
                }
            }
        }

        return crystal;
    }

    private Entity findCivCrystal() {
        Entity crystal = null;

        for (Vec3i vec : AntiCevBreakerVectors.Civ.vectors) {
            BlockPos pos = mc.player.getPosition().add(vec);
            for (Entity entity : mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos))) {
                if (entity instanceof EntityEnderCrystal) {
                    crystal = entity;
                }
            }
        }

        return crystal;
    }

    private Entity findCrystalWithMaxDamage() {
        Entity crystal = null;
        double maxDamage = 0.5;

        try {
            for (Entity entity : mc.world.loadedEntityList) {
                if (!(entity instanceof EntityEnderCrystal)) {
                    continue;
                }

                if (mc.player.getDistance(entity) > (mc.player.canEntityBeSeen(entity) ? breakRange.getValDouble() : breakWallRange.getValDouble())) {
                    continue;
                }

                if (checkFriend(entity)) {
                    return null;
                }

                double targetDamage = calculateCrystalDamage(entity.posX, entity.posY, entity.posZ, currentTarget);

                if (!checkTargetDamage(targetDamage)) {
                    continue;
                }

                double selfDamage = calculateCrystalDamage(entity.posX, entity.posY, entity.posZ, mc.player);

                if (!checkSelfDamage(selfDamage, targetDamage)) {
                    continue;
                }

                if (maxDamage <= targetDamage) {
                    maxDamage = targetDamage;
                    crystal = entity;
                }
            }
        } catch (Exception exception) {
            lagProtect(exception);
        }

        return crystal;
    }

    private boolean checkFriend(Entity entity) {
        Friend friend = getNearFriendWithMaxDamage(entity);

        if (friend != null && !friend_.getValString().equalsIgnoreCase(FriendMode.None.name())) {
            if (friend_.getValString().equalsIgnoreCase(FriendMode.AntiTotemPop.name()) && friend.isTotemPopped) {
                return true;
            } else if (friend.isTotemFailed) {
                return true;
            } else return friend.damage >= maxFriendDMG.getValInt();
        }

        return false;
    }

    private boolean checkTargetDamage(double damage) {
        if (damage >= minDMG.getValInt()) {
            return true;
        }

        if (damage * lethalMult.getValDouble() > currentTarget.getHealth() + currentTarget.getAbsorptionAmount()) {
            return true;
        }

        return InventoryUtil.isArmorUnderPercent(currentTarget, armorBreaker.getValInt());
    }

    private boolean checkSelfDamage(double selfDamage, double targetDamage) {
        if (selfDamage > maxSelfDMG.getValInt()) return false;
        if (selfDamage + 2 > mc.player.getHealth() + mc.player.getAbsorptionAmount()) return false;
        return !(selfDamage > targetDamage);
    }

    private void doBreak() {
        if (!break_.getValBoolean() || !breakTimer.passedMillis(breakDelay.getValLong())) return;

        Entity crystal = findBreakCrystal();
        if (crystal != null) {
            float[] oldRotations = breakRotate(crystal);
            breakAttack(crystal);
            breakRotatePost(oldRotations);
            breakRemove(crystal);
        }
    }

    private Entity findBreakCrystal() {
        Entity crystalWithMaxDamage = findCrystalWithMaxDamage();
        Entity crystal = breakPriority.getValString().equals("Damage") ? crystalWithMaxDamage : findCrystalForAntiCevBreaker();
        return crystal == null ? crystalWithMaxDamage : crystal;
    }

    private float[] breakRotate(Entity crystal) {
        float[] oldRotations = new float[] {mc.player.rotationYaw, mc.player.rotationPitch};

        if (rotate.getValString().equalsIgnoreCase("Break") || rotate.getValString().equalsIgnoreCase("All")) {
            float[] rots = RotationUtils.getRotation(crystal);
            mc.player.rotationYaw = rots[0];
            mc.player.rotationPitch = rots[1];
        }

        return oldRotations;
    }

    private void breakRotatePost(float[] rotations) {
        if ((rotate.getValString().equalsIgnoreCase("Break") || rotate.getValString().equalsIgnoreCase("All")) && rotateMode.getValString().equalsIgnoreCase("Silent")) {
            mc.player.rotationYaw = rotations[0];
            mc.player.rotationPitch = rotations[1];
        }
    }

    private void breakAttack(Entity crystal) {
        lastHitEntity = crystal;
        mc.player.connection.sendPacket(new CPacketUseEntity(crystal));
        swing();
        try {
            if (clientSide.getValBoolean()) {
                mc.world.removeEntityFromWorld(crystal.entityId);
            }
        } catch (Exception ignored) {}
        breakTimer.reset();
    }

    private void breakRemove(Entity crystal) {
        PlaceInfo toRemove = null;

        if (syns.getValBoolean()) {
            for (PlaceInfo info : placedList) {
                BlockPos pos = info.getBlockPos();
                if (crystal.getDistance(pos.getX(), pos.getY(), pos.getZ()) <= 3) {
                    toRemove = info;
                }
            }
        }

        if (toRemove != null) placedList.remove(toRemove);
    }

    private void swing() {
        if (swing.checkValString(SwingMode.None.name())) return;
        if (swing.getValString().equals(SwingMode.PacketSwing.name())) mc.player.connection.sendPacket(new CPacketAnimation(swing.getValString().equals(SwingMode.MainHand.name()) ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND));
        else mc.player.swingArm(swing.getValString().equals(SwingMode.MainHand.name()) ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND);
    }

    private Friend getNearFriendWithMaxDamage(Entity entity) {
        ArrayList<Friend> friendsWithMaxDamage = new ArrayList<>();

        for (EntityPlayer player : mc.world.playerEntities) {
            if (mc.player == player) continue;
            if (FriendManager.instance.isFriend(player)) {
                double friendDamage = calculateCrystalDamage(entity.posX, entity.posY, entity.posZ, currentTarget);
                if (friendDamage <= maxFriendDMG.getValInt() || friendDamage * lethalMult.getValDouble() >= player.getHealth() + player.getAbsorptionAmount()) friendsWithMaxDamage.add(new Friend(player, friendDamage, friendDamage * lethalMult.getValDouble() >= player.getHealth() + player.getAbsorptionAmount()));
            }
        }

        Friend nearFriendWithMaxDamage = null;
        double maxDamage = 0.5;

        for (Friend friend : friendsWithMaxDamage) {
            double friendDamage = calculateCrystalDamage(entity.posX, entity.posY, entity.posZ, currentTarget);
            if (friendDamage > maxDamage) {
                maxDamage = friendDamage;
                nearFriendWithMaxDamage = new Friend(friend.friend, friendDamage);
            }
        }

        return nearFriendWithMaxDamage;
    }

    private void lagProtect(Exception exception) {
        if (lagProtect.getValBoolean()) {
            new RuntimeException("Lag Protect Exception", exception).printStackTrace();
            super.setToggled(false);
        }
    }
    
    private float calculateCrystalDamage(double posX, double posY, double posZ, Entity entity) {
        return CrystalUtils.calculateDamage(mc.world, posX, posY, posZ, entity, terrain.getValBoolean());
    }

    private float calculateCrystalDamage(BlockPos pos, Entity entity) {
        return CrystalUtils.calculateDamage(pos, entity, terrain.getValBoolean());
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (mc.player == null || mc.world == null || mc.isGamePaused()) return;

        if (targetCharms.getValBoolean() && currentTarget != null) {
            try {
                FramebufferShader framebufferShader = FramebufferShader.SHADERS.get(targetCharmsShader.getValString().toLowerCase());

                if (framebufferShader == null) return;

                framebufferShader.animationSpeed = targetCharmsAnimationSpeed.getValInt();

                GlStateManager.matrixMode(5889);
                GlStateManager.pushMatrix();
                GlStateManager.matrixMode(5888);
                GlStateManager.pushMatrix();

                if (framebufferShader instanceof ItemShader) {
                    ItemShader itemShader = (ItemShader) framebufferShader;
                    itemShader.red = targetCharmsColor.getColour().r1;
                    itemShader.green = targetCharmsColor.getColour().g1;
                    itemShader.blue = targetCharmsColor.getColour().b1;
                    itemShader.radius = targetCharmsRadius.getValFloat();
                    itemShader.quality = targetCharmsQuality.getValFloat();
                    itemShader.blur = targetCharmsBlur.getValBoolean();
                    itemShader.mix = targetCharmsMix.getValFloat();
                    itemShader.alpha = 1f;
                    itemShader.useImage = false;
                } else if (framebufferShader instanceof GradientOutlineShader) {
                    GradientOutlineShader gradientShader = (GradientOutlineShader) framebufferShader;
                    gradientShader.color = targetCharmsColor.getColour().getColor();
                    gradientShader.radius = targetCharmsRadius.getValFloat();
                    gradientShader.quality = targetCharmsQuality.getValFloat();
                    gradientShader.gradientAlpha = targetCharmsGradientAlpha.getValBoolean();
                    gradientShader.alphaOutline = targetCharmsAlphaGradient.getValInt();
                    gradientShader.duplicate = targetCharmsDuplicateOutline.getValFloat();
                    gradientShader.moreGradient = targetCharmsMoreGradientOutline.getValFloat();
                    gradientShader.creepy = targetCharmsCreepyOutline.getValFloat();
                    gradientShader.alpha = targetCharmsAlpha.getValFloat();
                    gradientShader.numOctaves = targetCharmsNumOctavesOutline.getValInt();
                } else if (framebufferShader instanceof GlowShader) {
                    GlowShader glowShader = (GlowShader) framebufferShader;
                    glowShader.red = targetCharmsColor.getColour().r1;
                    glowShader.green = targetCharmsColor.getColour().g1;
                    glowShader.blue = targetCharmsColor.getColour().b1;
                    glowShader.radius = targetCharmsRadius.getValFloat();
                    glowShader.quality = targetCharmsQuality.getValFloat();
                } else if (framebufferShader instanceof OutlineShader) {
                    OutlineShader outlineShader = (OutlineShader) framebufferShader;
                    outlineShader.red = targetCharmsColor.getColour().r1;
                    outlineShader.green = targetCharmsColor.getColour().g1;
                    outlineShader.blue = targetCharmsColor.getColour().b1;
                    outlineShader.radius = targetCharmsRadius.getValFloat();
                    outlineShader.quality = targetCharmsQuality.getValFloat();
                }

                framebufferShader.startDraw(event.getPartialTicks());
                for (Entity entity : mc.world.loadedEntityList) {
                    if (entity == mc.player || entity == mc.getRenderViewEntity() || !entity.equals(currentTarget)) continue;
                    Vec3d vector = MathUtil.getInterpolatedRenderPos(entity, event.getPartialTicks());
                    Objects.requireNonNull(mc.getRenderManager().getEntityRenderObject(entity)).doRender(entity, vector.x, vector.y, vector.z, entity.rotationYaw, event.getPartialTicks());
                }
                framebufferShader.stopDraw();
                if (framebufferShader instanceof GradientOutlineShader) {
                    ((GradientOutlineShader) framebufferShader).update(targetCharmsSpeedOutline.getValDouble());
                }
                GlStateManager.color(1f, 1f, 1f);
                GlStateManager.matrixMode(5889);
                GlStateManager.popMatrix();
                GlStateManager.matrixMode(5888);
                GlStateManager.popMatrix();
            } catch (Exception ignored) {
                if (Config.instance.antiOpenGLCrash.getValBoolean() || lagProtect.getValBoolean()) {
                    super.setToggled(false);
                    ChatUtil.error("[AutoRer] Error, Config -> AntiOpenGLCrash disabled AutoRer");
                }
            }
        }

        if (render.getValBoolean() && placePos != null) {
            renderer.onRenderWorld(movingLength.getValFloat(), fadeLength.getValFloat(), renderer_, placePos, text.getValBoolean());
        }
    }

    public enum ThreadMode {None, Pool, Sound, While}
    public enum Render {None, Default, Advanced}
    public enum Rotate {Off, Place, Break, All}
    public enum Raytrace {None, Place, Break, Both}
    public enum SwitchMode {None, Normal, Silent, SilentBypass}
    public enum SwingMode {MainHand, OffHand, PacketSwing, None}
    public enum FriendMode {None, AntiTotemFail, AntiTotemPop}
    public enum LogicMode {PlaceBreak, BreakPlace}
    public enum RotateMode {Normal, Silent}
    public enum AntiCevBreakerMode {None, Cev, Civ, Both}
    public enum BreakPriority {Damage, CevBreaker}

    public enum AntiCevBreakerVectors {
        Cev(Collections.singletonList(new Vec3i(0, 2, 0))),
        Civ(Arrays.asList(new Vec3i(1, 2, 0), new Vec3i(-1, 2, 0), new Vec3i(0, 2, 1), new Vec3i(0, 2, -1), new Vec3i(1, 2, 1), new Vec3i(-1, 2, -1), new Vec3i(1, 2, -1), new Vec3i(-1, 2, 1)));

        public final List<Vec3i> vectors;

        AntiCevBreakerVectors(List<Vec3i> vectors) {
            this.vectors = vectors;
        }
    }

    private static class Friend {
        public final EntityPlayer friend;
        public double damage;
        public boolean isTotemPopped;
        public boolean isTotemFailed = false;

        public Friend(EntityPlayer friend, double damage) {
            this.friend = friend;
            this.damage = damage;
            this.isTotemPopped = false;
        }

        public Friend(EntityPlayer friend, double damage, boolean isTotemPopped) {
            this.friend = friend;
            this.damage = damage;
            if (isTotemPopped) isTotemFailed = !(mc.player.getHeldItemMainhand().getItem().equals(Items.TOTEM_OF_UNDYING) || mc.player.getHeldItemMainhand().getItem().equals(Items.TOTEM_OF_UNDYING));
            this.isTotemPopped = isTotemPopped;
        }
    }

    public static class RAutoRer implements Runnable {
        private static RAutoRer instance;
        private AutoRer autoRer;

        public static RAutoRer getInstance(AutoRer autoRer) {
            if (instance == null) {
                instance = new RAutoRer();
                instance.autoRer = autoRer;
            }
            return instance;
        }

        @Override
        @SuppressWarnings("BusyWait")
        public void run() {
            if (autoRer.threadMode.getValString().equalsIgnoreCase("While")) {
                while (autoRer.isToggled() && autoRer.threadMode.getValString().equalsIgnoreCase("While")) {
                    if (mc.player == null || mc.world == null || mc.isGamePaused()) break;

                    if (autoRer.shouldInterrupt.get()) {
                        autoRer.shouldInterrupt.set(false);
                        autoRer.synsTimer.reset();
                        autoRer.thread.interrupt();
                    }
                    autoRer.threadOngoing.set(true);
                    autoRer.doAutoRerForThread();
                    autoRer.threadOngoing.set(false);
                    try {
                        Thread.sleep(autoRer.threadDelay.getValLong());
                    } catch (InterruptedException e) {
                        autoRer.thread.interrupt();
                    }
                }
            } else if (!autoRer.threadMode.getValString().equalsIgnoreCase("None")) {
                autoRer.threadOngoing.set(true);
                autoRer.doAutoRerForThread();
                autoRer.threadOngoing.set(false);
            }

            if (!autoRer.thread.isInterrupted()) {
                autoRer.thread.interrupt();
            }
        }
    }
}
