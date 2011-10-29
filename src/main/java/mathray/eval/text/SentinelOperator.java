package mathray.eval.text;

import java.util.Stack;

import mathray.Value;
import mathray.Vector;

public class SentinelOperator extends Operator {

  public SentinelOperator() {
    super(null, Integer.MIN_VALUE);
  }

  @Override
  public PrecedenceString call(Vector<PrecedenceString> args) {
    throw new RuntimeException("sentinel call");
  }

  @Override
  public void reduce(Stack<Value> stack) {
    throw new RuntimeException("sentinel reduce");
  }

}
