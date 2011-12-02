package mathray.eval.legal;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;

import mathray.Definition;
import mathray.Function;
import mathray.Value;
import mathray.Symbol;
import mathray.eval.Environment;
import static mathray.Expressions.*;
import static mathray.Functions.*;

public class Legalize {
  
  private static Symbol x = sym("x");
  private static Symbol y = sym("y");
  
  private static Environment<Value> env = Environment.<Value>builder()
    .register(SUB, def(args(x, y), add(x, neg(y))))
    .register(DIV, def(args(x, y), mul(x, pow(y, num(-1)))))
    .register(NEG, def(args(x), mul(num(-1), x)))
    .register(TAN, def(args(x), div(sin(x), cos(x))))
    .register(SQRT, def(args(x), pow(x, num(1, 2))))
    .register(UP, def(args(x), x))
    .register(DOWN, def(args(x), x))
    .build();
  
  private static Map<Symbol, Value> syms = new HashMap<Symbol, Value>();
  
  public static Definition legalize(Definition def, Set<Function> legal) {
    return def;
  }

}
