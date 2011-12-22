package mathray;

import java.util.HashMap;
import java.util.Map;


public class Computation {
  
  public final Args args;
  
  public final Vector<Value> values;
  
  public Computation(Args args, Vector<Value> values) {
    this.args = args;
    this.values = values;
  }
  
  public Vector<Value> call(final Vector<Value> a) {
    final InternalVisitor<Value> v = new InternalVisitor<Value>() {
      @Override
      public Value call(Call call) {
        return call.func.call(call.visitArgs(this));
      }
      @Override
      public Value symbol(Symbol sym) {
        Integer index = args.getIndex(sym);
        if(index != null) {
          return a.get(index);
        }
        return sym;
      }
      @Override
      public Value constant(Rational cst) {
        return cst;
      }
    };
    return values.transform(new Transformer<Value, Value>() {
      @Override
      public Value transform(Value in) {
        return in.accept(v);
      }
    });
  }

  public Definition get(int i) {
    return new Definition(args, values.get(i));
  }
  
  private static class MyVisitor<T> implements InternalVisitor<T> {
    private final Map<Value, T> results = new HashMap<Value, T>();
    private final Visitor<T> v;
    
    public MyVisitor(Visitor<T> v) {
      this.v = v;
    }
    
    @Override
    public T call(Call call) {
      T ret = results.get(call);
      if(ret == null) {
        results.put(call, ret = v.call(call.func, call.args.transform(new Transformer<Value, T>() {
          @Override
          public T transform(Value in) {
            return in.accept(MyVisitor.this);
          }
        })));
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
    public T symbol(Symbol sym) {
      T ret = results.get(sym);
      if(ret == null) {
        results.put(sym, ret = v.symbol(sym));
      }
      return ret;
    }
  }
  
  public <T> Vector<T> accept(final Visitor<T> v) {
    
    final InternalVisitor<T> iv = new MyVisitor<T>(v);
    
    return values.transform(new Transformer<Value, T>() {
      @Override
      public T transform(Value in) {
        return in.accept(iv);
      }
    });
  }

}
