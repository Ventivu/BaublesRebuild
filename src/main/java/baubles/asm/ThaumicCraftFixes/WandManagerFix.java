package baubles.asm.ThaumicCraftFixes;

import baubles.common.Configuration.Configuration;
import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import ventivu.core.ASM.ASMBase;

public class WandManagerFix implements ASMBase {
    @Override
    public String transformedClassName() {
        return "thaumcraft.common.items.wands.WandManager";
    }

    @Override
    public void transform(String name, String transformedName, ClassNode node) {
        for (MethodNode method : node.methods) {
            String methodName = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(name, method.name, method.desc);
            if (methodName.equals("changeFocus") || methodName.equals("fetchFocusFromPouch") || methodName.equals("addFocusToPouch") || methodName.equals("getTotalVisDiscount")) {
                for (int a = 0; a < method.instructions.size(); ) {
                    AbstractInsnNode s = method.instructions.get(a++);
                    if (s.getOpcode() == Opcodes.ICONST_4) {
                        method.instructions.insertBefore(s, new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(Configuration.class), "getList", "()Ljava/util/List;", false));
                        method.instructions.set(s, new MethodInsnNode(Opcodes.INVOKEINTERFACE, "java/util/List", "size", "()I", true));
                        if (methodName.equals("getTotalVisDiscount")) break;
                    }
                }
            }
        }
    }
}
