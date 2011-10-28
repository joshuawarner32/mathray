package mathray.eval;

import mathray.Variable;

public interface Binder<T> {
  
  public T bind(Variable var);

}
