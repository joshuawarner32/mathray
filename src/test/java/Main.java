import mathray.Args;
import mathray.Definition;
import mathray.NamedConstants;
import mathray.Symbol;
import mathray.random.ValueRandom;
import static mathray.Expressions.*;
import static mathray.Functions.*;

public class Main {

  public static void main(String[] args) {
    printRandomExpressionForever();
  }

  private static void printRandomExpressionForever() {
    ValueRandom random = new ValueRandom(NamedConstants.ALL, vector(ADD, SUB, MUL, DIV, SIN, SQRT, NEG));
    Symbol x = sym("x");
    Args a = args(x);
    for(Definition orig : random.randomDefinitions(a, 0.99, 0.5, 0.7)) {
      System.out.println(orig.toString());
    }
  }
}
