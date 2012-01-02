package mathray;

import java.util.Arrays;
import java.util.Iterator;

import mathray.util.Vector;

public class Args implements Iterable<Symbol> {
  
  private Symbol[] syms;
  
  public int indexOf(Symbol sym) {
    for(int i = 0; i < syms.length; i++) {
      if(syms[i] == sym) {
        return i;
      }
    }
    return -1;
  }

  public Symbol get(int index) {
    return syms[index];
  }
  
  public Args(Symbol... args) {
    this.syms = Arrays.copyOf(args, args.length);
    for(int i = 0; i < args.length; i++) {
      if(indexOf(args[i]) < i) {
        throw new IllegalArgumentException("duplicate Symbol in Args");
      }
    }
  }
  
  public Args(int count) {
    this.syms = new Symbol[count];
    for(int i = 0; i < count; i++) {
      syms[i] = Symbol.index(i);
    }
  }
  
  private Args() {}

  public int size() {
    return syms.length;
  }

  public boolean contains(Symbol var) {
    return indexOf(var) != -1;
  }

  @Override
  public Iterator<Symbol> iterator() {
    return new Iterator<Symbol>() {
      int index = 0;
      @Override
      public boolean hasNext() {
        return index < syms.length;
      }

      @Override
      public Symbol next() {
        return syms[index++];
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
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

  public final Vector<Symbol> toVector() {
    return Vector.unsafe(syms);
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  public Vector<Value> toValueVector() {
    return (Vector)toVector();
  }

  public final boolean isSubsetOf(Args args) {
    for(int i = 0; i < syms.length; i++) {
      if(args.indexOf(syms[i]) == -1) {
        return false;
      }
    }
    return true;
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

  public Args concat(Symbol... nargs) {
    Symbol[] ns = Arrays.copyOf(syms, syms.length + nargs.length);
    Args ret = new Args();
    ret.syms = ns;
    for(int i = 0; i < nargs.length; i++) {
      ns[i + syms.length] = nargs[i];
    }
    return ret;
  }

}
