package mathray.eval.text;

import java.util.Vector;

import mathray.SelectFunction;


public class InfixOperator {

  public enum Associativity {LEFT, RIGHT};
  
  public final String name;
  public final int precedence;
  public final Associativity associativity;
  public final SelectFunction func;
  
  public InfixOperator(String name, int precedence, Associativity associativity, SelectFunction func) {
    this.name = name;
    this.precedence = precedence;
    this.associativity = associativity;
    this.func = func;
  }
  
  @Override
  public String toString() {
    return name + "/" + precedence;
  }
  
  public PrecedenceString call(Vector<PrecedenceString> args) {
    return new PrecedenceString(args.get(0).toString(precedence) + name + args.get(1).toString(precedence + 1), precedence);
  }
}