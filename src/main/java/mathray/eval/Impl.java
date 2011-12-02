package mathray.eval;

import mathray.Vector;

public interface Impl<T> {
  public T call(Vector<T> args);
}
