import static mathray.Expressions.add;
import static mathray.Expressions.args;
import static mathray.Expressions.def;
import static mathray.Expressions.div;
import static mathray.Expressions.max;
import static mathray.Expressions.min;
import static mathray.Expressions.mul;
import static mathray.Expressions.num;
import static mathray.Expressions.sin;
import static mathray.Expressions.var;
import static mathray.Expressions.vector;
import static org.junit.Assert.assertEquals;
import mathray.Definition;
import mathray.FunctionD;
import mathray.Variable;
import mathray.Vector;
import mathray.eval.java.JavaCompiler;

import org.junit.Test;


public class JavaCompilerEval {

  
  private static void assertCompileEvaluatesTo(Definition def, Vector<Double> args, Vector<Double> results) {
    FunctionD func = JavaCompiler.compile(def);
    double[] in = new double[args.size()];
    double[] out = new double[results.size()];
    for(int i = 0; i < in.length; i++) {
      in[i] = args.get(i);
    }
    func.call(in, out);
    for(int i = 0; i < out.length; i++) {
      assertEquals((Double)out[i], results.get(i));
    }
  }
  
  @Test
  public void testJavaCompiler() {
    Variable x = var("x");
    Variable y = var("y");
    
    assertCompileEvaluatesTo(def(args(x), add(num(2), x)),                   vector(2.0), vector(4.0));
    assertCompileEvaluatesTo(def(args(x), add(x, add(num(2), num(2)))),      vector(2.0), vector(6.0));
    assertCompileEvaluatesTo(def(args(x), mul(num(2), add(x, num(2)))),      vector(2.0), vector(8.0));
    assertCompileEvaluatesTo(def(args(x), add(add(num(2), num(2)), num(2))), vector(2.0), vector(6.0));
    assertCompileEvaluatesTo(def(args(x), mul(add(num(2), num(2)), x)),      vector(2.0), vector(8.0));
    assertCompileEvaluatesTo(def(args(x), sin(x)),                           vector(2.0), vector(0.9092974268256817));

    assertCompileEvaluatesTo(def(args(), min(num(1), num(2))),               Vector.<Double>empty(), vector(1.0));
    assertCompileEvaluatesTo(def(args(), max(num(1), num(2))),               Vector.<Double>empty(), vector(2.0));
    
    assertCompileEvaluatesTo(def(args(x, y), div(x, y), mul(x, y)),          vector(8.0, 2.0), vector(4.0, 16.0));
  }
  
}
