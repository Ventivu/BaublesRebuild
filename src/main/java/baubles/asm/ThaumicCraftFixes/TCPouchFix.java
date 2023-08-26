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

public class TCPouchFix implements ASMBase {
    @Override
    public String transformedClassName() {
        return "thaumcraft.client.lib.REHWandHandler";
    }

    @Override
    public void transform(String name, String transformedName, ClassNode cn) {
        for (MethodNode method : cn.methods) {
            String methodName = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(name, method.name, method.desc);
            if (!methodName.equals("handleFociRadial")) continue;
            for (int a = 0; a < method.instructions.size(); ) {
                AbstractInsnNode node = method.instructions.get(a++);
                if (node.getOpcode() == Opcodes.ICONST_4) {
                    method.instructions.insertBefore(node, new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(Configuration.class), "getList", "()Ljava/util/List;", false));
                    method.instructions.set(node, new MethodInsnNode(Opcodes.INVOKEINTERFACE, "java/util/List", "size", "()I", true));
                }
            }
            break;
        }
    }
}
