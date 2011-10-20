package mathray.eval.text;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import mathray.Call;
import mathray.Constant;
import mathray.Function;
import mathray.Value;
import mathray.Variable;
import mathray.Vector;
import mathray.eval.Visitor;
import mathray.eval.Impl;
import mathray.eval.text.ParseInfo.Builder;
import static mathray.Expressions.*;

public class ParseInfo {
  
  private Map<Function, Impl<PrecedenceString>> operators = new HashMap<Function, Impl<PrecedenceString>>();

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
        return implement(call.func).call(call.func, call.visitArgs(this));
      }

      @Override
      public PrecedenceString variable(Variable var) {
        return new PrecedenceString(var.name, Integer.MAX_VALUE);
      }

      @Override
      public PrecedenceString constant(Constant cst) {
        return new PrecedenceString(String.valueOf(cst.value), Integer.MAX_VALUE);
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
            
          } else {
            // parse error
            throw new RuntimeException("parse error");
          }
        }
        break;
      case Number:
        stack.push(num(Long.valueOf(tok.text)));
        break;
      case Symbol:
        if(tok.text.equals('(')) {
          
        } else if(tok.text.equals(')')) {
          
        } else {
          
        }
        break;
      }
    }
    if(stack.size() != 1) {
      // parse error
      throw new RuntimeException("parse error");
    }
    return stack.pop();
  }

  public Impl<PrecedenceString> implement(Function func) {
    Impl<PrecedenceString> ret = operators.get(func);
    if(ret != null) {
      return ret;
    }
    return FunctionPrecedenceImplementation.Instance;
  }

}
