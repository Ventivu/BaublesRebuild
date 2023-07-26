package baubles.gui;

import baubles.api.BaubleTypeProxy;
import baubles.common.Configuration.Configuration;
import baubles.common.lib.PlayerHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;
import ventivu.core.WindowFrame.GuiWindow;
import ventivu.core.WindowFrame.Widgets.BackGroundWidget;
import ventivu.core.WindowFrame.Widgets.WidgetContainer;
import ventivu.core.WindowFrame.Window;
import ventivu.core.WindowFrame.WindowContainer;
import ventivu.core.WindowFrame.WindowHandler;

import java.util.List;
import java.util.Map;

public class BaubleBoxGui extends Window {

    public BaubleBoxGui(WindowHandler handler) {
        super(handler);
    }

    @Override
    public WindowContainer mkContainer(EntityPlayer player, World world, int x, int y, int z) {
        return new ContainerBaubleBox(this, player, world, x, y, z);
    }

    @Override
    public void serverSideConfigs(WindowContainer container, EntityPlayer player, World world, int x, int y, int z) {
        int yy = 0, counter = 0;
        ContainerBaubleBox box = (ContainerBaubleBox) container;
        box.addPlayerInventory(0);
        List<BaubleTypeProxy> list = Configuration.getList();
        while (counter < list.size() && counter < 4)
            box.addSlotToContainer(new SlotBauble(box.baubles, list.get(counter).getOldtype(), counter++, 0, yy += 18).setGroupID(1));
        for (int i = 0; i < 6; ++i)
            for (int j = 0; j < 4; ++j)
                box.addSlotToContainer(new SlotBauble(box.baubleboxinv, null, i * 4 + j, i * 18, j * 18).setGroupID(2));
    }

    @Override
    public void updateConfigs(Map<String, GuiWindow.UpdateFunc> map) {

    }

    @Override
    public WidgetContainer widgetConfigs(GuiWindow window) {
        int width = window.getWidth();
        int height = window.getHeight();
        int xCenter = width >> 1;
        WidgetContainer root = new WidgetContainer(window);
        WidgetContainer container = new WidgetContainer("main", window.getGuiLeft(), window.getGuiLeft(), width, height);
        container.addWidget(new BackGroundWidget(0, 0, width, height));
        container.addSlotGroup("inv", 0, window, ((width - invWidth) >> 1) + 1, invHeight + 8);
        container.addSlotGroup("baubles", 1, window, 4, 8);
        container.addSlotGroup("box", 2, window, width - 112, 8);
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
        boolean can = ((IInventory)((ContainerBaubleBox) container).baubleboxinv).isUseableByPlayer(player);
        if (!can) container.onContainerClosed(player);
        return can;
    }
}
