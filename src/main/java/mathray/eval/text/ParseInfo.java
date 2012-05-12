package mathray.eval.text;

import static mathray.Expressions.num;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import com.google.common.base.Joiner;

import mathray.Call;
import mathray.Function;
import mathray.Rational;
import mathray.Value;
import mathray.Symbol;
import mathray.eval.Impl;
import mathray.eval.text.InfixOperator.Associativity;
import mathray.util.Vector;
import mathray.visitor.Processor;

public class ParseInfo {
  
  private Map<Function, Operator> operators = new HashMap<Function, Operator>();
  
  private Map<String, InfixOperator> infixes = new HashMap<String, InfixOperator>();
  
  private Map<String, PrefixOperator> prefixes = new HashMap<String, PrefixOperator>();
  
  private Set<String> symbols = new HashSet<String>();
  
  private String groupBegin;
  
  private String groupEnd;
  
  private String groupSep;

  private Map<String, Function> functions = new HashMap<String, Function>();
  
  private Map<String, Value> namedValues = new HashMap<String, Value>();
  
  private ParseInfo() {}
  
  private ParseInfo(ParseInfo copy) {
    operators = new HashMap<Function, Operator>(copy.operators);
    infixes = new HashMap<String, InfixOperator>(copy.infixes);
    prefixes = new HashMap<String, PrefixOperator>(copy.prefixes);
    symbols = new HashSet<String>(copy.symbols);
    groupBegin = copy.groupBegin;
    groupEnd = copy.groupEnd;
    groupSep = copy.groupSep;
    functions = new HashMap<String, Function>(copy.functions);
    namedValues = new HashMap<String, Value>(copy.namedValues);
  }
  
  public static class Builder {
    
    private ParseInfo info;
    
    private Builder() {
      info = new ParseInfo();
    }
    
    private Builder(ParseInfo info) {
      this.info = new ParseInfo(info);
    }
    
    public Builder group(String begin, String sep, String end) {
      info.symbols.add(begin);
      info.symbols.add(sep);
      info.symbols.add(end);
      info.groupBegin = begin;
      info.groupSep = sep;
      info.groupEnd = end;
      return this;
    }
    
    public Builder infix(String name, int precedence, Associativity associativity, Function function) {
      InfixOperator op = new InfixOperator(name, function, precedence, associativity);
      info.operators.put(function, op);
      info.infixes.put(name, op);
      info.symbols.add(name);
      return this;
    }
    
    public Builder prefix(String name, int precedence, Function function) {
      PrefixOperator op = new PrefixOperator(name, function, precedence);
      info.operators.put(function, op);
      info.prefixes.put(name, op);
      info.symbols.add(name);
      return this;
    }
    
    public Builder value(Symbol var) {
      info.namedValues.put(var.name, var);
      return this;
    }
    
    public Builder value(String name, Value v) {
      info.namedValues.put(name, v);
      return this;
    }

    public Builder values(Symbol... syms) {
      for(Symbol s : syms) {
        value(s);
      }
      return this;
    }
    
    public ParseInfo build() {
      ParseInfo ret = info;
      info = null;
      return ret;
    }

    public Builder function(String name, Function function) {
      info.functions.put(name, function);
      return this;
    }

    public Builder function(Function func) {
      return function(func.name, func);
    }

    public Builder functions(Function... funcs) {
      for(Function f : funcs) {
        function(f);
      }
      return this;
    }
  }
  
  public static Builder newBuilder() {
    return new Builder();
  }
  
  public Builder toBuilder() {
    return new Builder(this);
  }
  
  public String unparse(Value value) {
    return value.accept(new Processor<PrecedenceString>() {

      @Override
      public PrecedenceString process(Call call, Vector<PrecedenceString> args) {
        return implement(call.func).call(args);
      }

      @Override
      public PrecedenceString process(Symbol var) {
        return new PrecedenceString(var.name, Integer.MAX_VALUE);
      }

      @Override
      public PrecedenceString process(Rational r) {
        if(r.denom == 1) {
          return new PrecedenceString(String.valueOf(r.num), Integer.MAX_VALUE);
        } else {
          return new PrecedenceString(r.num + "/" + r.denom, Integer.MAX_VALUE);
        }
      }
      
    }).toString();
  }
  
  public Value parse(String text) throws ParseException {
    Stack<Value> stack = new Stack<Value>();
    Stack<Operator> ops = new Stack<Operator>();
    ops.push(new SentinelOperator());
    
    final int AFTER_OPERATOR = 0;
    final int AFTER_VALUE = 1;
    
    int state = AFTER_OPERATOR;
    
    for(Token tok : Token.tokens(symbols, text)) {
      switch(tok.type) {
      case Identifier:
        Value val = namedValues.get(tok.text);
        if(val != null) {
          stack.push(val);
          state = AFTER_VALUE;
        } else {
          Function func = functions.get(tok.text);
          if(func != null) {
            ops.push(new FunctionOperator(func));
          } else {
            throw new ParseException("unknown function '" + tok.text + "'");
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
          if(Integer.MIN_VALUE + 1 == ops.peek().precedence) {
            // this is a FunctionOperator
            ops.pop().reduce(stack);
          }
          state = AFTER_VALUE;
        } else if(tok.text.equals(groupSep)) {
          while(Integer.MIN_VALUE < ops.peek().precedence) {
            ops.pop().reduce(stack);
          }
          state = AFTER_OPERATOR;
        } else if(state == AFTER_VALUE) {
          InfixOperator op = infixes.get(tok.text);
          if(op != null) {
            while(op.precedence < ops.peek().precedence || (op.associativity == Associativity.LEFT && op.precedence == ops.peek().precedence)) {
              ops.pop().reduce(stack);
            }
            ops.push(op);
            state = AFTER_OPERATOR;
          } else {
            throw new ParseException("unexpected operator '" + tok.text + "'");
          }
        } else if(state == AFTER_OPERATOR) {
          PrefixOperator op = prefixes.get(tok.text);
          if(op != null) {
            ops.push(op);
          } else {
            throw new ParseException("unexpected operator '" + tok.text + "'");
          }
        }
        break;
      }
    }
    while(Integer.MIN_VALUE < ops.peek().precedence) {
      ops.pop().reduce(stack);
    }
    if(stack.size() != 1) {
      throw new ParseException("missing operator");
    }
    return stack.pop();
  }

  public Impl<PrecedenceString> implement(final Function func) {
    Impl<PrecedenceString> ret = operators.get(func);
    if(ret != null) {
      return ret;
    }
    return new Impl<PrecedenceString>() {
      @Override
      public PrecedenceString call(Vector<PrecedenceString> args) {
        return new PrecedenceString(func.name + '(' + Joiner.on(", ").join(args) + ')', Integer.MAX_VALUE);
      }
    };
  }

}
