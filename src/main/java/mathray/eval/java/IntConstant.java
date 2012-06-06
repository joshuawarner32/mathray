package mathray.eval.java;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

class IntConstant implements JavaValue {
  
  private final int value;
  
  public IntConstant(int value) {
    this.value = value;
  }
  
  @Override
  public void load(MethodGenerator m) {
    switch(value) {
    case -1:
      m.methodVisitor.visitInsn(Opcodes.ICONST_M1);
      break;
    case 0:
      m.methodVisitor.visitInsn(Opcodes.ICONST_0);
      break;
    case 1:
      m.methodVisitor.visitInsn(Opcodes.ICONST_1);
      break;
    case 2:
      m.methodVisitor.visitInsn(Opcodes.ICONST_2);
      break;
    case 3:
      m.methodVisitor.visitInsn(Opcodes.ICONST_3);
      break;
    case 4:
      m.methodVisitor.visitInsn(Opcodes.ICONST_4);
      break;
    case 5:
      m.methodVisitor.visitInsn(Opcodes.ICONST_5);
      break;
    default:
      m.methodVisitor.visitLdcInsn(value);
    }
  }

  @Override
  public void store(MethodGenerator methodGenerator) {
    // do nothing
  }
  
  @Override
  public Type getType() {
    return Type.INT_TYPE;
  }
  
  @Override
  public boolean needsStore() {
    return false;
  }

}
