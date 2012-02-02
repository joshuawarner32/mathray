package mathray.visitor;

import mathray.Call;
import mathray.Rational;
import mathray.Symbol;
import mathray.util.Vector;

public interface Processor<T> {
  
  public T process(Call call, Vector<T> args);
  
  public T process(Symbol sym);
  
  public T process(Rational rat);

}
