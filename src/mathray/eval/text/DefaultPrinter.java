package mathray.eval.text;

import static mathray.Functions.ADD;
import static mathray.Functions.DIV;
import static mathray.Functions.MUL;
import static mathray.Functions.SUB;
import mathray.Definition;
import mathray.Value;

public class DefaultPrinter {
  
  private DefaultPrinter() {}

  private static ParseInfo parser = ParseInfo.builder()
      .infix("+", 1, ADD)
      .infix("-", 1, SUB)
      .infix("*", 2, MUL)
      .infix("/", 2, DIV)
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
