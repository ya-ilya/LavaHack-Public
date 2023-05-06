package com.kisman.cc.module.player;

import com.kisman.cc.mixin.mixins.accessor.AccessorPlayerControllerMP;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.util.BlockUtil;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FastBreak extends Module{
    public FastBreak() {
        super("FastBreak", Category.PLAYER);
    }

    public void update() {
        ((AccessorPlayerControllerMP) mc.playerController).setBlockHitDelay(0);
    }

    @SubscribeEvent
	public void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event){
        float progress = ((AccessorPlayerControllerMP) mc.playerController).getCurBlockDamageMP() + getHardness(event.getPos());
    	if (progress >= 1) return;
    	mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.getPos(), mc.objectMouseOver.sideHit));
	}

    private float getHardness(BlockPos pos) {
        return BlockUtil.getState(pos).getPlayerRelativeBlockHardness(mc.player, mc.world, pos);
    }
}
