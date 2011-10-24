package mathray.eval.text;

import static mathray.Expressions.vector;
import mathray.Vector;
import mathray.eval.Impl;

public class OperatorPrecedenceImplementation implements Impl<PrecedenceString> {

  public final String begin;
  
  public final String join;
  
  public final String end;
  
  public final int precedence;

  public OperatorPrecedenceImplementation(String begin, String join, String end, int precedence) {
    this.begin = begin;
    this.join = join;
    this.end = end;
    this.precedence = precedence;
  }

  @Override
  public Vector<PrecedenceString> call(Vector<PrecedenceString> args) {
    StringBuilder b = new StringBuilder();
    if(begin != null) {
      b.append(begin);
    }
    b.append(args.get(0).toString(precedence));
    for(int i = 1; i < args.size(); i++) {
      b.append(join);
      b.append(args.get(1).toString(precedence + 1));
    }
    if(end != null) {
      b.append(end);
    }
    return vector(new PrecedenceString(b.toString(), precedence));
  }
}
