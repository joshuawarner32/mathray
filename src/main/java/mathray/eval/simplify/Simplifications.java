package mathray.eval.simplify;

import java.util.LinkedList;
import java.util.List;

import mathray.Call;
import mathray.Rational;
import mathray.Definition;
import mathray.Expressions;
import mathray.Transformer;
import mathray.Value;
import mathray.Variable;
import mathray.Vector;
import mathray.eval.Visitor;

import static mathray.Functions.*;
import static mathray.Expressions.*;

public class Simplifications {
  
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
    
    public Value toNegativeValue() {
      return neg(toValue());
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
    public boolean isNegative() {
      return value.isNegative();
    }
    
    @Override
    public boolean isReciprocal() {
      return !value.isInteger();
    }
    
    @Override
    public Value toNegativeValue() {
      return value.negative();
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
      return pow(base.toValue(), power.toNegativeValue());
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

  private static Value constAdd(Rational r, Value v) {
    if(r.equals(num(0))) {
      return v;
    } else {
      return add(r, v);
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
    
    @Override
    public Value toValue() {
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
      if(denom == null && coeff.denominator().equals(num(1))) {
        return constMul(coeff.numerator(), num);
      } else {
        return div(constMul(coeff.numerator(), num), constMul(coeff.denominator(), denom));
      }
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
      List<Value> negs = new LinkedList<Value>();
      for(Expr expr : terms) {
        if(expr.isNegative()) {
          negs.add(expr.toNegativeValue());
        } else {
          if(ret == null) {
            ret = expr.toValue();
          } else {
            ret = Expressions.add(ret, expr.toValue());
          }
        }
      }
      if(ret == null) {
        ret = neg(negs.remove(0));
      }
      for(Value v : negs) {
        ret = sub(ret, v);
      }
      return constAdd(offset, ret);
    }
  }
  
  private static Vector<Value> toValues(Vector<Expr> exprs) {
    return exprs.transform(new Transformer<Expr, Value>() {
      @Override
      public Value transform(Expr in) {
        return in.toValue();
      }
    });
  }
  
  private static Vector<Expr> toExprs(Vector<Value> values) {
    return values.transform(new Transformer<Value, Expr>() {
      @Override
      public Expr transform(Value in) {
        return new ValueExpr(in);
      }
    });
  }
  
  public static Definition simplify(Definition def) {
    return new Definition(def.args, def.values.transform(new Transformer<Value, Value>() {
      @Override
      public Value transform(Value in) {
        return in.accept(new Visitor<Expr>() {
          @Override
          public Vector<Expr> call(Visitor<Expr> v, Call call) {
            if(call.func == ADD) {
              Vector<Expr> args = call.visitArgs(v);
              return vector(args.get(0).exprAdd(args.get(1)));
            } else if(call.func == SUB) {
              Vector<Expr> args = call.visitArgs(v);
              return vector(args.get(0).exprAdd(args.get(1).exprNeg()));
            } else if(call.func == MUL) {
              Vector<Expr> args = call.visitArgs(v);
              return vector(args.get(0).exprMul(args.get(1)));
            } else if(call.func == DIV) {
              Vector<Expr> args = call.visitArgs(v);
              return vector(args.get(0).exprMul(args.get(1).exprRecip()));
            } else if(call.func == NEG) {
              Vector<Expr> args = call.visitArgs(v);
              return vector(args.get(0).exprNeg());
            } else if(call.func == POW) {
              Vector<Expr> args = call.visitArgs(v);
              return vector(args.get(0).exprPow(args.get(1)));
            } else {
              Vector<Expr> args = call.visitArgs(v);
              return toExprs(call.func.call(toValues(args)).selectAll());
            }
          }
          @Override
          public Expr constant(Rational cst) {
            return new ConstExpr(cst);
          }
          @Override
          public Expr variable(Variable var) {
            return new ValueExpr(var);
          }
        }).toValue();
      }
    }));
  }

}
