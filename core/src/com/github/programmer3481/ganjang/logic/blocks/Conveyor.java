package com.github.programmer3481.ganjang.logic.blocks;

import com.github.programmer3481.ganjang.logic.World;

import java.util.*;

public class Conveyor extends TickingBlock {
    private final int direction;
    private final int speed;
    private boolean searchDir = false;
    private final Deque<ConveyorItem> items = new ArrayDeque<>(3);

    public int color; //DEBUG

    public Conveyor(World world, int xPos, int yPos, int texX, int texY, boolean topLayer, int direction, int speed) {
        super(world, xPos, yPos, texX, texY, topLayer);
        this.direction = direction;
        this.speed = speed;
        this.color = new Random().nextInt(8); //DEBUG
    }

    @Override
    public void tick() {
        if (getDirectionBlock(direction) instanceof Conveyor c) { //DEBUG
            c.color = this.color;
        }

        boolean[] oldBlocked = blocked.clone();

        if (!moved) {
            ConveyorItem frontItem = null;
            for (Iterator<ConveyorItem> iterator = items.iterator(); iterator.hasNext(); ) {
                ConveyorItem item = iterator.next();
                int sign = Integer.signum(item.hPosition);
                item.hPosition -= sign * speed;
                if (sign != Integer.signum(item.hPosition)) item.hPosition = 0;

                item.position -= speed  ;
                if (frontItem == null && item.position < 0) {
                    if (getDirectionBlock(direction) instanceof TickingBlock target && !target.isBlocked((direction + 2) % 4, item.itemId)) {
                        if (target instanceof Conveyor) {
                            ((Conveyor) target).pushItem(item.itemId, (direction + 2) % 4, -item.position);
                        } else {
                            target.pushItem(item.itemId, (direction + 2) % 4);
                        }
                        iterator.remove();
                    } else {
                        moveBlocked = true;
                        item.position += speed;
                    }
                } else if (frontItem != null && frontItem.position > item.position - 128) {
                    item.position += speed;
                }

                frontItem = item;
            }
            searchDir = !searchDir;
            moved = true;
        }
        else if (moveBlocked && getDirectionBlock(direction) instanceof TickingBlock target && target.isBlocked((direction + 2) % 4, items.peekFirst().itemId)) {
            ConveyorItem frontItem = null;
            for (Iterator<ConveyorItem> iterator = items.iterator(); iterator.hasNext(); ) {
                ConveyorItem item = iterator.next();
                item.position -= speed;
                if (frontItem == null && item.position < 0) {
                    if (target instanceof Conveyor) {
                        ((Conveyor) target).pushItem(item.itemId, (direction + 2) % 4, -item.position);
                    } else {
                        target.pushItem(item.itemId, (direction + 2) % 4);
                    }
                    iterator.remove();
                } else if (frontItem != null && frontItem.position < item.position - 128 - speed) {
                    item.position += speed;
                    break;
                }

                frontItem = item;
            }
            moveBlocked = false;
        }
        for (int i = 0; i < 4; i++) {
            if ((direction + 1) % 4 == i || (direction + 3) % 4 == i) {
                blocked[i] = !(items.size() == 0 || items.peekLast().position == 0);
            }
            else if ((direction + 2) % 4 == i) {
                blocked[i] = !(items.size() == 0 || items.peekLast().position <= 256 - 129);
            }
        }

        for (int i = searchDir ? 0 : 3; searchDir ? i < 4 : i >= 0; i += searchDir ? 1 : -1) {
            if (oldBlocked[i] && !blocked[i]) {
                if (getDirectionBlock(i) instanceof TickingBlock tickingBlock) {
                    tickingBlock.tick();
                }
            }
        }

    }

    public void pushItem(int item, int direction, int offset) { //from dir
        if ((this.direction + 1) % 4 == direction) {
            items.addLast(new ConveyorItem(item, 128, -128 + offset));
        }
        else if ((this.direction + 2) % 4 == direction) {
            items.addLast(new ConveyorItem(item, 256 - offset, 0));
        }
        else if ((this.direction + 3) % 4 == direction) {
            items.addLast(new ConveyorItem(item, 128, 128 - offset));
        }
        for (int i = 0; i < 4; i++) {
            if ((this.direction + 1) % 4 == i || (this.direction + 3) % 4 == i) {
                blocked[i] = !(items.size() == 0 || items.peekLast().position == 0);
            }
            else if ((this.direction + 2) % 4 == i) {
                blocked[i] = !(items.size() == 0 || items.peekLast().position <= 256 - 129);
            }
        }
    }

    @Override
    public void pushItem(int item, int direction) {
        pushItem(item, direction, 1);
    }

    public int getDirection() {
        return direction;
    }

    public Deque<ConveyorItem> getItems() {
        return items;
    }

    public static class ConveyorItem {
        public final int itemId;
        public int hPosition;
        public int position;

        public ConveyorItem(int itemId, int position, int hPosition) {
            this.itemId = itemId;
            this.position = position;
            this.hPosition = hPosition;
        }
    }

    @Override
    public int getTexY() {
        return super.getTexY() + direction;
    }
}
