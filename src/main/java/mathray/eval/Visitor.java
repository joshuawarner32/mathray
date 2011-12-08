package mathray.eval;

import mathray.Call;
import mathray.Rational;
import mathray.Symbol;

public interface Visitor<T> {
  
  public T call(Call call);
  
  public T symbol(Symbol sym);

  public T constant(Rational cst);

}
