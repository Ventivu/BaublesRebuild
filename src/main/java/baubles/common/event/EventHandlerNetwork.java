package baubles.common.event;

import baubles.common.Configuration.Configuration;
import baubles.common.container.InventoryBaubles;
import baubles.common.lib.PlayerHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;

public class EventHandlerNetwork {

    public static void syncBaubles(EntityPlayer player) {
        InventoryBaubles inv = PlayerHandler.getPlayerBaubles(player);
        inv.syncContainerToClients();
        for (int a = 0; a < Configuration.getList().size(); a++) {
            inv.syncSlotToClients(a);
        }
    }

    @SubscribeEvent
    public void playerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event) {
        Side side = FMLCommonHandler.instance().getEffectiveSide();
        if (side == Side.SERVER) {
            syncBaubles(event.player);
        }
    }


}
