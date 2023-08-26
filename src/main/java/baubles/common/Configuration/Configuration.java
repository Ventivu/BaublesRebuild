package baubles.common.Configuration;

import baubles.api.BaubleType;
import baubles.common.Baubles;
import baubles.common.NameSpace;
import baubles.common.items.ItemRing;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.Item;
import ventivu.core.Configuration.ConfigFactory;

import java.util.ArrayList;
import java.util.List;

public class Configuration extends ConfigFactory {
    public static final List<BaubleType> slots = new ArrayList<>();
    public static List<BaubleType> ServerSlots = new ArrayList<>();
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

    public static List<BaubleType> getList() {
        return Baubles.proxy.getList();
    }

    @Override
    public void initConfigs() {
        splitSurvivalCreative = fastSet(get("general", "SplitSurvivalCreative", false, "是否分离生存和创造模式的饰品栏"), Action.NONE).getBoolean();//save it
        String[] allows = new String[BaubleType.values().length];
        for (int i = 0; i < allows.length; ++i)
            allows[i] = BaubleType.values()[i].name();
        String[] list = fastSet(getCycleList("slots", "general", new String[]{"AMULET", "RING", "RING", "BELT"}, "饰品栏各格位属性", allows), Action.NONE).getStringList();
        slots.clear();
        for (String s : list) {
            try {
                slots.add(BaubleType.valueOf(s));
            } catch (Exception e) {
                slots.add(BaubleType.ANY);
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
