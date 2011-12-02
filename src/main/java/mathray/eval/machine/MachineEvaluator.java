package mathray.eval.machine;

import mathray.Computation;
import mathray.Definition;
import mathray.Rational;
import mathray.Vector;
import mathray.eval.Context;
import mathray.eval.Environment;
import mathray.eval.Translator;
import mathray.eval.Impl;

import static mathray.Functions.*;

public class MachineEvaluator {
  
  private static final Environment<Double> env =
    Environment.<Double>builder()
      .register(ADD, new Impl<Double>() {
        public Double call(Vector<Double> args) {
          return args.get(0) + args.get(1);
        }
      })
      .register(SUB, new Impl<Double>() {
        public Double call(Vector<Double> args) {
          return args.get(0) - args.get(1);
        }
      })
      .register(MUL, new Impl<Double>() {
        public Double call(Vector<Double> args) {
          return args.get(0) * args.get(1);
        }
      })
      .register(DIV, new Impl<Double>() {
        public Double call(Vector<Double> args) {
          return args.get(0) / args.get(1);
        }
      })
      .register(NEG, new Impl<Double>() {
        public Double call(Vector<Double> args) {
          return -args.get(0);
        }
      })
      .register(POW, new Impl<Double>() {
        public Double call(Vector<Double> args) {
          return Math.pow(args.get(0), args.get(1));
        }
      })
      .register(SIN, new Impl<Double>() {
        public Double call(Vector<Double> args) {
          return Math.sin(args.get(0));
        }
      })
      .register(SINH, new Impl<Double>() {
        public Double call(Vector<Double> args) {
          return Math.sinh(args.get(0));
        }
      })
      .register(COS, new Impl<Double>() {
        public Double call(Vector<Double> args) {
          return Math.cos(args.get(0));
        }
      })
      .register(COSH, new Impl<Double>() {
        public Double call(Vector<Double> args) {
          return Math.cosh(args.get(0));
        }
      })
      .register(TAN, new Impl<Double>() {
        public Double call(Vector<Double> args) {
          return Math.tan(args.get(0));
        }
      })
      .register(TANH, new Impl<Double>() {
        public Double call(Vector<Double> args) {
          return Math.tanh(args.get(0));
        }
      })
      .register(LOG, new Impl<Double>() {
        public Double call(Vector<Double> args) {
          return Math.log(args.get(0));
        }
      })
      .register(SQRT, new Impl<Double>() {
        public Double call(Vector<Double> args) {
          return Math.sqrt(args.get(0));
        }
      })
      .register(MIN, new Impl<Double>() {
        public Double call(Vector<Double> args) {
          return Math.min(args.get(0), args.get(1));
        }
      })
      .register(MAX, new Impl<Double>() {
        public Double call(Vector<Double> args) {
          return Math.max(args.get(0), args.get(1));
        }
      })
      .register(ABS, new Impl<Double>() {
        public Double call(Vector<Double> args) {
          return Math.abs(args.get(0));
        }
      })
      .register(SELECT_SIGN, new Impl<Double>() {
        public Double call(Vector<Double> args) {
          double test = args.get(0);
          if(test < 0) {
            return args.get(1);
          } else if(test > 0) {
            return args.get(3);
          } else {
            return args.get(2);
          }
        }
      })
      .register(UP, new Impl<Double>() {
        public Double call(Vector<Double> args) {
          return Math.nextUp(args.get(0));
        }
      })
      .register(DOWN, new Impl<Double>() {
        public Double call(Vector<Double> args) {
          return -Math.nextUp(-args.get(0));
        }
      })
      .build();
  
  public static Vector<Double> eval(Computation comp, Vector<Double> params) {
    Context<Double> ctx = new Context<Double>(comp.args.<Double>bind(params), env, new Translator<Double>() {
      public Double translate(Rational r) {
        return r.toDouble();
      }
    });
    return ctx.run(comp.values);
  }

  public static double eval(Definition def, Vector<Double> args) {
    return eval(def.toComputation(), args).get(0);
  }

}
