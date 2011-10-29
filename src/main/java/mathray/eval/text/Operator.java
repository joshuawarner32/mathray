package mathray.eval.text;

import mathray.Value;
import mathray.Vector;
import mathray.SelectFunction;

import java.util.Stack;

public abstract class Operator {
  public final SelectFunction function;
  public final int precedence;

  public Operator(SelectFunction function, int precedence) {
    this.function = function;
    this.precedence = precedence;
  }
  
  public abstract PrecedenceString call(Vector<PrecedenceString> args);

  public abstract void reduce(Stack<Value> stack);
}
