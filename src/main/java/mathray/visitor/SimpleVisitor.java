package mathray.visitor;

import mathray.Call;

public interface SimpleVisitor<T> extends TerminalVisitor<T> {
  
  public T call(Call call);

}
