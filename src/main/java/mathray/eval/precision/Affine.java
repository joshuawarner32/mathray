package mathray.eval.precision;

import java.util.HashMap;
import java.util.Map;

import mathray.Args;
import mathray.Call;
import mathray.Multidef;
import mathray.Definition;
import mathray.Rational;
import mathray.Value;
import mathray.Symbol;
import mathray.eval.Environment;
import mathray.eval.Impl;
import mathray.eval.precision.AffineContext.AffineTerm;
import mathray.util.Vector;
import mathray.visitor.EvaluatingVisitor;

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
  
  public static Vector<AffineForm> affine(Multidef def, Args args) {
    //AffineContext affineContext = new AffineContext();
    
    final EvaluatingVisitor<AffineForm> v = new EvaluatingVisitor<AffineForm>() {
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
      public AffineForm call(Call call, Vector<AffineForm> args) {
        return env.implement(call.func).call(args);
      }
    };
    return def.accept(v);
  }

}
