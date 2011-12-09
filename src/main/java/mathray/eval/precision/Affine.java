package mathray.eval.precision;

import java.util.HashMap;
import java.util.Map;

import mathray.Args;
import mathray.Call;
import mathray.Computation;
import mathray.Definition;
import mathray.Rational;
import mathray.Transformer;
import mathray.Value;
import mathray.Symbol;
import mathray.Vector;
import mathray.eval.Environment;
import mathray.eval.Impl;
import mathray.eval.Visitor;
import mathray.eval.precision.AffineContext.AffineTerm;

import static mathray.Functions.*;

public class Affine {
  private static Environment<AffineForm> env = Environment.<AffineForm>builder()
    .register(ADD, new Impl<AffineForm>() {
      @Override
      public AffineForm call(Vector<AffineForm> args) {
        return args.get(0).pairwise(ADD.define(), Definition.identity(1), Definition.identity(1), args.get(1));
      }
    })
    .register(SUB, new Impl<AffineForm>() {
      @Override
      public AffineForm call(Vector<AffineForm> args) {
        return args.get(0).pairwise(SUB.define(), Definition.identity(1), NEG.define(), args.get(1));
      }
    })
    .register(NEG, new Impl<AffineForm>() {
      @Override
      public AffineForm call(Vector<AffineForm> args) {
        return args.get(0).all(NEG.define());
      }
    })
    .build();
  
  public static Vector<AffineForm> affine(Computation comp, Args args) {
    //AffineContext affineContext = new AffineContext();
    
    final Visitor<AffineForm> v = new Visitor<AffineForm>() {
      @Override
      public AffineForm symbol(Symbol sym) {
        Map<AffineTerm, Value> map = new HashMap<AffineTerm, Value>();
        // TODO: add variance term
        return new AffineForm(sym, map);
      }
      @Override
      public AffineForm constant(Rational rat) {
        return new AffineForm(rat, new HashMap<AffineTerm, Value>());
      }
      @Override
      public AffineForm call(Call call) {
        return env.implement(call.func).call(call.visitArgs(this));
      }
    };
    return comp.values.transform(new Transformer<Value, AffineForm>() {
      @Override
      public AffineForm transform(Value in) {
        return in.accept(v);
      }
    });
  }

}
