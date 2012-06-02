import static org.junit.Assert.*;
import static mathray.Expressions.*;
import static mathray.NamedConstants.*;
import static mathray.eval.linear.LinearExpressions.*;

import mathray.Args;
import mathray.Bindings;
import mathray.Multidef;
import mathray.Symbol;
import mathray.Value;
import mathray.eval.linear.LinearArithmetic;
import mathray.eval.linear.LinearTerm;
import mathray.util.Transformer;
import mathray.util.Vector;

import org.junit.Test;


public class TestLinearArithmetic {
  
  private Symbol x = sym("x");
  private Symbol y = sym("y");
  private Symbol dx = sym("dx");
  private Symbol dy = sym("dy");
  
  private Bindings<Symbol> errorSyms = bindings(args(x, y), vector(dx, dy));
  
  private LinearTerm term(Args args, final Symbol sym, final Symbol error) {
    return linear(num(0), sym, args.toVector().transform(new Transformer<Symbol, Value>() {
      @Override
      public Value transform(Symbol in) {
        return in == sym ? error : num(0);
      }
    }));
  }
  
  private Vector<LinearTerm> terms(final Args args) {
    return args.toVector().transform(new Transformer<Symbol, LinearTerm>() {
      @Override
      public LinearTerm transform(Symbol in) {
        return term(args, in, errorSyms.get(in));
      }
    });
  }
  
  private void assertLinearizesTo(Multidef def, Multidef result) {
    Multidef out = LinearArithmetic.linearize(def,
        bindings(def.args, terms(def.args)));
    assertEquals(result, out);
  }
  
  @Test
  public void testConstants() {
    assertLinearizesTo(multidef(args(x), num(2)), multidef(args(x), num(2)));
    assertLinearizesTo(multidef(args(x), TAU), multidef(args(x), TAU));
  }

  @Test
  public void testLinear() {
    assertLinearizesTo(multidef(args(x), x), multidef(args(x), x));
    assertLinearizesTo(multidef(args(x, y), x, y), multidef(args(x, y), x, y));
    assertLinearizesTo(multidef(args(x, y), add(x, y)), multidef(args(x, y), add(x, y)));
  }

}
