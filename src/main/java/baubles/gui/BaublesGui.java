package baubles.gui;

import baubles.api.IBauble;
import baubles.common.Configuration.Configuration;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import ventivu.core.WindowFrame.GuiWindow;
import ventivu.core.WindowFrame.Widgets.BackGroundWidget;
import ventivu.core.WindowFrame.Widgets.PlayerModelWidget;
import ventivu.core.WindowFrame.Widgets.WidgetContainer;
import ventivu.core.WindowFrame.Window;
import ventivu.core.WindowFrame.WindowContainer;
import ventivu.core.WindowFrame.WindowHandler;

public class BaublesGui extends Window {
    FlippableSlotsPack pack = new FlippableSlotsPack(100, 6, 4);
    WidgetContainer root;

    public BaublesGui(WindowHandler handler) {
        super(handler);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public GuiWindow mkWindow(EntityPlayer player, World world, int x, int y, int z) {
        return new GuiBaubleImpl(mkContainer(player, world, x, y, z));
    }

    @Override
    public WindowContainer mkContainer(EntityPlayer player, World world, int x, int y, int z) {
        return new ContainerBaubles(this, player, world, x, y, z);
    }

    @Override
    public void serverConfigs(WindowContainer container, EntityPlayer player, World world, int x, int y, int z) {
        ContainerBaubles ba = (ContainerBaubles) container;
        ba.addPlayerInventory(0);
        pack.genSlots(container, Configuration.getList(), ((ContainerBaubles) container).baubles);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public WidgetContainer widgetConfigs(GuiWindow window) {
        root = new WidgetContainer(window);
        int width = window.getWidth();
        int height = window.getHeight();
        WidgetContainer page = pack.genPackedContainer(62, -11, window);
        root.addWidget(new BackGroundWidget(0, 0, width, height));
        root.addWidget(new PlayerModelWidget(7, 7));
        root.addSlotGroup(0, window, ((width - invWidth) >> 1) + 1, invHeight + 8);
        root.addWidget(page);
        window.allowUserInput = true;
        return root;
    }

    @Override
    public ItemStack transferStackInSlot(WindowContainer container, EntityPlayer EntityPlayer, int slotID) {
        ItemStack itemstack = null;
        Slot slot = (Slot) container.inventorySlots.get(slotID);
        int playerslots = container.inventory.mainInventory.length;
        int baubleslots = ((ContainerBaubles) container).baubles.getSizeInventory();
        ItemStack oldStack;
        if (slot != null && slot.getHasStack()) {
            itemstack = slot.getStack();
            oldStack = itemstack.copy();
            if (slot instanceof SlotBauble) {
                if (!container.mergeItemStack(itemstack, 0, playerslots, false)) return null;
            }
            else if (itemstack.getItem() instanceof IBauble) {
                if (!container.mergeItemStack(itemstack, playerslots, playerslots + baubleslots, false)) {
                    if (!container.mergeItemStack(itemstack, 0, playerslots, false)) return null;
                }
            }
            else if (!container.mergeItemStack(itemstack, 0, playerslots, false)) return null;

            if (itemstack.stackSize == 0) {
                slot.putStack(null);
            }
            else {
                slot.onSlotChanged();
            }
            oldStack.stackSize -= itemstack.stackSize;
            slot.onPickupFromSlot(EntityPlayer, oldStack);
            if (itemstack.stackSize == 0) itemstack = null;
        }
        return itemstack;
    }
}
