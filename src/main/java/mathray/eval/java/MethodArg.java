package mathray.eval.java;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class MethodArg implements JavaValue {
  
  private Type type;
  
  public MethodArg(Type type) {
    this.type = type;
  }

  @Override
  public void load(MethodGenerator m) {
    m.methodVisitor.visitVarInsn(type.getOpcode(Opcodes.ILOAD), m.getLocalIndex(this));
  }

  @Override
  public void store(MethodGenerator m) {
    m.methodVisitor.visitVarInsn(type.getOpcode(Opcodes.ISTORE), m.getLocalIndex(this));
  }

  @Override
  public Type getType() {
    return type;
  }

  @Override
  public void forceStore(MethodVisitor m, int index) {
    m.visitVarInsn(type.getOpcode(Opcodes.ISTORE), index);
  }

  @Override
  public boolean needsStore() {
    return false;
  }

}
