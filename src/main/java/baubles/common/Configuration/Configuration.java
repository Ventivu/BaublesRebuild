package baubles.common.Configuration;

import baubles.api.BaubleTypeProxy;
import baubles.common.NameSpace;
import baubles.common.items.ItemRing;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import ventivu.core.Client.Configuration.ConfigFactory;
import ventivu.core.MagCore;

import java.util.ArrayList;
import java.util.List;

public class Configuration extends ConfigFactory {
    private static final List<BaubleTypeProxy> slots = new ArrayList<>();
    public static List<BaubleTypeProxy> ServerSlots = new ArrayList<>();
    public static Item itemRing;
    public static Configuration instance;
    private static boolean splitSurvivalCreative = false;

    public Configuration(FMLPreInitializationEvent event) {
        super(event, NameSpace.ModID);
        itemRing = (new ItemRing("Ring"));
        instance = this;
    }

    public static boolean isSplitSurvivalCreative() {
        return splitSurvivalCreative;
    }

    public static List<BaubleTypeProxy> getList() {
        if (MagCore.isClient())
            return new ArrayList<>(Minecraft.getMinecraft().isIntegratedServerRunning() ? slots : ServerSlots);
        return slots;
    }

    @SuppressWarnings("unused")
    public static int getCount() {
        return slots.size();
    }

    @Override
    public void initConfigs() {
        splitSurvivalCreative = fastSet(get("general", "SplitSurvivalCreative", false, "是否分离生存和创造模式的饰品栏"), Action.NONE).getBoolean();//save it
        String[] allows = new String[BaubleTypeProxy.values().length];

        for (int i = 0; i < allows.length; ++i)
            allows[i] = BaubleTypeProxy.values()[i].name();
        String[] list = fastSet(getCycleList("slots", "general", new String[]{"AMULET", "RING", "RING", "BELT"}, "饰品栏各格位属性", allows).setMaxListLength(24), Action.NONE).getStringList();
        slots.clear();
        for (String s : list) {
            try {
                slots.add(BaubleTypeProxy.valueOf(s));
            } catch (Exception e) {
                slots.add(BaubleTypeProxy.ANY);
            }
        }
        //ServerSlots = new ArrayList<>(slots);
    }

    @Override
    public void postInit() {
    }

    @Override
    public String getModID() {
        return NameSpace.ModID;
    }

    @Override
    protected String getprefix() {
        return "Baubles.Config.desc";
    }
}
