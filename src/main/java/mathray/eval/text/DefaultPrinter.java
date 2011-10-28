package mathray.eval.text;

import static mathray.Functions.*;
import mathray.Definition;
import mathray.Value;
import mathray.eval.text.ParseInfo.Associativity;

public class DefaultPrinter {
  
  private DefaultPrinter() {}

  private static ParseInfo parser = ParseInfo.builder()
      .group("(", ")")
      .infix("+", 1, Associativity.LEFT, ADD.select(0))
      .infix("-", 1, Associativity.LEFT, SUB.select(0))
      .infix("*", 2, Associativity.LEFT, MUL.select(0))
      .infix("/", 2, Associativity.LEFT, DIV.select(0))
      .infix("^", 2, Associativity.RIGHT, POW.select(0))
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
    if(def.values.size() == 1) {
      b.append(parser.unparse(def.values.get(0)));
    } else {
      b.append('<');
      if(def.values.size() > 0) {
        b.append(def.values.get(0));
      }
      for(int i = 1; i < def.values.size(); i++) {
        b.append(", ");
        b.append(def.values.get(i));
      }
      b.append('>');
    }
    return b.toString();
  }
  
  public static String toString(Value value) {
    return parser.unparse(value);
  }

}
