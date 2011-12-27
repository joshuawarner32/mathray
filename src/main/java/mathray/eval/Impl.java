package mathray.eval;

import mathray.util.Vector;

public interface Impl<T> {
  public T call(Vector<T> args);
}
