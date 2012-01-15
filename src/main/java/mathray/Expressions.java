package mathray;

import static mathray.Functions.*;

import java.util.Arrays;
import java.util.Iterator;

import mathray.util.Generator;
import mathray.util.Transformer;
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
  
  public static Struct struct(Value... values) {
    return struct(vector(values));
  }
  
  public static <T> Closure<T> closure(Args args, T def) {
    return new Closure<T>(args, def);
  }
  
  public static Struct struct(final Vector<Value> values) {
    Function func = null;
    boolean all = true;
    for(Value val : values) {
      if(val instanceof Call) {
        if(func == null) {
          func = ((Call)val).func;
        } else if(func != ((Call)val).func) {
          all = false;
          break;
        }
      } else {
        all = false;
        break;
      }
    }
    if(all) {
      Vector<Struct> args = Vector.generate(func.arity, new Generator<Struct>() {
        @Override
        public Struct generate(final int index) {
          return struct(values.transform(new Transformer<Value, Value>() {
            @Override
            public Value transform(Value in) {
              return ((Call)in).args.get(index);
            }
          }));
        }
      });
      return new Multicall(func, args);
    }
    return new Multivalue(values);
  }
  
  public static Definition def(Args args, Value value) {
    return new Definition(args, value);
  }
  
  public static Multidef multidef(Args args, Value... values) {
    return new Multidef(args, struct(values));
  }
  
  public static Value fold(Function func, Iterable<Value> values) {
    Value ret = null;
    for(Value v : values) {
      if(ret == null) {
        ret = v;
      } else {
        ret = func.call(ret, v);
      }
    }
    return ret;
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
  
  // so we don't get superfluous warnings elsewhere
  @SuppressWarnings("unchecked")
  public static Iterable<Value> zip(Definition def, Iterable<Value> a) {
    return zip(def, new Iterable[] {a});
  }

  // so we don't get superfluous warnings elsewhere
  @SuppressWarnings("unchecked")
  public static Iterable<Value> zip(Definition def, Iterable<Value> a, Iterable<Value> b) {
    return zip(def, new Iterable[] {a, b});
  }
  
  public static Iterable<Value> zip(final Definition def, final Iterable<Value>... lists) {
    if(lists.length != def.args.size()) {
      throw new IllegalArgumentException("def arity and lists size do not match");
    }
    return new Iterable<Value>() {
      @Override
      public Iterator<Value> iterator() {
        @SuppressWarnings("unchecked")
        final Iterator<Value>[] its = new Iterator[lists.length];
        for(int i = 0; i < lists.length; i++) {
          its[i] = lists[i].iterator();
        }
        final Value[] vals = new Value[its.length];
        return new Iterator<Value>() {
          @Override
          public boolean hasNext() {
            boolean ret = false;
            for(Iterator<Value> it : its) {
              if(ret && !it.hasNext()) {
                throw new IllegalStateException("Iterator length does not match");
              }
              ret = it.hasNext();
            }
            return ret;
          }
          
          @Override
          public Value next() {
            for(int i = 0; i < its.length; i++) {
              vals[i] = its[i].next();
            }
            return def.call(vals);
          }

          @Override
          public void remove() {
            throw new UnsupportedOperationException();
          }
        };
      }
    };
  }
  
  public static Value add(Value a, Value b) {
    return ADD.call(a, b);
  }
  
  public static Value add(Value... values) {
    return fold(ADD, Arrays.asList(values));
  }
  
  public static Value add(Iterable<Value> values) {
    return fold(ADD, values);
  }
  
  public static Value sub(Value a, Value b) {
    return SUB.call(a, b);
  }
  
  public static Value mul(Value... values) {
    return fold(MUL, Arrays.asList(values));
  }
  
  public static Value mul(Value a, Value b) {
    return MUL.call(a, b);
  }
  
  public static Value mul(Iterable<Value> values) {
    return fold(MUL, values);
  }
  
  public static Value div(Value a, Value b) {
    return DIV.call(a, b);
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
  
  public static Value min(Value a, Value b) {
    return MIN.call(a, b);
  }
  
  public static Value max(Value a, Value b) {
    return MAX.call(a, b);
  }
  
  public static Value mod(Value a, Value b) {
    return MOD.call(a, b);
  }
  
  public static Value abs(Value value) {
    return ABS.call(value);
  }
  
  public static Value selectSign(Value test, Value negative, Value zeroOrPositive) {
    return SELECT_SIGN.call(test, negative, zeroOrPositive);
  }
  
  public static Value selectEqual(Value a, Value b, Value equal, Value notEqual) {
    return SELECT_EQUAL.call(a, b, equal, notEqual);
  }
  
  public static Value selectInteger(Value test, Value even, Value odd, Value other) {
    return SELECT_INTEGER.call(test, even, odd, other);
  }
  
  public static Value sin(Value value) {
    return SIN.call(value);
  }
  
  public static Value sinh(Value value) {
    return SINH.call(value);
  }
  
  public static Value asin(Value value) {
    return ASIN.call(value);
  }
  
  public static Value cos(Value value) {
    return COS.call(value);
  }
  
  public static Value cosh(Value value) {
    return COSH.call(value);
  }
  
  public Value acos(Value value) {
    return ACOS.call(value);
  }
  
  public static Value tan(Value value) {
    return TAN.call(value);
  }
  
  public static Value tanh(Value value) {
    return TANH.call(value);
  }
  
  public static Value atan(Value value) {
    return ATAN.call(value);
  }
  
  public static Value atan2(Value a, Value b) {
    return ATAN2.call(a, b);
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
