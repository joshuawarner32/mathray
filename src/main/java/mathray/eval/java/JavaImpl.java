package mathray.eval.java;

import mathray.eval.java.ClassGenerator.MethodGenerator;
import mathray.util.Vector;

public interface JavaImpl {
  
  public JavaValue call(MethodGenerator mgen, Vector<JavaValue> args);

}
