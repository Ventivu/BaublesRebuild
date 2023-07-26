package baubles.asm;

import baubles.common.CommonProxy;
import baubles.gui.BaubleBoxGui;
import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import ventivu.core.ASM.ASMBase;

public class BaubleBoxFix implements ASMBase {
    @Override
    public String transformedClassName() {
        return "vazkii.botania.common.item.ItemBaubleBox";
    }

    @Override
    public void transform(String name, String transformedName, ClassNode cn) {
        for (MethodNode method : cn.methods) {
            String methodName = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(name, method.name, method.desc);
            if (!("onItemRightClick".equals(methodName) || "func_77659_a".equals(methodName))) continue;
            for (int a = 0; a < method.instructions.size(); ) {
                AbstractInsnNode node = method.instructions.get(a++);
                if (node.getOpcode() == Opcodes.GETSTATIC && ((FieldInsnNode) node).owner.equals("vazkii/botania/common/Botania")) {
                    ((FieldInsnNode) node).owner = "baubles/common/Baubles";
                    ((FieldInsnNode) node).desc = "Lbaubles/common/Baubles;";
                    node = node.getNext();
                    InsnList list = new InsnList();
                    list.add(new FieldInsnNode(Opcodes.GETSTATIC, Type.getInternalName(CommonProxy.class), "box", "Lbaubles/gui/BaubleBoxGui;"));
                    list.add(new FieldInsnNode(Opcodes.GETFIELD, Type.getInternalName(BaubleBoxGui.class), "id", "I"));
                    method.instructions.insert(node, list);
                    method.instructions.remove(node);
                }
            }
            break;
        }
    }
}
