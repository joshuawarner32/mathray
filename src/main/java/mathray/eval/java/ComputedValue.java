package mathray.eval.java;

import mathray.eval.java.ClassGenerator.MethodGenerator;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class ComputedValue implements JavaValue {
  
  private int localVarIndex;
  private Type type;
  
  public ComputedValue(int localVarIndex, Type type) {
    this.localVarIndex = localVarIndex;
    this.type = type;
  }
  
  public void load(MethodVisitor m) {
    m.visitVarInsn(type.getOpcode(Opcodes.ILOAD), localVarIndex);
  }
  
  @Override
  public void store(MethodGenerator m) {
    if(localVarIndex < 0) {
      localVarIndex = m.localVarIndex;
      m.localVarIndex += 2;
      m.methodVisitor.visitVarInsn(type.getOpcode(Opcodes.ISTORE), localVarIndex);
    }
  }
  
  @Override
  public Type getType() {
    return type;
  }

}
