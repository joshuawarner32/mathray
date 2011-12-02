package mathray.eval.precision;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import mathray.Args;
import mathray.Computation;
import mathray.Definition;
import mathray.Rational;
import mathray.Value;
import mathray.Symbol;
import mathray.Vector;
import mathray.eval.Binder;
import mathray.eval.Context;
import mathray.eval.Environment;
import mathray.eval.Impl;
import mathray.eval.Translator;
import mathray.eval.precision.AffineContext.AffineTerm;

import static mathray.Expressions.*;
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
    AffineContext affineContext = new AffineContext();
    return new Context<AffineForm>(new Binder<AffineForm>() {
      @Override
      public AffineForm bind(Symbol var) {
        Map<AffineTerm, Value> map = new HashMap<AffineTerm, Value>();
        // TODO: add variance term
        return new AffineForm(var, map);
      }
    }, env, new Translator<AffineForm>() {
      @Override
      public AffineForm translate(Rational r) {
        return new AffineForm(r, new HashMap<AffineTerm, Value>());
      }
    }).run(comp.values);
  }

}
