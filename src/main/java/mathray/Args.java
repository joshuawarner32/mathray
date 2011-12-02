package mathray;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import mathray.eval.Binder;

public class Args implements Iterable<Symbol> {
  
  private Map<Symbol, Integer> syms = new HashMap<Symbol, Integer>();
  
  public int getIndex(Symbol sym) {
    return syms.get(sym);
  }

  public Symbol get(int index) {
    for(Map.Entry<Symbol, Integer> entry : syms.entrySet()) {
      if(entry.getValue() == index) {
        return entry.getKey();
      }
    }
    throw new IndexOutOfBoundsException();
  }
  
  public Args(Symbol... args) {
    for(int i = 0; i < args.length; i++) {
      syms.put(args[i], i);
    }
  }
  
  public Args(int count) {
    for(int i = 0; i < count; i++) {
      syms.put(Symbol.index(i), i);
    }
  }

  public int size() {
    return syms.size();
  }

  public boolean contains(Symbol var) {
    return syms.get(var) != null;
  }

  @Override
  public Iterator<Symbol> iterator() {
    return syms.keySet().iterator();
  }
  
  public <T> Binder<T> bind(final Vector<T> params) {
    return new Binder<T>() {
      @Override
      public T bind(Symbol var) {
        return params.get(getIndex(var));
      }
    };
  }
  
  @Override
  public int hashCode() {
    return syms.hashCode();
  }
  
  @Override
  public boolean equals(Object obj) {
    return obj instanceof Args && syms.equals(((Args)obj).syms);
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  public final Vector<Symbol> toVector() {
    return new Vector(syms.keySet().toArray());
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  public Vector<Value> toValueVector() {
    return (Vector)toVector();
  }

  public final boolean isSubsetOf(Args args) {
    return args.syms.entrySet().containsAll(syms.entrySet());
  }

  public String toJavaString() {
    StringBuilder b = new StringBuilder();
    Vector<Symbol> vars = toVector();
    b.append("args(");
    if(vars.size() > 0) {
      b.append(vars.get(0).name);
    }
    b.append(")");
    return b.toString();
  }

}
