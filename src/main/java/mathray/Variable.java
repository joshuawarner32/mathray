package mathray;

import mathray.eval.Visitor;
import mathray.eval.text.DefaultPrinter;

public class Variable extends Value {
  public final String name;
  
  public Variable(String name) {
    this.name = name;
  }
  
  // Purposefully not overriding hashCode and equals - we want identity comparisons.
  
  @Override
  public String toString() {
    return DefaultPrinter.toString(this);
  }

  @Override
  public <T> T accept(Visitor<T> v) {
    return v.variable(this);
  }
  
  @Override
  public int compareTo(Value o) {
    if(o instanceof Variable) {
      int d = name.compareTo(((Variable)o).name);
      if(d != 0) {
        return d;
      }
      return hashCode() - o.hashCode();
    } else {
      throw new RuntimeException("unhandled case");
    }
  }

  public static Variable index(int i) {
    return new Variable("x" + i);
  }
  
}
