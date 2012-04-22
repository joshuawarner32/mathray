package mathray.eval.text;

import static mathray.Functions.*;
import mathray.Definition;
import mathray.Value;
import mathray.eval.text.InfixOperator.Associativity;

public class DefaultPrinter {
  
  private DefaultPrinter() {}

  public static ParseInfo BASIC_OPERATORS = ParseInfo.newBuilder()
    .group("(", ",", ")")
    .infix("+", 10, Associativity.LEFT, ADD)
    .infix("-", 10, Associativity.LEFT, SUB)
    .infix("*", 20, Associativity.LEFT, MUL)
    .infix("/", 20, Associativity.LEFT, DIV)
    .infix("^", 30, Associativity.RIGHT, POW)
    .prefix("-", 25, NEG)
    .build();
  
  public static ParseInfo BASIC_FUNCTIONS = BASIC_OPERATORS.toBuilder()
    .functions(SIN, SINH, ASIN, COS, COSH, ACOS, TAN, TANH, ATAN, ATAN2, SQRT, LOG, MIN, MAX)
    .build();
  
  public static String toString(Definition def) {
    StringBuilder b = new StringBuilder();
    b.append("f(");
    if(def.args.size() > 0) {
      b.append(def.args.get(0));
    }
    for(int i = 1; i < def.args.size(); i++) {
      b.append(", ");
      b.append(def.args.get(i));
    }
    b.append(") = ");
    b.append(BASIC_OPERATORS.unparse(def.value));
    return b.toString();
  }
  
  public static String toString(Value value) {
    return BASIC_OPERATORS.unparse(value);
  }
  
  public static Value parse(String text) {
    return BASIC_OPERATORS.parse(text);
  }

}
