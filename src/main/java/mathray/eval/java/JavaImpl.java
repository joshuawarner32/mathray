package mathray.eval.java;

import mathray.util.Vector;

public interface JavaImpl {
  
  public JavaValue call(MethodGenerator mgen, Vector<JavaValue> args);

}
