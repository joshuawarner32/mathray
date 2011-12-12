package mathray.eval.text;

import java.util.Stack;

import mathray.eval.text.Operator;
import mathray.Function;
import mathray.Value;
import mathray.Vector;

public class FunctionOperator extends Operator {

  public FunctionOperator(Function function) {
    super(function, Integer.MIN_VALUE + 1);
  }

  @Override
  public PrecedenceString call(Vector<PrecedenceString> args) {
    return null;
    //return new PrecedenceString(text, precedence);
  }

  @Override
  public void reduce(Stack<Value> stack) {
    Value[] values = new Value[function.arity];
    for(int i = values.length - 1; i >= 0; i--) {
      values[i] = stack.pop();
    }
    stack.push(function.call(values));
  }

}
