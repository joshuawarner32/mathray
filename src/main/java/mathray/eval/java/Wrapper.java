package mathray.eval.java;

import mathray.eval.java.ClassGenerator.MethodGenerator;

public interface Wrapper<T> {
  
  public MethodGenerator getMethodGenerator();
  
  public JavaValue arg(int index);
  
  public void ret(int index, JavaValue value);
  
  public T end();

}
