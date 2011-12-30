package mathray.eval.precision;

import mathray.Value;
import mathray.eval.text.ParseInfo;
import static mathray.NamedConstants.*;
import static mathray.Expressions.*;

public class Interval {

  public final Value a;
  public final Value b;
  
  public static final Interval INFINITE = new Interval(NEG_INF, POS_INF);
  
  private Interval(Value a, Value b) {
    this.a = a;
    this.b = b;
  }
  
  public static Interval exact(Value a, Value b) {
    return new Interval(a, b);
  }
  
  public static Interval approx(Value a, Value b) {
    return new Interval(down(a), up(b));
  }
  
  public String toString(ParseInfo parser) {
    return "[" + parser.unparse(a) + ", " + parser.unparse(b) + "]";
  }
  
}
