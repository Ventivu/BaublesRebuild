package baubles.common.Configuration;

import baubles.common.NameSpace;
import ventivu.core.Client.Configuration.ConfigFactory;
import ventivu.core.Client.Configuration.GuiFactory;

public class BaublesFactory extends GuiFactory {
    @Override
    public String getModID() {
        return NameSpace.ModID;
    }

    @Override
    public ConfigFactory getFactory() {
        return Configuration.instance;
    }
}
