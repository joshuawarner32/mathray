package mathray.eval.legal;

import java.util.Set;

import mathray.Definition;
import mathray.Function;
import mathray.Value;
import mathray.Variable;
import mathray.eval.Environment;
import static mathray.Expressions.*;
import static mathray.Functions.*;

public class Legalize {
  
  private static Variable x = var("x");
  private static Variable y = var("y");
  
  private static Environment<Value> env = Environment.<Value>builder()
    .register(SUB, def(args(x, y), add(x, neg(y))))
    .register(DIV, def(args(x, y), mul(x, pow(y, num(-1)))))
    .register(TAN, def(args(x), div(sin(x), cos(x))))
    .build();
  
  public static Definition legalize(Definition def, Set<Function> legal) {
    return def;
  }

}
