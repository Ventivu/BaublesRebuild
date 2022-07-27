
package baubles.common;

import baubles.common.event.KeyHandler;
import baubles.gui.BaublesGui;
import com.ventivu.core.GuiFactory.CustomGui;
import com.ventivu.core.GuiFactory.Guihandler;
import net.minecraft.world.World;


public class CommonProxy {
    public static Guihandler handler;
    public KeyHandler keyHandler;
    public CustomGui gui;

    public void registerHandlers() {
        handler = new Guihandler(Baubles.instance);
    }


    /*	@Override
        public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
            return null;
        }

        @Override
        public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
            switch (ID) {
                case Baubles.GUI: return new ContainerPlayerExpanded(player.inventory, !world.isRemote, player);
            }
            return null;
        }
    */
    public World getClientWorld() {
        return null;
    }


    public void registerKeyBindings() {
    }

    public void init(){
        registerHandlers();
        registerKeyBindings();
        gui=new BaublesGui();
    }

}
