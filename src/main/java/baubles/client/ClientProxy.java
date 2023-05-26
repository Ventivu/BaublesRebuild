
package baubles.client;

import baubles.common.CommonProxy;
import baubles.common.event.KeyHandler;
import baubles.gui.GuiEvents;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void registerHandlers() {
		super.registerHandlers();
	}
	
	@Override
	public void registerKeyBindings() {
		keyHandler = new KeyHandler();
		FMLCommonHandler.instance().bus().register(keyHandler);
		MinecraftForge.EVENT_BUS.register(new GuiEvents());
	}
				
	@Override
	public World getClientWorld() {
		return FMLClientHandler.instance().getClient().theWorld;
	}

	public void init(){
		super.init();
	}
}
