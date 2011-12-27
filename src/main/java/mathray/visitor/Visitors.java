package mathray.visitor;

import java.util.HashMap;
import java.util.Map;

import mathray.Call;
import mathray.Rational;
import mathray.Symbol;
import mathray.Value;

public class Visitors {
  
  private Visitors() {}
  
  public static <T> SimpleVisitor<T> cache(final SimpleVisitor<T> v) {
    return new SimpleVisitor<T>() {
      private final Map<Value, T> results = new HashMap<Value, T>();

      @Override
      public T symbol(Symbol sym) {
        T ret = results.get(sym);
        if(ret == null) {
          results.put(sym, ret = v.symbol(sym));
        }
        return ret;
      }

      @Override
      public T constant(Rational rat) {
        T ret = results.get(rat);
        if(ret == null) {
          results.put(rat, ret = v.constant(rat));
        }
        return ret;
      }

      @Override
      public T call(Call call) {
        T ret = results.get(call);
        if(ret == null) {
          results.put(call, ret = v.call(call));
        }
        return ret;
      }
      
    };
  }
  
  public static <T> SimpleVisitor<T> simple(final EvaluatingVisitor<T> v) {
    return new SimpleVisitor<T>() {

      @Override
      public T symbol(Symbol sym) {
        return v.symbol(sym);
      }

      @Override
      public T constant(Rational rat) {
        return v.constant(rat);
      }

      @Override
      public T call(Call call) {
        return v.call(call, call.visitArgs(this));
      }
      
    };
  }

}
