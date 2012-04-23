import static mathray.Expressions.*;
import static org.junit.Assert.assertEquals;
import mathray.Multidef;
import mathray.Symbol;
import mathray.Value;
import mathray.device.FunctionTypes;
import mathray.eval.java.JavaDevice;
import mathray.util.Vector;

import org.junit.Test;



public class TestJavaCompilerEval {
  
  private static final Symbol x = sym("x");
  private static final Symbol y = sym("y");
  
  private static void assertCompileEvaluatesTo(Multidef def, Vector<Double> args, Vector<Double> results) {
    FunctionTypes.D func = JavaDevice.compile(def);
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
    
    assertCompileEvaluatesTo(multidef(args(x), add(num(2), x)),                   vector(2.0), vector(4.0));
    assertCompileEvaluatesTo(multidef(args(x), add(x, add(num(2), num(2)))),      vector(2.0), vector(6.0));
    assertCompileEvaluatesTo(multidef(args(x), mul(num(2), add(x, num(2)))),      vector(2.0), vector(8.0));
    assertCompileEvaluatesTo(multidef(args(x), add(add(num(2), num(2)), num(2))), vector(2.0), vector(6.0));
    assertCompileEvaluatesTo(multidef(args(x), mul(add(num(2), num(2)), x)),      vector(2.0), vector(8.0));
    assertCompileEvaluatesTo(multidef(args(x), sin(x)),                           vector(2.0), vector(Math.sin(2.0)));
    
    assertCompileEvaluatesTo(multidef(args(), min(num(1), num(2))),               Vector.<Double>empty(), vector(1.0));
    assertCompileEvaluatesTo(multidef(args(), max(num(1), num(2))),               Vector.<Double>empty(), vector(2.0));
  }
  
  @Test
  public void testMultipleReturnValues() {
    assertCompileEvaluatesTo(multidef(args(x, y), div(x, y), mul(x, y)),          vector(8.0, 2.0), vector(4.0, 16.0));
  }
  
  @Test
  public void testMultipleUses() {
    Value v = sin(x);
    assertCompileEvaluatesTo(multidef(args(x), add(v, v)), vector(2.0), vector(Math.sin(2.0) + Math.sin(2.0)));
  }
  
  @Test
  public void testSelectSign() {
    assertCompileEvaluatesTo(multidef(args(x), selectSign(x, num(1), num(2))), vector(-1.0), vector(1.0));
    assertCompileEvaluatesTo(multidef(args(x), selectSign(x, num(1), num(2))), vector(0.0), vector(2.0));
    assertCompileEvaluatesTo(multidef(args(x), selectSign(x, num(1), num(2))), vector(1.0), vector(2.0));
  }
  
}
