import mathray.Args;
import mathray.NamedConstants;
import mathray.Variable;
import mathray.eval.simplify.Simplifications;
import mathray.eval.text.DefaultPrinter;
import mathray.random.ValueRandom;
import mathray.util.LineReader;
import static mathray.Expressions.*;
import static mathray.Functions.*;

public class Main {

  public static void main(String[] args) {
    ValueRandom random = new ValueRandom(NamedConstants.ALL, vector(ADD, SUB, MUL, DIV, SIN, SQRT, NEG));
    LineReader reader = new LineReader(System.in);
    Variable x = var("x");
    Args a = args(x);
    do {
      System.out.println(DefaultPrinter.toString(Simplifications.simplify(random.randomDefinition(a, 1, 0.95, 0.5, 0.5))));
    } while(!reader.readLine().equals("q"));
  }
}
