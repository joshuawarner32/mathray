package mathray.eval.set;

import static mathray.Functions.*;
import mathray.FunctionRegistrar;
import mathray.Vector;
import mathray.eval.Impl;

public class SetAnalysis extends FunctionRegistrar<Impl<MultivalueSet>> {
  
  {
    register(ADD, new Impl<MultivalueSet>() {
      @Override
      public MultivalueSet call(Vector<MultivalueSet> args) {
        return null;
      }
    });
  }

}
