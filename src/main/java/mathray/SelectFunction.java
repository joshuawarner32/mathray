package mathray;

public class SelectFunction {
  
  public final Function func;
  
  public final int outputIndex;
  
  public SelectFunction(Function func, int outputIndex) {
    this.func = func;
    this.outputIndex = outputIndex;
  }
  
  public Value call(Value... args) {
    return func.call(args).select(outputIndex);
  }

  public Value call(Vector<Value> args) {
    return func.call(args).select(outputIndex);
  }
}
