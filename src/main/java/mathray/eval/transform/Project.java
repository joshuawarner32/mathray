package mathray.eval.transform;

import static mathray.Expressions.*;

import mathray.Args;
import mathray.Closure;
import mathray.Multidef;
import mathray.Symbol;
import mathray.Value;
import mathray.util.Generator;
import mathray.util.Vector;

public class Project {
  
  public static Closure<Multidef> project(Multidef def, final Args args) {
    final int count = args.size();
    final Symbol[] nargs = new Symbol[count * count];
    final Symbol[] center = new Symbol[count];
    for(int i = 0; i < count; i++) {
      for(int j = 0; j < count; j++) {
        nargs[count * i + j] = sym("v_" + args.get(i).name + "_" + j);
      }
    }
    for(int i = 0; i < count; i++) {
      center[i] = sym("p_" + args.get(i).name);
    }
    final Value[] vals = new Value[count];
    Vector<Value> res = def.eval(args, Vector.generate(args.size(), new Generator<Value>() {
      @Override
      public Value generate(int index) {
        for(int i = 0; i < count - 1; i++) {
          vals[i] = mul(args.get(i), nargs[count * i + index]);
        }
        vals[count - 1] = nargs[count * (count - 1) + index];
        return add(mul(add(vals), args.get(count - 1)), center[index]);
      }
    }));
    return closure(args(center).concat(nargs), multidef(args, res));
  }

}
