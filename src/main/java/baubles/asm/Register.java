package baubles.asm;

import baubles.asm.ThaumicCraftFixes.TCPouchFix;
import baubles.asm.ThaumicCraftFixes.ThaumShieldFix;
import baubles.asm.ThaumicCraftFixes.WandManagerFix;
import ventivu.core.ASM.ASMManager;

public class Register {
    public static void regAll() {
        ASMManager.register(new ThaumShieldFix());
        ASMManager.register(new InvBaubleBoxFix());
        ASMManager.register(new BaubleBoxFix());
        ASMManager.register(new TCPouchFix());
        ASMManager.register(new WandManagerFix());
    }
}
