package mathray.eval.java;

import org.objectweb.asm.Opcodes;

import mathray.Call;
import mathray.Computation;
import mathray.Definition;
import mathray.FunctionD;
import mathray.Rational;
import mathray.Symbol;
import mathray.Vector;
import mathray.eval.Environment;
import mathray.eval.Impl;
import mathray.eval.Visitor;
import mathray.eval.java.ClassGenerator.MethodGenerator;

import static mathray.Expressions.*;
import static mathray.Functions.*;

public class JavaCompiler {
  
  public static FunctionD compile(final Computation comp) {
    
    ClassGenerator gen = new ClassGenerator(JavaArityGenerator.CLASS_NAME, new String[] {FunctionD.class.getName().replace('.', '/')});
    final MethodGenerator mgen = gen.method(false, "call", "([D[D)V");
    final JavaValue[] args = new JavaValue[comp.args.size()];
    for(int i = 0; i < args.length; i++) {
      args[i] = mgen.aload(mgen.arg(0), i);
    }
    Visitor<JavaValue> v = new Visitor<JavaValue>() {
      
      private Environment<JavaValue> env = 
        Environment.<JavaValue>builder()
          .register(ADD, new Impl<JavaValue>() {
            public JavaValue call(Vector<JavaValue> args) {
              return mgen.binOp(Opcodes.DADD, args.get(0), args.get(1));
            }
          })
          .register(SUB, new Impl<JavaValue>() {
            public JavaValue call(Vector<JavaValue> args) {
              return mgen.binOp(Opcodes.DSUB, args.get(0), args.get(1));
            }
          })
          .register(MUL, new Impl<JavaValue>() {
            public JavaValue call(Vector<JavaValue> args) {
              return mgen.binOp(Opcodes.DMUL, args.get(0), args.get(1));
            }
          })
          .register(DIV, new Impl<JavaValue>() {
            public JavaValue call(Vector<JavaValue> args) {
              return mgen.binOp(Opcodes.DDIV, args.get(0), args.get(1));
            }
          })
          .register(SQRT, new Impl<JavaValue>() {
            public JavaValue call(Vector<JavaValue> args) {
              return mgen.callStatic("java/lang/Math", "sqrt", "(D)D", args.get(0));
            }
          })
          .register(SIN, new Impl<JavaValue>() {
            public JavaValue call(Vector<JavaValue> args) {
              return mgen.callStatic("java/lang/Math", "sin", "(D)D", args.get(0));
            }
          })
          .register(COS, new Impl<JavaValue>() {
            public JavaValue call(Vector<JavaValue> args) {
              return mgen.callStatic("java/lang/Math", "cos", "(D)D", args.get(0));
            }
          })
          .register(POW, new Impl<JavaValue>() {
            public JavaValue call(Vector<JavaValue> args) {
              return mgen.callStatic("java/lang/Math", "pow", "(DD)D", args.get(0), args.get(1));
            }
          })
          .register(MIN, new Impl<JavaValue>() {
            public JavaValue call(Vector<JavaValue> args) {
              return mgen.callStatic("java/lang/Math", "min", "(DD)D", args.get(0), args.get(1));
            }
          })
          .register(MAX, new Impl<JavaValue>() {
            public JavaValue call(Vector<JavaValue> args) {
              return mgen.callStatic("java/lang/Math", "max", "(DD)D", args.get(0), args.get(1));
            }
          })
          .build();

      @Override
      public JavaValue call(Call call) {
        return env.implement(call.func).call(call.visitArgs(this));
      }

      @Override
      public JavaValue symbol(Symbol sym) {
        return args[comp.args.getIndex(sym)];
      }

      @Override
      public JavaValue constant(Rational r) {
        return mgen.cst(r.toDouble());
      }

    };
    for(int i = 0; i < comp.values.size(); i++) {
      mgen.astore(mgen.arg(1), i, comp.values.get(i).accept(v));
    }
    mgen.ret();
    mgen.end();
    gen.end();
    
    try {
      return (FunctionD)gen.load().newInstance();
    } catch (InstantiationException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

}
