package mathray.visitor;

import mathray.Call;
import mathray.Rational;
import mathray.Symbol;

public interface Visitor {
  
  public void visit(Call call);
  
  public void visit(Symbol sym);
  
  public void visit(Rational rat);

}
