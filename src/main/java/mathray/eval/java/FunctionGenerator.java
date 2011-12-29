package mathray.eval.java;

import mathray.Computation;

public interface FunctionGenerator<T> {
  
  public Wrapper<T> begin(Computation comp);

}
