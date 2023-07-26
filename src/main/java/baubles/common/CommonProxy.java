
package baubles.common;

import baubles.common.event.KeyHandler;
import baubles.gui.BaubleBoxGui;
import baubles.gui.BaublesGui;
import net.minecraft.world.World;
import ventivu.core.WindowFrame.WindowHandler;


public class CommonProxy {
    public static WindowHandler handler;
    public KeyHandler keyHandler;
    public static BaublesGui bgui;
    public static BaubleBoxGui box;

    public void registerHandlers() {
        handler = new WindowHandler(Baubles.instance);
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
        //TODO:饰品盒子的重加工
    }

}
