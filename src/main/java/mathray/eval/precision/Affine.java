package mathray.eval.precision;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import mathray.Args;
import mathray.Definition;
import mathray.Rational;
import mathray.Value;
import mathray.Variable;
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
      public Vector<AffineForm> call(Vector<AffineForm> args) {
        return vector(args.get(0).pairwise(ADD.define(), Definition.identity(1), Definition.identity(1), args.get(1)));
      }
    })
    .build();
  
  public static Vector<AffineForm> affine(Definition def, Args args) {
    AffineContext affineContext = new AffineContext();
    return new Context<AffineForm>(new Binder<AffineForm>() {
      @Override
      public AffineForm bind(Variable var) {
        Map<AffineTerm, Value> map = new HashMap<AffineTerm, Value>();
        // TODO: add variance term
        return new AffineForm(var, map);
      }
    }, env, new Translator<AffineForm>() {
      @Override
      public AffineForm translate(Rational r) {
        return new AffineForm(r, new HashMap<AffineTerm, Value>());
      }
    }).run(def.values);
  }

}
