package mathray.visitor;

import mathray.Call;
import mathray.util.Vector;


public interface EvaluatingVisitor<T> extends TerminalVisitor<T> {

  public T call(Call call, Vector<T> args);
  
}
