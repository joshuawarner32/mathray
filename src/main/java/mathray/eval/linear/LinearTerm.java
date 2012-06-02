package mathray.eval.linear;

import static mathray.Expressions.*;

import mathray.Value;
import mathray.util.Vector;

public class LinearTerm {
  public final Value error;
  public final Value center;
  
  public final Vector<Value> coefficients;
  
  public LinearTerm(Value error, Value center, Vector<Value> coefficients) {
    this.error = error;
    this.center = center;
    this.coefficients = coefficients;
  }
  
  public LinearTerm(Value error, Value center, Value... coefficients) {
    this(error, center, vector(coefficients));
  }
  
}
