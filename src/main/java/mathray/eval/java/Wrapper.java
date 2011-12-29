package mathray.eval.java;

import mathray.eval.java.ClassGenerator.MethodGenerator;

public interface Wrapper {
  
  public MethodGenerator getMethodGenerator();

  public ClassGenerator getClassGenerator();
  
  public JavaValue arg(int index);
  
  public void ret(int index, JavaValue value);
  
  public void end();

}
