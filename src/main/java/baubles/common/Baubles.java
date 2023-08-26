package baubles.common;

import baubles.api.BaubleType;
import baubles.asm.Register;
import baubles.common.Configuration.Configuration;
import baubles.common.event.EventHandlerEntity;
import baubles.common.event.EventHandlerNetwork;
import baubles.common.network.PacketHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.ModAPIManager;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ventivu.core.MagCore;

import static baubles.common.NameSpace.*;

@Mod(modid = ModID, name = ModName, version = ModVersion, guiFactory = GuiFactory, dependencies = "required-after:magcore@[" + MagCoreVersion + ",);required-after:Forge@[10.13.2,);")

public class Baubles {
    public static final Logger log = LogManager.getLogger(ModID);
    @SidedProxy(clientSide = "baubles.client.ClientProxy", serverSide = "baubles.common.CommonProxy")
    public static CommonProxy proxy;
    @Instance(ModID)
    public static Baubles instance;
    public EventHandlerEntity entityEventHandler;
    public EventHandlerNetwork entityEventNetwork;

    @EventHandler
    public void construct(FMLConstructionEvent event) {
        Register.regAll();
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        new Configuration(event);

        PacketHandler.init();

        entityEventHandler = new EventHandlerEntity();
        entityEventNetwork = new EventHandlerNetwork();

        MinecraftForge.EVENT_BUS.register(entityEventHandler);
        FMLCommonHandler.instance().bus().register(entityEventNetwork);
    }

    @EventHandler
    public void init(FMLInitializationEvent evt) {
        proxy.init();
        MagCore.commandManager.provide(new Provider());
    }
}
