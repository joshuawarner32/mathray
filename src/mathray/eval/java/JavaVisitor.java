package mathray.eval.java;

import java.util.HashMap;
import java.util.Map;
import org.objectweb.asm.Opcodes;

import mathray.Call;
import mathray.Constant;
import mathray.Function;
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
        public Vector<JavaValue> call(Function func, Vector<JavaValue> args) {
          return vector(mgen.binOp(Opcodes.DADD, args.get(0), args.get(1)));
        }
      })
      .register(SUB, new Impl<JavaValue>() {
        public Vector<JavaValue> call(Function func, Vector<JavaValue> args) {
          return vector(mgen.binOp(Opcodes.DSUB, args.get(0), args.get(1)));
        }
      })
      .register(MUL, new Impl<JavaValue>() {
        public Vector<JavaValue> call(Function func, Vector<JavaValue> args) {
          return vector(mgen.binOp(Opcodes.DMUL, args.get(0), args.get(1)));
        }
      })
      .register(DIV, new Impl<JavaValue>() {
        public Vector<JavaValue> call(Function func, Vector<JavaValue> args) {
          return vector(mgen.binOp(Opcodes.DDIV, args.get(0), args.get(1)));
        }
      })
      .register(SQRT, new Impl<JavaValue>() {
        public Vector<JavaValue> call(Function func, Vector<JavaValue> args) {
          return vector(mgen.callStatic("java/lang/Math", "sqrt", "(D)D", args.get(0)));
        }
      })
      .register(SIN, new Impl<JavaValue>() {
        public Vector<JavaValue> call(Function func, Vector<JavaValue> args) {
          return vector(mgen.callStatic("java/lang/Math", "sin", "(D)D", args.get(0)));
        }
      })
      .register(COS, new Impl<JavaValue>() {
        public Vector<JavaValue> call(Function func, Vector<JavaValue> args) {
          return vector(mgen.callStatic("java/lang/Math", "cos", "(D)D", args.get(0)));
        }
      })
      .register(POW, new Impl<JavaValue>() {
        public Vector<JavaValue> call(Function func, Vector<JavaValue> args) {
          return vector(mgen.callStatic("java/lang/Math", "pow", "(DD)D", args.get(0), args.get(1)));
        }
      })
      .register(MIN_MAX, new Impl<JavaValue>() {
        public Vector<JavaValue> call(Function func, Vector<JavaValue> args) {
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
    return env.implement(call.func).call(call.func, call.visitArgs(v));
  }

  @Override
  public JavaValue variable(Variable var) {
    throw new UnsupportedOperationException();
  }

  @Override
  public JavaValue constant(Constant cst) {
    return mgen.cst((double)cst.value);
  }

}
