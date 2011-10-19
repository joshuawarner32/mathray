package mathray.eval;

import mathray.Call;
import mathray.Constant;
import mathray.Value;
import mathray.Variable;
import mathray.Vector;

public class IdentityVisitor implements Visitor<Value> {
  
  private IdentityVisitor() {}
  
  public static final IdentityVisitor Instance = new IdentityVisitor();

  @Override
  public Vector<Value> call(Visitor<Value> v, Call call) {
    return call.selectAll();
  }

  @Override
  public Value variable(Variable var) {
    return var;
  }

  @Override
  public Value constant(Constant cst) {
    return cst;
  }

}
