package mathray.eval.machine;

import mathray.Value;

@SuppressWarnings("serial")
public class VisitorArithmeticException extends RuntimeException {
  
  public final double result;
  
  public final Value value;

  public VisitorArithmeticException(double result, Value value) {
    super(value + " evaluated to " + result);
    this.result = result;
    this.value = value;
  }

}
