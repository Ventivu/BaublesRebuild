package baubles.gui;

import baubles.common.Configuration.Configuration;
import baubles.common.container.InventoryBaubles;
import baubles.common.lib.PlayerHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vazkii.botania.client.gui.box.InventoryBaubleBox;
import ventivu.core.WindowFrame.Window;
import ventivu.core.WindowFrame.WindowContainer;

public class ContainerBaubleBox extends WindowContainer {
    public static final int baublesSize=12;
    public InventoryBaubleBox baubleboxinv;
    public InventoryBaubles baubles;

    public ContainerBaubleBox(Window window, EntityPlayer player, World world, int x, int y, int z) {
        super(window, player, world, x, y, z);

    }

    @Override
    protected void preProcess(Window window, EntityPlayer player, World world, int x, int y, int z) {
        baubleboxinv = new InventoryBaubleBox(player, player.inventory.currentItem);
        baubles = new InventoryBaubles(player, Math.min(baublesSize, Configuration.getList().size()));
        if (!player.worldObj.isRemote) {
            baubles = PlayerHandler.getPlayerBaubles(player);
            baubles.setEventHandler(this);
        }
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
    }

    @Override
    public void putStacksInSlots(ItemStack[] stacks) {
        baubles.blockEvents = true;
        super.putStacksInSlots(stacks);
        baubles.blockEvents = false;
    }
}
