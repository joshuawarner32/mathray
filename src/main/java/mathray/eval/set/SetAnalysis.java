package mathray.eval.set;

import static mathray.Expressions.*;
import static mathray.Functions.*;
import mathray.FunctionRegistrar;
import mathray.Symbol;
import mathray.eval.Impl;
import mathray.util.Vector;

public class SetAnalysis extends FunctionRegistrar<Impl<MultidimSet>> {
  
  private static Symbol x = sym("x");
  
  {
    register(ADD, new Impl<MultidimSet>() {
      @Override
      public MultidimSet call(Vector<MultidimSet> args) {
        return null;
      }
    });
    
    register(SIN, new Impl<MultidimSet>() {
      @Override
      public MultidimSet call(Vector<MultidimSet> args) {
        return args.get(0).apply(ADD);
      }
    });
    
    register(SQRT, new Impl<MultidimSet>() {
      @Override
      public MultidimSet call(Vector<MultidimSet> args) {
        return args.get(0).split(multidef(args(x), sqrt(x), neg(sqrt(x))));
      }
    });
  }

}
