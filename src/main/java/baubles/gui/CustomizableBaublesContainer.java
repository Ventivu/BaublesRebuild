package baubles.gui;

import baubles.api.BaubleTypeProxy;
import baubles.api.IBauble;
import baubles.common.Configuration.Configuration;
import baubles.common.container.InventoryBaubles;
import baubles.common.container.SlotBauble;
import baubles.common.lib.PlayerHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;
import ventivu.core.GuiFactory.AbstractContainer;
import ventivu.core.GuiFactory.CustomGui;
import ventivu.core.MagCore;

public class CustomizableBaublesContainer extends AbstractContainer {
    public InventoryBaubles baubles;
    private final EntityPlayer thePlayer;

    public CustomizableBaublesContainer(EntityPlayer player, World world, int x, int y, int z, CustomGui manager) {
        super(player, world, x, y, z, manager);
        thePlayer = player;
        baubles = new InventoryBaubles(player, Configuration.getList().size());
        baubles.setEventHandler(this);
        indexMap.put(baubles, 0);
        if (!player.worldObj.isRemote) {
            baubles.stackList = PlayerHandler.getPlayerBaubles(player).stackList;
        }
    }

    public void addBaubleSlotCenter(BaubleTypeProxy type) {
        addBaubleSlotMiddle(40, type);
    }

    public void addBaubleSlotMiddle(int x, BaubleTypeProxy type) {
        addBaubleSlot(x, CustomGui.cy + 26, type);
    }

    public void addBaubleSlot(int x, int y, BaubleTypeProxy type) {
        if (indexMap.containsKey(baubles)) {
            int index = indexMap.get(baubles);
            addBaubleSlot(x, y, index, type, baubles);
            indexMap.put(baubles, ++index);
        }
    }


    public void addBaubleSlot(int Xoffset, int Yoffset, int ID, BaubleTypeProxy type, IInventory inventory) {
        if (inventory == null) {
            if (world.isRemote) return;
            MagCore.logger.log(Level.ERROR, StatCollector.translateToLocalFormatted("exception.nullcontainer", ID));
            return;
        }
        if (ID >= inventory.getSizeInventory()) {
            MagCore.logger.log(Level.ERROR, StatCollector.translateToLocalFormatted("exception.outofcontainer", ID));
            return;
        }
        this.addSlotToContainer(new SlotBauble(inventory, type.getOldtype(), ID, Xoffset, Yoffset));
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

    public void unequipBauble(ItemStack stack) {
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
                        itemStack.stackSize = 0;
                        itemstack1.stackSize = l;
                        slot.onSlotChanged();
                        flag1 = true;
                    } else if (itemstack1.stackSize < itemStack.getMaxStackSize()) {
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

                if (itemstack1 == null && slot.isItemValid(itemStack)) {
//                    if(itemStack.getItem() instanceof IBauble) ((IBauble)itemStack.getItem()).onEquipped(itemStack, thePlayer);
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

    public int getindex(IInventory inv) {
        return indexMap.get(inv);
    }
}
