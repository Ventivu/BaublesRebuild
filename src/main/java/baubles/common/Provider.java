package baubles.common;

import ventivu.core.Core.Commands.AbstractAutoRegister;
import ventivu.core.Core.Commands.IVersionProvider;

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
