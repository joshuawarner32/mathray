package mathray.eval;

import mathray.Constant;

public interface Translator<T> {
  
  public T translate(Constant cst);
  
}
