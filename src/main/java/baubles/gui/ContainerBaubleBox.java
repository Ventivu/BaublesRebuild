package baubles.gui;

import baubles.api.BaubleTypeProxy;
import baubles.common.container.InventoryBaubles;
import baubles.common.container.SlotBauble;
import baubles.common.lib.PlayerHandler;
import ventivu.core.Core.LoggerHelper;
import ventivu.core.GuiFactory.AbstractContainer;
import ventivu.core.GuiFactory.CustomGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vazkii.botania.client.gui.box.InventoryBaubleBox;

public class ContainerBaubleBox extends AbstractContainer {
    public InventoryBaubleBox baubleboxinv;
    public InventoryBaubles baubles;

    public ContainerBaubleBox(EntityPlayer player, World world, int x, int y, int z, CustomGui manager) {
        super(player, world, x, y, z, manager);
        int slot = player.inventory.currentItem;
        baubleboxinv = new InventoryBaubleBox(player, slot);
        baubles = new InventoryBaubles(player);
        baubles.setEventHandler(this);
        if (!player.worldObj.isRemote) {
            baubles.stackList = PlayerHandler.getPlayerBaubles(player).stackList;
        }
        indexMap.put(baubleboxinv, 0);
        indexMap.put(baubles, 0);
    }

    public void addBaubleSlot(int x, int y, BaubleTypeProxy type) {
        if (indexMap.containsKey(baubles)) {
            int index = indexMap.get(baubles);
            addBaubleSlot(x, y, index, type, baubles);
            indexMap.put(baubles, ++index);
        }
    }

    public void addAnyBaubleSlot(int x, int y) {
        if (indexMap.containsKey(baubleboxinv)) {
            int index = indexMap.get(baubleboxinv);
            addBaubleSlot(x, y, index, BaubleTypeProxy.ANY, baubleboxinv);
            indexMap.put(baubleboxinv, ++index);
        }
    }

    public void addBaubleSlot(int Xoffset, int Yoffset, int ID, BaubleTypeProxy type, IInventory inventory) {
        if (inventory == null) {
            if (world.isRemote) return;
            LoggerHelper.logError(LoggerHelper.ErrorType.NullContainer, ID);
            return;
        }
        if (ID >= inventory.getSizeInventory()) {
            LoggerHelper.logError(LoggerHelper.ErrorType.SlotOutOfRange, ID);
            return;
        }
        this.addSlotToContainer(new SlotBauble(inventory, type.getOldtype(), ID, Xoffset, Yoffset));
    }

    public int getIndex() {
        return indexMap.get(baubles);
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        this.baubleboxinv.pushInventory();
        if (!player.worldObj.isRemote) {
            PlayerHandler.setPlayerBaubles(player, this.baubles);
        }
    }

    @Override
    public void putStacksInSlots(ItemStack[] stacks) {
        baubles.blockEvents = true;
        super.putStacksInSlots(stacks);
    }


}
