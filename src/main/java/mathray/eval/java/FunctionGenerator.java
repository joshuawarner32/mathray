package mathray.eval.java;

public interface FunctionGenerator<I, T> {
  
  public Wrapper<T> begin(I def);

}
