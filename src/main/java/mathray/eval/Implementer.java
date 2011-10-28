package mathray.eval;

import mathray.Function;

public interface Implementer<T> {
  
  public Impl<T> implement(Function func);

}
