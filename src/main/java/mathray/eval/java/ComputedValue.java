package mathray.eval.java;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class ComputedValue implements JavaValue {
  
  //int localVarIndex;
  private Type type;
  
  public ComputedValue(Type type) {
    this.type = type;
  }
  
  @Override
  public void load(MethodGenerator m) {
    m.methodVisitor.visitVarInsn(type.getOpcode(Opcodes.ILOAD), m.getLocalIndex(this));
  }
  
  @Override
  public void store(MethodGenerator m) {
    m.methodVisitor.visitVarInsn(type.getOpcode(Opcodes.ISTORE), m.findOrAllocLocalIndex(this));
  }
  
  @Override
  public Type getType() {
    return type;
  }
  
  @Override
  public boolean needsStore() {
    return true;
  }

}
