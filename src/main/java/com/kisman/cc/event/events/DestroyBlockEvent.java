package com.kisman.cc.event.events;

import com.kisman.cc.event.Event;
import net.minecraft.util.math.BlockPos;

public class DestroyBlockEvent extends Event {
    private final Era era;
    private BlockPos blockPos;

    public DestroyBlockEvent(Era era, BlockPos blockPos) {
        this.era = era;
        this.blockPos = blockPos;
    }

    public Era getEra() {
        return this.era;
    }

    public BlockPos getBlockPos() {
        return this.blockPos;
    }

    public void setBlockPos(BlockPos blockPos) {
        this.blockPos = blockPos;
    }
}
