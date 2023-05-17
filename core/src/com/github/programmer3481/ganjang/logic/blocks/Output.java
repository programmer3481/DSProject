package com.github.programmer3481.ganjang.logic.blocks;

import com.github.programmer3481.ganjang.logic.World;

public class Output extends TickingBlock {
    public Output(World world, int xPos, int yPos, int texX, int texY, boolean topLayer) {
        super(world, xPos, yPos, texX, texY, topLayer);
        for (int i = 0; i < 4; i++) {
            blocked[i] = false;
        }
    }

    @Override
    public void pushItem(int item, int direction) {
        getWorld().addStat(item);
    }

    @Override
    public void tick() {

    }
}
