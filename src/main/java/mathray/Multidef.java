package mathray;

import mathray.util.Transformer;
import mathray.util.Vector;
import mathray.visitor.Processor;
import static mathray.Expressions.*;

public class Multidef extends Closure<Struct> {
  
  public Multidef(Args args, Struct value) {
    super(args, value);
  }
  
  public Vector<Value> call(final Vector<Value> a) {
    final Processor<Value> v = new Processor<Value>() {
      @Override
      public Value process(Call call, Vector<Value> args) {
        return call.func.call(args);
      }
      @Override
      public Value process(Symbol sym) {
        int index = args.indexOf(sym);
        if(index != -1) {
          return a.get(index);
        }
        return sym;
      }
      @Override
      public Value process(Rational rat) {
        return rat;
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
  
  public Vector<Value> eval(final Args args, final Vector<Value> replacements) {
    return Vector.<Value>fromIterable(acceptVector(new Processor<Value>() {

      @Override
      public Value process(Symbol sym) {
        int index = args.indexOf(sym);
        if(index >= 0) {
          return replacements.get(index);
        } else {
          return sym;
        }
      }

      @Override
      public Value process(Rational rat) {
        return rat;
      }

      @Override
      public Value process(Call call, Vector<Value> args) {
        return call.func.call(args);
      }
      
    }));
  }
  
  public Multidef transform(Processor<Value> v) {
    return new Multidef(args, struct(acceptVector(v)));
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
    return transform(new Processor<Value>() {
      
      @Override
      public Value process(Symbol sym) {
        int index = args.indexOf(sym);
        if(index >= 0) {
          return replace.get(index);
        }
        return sym;
      }
      
      @Override
      public Value process(Rational rat) {
        return rat;
      }
      
      @Override
      public Value process(Call call, Vector<Value> args) {
        return call.func.call(args);
      }
    });
  }

  public Vector<Value> call(Struct args) {
    return call(args.toVector());
  }

}
