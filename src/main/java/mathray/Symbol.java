package mathray;

import mathray.visitor.Visitor;

public class Symbol extends Value {
  public final String name;
  
  public Symbol(String name) {
    this.name = name;
  }
  
  // Purposefully not overriding hashCode and equals - we want identity comparisons.

  @Override
  public void accept(Visitor v) {
    v.visit(this);
  }
  
  @Override
  public int compareTo(Value o) {
    if(o instanceof Symbol) {
      int d = name.compareTo(((Symbol)o).name);
      if(d != 0) {
        return d;
      }
      return hashCode() - o.hashCode();
    } else {
      throw new RuntimeException("unhandled case");
    }
  }

  public static Symbol index(int i) {
    return new Symbol("x" + i);
  }
  
}
