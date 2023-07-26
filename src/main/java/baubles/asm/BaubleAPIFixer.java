package baubles.asm;

import org.objectweb.asm.tree.ClassNode;
import ventivu.core.ASM.ASMBase;

/**
 * 傻逼exu，API都不好好用，还TM闭源，改都没办法改
 */
public class BaubleAPIFixer implements ASMBase {
    @Override
    public String transformedClassName() {
        return "baubles.api.BaubleType";
    }

    @Override
    public void transform(String name, String transformedName, ClassNode node) {

    }
}
