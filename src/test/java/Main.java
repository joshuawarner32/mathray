import mathray.Args;
import mathray.Definition;
import mathray.NamedConstants;
import mathray.Symbol;
import mathray.eval.simplify.Simplifications;
import mathray.eval.text.DefaultPrinter;
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
    do {
      Definition orig = random.randomDefinition(a, 0.99, 0.5, 0.7);
      //Definition simple = Simplifications.simplify(orig);
      System.out.println(orig.toJavaString());
      //System.out.println("\ttraditional: " + DefaultPrinter.toString(orig));
      //System.out.println("\tsimplified:  " + DefaultPrinter.toString(simple));
    } while(true);//while(!System.console().readLine().equals("q"));
  }
}
