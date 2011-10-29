package mathray.eval.text;

import static mathray.Expressions.num;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import mathray.Call;
import mathray.Function;
import mathray.Generator;
import mathray.Rational;
import mathray.SelectFunction;
import mathray.Value;
import mathray.Variable;
import mathray.Vector;
import mathray.eval.Impl;
import mathray.eval.Visitor;
import mathray.eval.text.InfixOperator.Associativity;

public class ParseInfo {
  
  private Map<Function, OperatorImpl> operators = new HashMap<Function, OperatorImpl>();
  
  private Map<String, InfixOperator> infixes = new HashMap<String, InfixOperator>();
  
  private Map<String, PrefixOperator> prefixes = new HashMap<String, PrefixOperator>();
  
  private Set<String> symbols = new HashSet<String>();
  
  private String groupBegin;
  
  private String groupEnd;

  private Map<String, SelectFunction> functions = new HashMap<String, SelectFunction>();
  
  private Map<String, Variable> variables = new HashMap<String, Variable>();
  
  private ParseInfo() {}
  
  private static class OperatorImpl implements Impl<PrecedenceString> {
    
    public final Operator[] ops;
    
    public OperatorImpl(int outputArity) {
      ops = new Operator[outputArity];
    }

    @Override
    public Vector<PrecedenceString> call(final Vector<PrecedenceString> args) {
      return Vector.<PrecedenceString>generate(ops.length, new Generator<PrecedenceString>() {
        @Override
        public PrecedenceString generate(int index) {
          return ops[index].call(args);
        }
      });
    }
    
  }
  
  public static class Builder {
    private Builder() {}
    
    private ParseInfo info = new ParseInfo();
    
    public Builder group(String begin, String end) {
      info.symbols.add(begin);
      info.symbols.add(end);
      info.groupBegin = begin;
      info.groupEnd = end;
      return this;
    }
    
    public Builder infix(String name, int precedence, Associativity associativity, SelectFunction function) {
      InfixOperator op = new InfixOperator(name, function, precedence, associativity);
      putOperator(name, op);
      info.infixes.put(name, op);
      info.symbols.add(name);
      return this;
    }
    
    public Builder prefix(String name, int precedence, SelectFunction function) {
      PrefixOperator op = new PrefixOperator(name, function, precedence);
      putOperator(name, op);
      info.prefixes.put(name, op);
      info.symbols.add(name);
      return this;
    }
    
    private void putOperator(String name, Operator operator) {
      Function func = operator.function.func;
      OperatorImpl impl = info.operators.get(func);
      if(impl == null) {
        info.operators.put(func, impl = new OperatorImpl(func.outputArity));
      }
      impl.ops[operator.function.outputIndex] = operator;
    }

    public Builder var(Variable var) {
      info.variables.put(var.name, var);
      return this;
    }
    
    public ParseInfo build() {
      // TODO: enforce immutability
      return info;
    }

    public Builder function(String name, SelectFunction function) {
      info.functions.put(name, function);
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
    Stack<Operator> ops = new Stack<Operator>();
    ops.push(new SentinelOperator());
    
    final int AFTER_OPERATOR = 0;
    final int AFTER_VALUE = 1;
    
    int state = AFTER_OPERATOR;
    
    for(Token tok : Token.tokens(symbols, text)) {
      switch(tok.type) {
      case Identifier:
        Value val = variables.get(tok.text);
        if(val != null) {
          stack.push(val);
          state = AFTER_VALUE;
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
        state = AFTER_VALUE;
        break;
      case Symbol:
        if(tok.text.equals(groupBegin)) {
          ops.push(new SentinelOperator());
          state = AFTER_OPERATOR;
        } else if(tok.text.equals(groupEnd)) {
          while(Integer.MIN_VALUE < ops.peek().precedence) {
            ops.pop().reduce(stack);
          }
          ops.pop();
          state = AFTER_VALUE;
        } else if(state == AFTER_VALUE) {
          InfixOperator op = infixes.get(tok.text);
          if(op != null) {
            while(op.precedence < ops.peek().precedence || (op.associativity == Associativity.LEFT && op.precedence == ops.peek().precedence)) {
              ops.pop().reduce(stack);
            }
            ops.push(op);
            state = AFTER_OPERATOR;
          } else {
            throw new RuntimeException("unexpected operator '" + tok.text + "'");
          }
        } else if(state == AFTER_OPERATOR) {
          PrefixOperator op = prefixes.get(tok.text);
          if(op != null) {
            ops.push(op);
          } else {
            throw new RuntimeException("unexpected operator '" + tok.text + "'");
          }
        }
        break;
      }
    }
    while(Integer.MIN_VALUE < ops.peek().precedence) {
      ops.pop().reduce(stack);
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
