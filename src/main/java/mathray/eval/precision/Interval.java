package mathray.eval.precision;

import mathray.Value;
import mathray.eval.text.ParseInfo;
import mathray.util.Vector;
import static mathray.NamedConstants.*;
import static mathray.Expressions.*;

public class Interval {

  public final Value a;
  public final Value b;
  
  public static final Interval INFINITE = new Interval(NEG_INF, POS_INF);
  
  public Interval(Value a, Value b) {
    this.a = down(a);
    this.b = up(b);
  }
  
  public String toString(ParseInfo parser) {
    return "[" + parser.unparse(a) + ", " + parser.unparse(b) + "]";
  }
  
  public Vector<Value> toVector() {
    return new Vector<Value>(a, b);
  }
  
}
