package baubles.gui;

import baubles.api.IBauble;
import baubles.common.Configuration.Configuration;
import baubles.common.container.InventoryBaubles;
import baubles.common.lib.PlayerHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import ventivu.core.WindowFrame.Window;
import ventivu.core.WindowFrame.WindowContainer;

public class ContainerBaubles extends WindowContainer {
    public InventoryBaubles baubles;
    private EntityPlayer thePlayer;

    public ContainerBaubles(Window window, EntityPlayer player, World world, int x, int y, int z) {
        super(window, player, world, x, y, z);
    }

    @Override
    protected void preProcess(Window window, EntityPlayer player, World world, int x, int y, int z) {
        thePlayer = player;
        baubles = new InventoryBaubles(player, Configuration.getList().size());
        baubles.setEventHandler(this);
        if (!player.worldObj.isRemote) {
            baubles.stackList = PlayerHandler.getPlayerBaubles(player).stackList;
        }
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        if (!player.worldObj.isRemote) {
            PlayerHandler.setPlayerBaubles(player, baubles);
        }
    }

    @Override
    public void putStacksInSlots(ItemStack[] stacks) {
        baubles.blockEvents = true;
        super.putStacksInSlots(stacks);
    }

    public void unequipBauble(ItemStack stack) {
        if (stack.getItem() instanceof IBauble) {
            ((IBauble) stack.getItem()).onUnequipped(stack, thePlayer);
        }
    }
}
