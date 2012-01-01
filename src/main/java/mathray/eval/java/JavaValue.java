package mathray.eval.java;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

interface JavaValue {

  void load(MethodVisitor m);

  void store(MethodGenerator methodGenerator);

  Type getType();

  void forceStore(MethodVisitor m, int index);

  boolean needsStore();

}
