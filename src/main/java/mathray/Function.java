package mathray;

import static mathray.Expressions.*;

public class Function implements Comparable<Function> {
  
  public final String name;
  
  public final int arity;
  
  // populated lazily
  private Definition definition = null;
  
  public Function(String name, int arity) {
    this.name = name;
    this.arity = arity;
  }
  
  public String fullName() {
    return name + "/" + arity;
  }
  
  public Call call(Vector<Value> vector) {
    return new Call(this, vector);
  }

  public Call call(Value... values) {
    return call(Expressions.vector(values));
  }

  public int compareTo(Function func) {
    int d = arity - func.arity;
    if(d != 0) {
      return d;
    }
    d = name.compareTo(func.name);
    if(d != 0) {
      return d;
    }
    return hashCode() - func.hashCode();
  }

  public Definition define() {
    if(definition == null) {
      Args args = args(arity);
      definition = def(args, call(args.toValueVector()));
    }
    return definition;
  }

  // Purposefully not overriding hashCode and equals - we want identity comparisons.
  
}
