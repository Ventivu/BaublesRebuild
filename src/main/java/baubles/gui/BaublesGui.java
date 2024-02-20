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

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean transferPlayerSlot(WindowContainer container, ItemStack stack, int start, int slotID) {
        final int realA = start + 9, realB = start + 36;
        if (slotID < realA) {
            if (!container.mergeItemStack(stack, realA, realB, false))
                return container.mergeStackInRange(stack, start, realA, slotID, false);
        }
        else if (!container.mergeItemStack(stack, start, start + 9, false))
                return container.mergeStackInRange(stack, realA, realB, slotID, false);
        return true;
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
    public ItemStack transferStackInSlot(WindowContainer container, EntityPlayer player, int slotID) {
        final Slot slot = (Slot) container.inventorySlots.get(slotID);
        final int playerslots = container.inventory.mainInventory.length;
        final int baubleslots = ((ContainerBaubles) container).baubles.getSizeInventory();
        ItemStack itemstack = null;
        ItemStack oldStack;
        if (slot != null && slot.getHasStack()) {
            itemstack = slot.getStack();
            oldStack = itemstack.copy();
            if (slot instanceof SlotBauble) {
                if (!container.mergeItemStack(itemstack, 0, playerslots, false)) return null;
            }
            else if (itemstack.getItem() instanceof IBauble) {
                if (!container.mergeItemStack(itemstack, playerslots, playerslots + baubleslots, false)) {
                    if (!transferPlayerSlot(container, itemstack, 0, slotID)) return null;
                }
            }
            else if (!transferPlayerSlot(container, itemstack, 0, slotID)) return null;

            oldStack.stackSize -= itemstack.stackSize;
            if (itemstack.stackSize == 0) {
                slot.putStack(null);
                itemstack = null;
            }
            else {
                slot.onSlotChanged();
            }
            slot.onPickupFromSlot(player, oldStack);
        }
        return itemstack;
    }
}
