package com.kisman.cc.module.movement;

import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import com.kisman.cc.util.PlayerUtil;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.math.BlockPos;

public class JesusPlus extends Module {
     public final Setting speedX = register(new Setting("Speed", this, 1.0f, 0.01f, 5.0f,  false));

     public JesusPlus(){
        super("JesusPlus", Category.MOVEMENT);
     }
     
     public void update() {
          if (mc.player == null || mc.world == null) return;
          
          if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY - (double)0.2f, mc.player.posZ)).getBlock() instanceof BlockLiquid && !mc.player.onGround) {
               float Speedz = speedX.getValFloat();
               PlayerUtil.setSpeed(mc.player, Speedz);
          }
          if (!(mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + (double)1.0E-4f, mc.player.posZ)).getBlock() instanceof BlockLiquid)) return;
          mc.player.motionX = 0.0;
          mc.player.motionZ = 0.0;
          mc.player.motionY = 0.05f;
     }
}
