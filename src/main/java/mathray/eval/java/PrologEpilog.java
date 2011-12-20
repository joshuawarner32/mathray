package mathray.eval.java;

import mathray.Computation;
import mathray.eval.java.ClassGenerator.MethodGenerator;

public interface PrologEpilog {
  
  public MethodGenerator begin(ClassGenerator gen, Computation comp);
  
  public JavaValue loadParam(MethodGenerator mgen, int index);
  
  public void storeRet(MethodGenerator mgen, int index, JavaValue value);
  
  public void end(MethodGenerator mgen);

}
