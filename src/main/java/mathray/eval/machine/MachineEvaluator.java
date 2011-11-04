package mathray.eval.machine;

import mathray.Rational;
import mathray.Definition;
import mathray.Vector;
import mathray.eval.Context;
import mathray.eval.Environment;
import mathray.eval.Translator;
import mathray.eval.Impl;

import static mathray.Functions.*;
import static mathray.Expressions.*;

public class MachineEvaluator {
  
  private static final Environment<Double> env =
    Environment.<Double>builder()
      .register(ADD, new Impl<Double>() {
        public Vector<Double> call(Vector<Double> args) {
          return vector(args.get(0) + args.get(1));
        }
      })
      .register(SUB, new Impl<Double>() {
        public Vector<Double> call(Vector<Double> args) {
          return vector(args.get(0) - args.get(1));
        }
      })
      .register(MUL, new Impl<Double>() {
        public Vector<Double> call(Vector<Double> args) {
          return vector(args.get(0) * args.get(1));
        }
      })
      .register(DIV, new Impl<Double>() {
        public Vector<Double> call(Vector<Double> args) {
          return vector(args.get(0) / args.get(1));
        }
      })
      .register(NEG, new Impl<Double>() {
        public Vector<Double> call(Vector<Double> args) {
          return vector(-args.get(0));
        }
      })
      .register(POW, new Impl<Double>() {
        public Vector<Double> call(Vector<Double> args) {
          return vector(Math.pow(args.get(0), args.get(1)));
        }
      })
      .register(SIN, new Impl<Double>() {
        public Vector<Double> call(Vector<Double> args) {
          return vector(Math.sin(args.get(0)));
        }
      })
      .register(COS, new Impl<Double>() {
        public Vector<Double> call(Vector<Double> args) {
          return vector(Math.cos(args.get(0)));
        }
      })
      .register(TAN, new Impl<Double>() {
        public Vector<Double> call(Vector<Double> args) {
          return vector(Math.tan(args.get(0)));
        }
      })
      .register(LOG, new Impl<Double>() {
        public Vector<Double> call(Vector<Double> args) {
          return vector(Math.log(args.get(0)));
        }
      })
      .register(MIN_MAX, new Impl<Double>() {
        public Vector<Double> call(Vector<Double> args) {
          double a = args.get(0);
          double b = args.get(1);
          if(a > b) {
            return vector(b, a);
          } else {
            return vector(a, b);
          }
        }
      })
      .register(ABS, new Impl<Double>() {
        public Vector<Double> call(Vector<Double> args) {
          return vector(Math.abs(args.get(0)));
        }
      })
      .register(SQRT, new Impl<Double>() {
        public Vector<Double> call(Vector<Double> args) {
          return vector(Math.sqrt(args.get(0)));
        }
      })
      .build();
  
  public static Vector<Double> eval(Definition def, Vector<Double> params) {
    Context<Double> ctx = new Context<Double>(def.args.<Double>bind(params), env, new Translator<Double>() {
      public Double translate(Rational r) {
        return r.toDouble();
      }
    });
    return ctx.run(def.values);
  }
  
}
