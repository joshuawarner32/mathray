package mathray.eval.linear;

import mathray.Bindings;
import mathray.FunctionRegistrar;
import mathray.Multidef;
import mathray.eval.Impl;

public class LinearArithmetic extends FunctionRegistrar<Impl<LinearTerm>> {

  public static Multidef linearize(Multidef def, Bindings<LinearTerm> bindings) {
    return def;
  }

}
