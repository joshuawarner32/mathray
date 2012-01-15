package mathray;

import mathray.util.Transformer;
import mathray.util.Vector;
import mathray.visitor.EvaluatingVisitor;
import mathray.visitor.SimpleVisitor;
import mathray.visitor.Visitors;
import static mathray.Expressions.*;


public class Multidef extends Closure<Struct> {
  
  public Multidef(Args args, Struct value) {
    super(args, value);
  }
  
  public Vector<Value> call(final Vector<Value> a) {
    final SimpleVisitor<Value> v = new SimpleVisitor<Value>() {
      @Override
      public Value call(Call call) {
        return call.func.call(call.visitArgs(this));
      }
      @Override
      public Value symbol(Symbol sym) {
        int index = args.indexOf(sym);
        if(index != -1) {
          return a.get(index);
        }
        return sym;
      }
      @Override
      public Value constant(Rational cst) {
        return cst;
      }
    };
    return value.toVector().transform(new Transformer<Value, Value>() {
      @Override
      public Value transform(Value in) {
        return in.accept(v);
      }
    });
  }

  public Definition get(int i) {
    return new Definition(args, value.get(i));
  }
  
  public <T> Vector<T> accept(final EvaluatingVisitor<T> v) {
    
    final SimpleVisitor<T> iv = Visitors.cache(v);
    
    return value.toVector().transform(new Transformer<Value, T>() {
      @Override
      public T transform(Value in) {
        return in.accept(iv);
      }
    });
  }
  
  public Vector<Value> eval(final Args args, final Vector<Value> replacements) {
    return accept(new EvaluatingVisitor<Value>() {

      @Override
      public Value symbol(Symbol sym) {
        int index = args.indexOf(sym);
        if(index >= 0) {
          return replacements.get(index);
        } else {
          return sym;
        }
      }

      @Override
      public Value constant(Rational rat) {
        return rat;
      }

      @Override
      public Value call(Call call, Vector<Value> args) {
        return call.func.call(args);
      }
      
    });
  }
  
  public Multidef transform(final EvaluatingVisitor<Value> v) {
    return new Multidef(args, struct(accept(v)));
  }
  
  @Override
  public String toString() {
    StringBuilder b = new StringBuilder();
    b.append('{');
    if(value.size() > 0) {
      b.append(value.get(0));
    }
    for(int i = 1; i < value.size(); i++) {
      b.append(", ");
      b.append(value.get(i));
    }
    b.append('}');
    return b.toString();
  }

  public Multidef call(final Args args, final Vector<Value> replace) {
    return transform(new EvaluatingVisitor<Value>() {
      
      @Override
      public Value symbol(Symbol sym) {
        int index = args.indexOf(sym);
        if(index >= 0) {
          return replace.get(index);
        }
        return sym;
      }
      
      @Override
      public Value constant(Rational rat) {
        return rat;
      }
      
      @Override
      public Value call(Call call, Vector<Value> args) {
        return call.func.call(args);
      }
    });
  }

}
