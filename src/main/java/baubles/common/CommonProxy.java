
package baubles.common;

import baubles.common.event.KeyHandler;
import baubles.gui.BaubleBoxGui;
import baubles.gui.BaublesGui;
import ventivu.core.GuiFactory.Guihandler;
import net.minecraft.world.World;


public class CommonProxy {
    public static Guihandler handler;
    public KeyHandler keyHandler;
    public static BaublesGui bgui;
    public static BaubleBoxGui box;

    public void registerHandlers() {
        handler = new Guihandler(Baubles.instance);
    }

    public World getClientWorld() {
        return null;
    }

    public void registerKeyBindings() {
    }

    public void init(){
        registerHandlers();
        registerKeyBindings();
        bgui =new BaublesGui(handler);
        box=new BaubleBoxGui(handler);
    }

}
