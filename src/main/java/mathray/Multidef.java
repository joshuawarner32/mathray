package mathray;

import mathray.util.Vector;
import mathray.visitor.Processor;
import static mathray.Expressions.*;

public class Multidef extends Lambda<Struct> {
  
  public Multidef(Args args, Struct value) {
    super(args, value);
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

  public Struct call(Struct args) {
    return call(args.toVector());
  }
  
  public Closable wrap(Vector<Value> results) {
    return new Multidef(args, struct(results));
  }

}
