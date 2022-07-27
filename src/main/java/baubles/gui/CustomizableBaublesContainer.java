package baubles.gui;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import baubles.common.Config;
import baubles.common.container.InventoryBaubles;
import baubles.common.lib.PlayerHandler;
import com.ventivu.core.GuiFactory.AbstractContainer;
import com.ventivu.core.GuiFactory.CustomGui;
import com.ventivu.core.MagCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

public class CustomizableBaublesContainer extends AbstractContainer {
    public InventoryBaubles baubles;
    private final EntityPlayer thePlayer;

    public CustomizableBaublesContainer(EntityPlayer player, World world, int x, int y, int z, CustomGui manager) {
        super(player, world, x, y, z, manager);
        thePlayer = player;
        baubles = new InventoryBaubles(player, Config.controller.storage.baubleSlots.size());
        baubles.setEventHandler(this);
        indexMap.put(baubles, 0);
        if (!player.worldObj.isRemote) {
            baubles.stackList = PlayerHandler.getPlayerBaubles(player).stackList;
        }
    }

    public void addBaubleSlotCenter(BaubleType type) {
        addBaubleSlotMiddle(40, type);
    }

    public void addBaubleSlotMiddle(int x, BaubleType type) {
        addBaubleSlot(x, CustomGui.cy + 26, type);
    }

    public void addBaubleSlot(int x, int y, BaubleType type) {
        if (indexMap.containsKey(baubles)) {
            int index = indexMap.get(baubles);
            addBaubleSlot(x, y, index, type, baubles);
            indexMap.put(baubles, ++index);
        }
    }


    public void addBaubleSlot(int Xoffset, int Yoffset, int ID, BaubleType type, IInventory inventory) {
        if (inventory == null) {
            if (world.isRemote) return;
            MagCore.logger.log(Level.ERROR, StatCollector.translateToLocalFormatted("exception.nullcontainer", ID));
            return;
        }
        if (ID >= inventory.getSizeInventory()) {
            MagCore.logger.log(Level.ERROR, StatCollector.translateToLocalFormatted("exception.outofcontainer", ID));
            return;
        }
        this.addSlotToContainer(new BaubleSlot(inventory, ID, Xoffset, Yoffset, type));
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        if (!player.worldObj.isRemote) {
            PlayerHandler.setPlayerBaubles(player, baubles);
        }
    }

    @Override
    public void putStacksInSlots(ItemStack[] p_75131_1_) {
        baubles.blockEvents = true;
        super.putStacksInSlots(p_75131_1_);
    }

    private void unequipBauble(ItemStack stack) {
        if (stack.getItem() instanceof IBauble) {
            ((IBauble) stack.getItem()).onUnequipped(stack, thePlayer);
        }
    }

    protected boolean mergeItemStack(ItemStack itemStack, int min, int max, boolean par4, Slot ss) {
        boolean flag1 = false;
        int k = min;

        if (par4) {
            k = max - 1;
        }

        Slot slot;
        ItemStack itemstack1;

        if (itemStack.isStackable()) {
            while (itemStack.stackSize > 0 && (!par4 && k < max || par4 && k >= min)) {
                slot = (Slot) this.inventorySlots.get(k);
                itemstack1 = slot.getStack();

                if (itemstack1 != null && itemstack1.getItem() == itemStack.getItem() && (!itemStack.getHasSubtypes() || itemStack.getItemDamage() == itemstack1.getItemDamage()) && ItemStack.areItemStackTagsEqual(itemStack, itemstack1)) {
                    int l = itemstack1.stackSize + itemStack.stackSize;
                    if (l <= itemStack.getMaxStackSize()) {
                        if (ss instanceof BaubleSlot) unequipBauble(itemStack);
                        itemStack.stackSize = 0;
                        itemstack1.stackSize = l;
                        slot.onSlotChanged();
                        flag1 = true;
                    } else if (itemstack1.stackSize < itemStack.getMaxStackSize()) {
                        if (ss instanceof BaubleSlot) unequipBauble(itemStack);
                        itemStack.stackSize -= itemStack.getMaxStackSize() - itemstack1.stackSize;
                        itemstack1.stackSize = itemStack.getMaxStackSize();
                        slot.onSlotChanged();
                        flag1 = true;
                    }
                }

                if (par4) {
                    --k;
                } else {
                    ++k;
                }
            }
        }

        if (itemStack.stackSize > 0) {
            if (par4) {
                k = max - 1;
            } else {
                k = min;
            }

            while (!par4 && k < max || par4 && k >= min) {
                slot = (Slot) this.inventorySlots.get(k);
                itemstack1 = slot.getStack();

                if (itemstack1 == null) {
                    if (ss instanceof BaubleSlot) unequipBauble(itemStack);
                    slot.putStack(itemStack.copy());
                    slot.onSlotChanged();
                    itemStack.stackSize = 0;
                    flag1 = true;
                    break;
                }

                if (par4) {
                    --k;
                } else {
                    ++k;
                }
            }
        }
        return flag1;
    }
}
