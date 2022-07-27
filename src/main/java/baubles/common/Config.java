package baubles.common;

import baubles.common.lib.PlayerHandler;
import baubles.gui.UIController;
import com.ventivu.core.Core.Commands;
import com.ventivu.core.Core.PropLoader;
import net.minecraft.item.Item;
import baubles.common.items.ItemRing;


public class Config extends PropLoader {

    public static Item itemRing;
    public static UIController controller;
    // config properties
    private static boolean splitSurvivalCreative = false;
    private static boolean defaultMode = true;
    static Config instance;

    public Config() {
        setFile(Baubles.MODID);
        instance = this;
        reload();
    }

    public static void initialize() {
        itemRing = (new ItemRing("Ring"));
        Commands.addReloadControl(Baubles.MODNAME, Config.class);
        reload();
    }

    public static void load() {
        {
            splitSurvivalCreative = instance.loadProp("server", "SplitSurvivalCreative", false, "是否分离生存和创造模式的饰品栏").getBoolean();//save it
            defaultMode = instance.loadProp("common", "defaultMode", true, "是否使用默认的饰品栏").getBoolean();
            instance.config.save();
            if (!defaultMode) {
                controller = new UIController();
            }
        }
    }


    public static void save() {
        if (instance.config != null) instance.config.save();
    }

    public static boolean isSplitSurvivalCreative() {
        return splitSurvivalCreative;
    }

    public static boolean isDefaultMode() {
        return defaultMode;
    }

    public static void reload() {
        instance.config.load();
        load();
        PlayerHandler.refreshPlayerBaubles();
    }
}
