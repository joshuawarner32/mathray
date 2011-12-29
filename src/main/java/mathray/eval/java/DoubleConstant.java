package mathray.eval.java;

import mathray.eval.java.ClassGenerator.MethodGenerator;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

class DoubleConstant implements JavaValue {
  
  private final double value;
  
  public DoubleConstant(double value) {
    this.value = value;
  }
  
  @Override
  public void load(MethodVisitor m) {
    if(value == 0) {
      m.visitInsn(Opcodes.DCONST_0);
    } else if(value == 1) {
      m.visitInsn(Opcodes.DCONST_1);
    } else {
      m.visitLdcInsn(value);
    }
  }

  @Override
  public void store(MethodGenerator methodGenerator) {
    // do nothing
  }
  
  @Override
  public Type getType() {
    return Type.DOUBLE_TYPE;
  }
}
