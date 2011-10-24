package mathray.eval;

import mathray.Vector;

public interface Impl<T> {
  public Vector<T> call(Vector<T> args);
}
