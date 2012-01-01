package mathray.eval.java;

import mathray.Multidef;

public interface FunctionGenerator<T> {
  
  public Wrapper<T> begin(Multidef def);

}
