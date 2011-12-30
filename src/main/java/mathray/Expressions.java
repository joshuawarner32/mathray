package mathray;

import static mathray.Functions.*;
import mathray.util.Vector;

public class Expressions {
  
  private Expressions() {}
  
  public static Symbol sym(String name) {
    return new Symbol(name);
  }
  
  public static Args args(Symbol... args) {
    return new Args(args);
  }
  
  public static Args args(int count) {
    return new Args(count);
  }
  
  public static <T> Vector<T> vector(T... values) {
    return new Vector<T>(values);
  }
  
  public static Definition def(Args args, Value value) {
    return new Definition(args, value);
  }
  
  public static Computation compute(Args args, Value... values) {
    return new Computation(args, vector(values));
  }
  
  public static Value fold(Function func, Value... values) {
    if(values.length < 2) {
      throw new IllegalArgumentException("length of values must be > 1");
    }
    if(func.arity != 2) {
      throw new IllegalArgumentException("arity of func");
    }
    Value res = func.call(vector(values[0], values[1]));
    for(int i = 2; i < values.length; i++) {
      res = func.call(vector(res, values[i]));
    }
    return res;
  }
  
  public static Value fold(Function func, Vector<Value> values) {
    if(values.size() < 1) {
      throw new IllegalArgumentException("length of values must be > 0");
    } else if(values.size() < 2) {
      return values.get(0);
    }
    if(func.arity != 2) {
      throw new IllegalArgumentException("arity of func");
    }
    Value res = func.call(vector(values.get(0), values.get(1)));
    for(int i = 2; i < values.size(); i++) {
      res = func.call(vector(res, values.get(i)));
    }
    return res;
  }
  
  public static Value fold(Function func, Value start, Vector<Value> values) {
    if(values.size() < 1) {
      return start;
    }
    if(func.arity != 2) {
      throw new IllegalArgumentException("arity of func");
    }
    Value res = start;
    for(int i = 1; i < values.size(); i++) {
      res = func.call(vector(res, values.get(i)));
    }
    return res;
  }
  
  public static Value add(Value... values) {
    return fold(ADD, values);
  }
  
  public static Value add(Vector<Value> values) {
    return fold(ADD, values);
  }
  
  public static Value sub(Value... values) {
    return fold(SUB, values);
  }
  
  public static Value mul(Value... values) {
    return fold(MUL, values);
  }
  
  public static Value mul(Vector<Value> values) {
    return fold(MUL, values);
  }
  
  public static Value div(Value... values) {
    return fold(DIV, values);
  }
  
  public static Value pow(Value a, Value b) {
    return POW.call(a, b);
  }
  
  public static Value pow(Value a, long b) {
    return pow(a, num(b));
  }
  
  public static Value neg(Value value) {
    return NEG.call(value);
  }
  
  public static Value min(Value... values) {
    return MIN.call(values);
  }
  
  public static Value max(Value... values) {
    return MAX.call(values);
  }
  
  public static Value abs(Value value) {
    return ABS.call(value);
  }
  
  public static Value selectSign(Value test, Value negative, Value zero, Value positive) {
    return SELECT_SIGN.call(test, negative, zero, positive);
  }
  
  public static Value sin(Value value) {
    return SIN.call(value);
  }
  
  public static Value sinh(Value value) {
    return SINH.call(value);
  }
  
  public static Value cos(Value value) {
    return COS.call(value);
  }
  
  public static Value cosh(Value value) {
    return COSH.call(value);
  }
  
  public static Value tan(Value value) {
    return TAN.call(value);
  }
  
  public static Value sqrt(Value value) {
    return SQRT.call(value);
  }

  public static Value log(Value value) {
    return LOG.call(value);
  }
  
  public static Value up(Value value) {
    return UP.call(value);
  }
  
  public static Value down(Value value) {
    return DOWN.call(value);
  }
  
  public static Rational num(long value) {
    return Rational.get(value);
  }
  
  public static Rational num(long num, long denom) {
    return Rational.get(num, denom);
  }

}
