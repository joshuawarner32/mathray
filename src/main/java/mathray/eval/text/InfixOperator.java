package mathray.eval.text;

import mathray.Function;
import mathray.Value;
import mathray.Vector;

import java.util.Stack;

public class InfixOperator extends Operator {

  public enum Associativity {LEFT, RIGHT};
  
  public final String name;
  public final Associativity associativity;
  
  public InfixOperator(String name, Function function, int precedence, Associativity associativity) {
    super(function, precedence);
    this.name = name;
    this.associativity = associativity;
  }
  
  @Override
  public String toString() {
    return "1" + name + "1:" + precedence;
  }
  
  @Override
  public PrecedenceString call(Vector<PrecedenceString> args) {
    int precA = precedence;
    int precB = precedence;
    if(associativity == Associativity.LEFT) {
      precB++;
    } else {
      precA++;
    }
    return new PrecedenceString(args.get(0).toString(precA) + name + args.get(1).toString(precB), precedence);
  }

  @Override
  public void reduce(Stack<Value> stack) {
    Value b = stack.pop();
    Value a = stack.pop();
    stack.push(function.call(a, b));
  }
}