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
    final Symbol[] center = new Symbol[count];
    for(int i = 0; i < count; i++) {
      for(int j = 0; j < count; j++) {
        nargs[count * i + j] = sym("v_" + i + "_" + j);
      }
    }
    for(int i = 0; i < count; i++) {
      center[i] = sym("p_" + i);
    }
    final Value[] vals = new Value[count + 1];
    Vector<Value> res = def.eval(args, Vector.generate(args.size(), new Generator<Value>() {
      @Override
      public Value generate(int index) {
        vals[0] = center[index];
        for(int i = 0; i < count; i++) {
          vals[i + 1] = mul(args.get(i), nargs[count * i + index]);
        }
        return add(vals);
      }
    }));
    return multidef(def.args.concat(nargs), vals);
  }

}
