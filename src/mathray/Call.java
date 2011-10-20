package mathray;

import java.util.Arrays;

import mathray.eval.Visitor;
import mathray.eval.text.DefaultPrinter;

public final class Call implements Comparable<Call> {
  
  private static class Select extends Value {
    
    private final Call call;
    private final int index;
    
    public Select(Call call, int index) {
      this.call = call;
      this.index = index;
    }
  
    @Override
    public <T> T accept(Visitor<T> v) {
      return v.call(v, call).get(index);
    }
    
    @Override
    public String toString() {
      return DefaultPrinter.toString(this);
    }
    
    @Override
    public int hashCode() {
      return index + call.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
      return obj instanceof Select &&
          index == ((Select)obj).index &&
          call.equals(((Select)obj).call);
    }
    
    @Override
    public int compareTo(Value o) {
      if(o instanceof Select) {
        int d = call.compareTo(((Select)o).call);
        if(d == 0) {
          return index - ((Select)o).index;
        } else {
          return d;
        }
      } else {
        throw new RuntimeException("unhandled case");
      }
    }
  
  }

  
  public final Function func;
  public final Vector<Value> args;
  
  private final Select[] selects;
  
  public Call(Function func, Vector<Value> args) {
    if(args.size() != func.inputArity) {
      throw new IllegalArgumentException("function arity does not match");
    }
    
    this.func = func;
    this.args = args;
    selects = new Select[func.outputArity];
    
    for(int i = 0; i < selects.length; i++) {
      selects[i] = new Select(this, i);
    }
  }
  
  public <T> Vector<T> visitArgs(final Visitor<T> v) {
    return args.transform(new Transformer<Value, T>() {
      public T transform(Value in) {
        return in.accept(v);
      }
    });
  }
  
  public Value select(int index) {
    return selects[index];
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

  public Vector<Value> selectAll() {
    return new Vector<Value>(selects);
  }

  public int compareTo(Call call) {
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
  }
  
  @Override
  public String toString() {
    return Arrays.toString(selects);
  }

}
