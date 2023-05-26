package baubles.gui;

import baubles.api.BaubleTypeProxy;
import baubles.api.IBauble;
import baubles.common.Configuration.Configuration;
import baubles.common.container.SlotBauble;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import ventivu.core.GuiFactory.*;

public class BaublesGui extends CustomGui {
    public static int playerX = 7, playerY = 7;

    public BaublesGui(Guihandler handler) {
        super(handler);
    }

    @Override
    protected AbstractContainer getContainer(EntityPlayer player, World world, int x, int y, int z, CustomGui manager) {
        return new CustomizableBaublesContainer(player, world, x, y, z, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected CustomizableGui getGuiContainer(AbstractContainer container, @Nullable TileEntity entity, EntityLivingBase user) {
        return new CustomizableBaubleGui(container, entity, user);
    }

    @Override
    protected void containerConfig(AbstractContainer customizableContainer) {
        if (customizableContainer instanceof CustomizableBaublesContainer) {
            CustomizableBaublesContainer container = (CustomizableBaublesContainer) customizableContainer;
            container.addPlayerInventoryDefault();
            double a=4;
            int count = Configuration.getList().size();
            for (int i = 0; i < Math.ceil(count / a); i++)
                for (int j = 0; j < count-a*i&&j<a; j++) {
                    BaubleTypeProxy type = Configuration.getList().get((int) (i * a + j));
                    container.addBaubleSlot(48+BaublesGui.playerX+i*18, BaublesGui.playerY+1+j*18, type);
                }
        }
    }

    @Override
    protected void guiConfig(CustomizableGui gui) {
        gui.addPlayerModel(playerX,playerY, true);
        gui.allowUserInput = true;
    }

    @Override
    public Object getProgressLoad(String s) {
        return null;
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer, World world, int i, int i1, int i2) {
        return true;
    }

    @Override
    protected ItemStack transferStackInSlot(AbstractContainer container, EntityPlayer EntityPlayer, int slotID) {
        ItemStack itemStacktemp = null;
        Slot slot = (Slot) container.inventorySlots.get(slotID);
        int playerslots = ((CustomizableBaublesContainer) container).getindex(container.inventory);
        int baubleslots = ((CustomizableBaublesContainer) container).getindex(((CustomizableBaublesContainer) container).baubles);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack = slot.getStack();
            itemStacktemp = itemstack.copy();
            if (slot instanceof SlotBauble) {
                if (!container.mergeItemStacks(itemstack, 0, playerslots, false)) return null;
            } else if (itemstack.getItem() instanceof IBauble) {
                if (!((CustomizableBaublesContainer) container).mergeItemStack(itemstack, playerslots, playerslots + baubleslots, false, slot)) {
                    if (slotID < 9 && !container.mergeItemStacks(itemstack, 9, playerslots, false)) return null;
                    else if (slotID >= 9 && !container.mergeItemStacks(itemstack, 0, 9, false)) return null;
                }
            } else if (slotID < 9 && !container.mergeItemStacks(itemstack, 9, playerslots, false)) return null;
            else if (slotID >= 9 && !container.mergeItemStacks(itemstack, 0, 9, false)) return null;

            if (itemstack.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }

            if (itemStacktemp.stackSize == itemstack.stackSize) {
                return null;
            }

            slot.onPickupFromSlot(EntityPlayer, itemstack);
        }
        return itemStacktemp;
    }
}
