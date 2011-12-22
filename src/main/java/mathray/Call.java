package mathray;

import mathray.eval.text.DefaultPrinter;

public final class Call extends Value {
  
  public final Function func;
  public final Vector<Value> args;
  
  public Call(Function func, Vector<Value> args) {
    if(args.size() != func.arity) {
      throw new IllegalArgumentException("function arity does not match");
    }
    
    this.func = func;
    this.args = args;
  }
  
  @Override
  public <T> T accept(InternalVisitor<T> v) {
    return v.call(this);
  }
  
  public <T> Vector<T> visitArgs(final InternalVisitor<T> v) {
    return args.transform(new Transformer<Value, T>() {
      public T transform(Value in) {
        return in.accept(v);
      }
    });
  }
  
  @Override
  public int hashCode() {
    return func.hashCode() + args.hashCode();
  }
  
  @Override
  public boolean equals(Object obj) {
    return obj instanceof Call &&
        func.equals(((Call)obj).func) &&
        args.equals(((Call)obj).args);
  }

  public int compareTo(Value value) {
    if(value instanceof Call) {
      Call call = (Call) value;
      int d = func.compareTo(call.func);
      if(d != 0) {
        return d;
      }
      for(int i = 0; i < args.size(); i++) {
        d = args.get(i).compareTo(call.args.get(i));
        if(d != 0) {
          return d;
        }
      }
      return 0;
    } else {
      throw new RuntimeException("unhandled case");
    }
  }
  
  @Override
  public String toString() {
    return DefaultPrinter.toString(this);
  }

  @Override
  public String toJavaString() {
    StringBuilder b = new StringBuilder();
    b.append(func.name);
    b.append("(");
    if(args.size() > 0) {
      b.append(args.get(0).toJavaString());
    }
    for(int i = 1; i < args.size(); i++) {
      b.append(", ");
      b.append(args.get(i).toJavaString());
    }
    b.append(")");
    return b.toString();
  }

}
