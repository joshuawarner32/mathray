package mathray.eval.text;

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
    StringBuilder b = new StringBuilder();
    b.append(func.name);
    b.append('(');
    if(args.size() > 0) {
      b.append(args.get(0).text);
    }
    for(int i = 1; i < args.size(); i++) {
      b.append(", ");
      b.append(args.get(i).text);
    }
    b.append(')');
    return new PrecedenceString(b.toString(), Integer.MAX_VALUE);
  }

}
