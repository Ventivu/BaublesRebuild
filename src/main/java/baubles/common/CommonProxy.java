package baubles.common;

import baubles.api.BaubleType;
import baubles.common.event.KeyHandler;
import baubles.gui.BaubleBoxGui;
import baubles.gui.BaublesGui;
import net.minecraft.world.World;
import ventivu.core.WindowFrame.WindowHandler;

import java.util.List;

import static baubles.common.Configuration.Configuration.slots;


public class CommonProxy {
    public static WindowHandler handler;
    public static BaublesGui bgui;
    public static BaubleBoxGui box;
    public KeyHandler keyHandler;

    public void registerHandlers() {
        handler = new WindowHandler(Baubles.instance);
    }

    public World getClientWorld() {
        return null;
    }

    public void registerKeyBindings() {
    }

    public List<BaubleType> getList(){
        return slots;
    }

    public void init() {
        registerHandlers();
        registerKeyBindings();
        bgui = new BaublesGui(handler);
        box = new BaubleBoxGui(handler);
    }

}
