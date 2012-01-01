package mathray.eval.java;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

interface JavaValue {

  void load(MethodGenerator m);

  void store(MethodGenerator m);

  Type getType();

  void forceStore(MethodVisitor m, int index);

  boolean needsStore();

}
