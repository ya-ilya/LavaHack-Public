package com.kisman.cc.event.events;

import com.kisman.cc.event.Event;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class DamageBlockEvent extends Event {
    private final BlockPos blockPos;
    private final EnumFacing faceDirection;

    public DamageBlockEvent(BlockPos blockPos, EnumFacing faceDirection) {
        this.blockPos = blockPos;
        this.faceDirection = faceDirection;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public EnumFacing getFaceDirection() {
        return faceDirection;
    }

}