package com.github.programmer3481.ganjang.logic.blocks;

import com.github.programmer3481.ganjang.logic.World;

public class Input extends TickingBlock {
    private final int coolDown;
    private final int outputItem;

    private int currentCooldown = 0;

    public Input(World world, int xPos, int yPos, int texX, int texY, boolean topLayer, int coolDown, int outputItem) {
        super(world, xPos, yPos, texX, texY, topLayer);
        this.coolDown = coolDown;
        this.outputItem = outputItem;
    }

    @Override
    public void tick() {
        if (!moved || moveBlocked) {
            if (currentCooldown >= coolDown) {
                boolean pushSuccess = false;
                for (int i = 0; i < 4; i++) {
                    if (getDirectionBlock(i) instanceof TickingBlock target && !target.isBlocked((i + 2) % 4, outputItem)) {
                        target.pushItem(outputItem, (i + 2) % 4);
                        currentCooldown = 0;
                        pushSuccess = true;
                        moveBlocked = false;
                        break;
                    }
                }
                if (!pushSuccess) {
                    moveBlocked = true;
                }
            }
            if (!moved) currentCooldown++;
            moved = true;
        }
    }

    @Override
    public void pushItem(int item, int direction) {
    }
}
