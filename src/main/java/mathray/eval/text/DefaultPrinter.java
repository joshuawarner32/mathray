package mathray.eval.text;

import static mathray.Functions.*;
import mathray.Value;
import mathray.eval.text.InfixOperator.Associativity;

public class DefaultPrinter {
  
  private DefaultPrinter() {}

  public static ParseInfo BASIC_OPERATORS = ParseInfo.newBuilder()
    .group("(", ",", ")")
    .infix("+", 10, Associativity.LEFT, ADD)
    .infix("-", 10, Associativity.LEFT, SUB)
    .infix("", 20, Associativity.LEFT, MUL)
    .infix("*", 20, Associativity.LEFT, MUL)
    .infix("/", 20, Associativity.LEFT, DIV)
    .infix("^", 30, Associativity.RIGHT, POW)
    .prefix("-", 25, NEG)
    .build();
  
  public static ParseInfo BASIC_FUNCTIONS = BASIC_OPERATORS.toBuilder()
    .functions(SIN, SINH, ASIN, COS, COSH, ACOS, TAN, TANH, ATAN, ATAN2, SQRT, LOG, MIN, MAX)
    .build();
  
  public static String toString(Value value) {
    return BASIC_OPERATORS.unparse(value);
  }
  
  public static Value parse(String text) throws ParseException {
    return BASIC_OPERATORS.parse(text);
  }

}
