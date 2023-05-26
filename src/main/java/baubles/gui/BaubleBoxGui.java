package baubles.gui;

import baubles.common.Configuration.Configuration;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;
import ventivu.core.GuiFactory.AbstractContainer;
import ventivu.core.GuiFactory.CustomGui;
import ventivu.core.GuiFactory.CustomizableGui;
import ventivu.core.GuiFactory.Guihandler;

public class BaubleBoxGui extends CustomGui {
    static final int offsetx = 1, offsety = 8;

    public BaubleBoxGui(Guihandler handler) {
        super(handler);
    }

    @Override
    protected AbstractContainer getContainer(EntityPlayer player, World world, int x, int y, int z, CustomGui manager) {
        return new ContainerBaubleBox(player, world, x, y, z, manager);
    }

    @Override
    protected void containerConfig(AbstractContainer container) {
        ContainerBaubleBox box = (ContainerBaubleBox) container;
        box.addPlayerInventoryDefault();
        box.addBaubleSlot(offsetx, offsety, Configuration.getList().get(box.getIndex()));
        box.addBaubleSlot(offsetx, offsety + 18, Configuration.getList().get(box.getIndex()));
        box.addBaubleSlot(offsetx, offsety + 36, Configuration.getList().get(box.getIndex()));
        box.addBaubleSlot(offsetx, offsety + 54, Configuration.getList().get(box.getIndex()));
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 6; ++j) {
                box.addAnyBaubleSlot(offsetx + 54 + j * 18, offsety + i * 18);
            }
        }
    }

    @Override
    protected void guiConfig(CustomizableGui gui) {
        gui.addPlayerModel(offsetx + 16, offsety, false);
    }

    @Override
    public Object getProgressLoad(String s) {
        return null;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player, World world, int i, int i1, int i2) {
        ContainerBaubleBox container = ((ContainerBaubleBox) getLoadedContainer());
        //这个转换不能删，删了就找不到isUseableByPlayer
        boolean can = ((IInventory) container.baubleboxinv).isUseableByPlayer(player);
        if (!can) container.onContainerClosed(player);

        return can;
    }
}
