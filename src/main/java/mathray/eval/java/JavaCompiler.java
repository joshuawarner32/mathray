package mathray.eval.java;

import org.objectweb.asm.Opcodes;

import mathray.Call;
import mathray.Computation;
import mathray.FunctionD;
import mathray.Rational;
import mathray.Symbol;
import mathray.Vector;
import mathray.eval.Environment;
import mathray.eval.Impl;
import mathray.eval.Visitor;
import mathray.eval.java.ClassGenerator.MethodGenerator;

import static mathray.Functions.*;

public class JavaCompiler {
  
  public static final PrologEpilog FUNCTION_D = new PrologEpilog() {
    
    @Override
    public MethodGenerator begin(ClassGenerator gen, Computation comp) {
      return gen.method(false, "call", "([D[D)V");
    }
    
    @Override
    public JavaValue loadParam(MethodGenerator mgen, int index) {
      return mgen.aload(mgen.arg(0), index);
    }
    
    @Override
    public void storeRet(MethodGenerator mgen, int index, JavaValue value) {
      mgen.astore(mgen.arg(1), index, value);
    }
    
    @Override
    public void end(MethodGenerator mgen) {
      mgen.ret();
    }
  };
  
  private static Impl<JavaValue> staticCall1(final MethodGenerator mgen, final String className, final String name) {
    return new Impl<JavaValue>() {
      @Override
      public JavaValue call(Vector<JavaValue> args) {
        return mgen.callStatic(className, name, JavaArityGenerator.getArityDesc(1), args.get(0));
      }
    };
  }
  
  private static Impl<JavaValue> staticCall2(final MethodGenerator mgen, final String className, final String name) {
    return new Impl<JavaValue>() {
      @Override
      public JavaValue call(Vector<JavaValue> args) {
        return mgen.callStatic(className, name, JavaArityGenerator.getArityDesc(2), args.get(0), args.get(1));
      }
    };
  }
  
  private static Impl<JavaValue> binOp(final MethodGenerator mgen, final int op) {
    return new Impl<JavaValue>() {
      @Override
      public JavaValue call(Vector<JavaValue> args) {
        return mgen.binOp(op, args.get(0), args.get(1));
      }
    };
  }
  
  public static FunctionD compile(final Computation comp) {
    
    PrologEpilog ctx = FUNCTION_D;
    
    ClassGenerator gen = new ClassGenerator(JavaArityGenerator.CLASS_NAME, new String[] {FunctionD.class.getName().replace('.', '/')});
    final MethodGenerator mgen = ctx.begin(gen, comp);
    final JavaValue[] args = new JavaValue[comp.args.size()];
    for(int i = 0; i < args.length; i++) {
      args[i] = ctx.loadParam(mgen, i);
    }
    Visitor<JavaValue> v = new Visitor<JavaValue>() {
      
      private Environment<JavaValue> env = 
        Environment.<JavaValue>builder()
          .register(ADD, binOp(mgen, Opcodes.DADD))
          .register(SUB, binOp(mgen, Opcodes.DSUB))
          .register(MUL, binOp(mgen, Opcodes.DMUL))
          .register(DIV, binOp(mgen, Opcodes.DDIV))
          .register(SQRT, staticCall1(mgen, "java/lang/Math", "sqrt"))
          .register(SIN, staticCall1(mgen, "java/lang/Math", "sin"))
          .register(SINH, staticCall1(mgen, "java/lang/Math", "sinh"))
          .register(ASIN, staticCall1(mgen, "java/lang/Math", "asin"))
          .register(COS, staticCall1(mgen, "java/lang/Math", "cos"))
          .register(COSH, staticCall1(mgen, "java/lang/Math", "cosh"))
          .register(ACOS, staticCall1(mgen, "java/lang/Math", "acos"))
          .register(TAN, staticCall1(mgen, "java/lang/Math", "tan"))
          .register(TANH, staticCall1(mgen, "java/lang/Math", "tanh"))
          .register(ATAN, staticCall1(mgen, "java/lang/Math", "atan"))
          .register(LOG, staticCall1(mgen, "java/lang/Math", "log"))
          .register(POW, staticCall2(mgen, "java/lang/Math", "pow"))
          .register(MIN, staticCall2(mgen, "java/lang/Math", "min"))
          .register(MAX, staticCall2(mgen, "java/lang/Math", "max"))
          .register(UP, staticCall1(mgen, "java/lang/Math", "nextUp"))
          .register(DOWN, new Impl<JavaValue>() {
            @Override
            public JavaValue call(Vector<JavaValue> args) {
              return mgen.unaryOp(Opcodes.DNEG, mgen.callStatic("java/lang/Math", "nextUp", "(D)D", mgen.unaryOp(Opcodes.DNEG, args.get(0))));
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
      ctx.storeRet(mgen, i, comp.values.get(i).accept(v));
    }
    ctx.end(mgen);
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
