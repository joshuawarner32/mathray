package mathray.eval.java;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

class DoubleConstant implements JavaValue {
  
  private final double value;
  
  public DoubleConstant(double value) {
    this.value = value;
  }
  
  @Override
  public void load(MethodGenerator m) {
    if(value == 0) {
      m.methodVisitor.visitInsn(Opcodes.DCONST_0);
    } else if(value == 1) {
      m.methodVisitor.visitInsn(Opcodes.DCONST_1);
    } else {
      m.methodVisitor.visitLdcInsn(value);
    }
  }

  @Override
  public void store(MethodGenerator m) {
    // do nothing
  }
  
  @Override
  public void forceStore(MethodVisitor m, int index) {
    m.visitVarInsn(Opcodes.DSTORE, index);
  }
  
  @Override
  public Type getType() {
    return Type.DOUBLE_TYPE;
  }
  
  @Override
  public boolean needsStore() {
    return false;
  }
}
