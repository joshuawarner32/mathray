package mathray.eval.precision;

import mathray.Args;
import mathray.Definition;
import mathray.Vector;
import mathray.eval.Environment;
import mathray.eval.Impl;

import static mathray.Expressions.*;
import static mathray.Functions.*;

public class Affine {
  Environment<AffineForm> env = Environment.<AffineForm>builder()
    .register(ADD, new Impl<AffineForm>() {
      @Override
      public Vector<AffineForm> call(Vector<AffineForm> args) {
        return vector(args.get(0).pairwise(ADD.define(), Definition.identity(1), Definition.identity(1), args.get(1)));
      }
    })
    .build();
  
  public static Vector<AffineForm> affine(Definition def, Args args) {
    return null;
  }

}
