package com.github.programmer3481.ganjang.logic.blocks;

import com.github.programmer3481.ganjang.logic.World;

import java.util.Arrays;

public class Machine extends TickingBlock {
    private final int coolDown;
    private final int inputItem;
    private final int inputCount;
    private final int outputItem;

    private boolean searchDir = false;

    private int itemCount = 0;
    private int currentCooldown = 0;

    public Machine(World world, int xPos, int yPos, int texX, int texY, boolean topLayer, int coolDown, int inputItem, int inputCount, int outputItem) {
        super(world, xPos, yPos, texX, texY, topLayer);
        this.coolDown = coolDown;
        this.inputItem = inputItem;
        this.inputCount = inputCount;
        this.outputItem = outputItem;
    }

    @Override
    public void tick() {
        boolean[] oldBlocked = blocked.clone();

        if (!moved || moveBlocked) {
            if (itemCount >= inputCount) {
                if (currentCooldown >= coolDown) {
                    boolean pushSuccess = false;
                    for (int i = 0; i < 4; i++) {
                        if (getDirectionBlock(i) instanceof TickingBlock target && !target.isBlocked((i + 2) % 4, outputItem)) {
                            target.pushItem(outputItem, (i + 2) % 4);
                            currentCooldown = 0;
                            itemCount = 0;
                            System.out.println(i);
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
            }
            moved = true;
        }

        for (int i = 0; i < 4; i++) {
            blocked[i] = itemCount >= inputCount;
        }

        for (int i = searchDir ? 0 : 3; searchDir ? i < 4 : i >= 0; i += searchDir ? 1 : -1) {
            if (oldBlocked[i] && !blocked[i]) {
                if (getDirectionBlock(i) instanceof TickingBlock tickingBlock) {
                    tickingBlock.tick();
                }
            }
        }
    }

    @Override
    public boolean isBlocked(int direction, int item) {
        return blocked[direction] || inputItem != item;
    }

    @Override
    public void pushItem(int item, int direction) {
        itemCount++;
        for (int i = 0; i < 4; i++) {
            blocked[i] = itemCount >= inputCount;
        }
    }
}