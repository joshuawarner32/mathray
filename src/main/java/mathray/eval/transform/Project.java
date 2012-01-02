package mathray.eval.transform;

import static mathray.Expressions.*;

import mathray.Args;
import mathray.Multidef;
import mathray.Symbol;
import mathray.Value;
import mathray.util.Generator;
import mathray.util.Vector;

public class Project {
  
  public static Multidef project(Multidef def, final Args args) {
    final int count = args.size();
    final Symbol[] nargs = new Symbol[count * count];
    for(int i = 0; i < count; i++) {
      for(int j = 0; j < count; j++) {
        nargs[count * i + j] = sym("v_" + i + "_" + j);
      }
    }
    final Value[] vals = new Value[count];
    Vector<Value> res = def.eval(args, Vector.generate(args.size(), new Generator<Value>() {
      @Override
      public Value generate(int index) {
        for(int i = 0; i < count; i++) {
          vals[i] = mul(args.get(i), nargs[count * i + index]);
        }
        return add(vals);
      }
    }));
    return multidef(def.args.concat(nargs), vals);
  }

}
