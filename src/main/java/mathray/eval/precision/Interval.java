package mathray.eval.precision;

import mathray.Value;
import mathray.Vector;
import mathray.eval.text.ParseInfo;

public class Interval {

  public final Value a;
  public final Value b;
  
  public Interval(Value a, Value b) {
    this.a = a;
    this.b = b;
  }
  
  public String toString(ParseInfo parser) {
    return "[" + parser.unparse(a) + ", " + parser.unparse(b) + "]";
  }
  
  public Vector<Value> toVector() {
    return new Vector<Value>(a, b);
  }
  
}
