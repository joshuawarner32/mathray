package mathray.eval.text;

import com.google.common.base.Joiner;

import mathray.Function;
import mathray.eval.Impl;
import mathray.util.Vector;

public class FunctionPrecedenceImplementation implements Impl<PrecedenceString> {
  
  private final Function func;
  
  public FunctionPrecedenceImplementation(Function func) {
    this.func = func;
  }
  
  @Override
  public PrecedenceString call(Vector<PrecedenceString> args) {
    return new PrecedenceString(func.name + '(' + Joiner.on(", ").join(args) + ')', Integer.MAX_VALUE);
  }

}
