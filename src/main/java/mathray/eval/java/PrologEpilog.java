package mathray.eval.java;

import mathray.eval.java.ClassGenerator.MethodGenerator;

public interface PrologEpilog {
  
  public void begin(MethodGenerator mgen);
  
  public JavaValue loadParam(int index);
  
  public void storeRet(int index, JavaValue value);
  
  public void end(MethodGenerator mgen);

}
