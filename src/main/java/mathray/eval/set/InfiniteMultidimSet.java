package mathray.eval.set;

import mathray.Computation;
import mathray.Function;
import mathray.Symbol;
import mathray.Value;

public class InfiniteMultidimSet implements MultidimSet {

  private final Symbol sym;
  private final Object value;
  
  private InfiniteMultidimSet(Symbol sym, Object value) {
    this.sym = sym;
    this.value = value;
  }
  
  @Override
  public MultidimSet apply(Function func) {
    if(value instanceof Value) {
      return new InfiniteMultidimSet(sym, func.call((Value)value));
    } else {
      return new InfiniteMultidimSet(sym, ((MultidimSet)value).apply(func));
    }
  }
  
  @Override
  public MultidimSet split(Computation compute) {
    return null;
  }
  
}
