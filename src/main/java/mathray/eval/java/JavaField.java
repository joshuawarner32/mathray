package mathray.eval.java;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class JavaField {
  
  public final String class_;
  public final String name;
  public final String desc;
  
  public JavaField(String class_, String name, String desc) {
    this.class_ = class_;
    this.name = name;
    this.desc = desc;
  }

  public void store(MethodGenerator mgen) {
    mgen.methodVisitor.visitFieldInsn(Opcodes.PUTFIELD, class_, name, desc);
  }

  public JavaValue load(MethodGenerator mgen) {
    mgen.methodVisitor.visitFieldInsn(Opcodes.GETFIELD, class_, name, desc);
    return mgen.push(new ComputedValue(Type.getType(desc)));
  }

}
