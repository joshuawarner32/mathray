package mathray.eval.linear;

import mathray.Value;
import mathray.annotate.ConstExpr;
import mathray.util.Vector;

public class LinearExpressions {
  
  private LinearExpressions() {}
  
  @ConstExpr
  public static LinearTerm linear(Value error, Value center, Value... coefficients) {
    return new LinearTerm(error, center, coefficients);
  }
  
  @ConstExpr
  public static LinearTerm linear(Value error, Value center, Vector<Value> coefficients) {
    return new LinearTerm(error, center, coefficients);
  }

}
