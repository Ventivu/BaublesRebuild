package baubles.gui;

import baubles.common.network.PacketHandler;
import baubles.common.network.PacketOpenBaublesInventory;
import baubles.common.network.PacketOpenNormalInventory;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiScreenEvent;

import java.lang.reflect.Method;

public class GuiEvents {
    static Method isNEIHidden;

    @SideOnly(value = Side.CLIENT)
    @SuppressWarnings("all")
    @SubscribeEvent
    public void guiPostInit(GuiScreenEvent.InitGuiEvent.Post event) {

        if (event.gui instanceof GuiInventory || event.gui instanceof CustomizableBaubleGui) {

            int xSize = 176;
            int ySize = 166;

            int guiLeft = (event.gui.width - xSize) / 2;
            int guiTop = (event.gui.height - ySize) / 2;


            if (event.gui instanceof GuiInventory) {
                if (!event.gui.mc.thePlayer.getActivePotionEffects().isEmpty() && isNeiHidden())
                    guiLeft = 160 + (event.gui.width - xSize - 200) / 2;
                event.buttonList.add(new GuiBaublesButton(55, guiLeft + 66, guiTop + 9, 10, 10, I18n.format("button.baubles")));
            } else
                event.buttonList.add(new GuiBaublesButton(55, guiLeft + 43, guiTop + 7, 10, 10, I18n.format("button.normal")));
        }

    }

    @SideOnly(value = Side.CLIENT)
    @SubscribeEvent
    public void guiPostAction(GuiScreenEvent.ActionPerformedEvent.Post event) {

        if (event.gui instanceof GuiInventory) {
            if (event.button.id == 55) {
                PacketHandler.INSTANCE.sendToServer(new PacketOpenBaublesInventory(event.gui.mc.thePlayer));
            }
        }

        if (event.gui instanceof CustomizableBaubleGui) {
            if (event.button.id == 55) {
                event.gui.mc.displayGuiScreen(new GuiInventory(event.gui.mc.thePlayer));
                PacketHandler.INSTANCE.sendToServer(new PacketOpenNormalInventory(event.gui.mc.thePlayer));
            }
        }
    }

    public static boolean isNeiHidden() {
        boolean hidden = true;
        try {
            if (isNEIHidden == null) {
                Class<?> fake = Class.forName("codechicken.nei.NEIClientConfig");
                isNEIHidden = fake.getMethod("isHidden");
            }
            hidden = (Boolean) isNEIHidden.invoke(null, new Object[0]);
        } catch (Exception ignored) {}
        return hidden;
    }
}
