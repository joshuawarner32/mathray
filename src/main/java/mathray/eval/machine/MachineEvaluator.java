package mathray.eval.machine;

import mathray.Call;
import mathray.Multidef;
import mathray.Definition;
import mathray.FunctionSymbolRegistrar;
import mathray.NamedConstants;
import mathray.Rational;
import mathray.Symbol;
import mathray.eval.Impl;
import mathray.util.MathEx;
import mathray.util.Vector;
import mathray.visitor.Processor;

import static mathray.Functions.*;

public class MachineEvaluator extends FunctionSymbolRegistrar<Impl<Double>, Double> {
  
  private static final MachineEvaluator INSTANCE = new MachineEvaluator();
  
  {
    register(ADD, new Impl<Double>() {
      public Double call(Vector<Double> args) {
        return args.get(0) + args.get(1);
      }
    });
    register(SUB, new Impl<Double>() {
      public Double call(Vector<Double> args) {
        return args.get(0) - args.get(1);
      }
    });
    register(MUL, new Impl<Double>() {
      public Double call(Vector<Double> args) {
        return args.get(0) * args.get(1);
      }
    });
    register(DIV, new Impl<Double>() {
      public Double call(Vector<Double> args) {
        return args.get(0) / args.get(1);
      }
    });
    register(NEG, new Impl<Double>() {
      public Double call(Vector<Double> args) {
        return -args.get(0);
      }
    });
    register(POW, new Impl<Double>() {
      public Double call(Vector<Double> args) {
        return Math.pow(args.get(0), args.get(1));
      }
    });
    register(SIN, new Impl<Double>() {
      public Double call(Vector<Double> args) {
        return Math.sin(args.get(0));
      }
    });
    register(SINH, new Impl<Double>() {
      public Double call(Vector<Double> args) {
        return Math.sinh(args.get(0));
      }
    });
    register(ASIN, new Impl<Double>() {
      public Double call(Vector<Double> args) {
        return Math.asin(args.get(0));
      }
    });
    register(COS, new Impl<Double>() {
      public Double call(Vector<Double> args) {
        return Math.cos(args.get(0));
      }
    });
    register(COSH, new Impl<Double>() {
      public Double call(Vector<Double> args) {
        return Math.cosh(args.get(0));
      }
    });
    register(ACOS, new Impl<Double>() {
      public Double call(Vector<Double> args) {
        return Math.acos(args.get(0));
      }
    });
    register(TAN, new Impl<Double>() {
      public Double call(Vector<Double> args) {
        return Math.tan(args.get(0));
      }
    });
    register(TANH, new Impl<Double>() {
      public Double call(Vector<Double> args) {
        return Math.tanh(args.get(0));
      }
    });
    register(ATAN, new Impl<Double>() {
      public Double call(Vector<Double> args) {
        return Math.atan(args.get(0));
      }
    });
    register(ATAN2, new Impl<Double>() {
      public Double call(Vector<Double> args) {
        return Math.atan2(args.get(0), args.get(1));
      }
    });
    register(LOG, new Impl<Double>() {
      public Double call(Vector<Double> args) {
        return Math.log(args.get(0));
      }
    });
    register(SQRT, new Impl<Double>() {
      public Double call(Vector<Double> args) {
        return Math.sqrt(args.get(0));
      }
    });
    register(MIN, new Impl<Double>() {
      public Double call(Vector<Double> args) {
        return Math.min(args.get(0), args.get(1));
      }
    });
    register(MAX, new Impl<Double>() {
      public Double call(Vector<Double> args) {
        return Math.max(args.get(0), args.get(1));
      }
    });
    register(ABS, new Impl<Double>() {
      public Double call(Vector<Double> args) {
        return Math.abs(args.get(0));
      }
    });
    register(SELECT_SIGN, new Impl<Double>() {
      public Double call(Vector<Double> args) {
        return MathEx.selectSign(args.get(0), args.get(1), args.get(2));
      }
    });
    register(SELECT_EQUAL, new Impl<Double>() {
      public Double call(Vector<Double> args) {
        return MathEx.selectEqual(args.get(0), args.get(1), args.get(2), args.get(3));
      }
    });
    register(SELECT_INTEGER, new Impl<Double>() {
      public Double call(Vector<Double> args) {
        return MathEx.selectInteger(args.get(0), args.get(1), args.get(2), args.get(3));
      }
    });
    register(UP, new Impl<Double>() {
      public Double call(Vector<Double> args) {
        return Math.nextUp(args.get(0));
      }
    });
    register(DOWN, new Impl<Double>() {
      public Double call(Vector<Double> args) {
        return -Math.nextUp(-args.get(0));
      }
    });
    
    register(NamedConstants.TAU, MathEx.TAU);
    register(NamedConstants.PI, Math.PI);
    register(NamedConstants.E, Math.E);
    register(NamedConstants.NEG_INF, Double.NEGATIVE_INFINITY);
    register(NamedConstants.POS_INF, Double.POSITIVE_INFINITY);
    register(NamedConstants.UNDEF, Double.NaN);
  }
    
  public static Vector<Double> eval(final Multidef def, final Vector<Double> params) {
    return INSTANCE.transform(def, params);
  }
  
  public Vector<Double> transform(final Multidef def, final Vector<Double> params) {
    final Processor<Double> v = new Processor<Double>() {
      @Override
      public Double process(Call call, Vector<Double> args) {
        return lookup(call.func).call(args);
      }

      @Override
      public Double process(Symbol sym) {
        int ret = def.args.indexOf(sym);
        if(ret != -1) {
          return params.get(ret);
        } else {
          return lookup(sym);
        }
      }

      @Override
      public Double process(Rational rat) {
        return rat.toDouble();
      }
    };
    return def.acceptVector(v);
  }

  public static double eval(Definition def, Vector<Double> args) {
    return eval(def.toMultidef(), args).get(0);
  }

}
