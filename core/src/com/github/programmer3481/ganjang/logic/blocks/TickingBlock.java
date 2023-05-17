package com.github.programmer3481.ganjang.logic.blocks;

import com.github.programmer3481.ganjang.logic.World;

public abstract class TickingBlock extends Block {
    protected final boolean[] blocked = new boolean[] {true, true, true, true};
    protected boolean moved = false;
    protected boolean moveBlocked = false;
    protected TickingBlock[] targets = new TickingBlock[4];


    public TickingBlock(World world, int xPos, int yPos, int texX, int texY, boolean topLayer) {
        super(world, xPos, yPos, texX, texY, topLayer);
    }

    public boolean isBlocked(int direction, int item) {
        return blocked[direction];
    }

    public abstract void pushItem(int item, int direction);

    public void reset() {
        moved = false;
        moveBlocked = false;
    }

    protected Block getDirectionBlock(int direction) {
        return getWorld().getBlock(getxPos() + (direction       % 2 == 0 ? 0 : 2 - direction),
                                   getyPos() + ((direction + 1) % 2 == 0 ? 0 : 1 - direction));
    }

    public abstract void tick();
}
