package baubles.asm;

import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.MethodNode;
import ventivu.core.ASM.ASMBase;

public class InvBaubleBoxFix implements ASMBase {
    @Override
    public String transformedClassName() {
        return "vazkii.botania.client.gui.box.InventoryBaubleBox";
    }

    @Override
    public void transform(String name, String transformedName, ClassNode cn) {
        for (MethodNode method : cn.methods) {
            String methodName = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(name, method.name, method.desc);
            if (!("func_70302_i_".equals(methodName)||"getSizeInventory".equals(methodName))) continue;
            for (int a = 0; a < method.instructions.size(); ) {
                AbstractInsnNode node = method.instructions.get(a++);
                if (node.getOpcode() == Opcodes.BIPUSH&&((IntInsnNode)node).operand==16) {
                    ((IntInsnNode) node).operand=36;
                    break;
                }
            }
            break;
        }
    }
}
