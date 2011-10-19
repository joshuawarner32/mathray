import static mathray.Expressions.*;
import static mathray.Functions.ADD;
import static mathray.Functions.DIV;
import static mathray.Functions.MUL;
import static mathray.Functions.SUB;
import static org.junit.Assert.*;

import mathray.Definition;
import mathray.FunctionD;
import mathray.Value;
import mathray.Variable;
import mathray.Vector;
import mathray.calc.Derivatives;
import mathray.eval.java.JavaCompiler;
import mathray.eval.machine.MachineEvaluator;
import mathray.eval.precision.Intervals;
import mathray.eval.simplify.Simplifications;
import mathray.eval.text.ParseInfo;

import org.junit.Test;


public class Sandbox {
  
  @Test
  public void testParser() {
    ParseInfo parser = ParseInfo.builder()
      .infix("+", 1, ADD)
      .infix("-", 1, SUB)
      .infix("*", 2, MUL)
      .infix("/", 2, DIV)
      .build();
    
    assertEquals(add(num(1), num(2)), parser.parse("1+2"));
    assertEquals(mul(num(1), num(2)), parser.parse("1*2"));
    assertEquals(add(num(1), mul(num(2), num(3))), parser.parse("1+2*3"));
    assertEquals(mul(add(num(1), num(2)), num(3)), parser.parse("1*2+3"));
  }
  
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
  
  private static void assertEvaluatesTo(Definition def, Vector<Double> args, Vector<Double> results) {
    assertEquals(results, MachineEvaluator.eval(def, args));
  }

  @Test
  public void testMachineEvaluation() {
    Variable x = var("x");
    assertEvaluatesTo(def(args(x), add(num(2), x)),                   vector(2.0), vector(4.0));
    assertEvaluatesTo(def(args(x), add(x, add(num(2), num(2)))),      vector(2.0), vector(6.0));
    assertEvaluatesTo(def(args(x), mul(num(2), add(x, num(2)))),      vector(2.0), vector(8.0));
    assertEvaluatesTo(def(args(x), add(add(num(2), num(2)), num(2))), vector(2.0), vector(6.0));
    assertEvaluatesTo(def(args(x), mul(add(num(2), num(2)), x)),      vector(2.0), vector(8.0));
    assertEvaluatesTo(def(args(x), sin(x)),                           vector(2.0), vector(0.9092974268256817));
  }

  private static void assertSimplifiesTo(Definition def, Definition result) {
    assertEquals(result, Simplifications.simplify(def));
  }
  
  @Test
  public void testSimplify() {
    Variable x = var("x");
    assertSimplifiesTo(def(args(x), x), def(args(x), x));
    assertSimplifiesTo(def(args(x), add(x, num(0))), def(args(x), x));
    assertSimplifiesTo(def(args(x), add(num(0), x)), def(args(x), x));
    assertSimplifiesTo(def(args(x), mul(x, num(1))), def(args(x), x));
    assertSimplifiesTo(def(args(x), mul(num(1), x)), def(args(x), x));
    assertSimplifiesTo(def(args(x), mul(x, num(0))), def(args(x), num(0)));
    assertSimplifiesTo(def(args(x), mul(num(0), x)), def(args(x), num(0)));
  }

  private static void assertDerivesTo(Definition def, Variable x, Definition result) {
    assertEquals(result, Simplifications.simplify(Derivatives.derive(def, x)));
  }
  
  @Test
  public void testDerive() {
    Variable x = var("x");
    Variable y = var("y");
    assertDerivesTo(def(args(x), x), x, def(args(x), num(1)));
    assertDerivesTo(def(args(x, y), add(x, y)), x, def(args(x, y), num(1)));
    assertDerivesTo(def(args(x, y), mul(x, y)), x, def(args(x, y), y));
    
    assertDerivesTo(def(args(x), cos(x)), x, def(args(x), sin(x)));
    assertDerivesTo(def(args(x), sin(x)), x, def(args(x), neg(cos(x))));
    assertDerivesTo(def(args(x), neg(sin(x))), x, def(args(x), cos(x)));
  }
  
  private static void assertIntervalizesTo(Definition def, Vector<Variable> vars, Definition result) {
    assertEquals(result, Simplifications.simplify(Intervals.intervalize(def, vars)));
  }
  
  @Test
  public void testInterval() {
    Variable x = var("x");
    Variable y = var("y");
    
    assertIntervalizesTo(def(args(x), div(num(1), x)), vector(x), null);
  }

}
