package mathray.eval;

import java.util.HashMap;
import java.util.Map;

import mathray.Args;
import mathray.Call;
import mathray.Constant;
import mathray.Variable;
import mathray.Vector;

public class Visitors {
  
  private Visitors() {}
  
  public static final <T> Visitor<T> bind(final Visitor<T> v, final Args args, final Vector<T> values) {
    return new DelegatingVisitor<T>(v) {

      @Override
      public T variable(Variable var) {
        return values.get(args.getIndex(var));
      }

    };
  }
  
  @SuppressWarnings("unchecked")
  public static final <T> Visitor<T> cache(final Visitor<T> v) {
    return new Visitor<T>() {
      
      @SuppressWarnings("rawtypes")
      private Map cache = new HashMap();

      @Override
      public Vector<T> call(Visitor<T> v, Call call) {
        Vector<T> ret = (Vector<T>)cache.get(call);
        if(ret != null) {
          return ret;
        }
        return v.call(v, call);
      }

      @Override
      public T variable(Variable var) {
        T ret = (T)cache.get(var);
        if(ret != null) {
          return ret;
        }
        return v.variable(var);
      }

      @Override
      public T constant(Constant cst) {
        T ret = (T)cache.get(cst);
        if(ret != null) {
          return ret;
        }
        return v.constant(cst);
      }
      
    };
  }

}
