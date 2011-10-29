package mathray.eval.text;

import mathray.Value;
import mathray.Vector;
import mathray.SelectFunction;

import java.util.Stack;

public class PrefixOperator extends Operator {

  public final String name;

  public PrefixOperator(String name, SelectFunction function, int precedence) {
    super(function, precedence);
    this.name = name;
  }
  
  @Override
  public String toString() {
    return name + "1:" + precedence;
  }
  
  @Override
  public PrecedenceString call(Vector<PrecedenceString> args) {
    return new PrecedenceString(name + args.get(0).toString(precedence), precedence);
  }

  @Override
  public void reduce(Stack<Value> stack) {
    stack.push(function.call(stack.pop()));
  }
  
}