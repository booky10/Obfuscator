package tk.booky.obfuscator.transformer;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import tk.booky.obfuscator.main.Obfuscator;
import tk.booky.obfuscator.utils.AsmUtils;
import tk.booky.obfuscator.utils.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StringTransformer extends AbstractTransformer {

    private static final Integer PARTITION_BITS = 10, PARTITION_SIZE = 1 << PARTITION_BITS, PARTITION_MASK = PARTITION_SIZE - 1;
    private final List<String> strings = new ArrayList<>();
    private final Long runnerID = Math.abs(random.nextLong());

    public StringTransformer(Obfuscator obfuscator) {
        super(obfuscator);
    }

    @Override
    public void visit(ClassNode classNode) {
        for (MethodNode method : classNode.methods) {
            for (Iterator<AbstractInsnNode> iter = method.instructions.iterator(); iter.hasNext(); ) {
                AbstractInsnNode insn = iter.next();
                if (insn.getOpcode() != Opcodes.LDC) continue;

                LdcInsnNode ldc = (LdcInsnNode) insn;
                if (!(ldc.cst instanceof String)) continue;

                String string = (String) ldc.cst;
                int id = strings.indexOf(string);
                if (id == -1) {
                    id = strings.size();
                    strings.add(string);
                }

                int index = id & PARTITION_MASK;
                int classID = id >> PARTITION_BITS;
                int mask = (short) random.nextInt();

                int a = (short) random.nextInt() & mask | index;
                int b = (short) random.nextInt() & ~mask | index;

                method.instructions.insertBefore(insn, new FieldInsnNode(Opcodes.GETSTATIC, "Hey/_" + classID + runnerID, "strings", "[Ljava/lang/String;"));
                method.instructions.insertBefore(insn, AsmUtils.pushInt(a));
                method.instructions.insertBefore(insn, AsmUtils.pushInt(b));
                method.instructions.insertBefore(insn, new InsnNode(Opcodes.IAND));
                method.instructions.insertBefore(insn, new InsnNode(Opcodes.AALOAD));

                iter.remove();
            }
        }
    }

    @Override
    public void after() {
        for (int classID = 0; classID <= strings.size() >> PARTITION_BITS; classID++) {
            ClassNode classNode = new ClassNode();
            classNode.version = Opcodes.V1_8;
            classNode.access = Opcodes.ACC_PUBLIC;
            classNode.name = "Hey/_" + classID + runnerID;
            classNode.superName = "java/lang/Object";
            classNode.signature = StringUtils.crazyString(obfuscator, 10);

            classNode.fields.add(new FieldNode(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "strings", "[Ljava/lang/String;", null, null));
            MethodNode clinit = new MethodNode(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "<clinit>", "()V", null, null);
            classNode.methods.add(clinit);

            int start = classID << PARTITION_BITS;
            int end = Math.min(start + PARTITION_SIZE, strings.size());

            clinit.instructions.add(AsmUtils.pushInt(end - start));
            clinit.instructions.add(new TypeInsnNode(Opcodes.ANEWARRAY, "Ljava/lang/String;"));

            for (int id = start; id < end; id++) {
                clinit.instructions.add(new InsnNode(Opcodes.DUP));
                clinit.instructions.add(AsmUtils.pushInt(id & PARTITION_MASK));
                clinit.instructions.add(new LdcInsnNode(strings.get(id)));
                clinit.instructions.add(new InsnNode(Opcodes.AASTORE));
            }

            clinit.instructions.add(new FieldInsnNode(Opcodes.PUTSTATIC, classNode.name, "strings", "[Ljava/lang/String;"));
            clinit.instructions.add(new InsnNode(Opcodes.RETURN));

            obfuscator.addClass(classNode);
        }
    }
}
