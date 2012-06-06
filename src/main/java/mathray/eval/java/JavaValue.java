package mathray.eval.java;

import org.objectweb.asm.Type;

interface JavaValue {

  void load(MethodGenerator m);

  void store(MethodGenerator m);

  Type getType();

  boolean needsStore();

}
