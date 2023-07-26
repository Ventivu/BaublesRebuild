package baubles.gui;

import baubles.api.BaubleTypeProxy;
import baubles.api.IBauble;
import baubles.common.Configuration.Configuration;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import ventivu.core.Utils.IFunctions;
import ventivu.core.WindowFrame.GuiWindow;
import ventivu.core.WindowFrame.Widgets.BackGroundWidget;
import ventivu.core.WindowFrame.Widgets.PlayerModelWidget;
import ventivu.core.WindowFrame.Widgets.WidgetContainer;
import ventivu.core.WindowFrame.Window;
import ventivu.core.WindowFrame.WindowContainer;
import ventivu.core.WindowFrame.WindowHandler;

import java.util.ListIterator;
import java.util.Map;

public class BaublesGui extends Window {
    FlippableSlotsPack pack = new FlippableSlotsPack(100, 6, 4);
    WidgetContainer root;

    public BaublesGui(WindowHandler handler) {
        super(handler);
    }

    @Override
    public WindowContainer mkContainer(EntityPlayer player, World world, int x, int y, int z) {
        return new CustomizableBaublesContainer(this, player, world, x, y, z);
    }

    @Override
    public void serverSideConfigs(WindowContainer container, EntityPlayer player, World world, int x, int y, int z) {
        CustomizableBaublesContainer ba = (CustomizableBaublesContainer) container;
        ba.addPlayerInventory(0);
        IInventory baub = ((CustomizableBaublesContainer) container).baubles;
        ListIterator<BaubleTypeProxy> list = Configuration.getList().listIterator();
        pack.genSlotsWithLimit(container, (position) -> {
            if (!list.hasNext()) return null;
            int index = list.nextIndex();
            BaubleTypeProxy type = list.next();
            return new SlotBauble(baub, type.getOldtype(), index, (int) position[0], (int) position[1]);
        });
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateConfigs(Map<String, GuiWindow.UpdateFunc> map) {
        pack.addTextFunc(map, root, "page");
    }

    @Override
    public void buttonActions(Map<Integer, IFunctions.IFunctionValueVoid> funcMap) {
        super.buttonActions(funcMap);
        pack.addButtonFuncs(funcMap);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public WidgetContainer widgetConfigs(GuiWindow window) {
        root = new WidgetContainer(window);
        int width = window.getWidth();
        int height = window.getHeight();
        WidgetContainer page = pack.genPackedContainer("page", 61, -11, window);
        root.addWidget(new BackGroundWidget(0, 0, width, height));
        root.addWidget(new PlayerModelWidget(7, 7));
        root.addSlotGroup("inv", 0, window, ((width - invWidth) >> 1) + 1, invHeight + 8);
        root.addWidget(page);
        window.allowUserInput = true;
        return root;
    }

    @Override
    public ItemStack transferStackInSlot(WindowContainer container, EntityPlayer EntityPlayer, int slotID) {
        ItemStack itemStacktemp = null;
        Slot slot = (Slot) container.inventorySlots.get(slotID);
        int playerslots = container.inventory.mainInventory.length;
        int baubleslots = ((CustomizableBaublesContainer) container).baubles.getSizeInventory();

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack = slot.getStack();
            itemStacktemp = itemstack.copy();
            if (slot instanceof SlotBauble) {
                if (!container.mergeItemStack(itemstack, 0, playerslots, false)) return null;
            } else if (itemstack.getItem() instanceof IBauble) {
                if (!container.mergeItemStack(itemstack, playerslots, playerslots + baubleslots, false)) {
                    if (slotID < 9 && !container.mergeItemStack(itemstack, 9, playerslots, false)) return null;
                    else if (slotID >= 9 && !container.mergeItemStack(itemstack, 0, 9, false)) return null;
                }
            } else if (slotID < 9 && !container.mergeItemStack(itemstack, 9, playerslots, false)) return null;
            else if (slotID >= 9 && !container.mergeItemStack(itemstack, 0, 9, false)) return null;

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
