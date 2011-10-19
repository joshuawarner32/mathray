package mathray.eval.text;

import java.util.Stack;

import mathray.Function;
import mathray.Value;

public class SingularFunction {
  
  public final Function func;
  
  public final int outputIndex;
  
  public SingularFunction(Function func, int outputIndex) {
    this.func = func;
    this.outputIndex = outputIndex;
  }
  
  public Value call(Value... args) {
    return func.call(args).select(outputIndex);
  }
}
