package mathray;

import mathray.visitor.Visitor;

public final class Call extends Value {
  
  public final Function func;
  public final Struct args;
  
  private int hash = 0;
  
  public Call(Function func, Struct args) {
    if(args.size() != func.arity) {
      throw new IllegalArgumentException("function arity does not match");
    }
    
    this.func = func;
    this.args = args;
  }
  
  @Override
  public void accept(Visitor v) {
    v.visit(this);
  }
  
  @Override
  public int hashCode() {
    if(hash == 0) {
      hash = func.hashCode() + args.hashCode();
    }
    return hash;
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

}
