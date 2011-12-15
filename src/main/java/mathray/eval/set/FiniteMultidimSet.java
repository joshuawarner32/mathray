package mathray.eval.set;

import mathray.Computation;
import mathray.Function;
import mathray.Value;

public class FiniteMultidimSet implements MultidimSet {
  
  private final Object[] values;
  
  public FiniteMultidimSet(Value value) {
    values = new Object[] {value};
  }
  
  private FiniteMultidimSet(Object[] values) {
    this.values = values;
  }
  
  @Override
  public MultidimSet apply(Function func) {
    Object[] n = new Object[values.length];
    for(int i = 0; i < values.length; i++) {
      if(values[i] instanceof Value) {
        n[i] = func.call((Value)values[i]);
      } else {
        n[i] = ((MultidimSet)values[i]).apply(func);
      }
    }
    return new FiniteMultidimSet(n);
  }

  @Override
  public MultidimSet split(Computation compute) {
    return null;
  }

}
