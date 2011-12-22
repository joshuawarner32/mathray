package mathray;


interface InternalVisitor<T> extends TerminalVisitor<T> {
  
  public T call(Call call);

}
