package mathray.eval.linear;

import mathray.Args;
import mathray.FunctionRegistrar;
import mathray.Multidef;
import mathray.eval.Impl;

public class LinearArithmetic extends FunctionRegistrar<Impl<LinearTerm>> {

  public static Multidef linearize(Multidef def, Args args) {
    return def;
  }

}
