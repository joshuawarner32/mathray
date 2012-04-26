package mathray.eval.java;

import java.util.List;

public interface FunctionGenerator<I, T> {
  
  public Wrapper<T> begin(I def, List<String> extraInterfaces);

}
