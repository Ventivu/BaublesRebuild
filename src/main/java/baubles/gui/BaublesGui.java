package baubles.gui;

import baubles.api.IBauble;
import baubles.common.CommonProxy;
import baubles.common.Config;
import com.ventivu.core.GuiFactory.AbstractContainer;
import com.ventivu.core.GuiFactory.CustomGui;
import com.ventivu.core.GuiFactory.CustomizableGui;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BaublesGui extends CustomGui {
    public static int playerX = 3, playerY = 5;

    public BaublesGui() {
        super(CommonProxy.handler);
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected CustomizableGui creatGui(EntityPlayer player, World world, int x, int y, int z) {
        AbstractContainer container=new CustomizableBaublesContainer(player,world,x,y,z,this);
        containerConfig(container);
        CustomizableGui gui = new CustomizableBaubleGui(container, world.getTileEntity(x,y,z), player);
        guiConfig(gui);
        return gui;
    }

    @Override
    protected AbstractContainer creatContainer(EntityPlayer player, World world, int x, int y, int z) {
        this.container = new CustomizableBaublesContainer(player, world, x, y, z, this);
        this.tile = world.getTileEntity(x, y, z);
        this.containerConfig(this.container);
        return container;
    }

    @Override
    protected void containerConfig(AbstractContainer customizableContainer) {
        if (customizableContainer instanceof CustomizableBaublesContainer) {
            CustomizableBaublesContainer container = (CustomizableBaublesContainer) customizableContainer;
            container.addPlayerInventoryDefault();
            if (Config.isDefaultMode()) UIController.apply(UIController.example(), container);
            else UIController.apply(Config.controller.storage, container);
        }
    }

    @Override
    protected void guiConfig(CustomizableGui gui) {
        gui.addPlayerModel(gui.getguiLeft() + playerX, gui.getguiTop() + playerY, true);
        gui.allowUserInput = true;
    }

    @Override
    public Object getProgressLoad(String s) {
        return null;
    }

    @Override
    protected boolean canInteractWith(EntityPlayer entityPlayer, World world, int i, int i1, int i2) {
        return true;
    }

    @Override
    protected ItemStack transferStackInSlot(AbstractContainer container,EntityPlayer EntityPlayer, int slotID) {
        ItemStack itemStacktemp=null;
        Slot slot = (Slot) container.inventorySlots.get(slotID);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack = slot.getStack();
            itemStacktemp = itemstack.copy();
            if (itemstack.getItem() instanceof IBauble) {
                if (slot instanceof BaubleSlot) {
                    if (!container.mergeItemStack(itemstack, 0, 35, false)) {
                        return null;
                    }
                }
            }

            if (itemstack.stackSize == 0) {
                slot.putStack(null);
            }
            else {
                slot.onSlotChanged();
            }

            if (itemStacktemp.stackSize == itemstack.stackSize) {
                return null;
            }

            slot.onPickupFromSlot(EntityPlayer, itemstack);
        }
        return itemStacktemp;


        /*if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (slotID >= 1 && slotID < 9) {
                if (!container.mergeItemStack(itemstack1, 9 + 4, 45 + 4, false)) {
                    return null;
                }
            } else if (itemstack.getItem() instanceof ItemArmor && !((Slot) container.inventorySlots.get(5 + ((ItemArmor) itemstack.getItem()).armorType)).getHasStack()) {
                int j = 5 + ((ItemArmor) itemstack.getItem()).armorType;

                if (!container.mergeItemStack(itemstack1, j, j + 1, false)) {
                    return null;
                }
            } else if (itemstack.getItem() instanceof IBauble && ((IBauble) itemstack.getItem()).getBaubleType(itemstack) == BaubleType.AMULET && ((IBauble) itemstack.getItem()).canEquip(itemstack, EntityPlayer) && !((Slot) container.inventorySlots.get(9)).getHasStack()) {
                int j = 9;
                if (!container.mergeItemStack(itemstack1, j, j + 1, false)) {
                    return null;
                }
            } else if (slotID > 11 && itemstack.getItem() instanceof IBauble && ((IBauble) itemstack.getItem()).getBaubleType(itemstack) == BaubleType.RING && ((IBauble) itemstack.getItem()).canEquip(itemstack, EntityPlayer) && !((Slot) container.inventorySlots.get(10)).getHasStack()) {
                int j = 10;
                if (!container.mergeItemStack(itemstack1, j, j + 1, false)) {
                    return null;
                }
            } else if (slotID > 11 && itemstack.getItem() instanceof IBauble && ((IBauble) itemstack.getItem()).getBaubleType(itemstack) == BaubleType.RING && ((IBauble) itemstack.getItem()).canEquip(itemstack, EntityPlayer) && !((Slot) container.inventorySlots.get(11)).getHasStack()) {
                int j = 11;
                if (!container.mergeItemStack(itemstack1, j, j + 1, false)) {
                    return null;
                }
            } else if (itemstack.getItem() instanceof IBauble && ((IBauble) itemstack.getItem()).getBaubleType(itemstack) == BaubleType.BELT && ((IBauble) itemstack.getItem()).canEquip(itemstack, EntityPlayer) && !((Slot) container.inventorySlots.get(12)).getHasStack()) {
                int j = 12;
                if (!container.mergeItemStack(itemstack1, j, j + 1, false)) {
                    return null;
                }
            } else if (slotID >= 9 + 4 && slotID < 36 + 4) {
                if (!container.mergeItemStack(itemstack1, 36 + 4, 45 + 4, false)) {
                    return null;
                }
            } else if (slotID >= 36 + 4 && slotID < 45 + 4) {
                if (!container.mergeItemStack(itemstack1, 9 + 4, 36 + 4, false)) {
                    return null;
                }
            } else if (!container.mergeItemStack(itemstack1, 9 + 4, 45 + 4, false, slot)) {
                return null;
            }

            if (itemstack1.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize) {
                return null;
            }

            slot.onPickupFromSlot(EntityPlayer, itemstack1);
        }

        return itemstack;*/
    }
}
