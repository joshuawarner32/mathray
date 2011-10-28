package mathray.eval;

import mathray.Rational;

public interface Translator<T> {
  
  public T translate(Rational r);
  
}
