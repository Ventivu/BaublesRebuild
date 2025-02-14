package baubles.gui;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import baubles.common.Configuration.Configuration;
import baubles.common.lib.PlayerHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import ventivu.core.Core.DeployError;
import ventivu.core.Core.Reason;
import ventivu.core.WindowFrame.*;
import ventivu.core.WindowFrame.Widgets.BackGroundWidget;
import ventivu.core.WindowFrame.Widgets.WidgetContainer;
import ventivu.core.WindowFrame.Widgets.interfaces.IWidget;

import java.util.List;

public class BaubleBoxGui extends Window {

    public BaubleBoxGui(WindowHandler handler) {
        super(handler);
    }

    @Override
    public WindowContainer mkContainer(EntityPlayer player, World world, int x, int y, int z) {
        return new ContainerBaubleBox(this, player, world, x, y, z);
    }

    @Override
    public void serverConfigs(WindowContainer container, EntityPlayer player, World world, int x, int y, int z) {
        int counter = 0;
        ContainerBaubleBox box = (ContainerBaubleBox) container;
        box.addPlayerInventory(0);
        List<BaubleType> list = Configuration.getList();
        while (counter < list.size() && counter < ContainerBaubleBox.baublesSize)
            box.addSlotToContainer(new SlotBauble(box.baubles, list.get(counter), counter, (counter / 4) * 18, (counter++ % 4) * 18).setGroupID(1));
        for (int i = 0; i < 6; ++i)
            for (int j = 0; j < 4; ++j)
                box.addSlotToContainer(new SlotBauble(box.baubleboxinv, null, i * 4 + j, i * 18, j * 18).setGroupID(2));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public WidgetContainer widgetConfigs(GuiWindow window) {
        int round = 7;
        int width = window.getWidth();
        int height = window.getHeight();
        GuiRenderUtils.bindTexture(new ResourceLocation("magcore", "textures/gui/widgets.png"));
        WidgetBorder boreder = GuiRenderUtils.getBorder("BG");
        WidgetContainer root = new WidgetContainer(window);
        WidgetContainer container = new WidgetContainer(0, 0, width, height);

        container.addWidget(new BackGroundWidget(0, 0, width, height));

        BackGroundWidget widget = new BackGroundWidget(((width - invWidth) >> 1) - boreder.getLeft(), round - boreder.getTop() - 1, 0, 0);
        container.addWidget(widget);

        container.addSlotGroup(0, window, ((width - invWidth) >> 1) + 1, height - invHeight - round);
        container.addSlotGroup(1, window, ((width - invWidth) >> 1) + 1, round);
        List<IWidget<?>> widgets = container.get();
        int count = ((WidgetContainer) widgets.get(widgets.size() - 1)).get().size();
        widget.setWidth((int) (18 * (Math.ceil(count / 4f))) + boreder.getLeft() + boreder.getRight());
        widget.setHeight((count > 4 ? 72 : (18 * count)) + boreder.getTop() + boreder.getBottom());

        container.addSlotGroup(2, window, width - 114, round);
        root.addWidget(container);
        return root;
    }

    @Override
    public void onContainerClosed(WindowContainer container, EntityPlayer player) {
        ((ContainerBaubleBox) container).baubleboxinv.pushInventory();
        if (!player.worldObj.isRemote) {
            PlayerHandler.setPlayerBaubles(player, ((ContainerBaubleBox) container).baubles);
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player, WindowContainer container) {
        //这个转换不能删，删了就找不到isUseableByPlayer
        boolean can = ((IInventory) ((ContainerBaubleBox) container).baubleboxinv).isUseableByPlayer(player);
        if (!can) container.onContainerClosed(player);
        return can;
    }

    @Override
    public ItemStack transferStackInSlot(WindowContainer container, EntityPlayer EntityPlayer, int slotID) {
        final int playerslots = container.inventory.mainInventory.length;
        final int baubleslots = Math.min((((ContainerBaubleBox) container).baubles).getSizeInventory(), ContainerBaubleBox.baublesSize) + playerslots;
        final int boxSlots = ((IInventory) ((ContainerBaubleBox) container).baubleboxinv).getSizeInventory() + baubleslots;

        ItemStack itemstack = null;
        Slot slot = (Slot) container.inventorySlots.get(slotID);
        ItemStack oldStack;

        if (slot != null && slot.getHasStack()) {
            itemstack = slot.getStack();
            oldStack = itemstack.copy();

            if (slotID < playerslots) {
                if (itemstack.getItem() instanceof IBauble) {
                    if (!container.mergeItemStack(itemstack, playerslots, baubleslots, false))
                        if (!container.mergeItemStack(itemstack, baubleslots, boxSlots, false))
                            if(BaublesGui.transferPlayerSlot(container,itemstack,0,slotID))return null;
                }
                else if(BaublesGui.transferPlayerSlot(container,itemstack,0,slotID))return null;
            }
            else if (slotID < baubleslots) {
                if (!container.mergeItemStack(itemstack, baubleslots, boxSlots, false))
                    if (!container.mergeItemStack(itemstack, 0, playerslots, false)) return null;
            }
            else if (slotID < boxSlots) {
                if (!container.mergeItemStack(itemstack, playerslots, baubleslots, false))
                    if (!container.mergeItemStack(itemstack, 0, playerslots, false)) return null;
            }
            else throw new DeployError(Reason.WRONGDATA);

            if (itemstack.stackSize == 0) slot.putStack(null);
            else slot.onSlotChanged();

            oldStack.stackSize -= itemstack.stackSize;
            slot.onPickupFromSlot(EntityPlayer, oldStack);
            if (itemstack.stackSize == 0) itemstack = null;
        }
        return itemstack;
    }
}
