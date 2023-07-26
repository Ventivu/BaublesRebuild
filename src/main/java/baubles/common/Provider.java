package baubles.common;

import ventivu.api.Registers.AbstractAutoRegister;
import ventivu.api.Registers.IVersionProvider;

public class Provider extends AbstractAutoRegister implements IVersionProvider {
    @Override
    public String version() {
        return NameSpace.ModVersion;
    }

    @Override
    public String name() {
        return NameSpace.ModName;
    }
}
