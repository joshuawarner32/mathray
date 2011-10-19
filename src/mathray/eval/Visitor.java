package mathray.eval;

import mathray.Call;
import mathray.Constant;
import mathray.Variable;
import mathray.Vector;

public interface Visitor<T> {
  
  public Vector<T> call(Visitor<T> v, Call call);
  
  public T variable(Variable var);

  public T constant(Constant cst);

}
