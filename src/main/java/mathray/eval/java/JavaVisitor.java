package mathray.eval.java;

import org.objectweb.asm.Opcodes;

import mathray.Call;
import mathray.Rational;
import mathray.Variable;
import mathray.Vector;
import mathray.eval.Environment;
import mathray.eval.Visitor;
import mathray.eval.Impl;
import mathray.eval.java.ClassGenerator.MethodGenerator;

import static mathray.Functions.*;

import static mathray.Expressions.*;

public class JavaVisitor implements Visitor<JavaValue> {
  
  private Environment<JavaValue> env = 
    Environment.<JavaValue>builder()
      .register(ADD, new Impl<JavaValue>() {
        public Vector<JavaValue> call(Vector<JavaValue> args) {
          return vector(mgen.binOp(Opcodes.DADD, args.get(0), args.get(1)));
        }
      })
      .register(SUB, new Impl<JavaValue>() {
        public Vector<JavaValue> call(Vector<JavaValue> args) {
          return vector(mgen.binOp(Opcodes.DSUB, args.get(0), args.get(1)));
        }
      })
      .register(MUL, new Impl<JavaValue>() {
        public Vector<JavaValue> call(Vector<JavaValue> args) {
          return vector(mgen.binOp(Opcodes.DMUL, args.get(0), args.get(1)));
        }
      })
      .register(POW, new Impl<JavaValue>() {
        public Vector<JavaValue> call(Vector<JavaValue> args) {
          return vector(mgen.callStatic("java/lang/Math", "pow", "(DD)D", args.get(0), args.get(1)));
        }
      })
      .register(DIV, new Impl<JavaValue>() {
        public Vector<JavaValue> call(Vector<JavaValue> args) {
          return vector(mgen.binOp(Opcodes.DDIV, args.get(0), args.get(1)));
        }
      })
      .register(NEG, new Impl<JavaValue>() {
        public Vector<JavaValue> call(Vector<JavaValue> args) {
          return vector(mgen.unaryOp(Opcodes.DNEG, args.get(0)));
        }
      })
      .register(SQRT, new Impl<JavaValue>() {
        public Vector<JavaValue> call(Vector<JavaValue> args) {
          return vector(mgen.callStatic("java/lang/Math", "sqrt", "(D)D", args.get(0)));
        }
      })
      .register(LOG, new Impl<JavaValue>() {
        public Vector<JavaValue> call(Vector<JavaValue> args) {
          return vector(mgen.callStatic("java/lang/Math", "log", "(D)D", args.get(0)));
        }
      })
      .register(SIN, new Impl<JavaValue>() {
        public Vector<JavaValue> call(Vector<JavaValue> args) {
          return vector(mgen.callStatic("java/lang/Math", "sin", "(D)D", args.get(0)));
        }
      })
      .register(SINH, new Impl<JavaValue>() {
        public Vector<JavaValue> call(Vector<JavaValue> args) {
          return vector(mgen.callStatic("java/lang/Math", "sinh", "(D)D", args.get(0)));
        }
      })
      .register(COS, new Impl<JavaValue>() {
        public Vector<JavaValue> call(Vector<JavaValue> args) {
          return vector(mgen.callStatic("java/lang/Math", "cos", "(D)D", args.get(0)));
        }
      })
      .register(COSH, new Impl<JavaValue>() {
        public Vector<JavaValue> call(Vector<JavaValue> args) {
          return vector(mgen.callStatic("java/lang/Math", "cosh", "(D)D", args.get(0)));
        }
      })
      .register(TAN, new Impl<JavaValue>() {
        public Vector<JavaValue> call(Vector<JavaValue> args) {
          return vector(mgen.callStatic("java/lang/Math", "tan", "(D)D", args.get(0)));
        }
      })
      .register(TANH, new Impl<JavaValue>() {
        public Vector<JavaValue> call(Vector<JavaValue> args) {
          return vector(mgen.callStatic("java/lang/Math", "tanh", "(D)D", args.get(0)));
        }
      })
      .register(ABS, new Impl<JavaValue>() {
        public Vector<JavaValue> call(Vector<JavaValue> args) {
          return vector(mgen.callStatic("java/lang/Math", "abs", "(D)D", args.get(0)));
        }
      })
      .register(UP, new Impl<JavaValue>() {
        public Vector<JavaValue> call(Vector<JavaValue> args) {
          return vector(mgen.callStatic("java/lang/Math", "nextUp", "(D)D", args.get(0)));
        }
      })
      .register(DOWN, new Impl<JavaValue>() {
        public Vector<JavaValue> call(Vector<JavaValue> args) {
          JavaValue neg = mgen.unaryOp(Opcodes.DNEG, args.get(0));
          JavaValue v = mgen.callStatic("java/lang/Math", "nextUp", "(D)D", neg);
          return vector(mgen.unaryOp(Opcodes.DNEG, v));
        }
      })
      .register(MIN_MAX, new Impl<JavaValue>() {
        public Vector<JavaValue> call(Vector<JavaValue> args) {
          return vector(
            mgen.callStatic("java/lang/Math", "min", "(DD)D", args.get(0), args.get(1)),
            mgen.callStatic("java/lang/Math", "max", "(DD)D", args.get(0), args.get(1)));
        }
      })
      .build();
  
  private MethodGenerator mgen;
  
  public JavaVisitor(MethodGenerator mgen) {
    this.mgen = mgen;
  }

  @Override
  public Vector<JavaValue> call(Visitor<JavaValue> v, Call call) {
    return env.implement(call.func).call(call.visitArgs(v));
  }

  @Override
  public JavaValue variable(Variable var) {
    throw new UnsupportedOperationException();
  }

  @Override
  public JavaValue constant(Rational r) {
    return mgen.cst(r.toDouble());
  }

}
