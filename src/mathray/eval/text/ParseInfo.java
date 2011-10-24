package mathray.eval.text;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import mathray.Call;
import mathray.Rational;
import mathray.Function;
import mathray.Value;
import mathray.Variable;
import mathray.Vector;
import mathray.eval.Visitor;
import mathray.eval.Impl;
import static mathray.Expressions.*;

public class ParseInfo {
  
  private Map<Function, Impl<PrecedenceString>> operators = new HashMap<Function, Impl<PrecedenceString>>();
  
  private Map<String, Object> thingies = new HashMap<String, Object>();

  private Map<String, SingularFunction> functions = new HashMap<String, SingularFunction>();
  
  private Map<String, Variable> variables = new HashMap<String, Variable>();
  
  private ParseInfo() {}
  
  public static class Builder {
    private Builder() {}

    private Map<Function, Impl<PrecedenceString>> operators = new HashMap<Function, Impl<PrecedenceString>>();
    
    private Map<String, SingularFunction> functions = new HashMap<String, SingularFunction>();
    
    private Map<String, Variable> variables = new HashMap<String, Variable>();
    
    public Builder infix(String name, int prec, Function function) {
      operators.put(function, new OperatorPrecedenceImplementation(null, name, null, prec));
      return this;
    }
    
    public Builder prefix(String name, int prec, Function function) {
      operators.put(function, new OperatorPrecedenceImplementation(name, null, null, prec));
      return this;
    }
    
    public Builder postfix(String name, int prec, Function function) {
      operators.put(function, new OperatorPrecedenceImplementation(null, null, name, prec));
      return this;
    }

    public Builder var(Variable var) {
      variables.put(var.name, var);
      return this;
    }
    
    public ParseInfo build() {
      ParseInfo info = new ParseInfo();
      info.operators.putAll(operators);
      info.functions.putAll(functions);
      info.variables.putAll(variables);
      return info;
    }
  }
  
  public static Builder builder() {
    return new Builder();
  }
  
  public String unparse(Value value) {
    return value.accept(new Visitor<PrecedenceString>() {

      @Override
      public Vector<PrecedenceString> call(Visitor<PrecedenceString> v, Call call) {
        return implement(call.func).call(call.visitArgs(this));
      }

      @Override
      public PrecedenceString variable(Variable var) {
        return new PrecedenceString(var.name, Integer.MAX_VALUE);
      }

      @Override
      public PrecedenceString constant(Rational r) {
        return new PrecedenceString(r.toString(), Integer.MAX_VALUE);
      }
      
    }).toString();
  }
  
  public Value parse(String text) {
    Stack<Value> stack = new Stack<Value>();
    for(Token tok : Token.tokens(text)) {
      switch(tok.type) {
      case Identifier:
        Value val = variables.get(tok.text);
        if(val != null) {
          stack.push(val);
        } else {
          SingularFunction func = functions.get(tok.text);
          if(func != null) {
            throw new RuntimeException("unhandled case");
          } else {
            // TODO: real parse exceptions
            throw new RuntimeException("parse error");
          }
        }
        break;
      case Number:
        stack.push(num(Long.valueOf(tok.text)));
        break;
      case Symbol:
        if(tok.text.equals('(')) {
          throw new RuntimeException("unhandled case");
        } else if(tok.text.equals(')')) {
          throw new RuntimeException("unhandled case");
        } else {
          
        }
        break;
      }
    }
    if(stack.size() != 1) {
      // TODO: real parse exceptions
      throw new RuntimeException("stack size wrong: 1 != " + stack.size());
    }
    return stack.pop();
  }

  public Impl<PrecedenceString> implement(Function func) {
    Impl<PrecedenceString> ret = operators.get(func);
    if(ret != null) {
      return ret;
    }
    return new FunctionPrecedenceImplementation(func);
  }

}
