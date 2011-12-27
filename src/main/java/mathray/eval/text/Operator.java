package mathray.eval.text;

import mathray.Value;
import mathray.Function;
import mathray.eval.Impl;
import mathray.util.Vector;

import java.util.Stack;

public abstract class Operator implements Impl<PrecedenceString>{
  public final Function function;
  public final int precedence;

  public Operator(Function function, int precedence) {
    this.function = function;
    this.precedence = precedence;
  }
  
  public abstract PrecedenceString call(Vector<PrecedenceString> args);

  public abstract void reduce(Stack<Value> stack);
}
