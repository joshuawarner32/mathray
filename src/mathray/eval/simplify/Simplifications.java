package mathray.eval.simplify;

import mathray.Call;
import mathray.Constant;
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
    
    public Expr mul(Expr expr) {
      if(expr instanceof ProductExpr) {
        return expr.mul(this);
      } else if(expr instanceof ConstExpr) {
        return new ProductExpr(new Vector<Expr>(this), Vector.<Expr>empty(), ((ConstExpr)expr).value, Constant.get(1));
      }
      return new ProductExpr(new Vector<Expr>(this, expr), Vector.<Expr>empty(), Constant.get(1), Constant.get(1));
    }
    
    public Expr add(Expr expr) {
      if(expr instanceof SumExpr) {
        return expr.add(this);
      } else if(expr instanceof ConstExpr) {
        return new SumExpr(new Vector<Expr>(this), Vector.<Expr>empty(), Constant.get(0));
      }
      return new SumExpr(new Vector<Expr>(this, expr), Vector.<Expr>empty(), Constant.get(0));
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
    private final Constant value;
    
    public ConstExpr(Constant value) {
      this.value = value;
    }
    
    @Override
    public Expr mul(Expr expr) {
      if(expr instanceof ConstExpr) {
        return new ConstExpr(value.mul(((ConstExpr)expr).value));
      }
      return new ProductExpr(new Vector<Expr>(expr), Vector.<Expr>empty(), value, Constant.get(1));
    }

    @Override
    public Expr add(Expr expr) {
      if(expr instanceof ConstExpr) {
        return new ConstExpr(value.add(((ConstExpr)expr).value));
      } else {
        return new SumExpr(new Vector<Expr>(expr), Vector.<Expr>empty(), value);
      }
    }
    
    @Override
    public Value toValue() {
      return value;
    }
  }
  
  private static class ProductExpr extends Expr {
    private final Vector<Expr> num;
    private final Vector<Expr> denom;
    private final Constant coeffNum;
    private final Constant coeffDenom;
    
    private ProductExpr(Vector<Expr> num, Vector<Expr> denom, Constant coeffNum, Constant coeffDenom) {
      this.num = num;
      this.denom = denom;
      this.coeffNum = coeffNum;
      this.coeffDenom = coeffDenom;
    }

    @Override
    public Expr mul(Expr expr) {
      if(expr instanceof ConstExpr) {
        return new ProductExpr(num, denom, coeffNum.mul(((ConstExpr)expr).value), coeffDenom);
      } else if(expr instanceof ProductExpr) {
        ProductExpr prod = (ProductExpr)expr;
        return new ProductExpr(num.concat(prod.num), denom.concat(prod.denom), coeffNum.mul(prod.coeffNum), coeffDenom.mul(prod.coeffDenom));
      } else {
        throw new RuntimeException("unhandled case");
      }
    }
    
    @Override
    public Value toValue() {
      if(coeffNum.equals(Constant.get(0))) {
        return Constant.get(0);
      } else {
        Value ret = fold(DIV, fold(MUL, toValues(num)), toValues(denom));
        if(coeffNum.equals(coeffDenom)) {
          return ret;
        } else {
          return Expressions.mul(div(coeffNum, coeffDenom), ret);
        }
      }
    }
  }
  
  private static class SumExpr extends Expr {
    private final Vector<Expr> pos;
    private final Vector<Expr> neg;
    private final Constant offset;
    
    private SumExpr(Vector<Expr> pos, Vector<Expr> neg, Constant offset) {
      this.pos = pos;
      this.neg = neg;
      this.offset = offset;
    }

    @Override
    public Expr add(Expr expr) {
      if(expr instanceof ConstExpr) {
        return new SumExpr(pos, neg, offset.add(((ConstExpr)expr).value));
      } else if(expr instanceof ProductExpr) {
        SumExpr sum = (SumExpr)expr;
        return new SumExpr(pos.concat(sum.pos), neg.concat(sum.neg), offset.add(sum.offset));
      } else {
        throw new RuntimeException("unhandled case");
      }
    }
    
    @Override
    public Value toValue() {
      Value ret = fold(SUB, fold(ADD, toValues(pos)), toValues(neg));
      if(offset.equals(Constant.get(0))) {
        return ret;
      } else {
        return Expressions.add(offset, ret);
      }
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
              return vector(args.get(0).add(args.get(1)));
            } else if(call.func == MUL) {
              Vector<Expr> args = call.visitArgs(v);
              return vector(args.get(0).mul(args.get(1)));
            } else {
              return toExprs(call.selectAll());
            }
          }
          @Override
          public Expr constant(Constant cst) {
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
