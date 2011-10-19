package mathray.eval;

import mathray.Function;
import mathray.Vector;

public interface Impl<T> {
  public Vector<T> call(Function func, Vector<T> args);
}
