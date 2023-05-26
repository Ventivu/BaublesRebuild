package baubles.asm;

import baubles.common.Configuration.Configuration;
import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import ventivu.core.ASM.ASMBase;

public class ThaumShieldFix implements ASMBase {
    @Override
    public String transformedClassName() {
        return "thaumcraft.common.lib.events.EventHandlerRunic";
    }

    @Override
    public void transform(String name, String transformedName, ClassNode node) {
        for (MethodNode method : node.methods) {
            String methodName = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(name, method.name, method.desc);
            if (!"livingTick".equals(methodName)) continue;
            int count = 0;
            for (int a = 0; a < method.instructions.size(); ) {
                AbstractInsnNode insn = method.instructions.get(a++);
                if (insn instanceof InsnNode && insn.getOpcode() == Opcodes.ICONST_4) {
                    if (count++ != 1) continue;
                    AbstractInsnNode insert = new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(Configuration.class), "getCount", "()I", false);
                    method.instructions.insertBefore(insn, insert);
                    method.instructions.remove(insn);
                    break;
                }
            }
            break;
        }
    }
}
