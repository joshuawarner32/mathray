package mathray.eval;

import mathray.Call;
import mathray.Rational;
import mathray.Variable;
import mathray.Vector;

public class DelegatingVisitor<T> implements Visitor<T> {
  
  private Visitor<T> visitor;
  
  public DelegatingVisitor(Visitor<T> visitor) {
    this.visitor = visitor;
  }

  @Override
  public Vector<T> call(Visitor<T> v, Call call) {
    return visitor.call(v, call);
  }

  @Override
  public T variable(Variable var) {
    return visitor.variable(var);
  }

  @Override
  public T constant(Rational cst) {
    return visitor.constant(cst);
  }

}
