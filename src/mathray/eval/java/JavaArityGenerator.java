package mathray.eval.java;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class JavaArityGenerator {
  
  public static final String CLASS_NAME = "mathray/eval/java/SomeFunction";

  private JavaArityGenerator() {}
  
  public static String getArityName(int arity) {
    return "mathray/eval/java/Function_double_" + arity;
  }
  
  public static String getArityDesc(int arity) {
    StringBuilder b = new StringBuilder();
    b.append('(');
    for(int i = 0; i < arity; i++) {
      b.append('D');
    }
    b.append(")D");
    return b.toString();
  }
  
  public static void generateInterface(ClassVisitor classVisitor, int arity) {
    classVisitor.visit(Opcodes.V1_6,
        Opcodes.ACC_INTERFACE | Opcodes.ACC_PUBLIC,
        getArityName(arity),
        null, 
        null,
        null);
    
    MethodVisitor methodVisitor = classVisitor.visitMethod(Opcodes.ACC_PUBLIC, "call", getArityDesc(arity), null, null);
    methodVisitor.visitEnd();
    classVisitor.visitEnd();
  }

}
