package mathray.eval.java;

public interface Wrapper<T> {
  
  public MethodGenerator getMethodGenerator();
  
  public JavaValue arg(int index);
  
  public void ret(int index, JavaValue value);
  
  public T end();

}
