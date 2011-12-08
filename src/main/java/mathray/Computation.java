package mathray;

import mathray.eval.Visitor;

public class Computation {
  
  public final Args args;
  
  public final Vector<Value> values;
  
  public Computation(Args args, Vector<Value> values) {
    this.args = args;
    this.values = values;
  }
  
  public Vector<Value> call(final Vector<Value> a) {
    final Visitor<Value> v = new Visitor<Value>() {
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

}
