package mathray.eval;

import mathray.Symbol;

public interface Binder<T> {
  
  public T bind(Symbol var);

}
