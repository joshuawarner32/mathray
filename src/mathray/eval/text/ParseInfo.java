package mathray.eval.text;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import mathray.Call;
import mathray.Expressions;
import mathray.Rational;
import mathray.Function;
import mathray.SelectFunction;
import mathray.Value;
import mathray.Variable;
import mathray.Vector;
import mathray.eval.Visitor;
import mathray.eval.Impl;
import static mathray.Expressions.*;

public class ParseInfo {
  
  private static class InfixOperator {
    public final String name;
    public final int precedence;
    public final SelectFunction func;
    
    public InfixOperator(String name, int precedence, SelectFunction func) {
      this.name = name;
      this.precedence = precedence;
      this.func = func;
    }
    
    @Override
    public String toString() {
      return name + "/" + precedence;
    }
  }
  
  private Map<SelectFunction, Impl<PrecedenceString>> operators = new HashMap<SelectFunction, Impl<PrecedenceString>>();
  
  private Map<String, InfixOperator> infixes = new HashMap<String, InfixOperator>();
  
  private Set<String> symbols = new HashSet<String>();
  
  private String groupBegin;
  
  private String groupEnd;

  private Map<String, SelectFunction> functions = new HashMap<String, SelectFunction>();
  
  private Map<String, Variable> variables = new HashMap<String, Variable>();
  
  private ParseInfo() {}
  
  public static class Builder {
    private Builder() {}

    private Map<SelectFunction, Impl<PrecedenceString>> operators = new HashMap<SelectFunction, Impl<PrecedenceString>>();
    
    private Map<String, InfixOperator> infixes = new HashMap<String, InfixOperator>();
    
    private Set<String> symbols = new HashSet<String>();
    
    private String groupBegin;
    
    private String groupEnd;
    
    private Map<String, SelectFunction> functions = new HashMap<String, SelectFunction>();
    
    private Map<String, Variable> variables = new HashMap<String, Variable>();
    
    public Builder group(String begin, String end) {
      symbols.add(begin);
      symbols.add(end);
      groupBegin = begin;
      groupEnd = end;
      return this;
    }
    
    public Builder infix(String name, int precedence, SelectFunction function) {
      operators.put(function, new OperatorPrecedenceImplementation(null, name, null, precedence));
      infixes.put(name, new InfixOperator(name, precedence, function));
      symbols.add(name);
      return this;
    }
    
    public Builder prefix(String name, int precedence, SelectFunction function) {
      operators.put(function, new OperatorPrecedenceImplementation(name, null, null, precedence));
      symbols.add(name);
      return this;
    }
    
    public Builder postfix(String name, int precedence, SelectFunction function) {
      operators.put(function, new OperatorPrecedenceImplementation(null, null, name, precedence));
      symbols.add(name);
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
      info.infixes.putAll(infixes);
      info.symbols.addAll(symbols);
      info.groupBegin = groupBegin;
      info.groupEnd = groupEnd;
      return info;
    }

    public Builder function(String name, SelectFunction function) {
      functions.put(name, function);
      return this;
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
    Stack<InfixOperator> ops = new Stack<InfixOperator>();
    ops.push(new InfixOperator("sentinel", Integer.MIN_VALUE, null));
    for(Token tok : Token.tokens(symbols, text)) {
      switch(tok.type) {
      case Identifier:
        Value val = variables.get(tok.text);
        if(val != null) {
          stack.push(val);
        } else {
          SelectFunction func = functions.get(tok.text);
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
        if(tok.text.equals(groupBegin)) {
          ops.push(new InfixOperator("paren", Integer.MIN_VALUE, null));
        } else if(tok.text.equals(groupEnd)) {
          while(Integer.MIN_VALUE < ops.peek().precedence) {
            InfixOperator o = ops.pop();
            Value b = stack.pop();
            Value a = stack.pop();
            stack.push(o.func.call(a, b));
          }
          ops.pop();
        } else {
          InfixOperator op = infixes.get(tok.text);
          if(op != null) {
            while(op.precedence < ops.peek().precedence) {
              InfixOperator o = ops.pop();
              Value b = stack.pop();
              Value a = stack.pop();
              stack.push(o.func.call(a, b));
            }
            ops.push(op);
          }
        }
        break;
      }
    }
    while(Integer.MIN_VALUE < ops.peek().precedence) {
      InfixOperator o = ops.pop();
      Value b = stack.pop();
      Value a = stack.pop();
      stack.push(o.func.call(a, b));
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
