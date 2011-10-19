package mathray;

import mathray.eval.Visitor;
import mathray.eval.text.DefaultPrinter;

public class Constant extends Value {
  public final long value;
  
  private Constant(long value) {
    this.value = value;
  }
  
  public static Constant get(long value) {
    return new Constant(value);
  }

  @Override
  public <T> T accept(Visitor<T> v) {
    return v.constant(this);
  }
  
  @Override
  public String toString() {
    return DefaultPrinter.toString(this);
  }
  
  @Override
  public int hashCode() {
    return ((int)value);
  }
  
  @Override
  public boolean equals(Object obj) {
    return compareTo((Value) obj) == 0;
  }
  
  @Override
  public int compareTo(Value v) {
    if(this == v) {
      return 0;
    }
    if(v instanceof Constant) {
      long a = ((Constant)v).value;
      if(value > a) {
        return 1;
      } else if(value < a) {
        return -1;
      } else {
        return 0;
      }
    } else { // Variable or Select
      return -1;
    }
  }

  public Constant add(Constant cst) {
    return get(value + cst.value);
  }

  public Constant mul(Constant cst) {
    return get(value * cst.value);
  }

}
