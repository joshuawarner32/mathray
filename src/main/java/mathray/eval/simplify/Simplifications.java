package mathray.eval.simplify;

import java.util.LinkedList;
import java.util.List;

import mathray.Call;
import mathray.Multidef;
import mathray.Definition;
import mathray.Function;
import mathray.Rational;
import mathray.Expressions;
import mathray.util.Transformer;
import mathray.Value;
import mathray.Symbol;
import mathray.util.Vector;
import mathray.visitor.Processor;

import static mathray.Functions.*;
import static mathray.Expressions.*;
import static mathray.NamedConstants.*;
import static mathray.eval.simplify.Pattern.*;

public class Simplifications extends PatternRegistry {
  
  private static final Simplifications INSTANCE = new Simplifications();
  
  {
    Symbol x = sym("x");
    Symbol y = sym("y");
    Symbol z = sym("z");
    
    Symbol test = sym("test");
    Symbol even = sym("even");
    Symbol odd = sym("odd");
    Symbol other = sym("other");
    
    register(pattern(args(), PI, div(TAU, num(2))));
    
    register(pattern(args(), sin(num(0)), num(0)));
    register(pattern(args(), sin(TAU), num(0)));
    register(pattern(args(x), sin(mul(TAU, x)), num(0), isInteger(x)));
    register(pattern(args(x), sin(mul(x, TAU)), num(0), isInteger(x)));
    register(pattern(args(x), sin(neg(x)), neg(sin(x))));
    
    register(pattern(args(), cos(num(0)), num(1)));
    register(pattern(args(), cos(TAU), num(1)));
    register(pattern(args(x), cos(mul(TAU, x)), num(1), isInteger(x)));
    register(pattern(args(x), cos(mul(x, TAU)), num(1), isInteger(x)));
    register(pattern(args(x), cos(neg(x)), cos(x)));
    
    register(pattern(args(x), abs(neg(x)), abs(x)));
    register(pattern(args(x), abs(abs(x)), abs(x)));
    register(pattern(args(x, y), selectSign(abs(x), y, z), y));
    register(pattern(args(x, y), selectSign(neg(abs(x)), y, z), z));
    
    register(pattern(args(x), sin(abs(x)), abs(sin(x))));
    register(pattern(args(x), cos(abs(x)), cos(x)));
    
    register(pattern(args(x, y, z), selectEqual(x, x, y, z), y));
    register(pattern(args(test, even, odd, other), selectInteger(test, even, odd, other), even, isEven(test)));
    register(pattern(args(test, even, odd, other), selectInteger(test, even, odd, other), odd, isOdd(test)));
  }
  
  private static abstract class Expr {
    
    public Expr exprMul(Expr expr) {
      if(expr instanceof ProductExpr || expr instanceof ConstExpr) {
        return expr.exprMul(this);
      }
      return new ProductExpr(num(1), vector(this, expr));
    }
    
    public Expr exprAdd(Expr expr) {
      if(expr instanceof SumExpr) {
        return expr.exprAdd(this);
      } else if(expr instanceof ConstExpr) {
        return new SumExpr(((ConstExpr)expr).value, vector(this));
      }
      return new SumExpr(num(0), vector(this, expr));
    }
    
    public Expr exprNeg() {
      return new ProductExpr(num(-1), vector(this));
    }
    
    public Expr exprRecip() {
      return new PowerExpr(this, new ConstExpr(num(-1)));
    }

    public Expr exprPow(Expr expr) {
      return new PowerExpr(this, expr);
    }
    
    public boolean isNegative() {
      return false;
    }
    
    public boolean isReciprocal() {
      return false;
    }
    
    public Value toReciprocalValue() {
      return div(num(1), toValue());
    }
    
    public abstract Value toValue();
  }
  
  private static class ValueExpr extends Expr {
    private final Value value;
    
    public ValueExpr(Value value) {
      this.value = value;
    }
    
    @Override
    public Value toValue() {
      return value;
    }
  }
  
  private static class ConstExpr extends Expr {
    private final Rational value;
    
    public ConstExpr(Rational value) {
      this.value = value;
    }
    
    @Override
    public Expr exprMul(Expr expr) {
      if(value.equals(Rational.get(0))) {
        return this;
      } else if(expr instanceof ConstExpr) {
        return new ConstExpr(value.mul(((ConstExpr)expr).value));
      } else if(value.equals(num(1))) {
        return expr;
      } else if(expr instanceof ProductExpr) {
        return expr.exprMul(this);
      } else {
        return new ProductExpr(value, vector(expr));
      }
    }

    @Override
    public Expr exprAdd(Expr expr) {
      if(expr instanceof ConstExpr) {
        return new ConstExpr(value.add(((ConstExpr)expr).value));
      } else {
        return new SumExpr(value, vector(expr));
      }
    }
    
    @Override
    public Expr exprNeg() {
      return new ConstExpr(value.negative());
    }
    
    @Override
    public Expr exprRecip() {
      return new ConstExpr(value.reciprocal());
    }
    
    @Override
    public boolean isNegative() {
      return value.isNegative();
    }
    
    @Override
    public boolean isReciprocal() {
      return !value.isInteger();
    }
    
    @Override
    public Value toReciprocalValue() {
      return value.reciprocal();
    }
    
    @Override
    public Value toValue() {
      return value;
    }
  }
  
  private static class PowerExpr extends Expr {
    private final Expr base;
    private final Expr power;
    
    public PowerExpr(Expr base, Expr power) {
      this.base = base;
      this.power = power;
    }
    
    @Override
    public Expr exprPow(Expr expr) {
      return new PowerExpr(base, power.exprMul(expr));
    }
    
    @Override
    public boolean isNegative() {
      if(power instanceof ConstExpr) {
        ConstExpr cexp = (ConstExpr)power;
        return cexp.value.isEven() && base.isNegative();
      }
      return false;
    }
    
    @Override
    public boolean isReciprocal() {
      return power.isNegative();
    }

    @Override
    public Value toValue() {
      if(power instanceof ConstExpr) {
        ConstExpr cexp = (ConstExpr)power;
        if(cexp.value.equals(num(1))) {
          return base.toValue();
        } else if(cexp.value.equals(num(-1))) {
          return base.toReciprocalValue();
        } else if(cexp.value.equals(num(0))) {
          return num(1);
        } else if(cexp.value.equals(num(1, 2))) {
          return sqrt(base.toValue());
        }
      }
      return pow(base.toValue(), power.toValue());
    }
    
    @Override
    public Value toReciprocalValue() {
      if(power instanceof ConstExpr) {
        ConstExpr cexp = (ConstExpr)power;
        if(cexp.value.equals(num(1))) {
          return div(num(1), base.toValue());
        } else if(cexp.value.equals(num(-1))) {
          return base.toValue();
        } else if(cexp.value.equals(num(0))) {
          return num(1);
        }
      }
      return pow(base.toValue(), power.exprNeg().toValue());
    }
  }
  
  private static Value constMul(Rational r, Value v) {
    if(r.equals(num(0))) {
      return num(0);
    } else {
      if(v != null) { 
        if(r.equals(num(1))) {
          return v;
        } else if(r.equals(num(-1))) {
          return neg(v);
        } else {
          return mul(r, v);
        }
      } else {
        return r;
      }
    }
  }

  private static class ProductExpr extends Expr {
    private final Rational coeff;
    private final Vector<Expr> factors;
    
    private ProductExpr(Rational coeff, Vector<Expr> factors) {
      this.coeff = coeff;
      this.factors = factors;
    }

    @Override
    public Expr exprMul(Expr expr) {
      if(expr instanceof ConstExpr) {
        return new ProductExpr(coeff.mul(((ConstExpr)expr).value), factors);
      } else if(expr instanceof ProductExpr) {
        ProductExpr prod = (ProductExpr)expr;
        return new ProductExpr(coeff.mul(prod.coeff), factors.concat(prod.factors));
      } else {
        return new ProductExpr(coeff, factors.append(expr));
      }
    }
    
    @Override
    public Expr exprNeg() {
      return new ProductExpr(coeff.negative(), factors);
    }
    
    @Override
    public boolean isNegative() {
      boolean neg = coeff.isNegative();
      for(Expr expr : factors) {
        neg ^= expr.isNegative();
      }
      return neg;
    }
    
    @Override
    public boolean isReciprocal() {
      int direct = 0;
      int recip = 0;
      for(Expr expr : factors) {
        if(expr.isReciprocal()) {
          direct++;
        } else {
          recip++;
        }
      }
      return recip >= direct;
    }
    
    private Value toValue(boolean neg) {
      Value num = null;
      Value denom = null;
      for(Expr expr : factors) {
        if(expr.isReciprocal()) {
          if(denom == null) {
            denom = expr.toReciprocalValue();
          } else {
            denom = Expressions.mul(denom, expr.toReciprocalValue());
          }
        } else {
          if(num == null) {
            num = expr.toValue();
          } else {
            num = Expressions.mul(num, expr.toValue());
          }
        }
      }
      Rational coeff = this.coeff;
      if(neg) {
        coeff = coeff.negative();
      }
      if(denom == null && coeff.denominator().equals(num(1))) {
        return constMul(coeff.numerator(), num);
      } else {
        return div(constMul(coeff.numerator(), num), constMul(coeff.denominator(), denom));
      }
    }
    
    @Override
    public Value toValue() {
      return toValue(false);
    }
  }
  
  private static class SumExpr extends Expr {
    private final Rational offset;
    private final Vector<Expr> terms;
    
    private SumExpr(Rational offset, Vector<Expr> terms) {
      this.offset = offset;
      this.terms = terms;
    }

    @Override
    public Expr exprAdd(Expr expr) {
      if(expr instanceof ConstExpr) {
        return new SumExpr(offset.add(((ConstExpr)expr).value), terms);
      } else if(expr instanceof SumExpr) {
        SumExpr sum = (SumExpr)expr;
        return new SumExpr(offset.add(sum.offset), terms.concat(sum.terms));
      } else {
        return new SumExpr(offset, terms.append(expr));
      }
    }
    
    @Override
    public Value toValue() {
      Value ret = null;
      List<Expr> negs = new LinkedList<Expr>();
      if(!offset.equals(num(0))) {
        if(offset.isNegative()) {
          negs.add(new ConstExpr(offset.negative()));
        } else {
          ret = offset;
        }
      }
      for(Expr expr : terms) {
        if(expr.isNegative()) {
          negs.add(expr.exprNeg());
        } else {
          if(ret == null) {
            ret = expr.toValue();
          } else {
            ret = Expressions.add(ret, expr.toValue());
          }
        }
      }
      if(ret == null) {
        if(offset.equals(num(0))) {
          ret = negs.remove(0).exprNeg().toValue();
        } else {
          ret = offset;
        }
      }
      for(Expr e : negs) {
        ret = sub(ret, e.toValue());
      }
      return ret;
    }
  }
  
  private Vector<Value> toValues(Vector<Expr> exprs) {
    return exprs.transform(new Transformer<Expr, Value>() {
      @Override
      public Value transform(Expr in) {
        return applyPatterns(in.toValue());
      }
    });
  }
  
  private Processor<Expr> visitor = new Processor<Expr>() {
    @Override
    public Expr process(Call call, Vector<Expr> args) {
      Function func = call.func;
      if(func == ADD) {
        return args.get(0).exprAdd(args.get(1));
      } else if(func == SUB) {
        return args.get(0).exprAdd(args.get(1).exprNeg());
      } else if(func == MUL) {
        return args.get(0).exprMul(args.get(1));
      } else if(func == DIV) {
        return args.get(0).exprMul(args.get(1).exprRecip());
      } else if(func == NEG) {
        return args.get(0).exprNeg();
      } else if(func == POW) {
        return args.get(0).exprPow(args.get(1));
      } else {
        return new ValueExpr(applyPatterns(func.call(toValues(args))));
      }
    }
    @Override
    public Expr process(Rational cst) {
      return new ConstExpr(cst);
    }
    @Override
    public Expr process(Symbol var) {
      return new ValueExpr(applyPatterns(var));
    }
  };
  
  public static Multidef simplify(Multidef def) {
    return INSTANCE.transform(def);
  }
  
  public static Definition simplify(Definition def) {
    return INSTANCE.transform(def);
  }
  
  public static Value simplify(Value value) {
    return INSTANCE.transform(value);
  }
  
  public Multidef transform(Multidef def) {
    return new Multidef(def.args, struct(def.value.toVector().transform(new Transformer<Value, Value>() {
      @Override
      public Value transform(Value in) {
        return Simplifications.this.transform(in);
      }
    })));
  }

  public Definition transform(Definition orig) {
    return transform(orig.toMultidef()).get(0);
  }

  public Value transform(Value v) {
    try {
      return v.accept(visitor).toValue();
    } catch(ArithmeticException e) {
      // we can't simplify expressions that have division by zero
      return v;
    }
  }

}
