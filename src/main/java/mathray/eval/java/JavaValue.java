package mathray.eval.java;

import mathray.eval.java.ClassGenerator.MethodGenerator;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public interface JavaValue {

  void load(MethodVisitor m);

  void store(MethodGenerator methodGenerator);

  Type getType();

}
