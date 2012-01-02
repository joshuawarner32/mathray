package mathray;

import mathray.util.Transformer;
import mathray.util.Vector;
import mathray.visitor.EvaluatingVisitor;
import mathray.visitor.SimpleVisitor;
import mathray.visitor.Visitors;


public class Multidef {
  
  public final Args args;
  
  public final Vector<Value> values;
  
  public Multidef(Args args, Vector<Value> values) {
    this.args = args;
    this.values = values;
  }
  
  public Vector<Value> call(final Vector<Value> a) {
    final SimpleVisitor<Value> v = new SimpleVisitor<Value>() {
      @Override
      public Value call(Call call) {
        return call.func.call(call.visitArgs(this));
      }
      @Override
      public Value symbol(Symbol sym) {
        Integer index = args.indexOf(sym);
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
  
  public <T> Vector<T> accept(final EvaluatingVisitor<T> v) {
    
    final SimpleVisitor<T> iv = Visitors.cache(v);
    
    return values.transform(new Transformer<Value, T>() {
      @Override
      public T transform(Value in) {
        return in.accept(iv);
      }
    });
  }
  
  public Multidef transform(final EvaluatingVisitor<Value> v) {
    return new Multidef(args, accept(v));
  }

}
