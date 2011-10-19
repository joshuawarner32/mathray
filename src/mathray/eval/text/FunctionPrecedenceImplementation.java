package mathray.eval.text;

import mathray.Function;
import mathray.Vector;
import mathray.eval.Impl;

public class FunctionPrecedenceImplementation implements Impl<PrecedenceString> {
  
  private FunctionPrecedenceImplementation() {}
  
  public static FunctionPrecedenceImplementation Instance = new FunctionPrecedenceImplementation();

  @Override
  public Vector<PrecedenceString> call(Function func, Vector<PrecedenceString> args) {
    if(func.outputArity == 1) {
      return new Vector<PrecedenceString>(f(func.name, args));
    } else {
      PrecedenceString[] output = new PrecedenceString[func.outputArity];
      for(int i = 0; i < func.outputArity; i++) {
        output[i] = f(func.individualNames.get(i), args);
      }
      return new Vector<PrecedenceString>(output);
    }
  }

  private PrecedenceString f(String name, Vector<PrecedenceString> args) {
    StringBuilder b = new StringBuilder();
    b.append(name);
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
