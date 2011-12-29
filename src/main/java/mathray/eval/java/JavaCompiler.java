package mathray.eval.java;


import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;


import mathray.Call;
import mathray.Computation;
import mathray.FunctionRegistrar;
import mathray.Rational;
import mathray.Symbol;
import mathray.concrete.FunctionTypes;
import mathray.concrete.FunctionTypes.IntervalOnRayD3;
import mathray.concrete.VectorD3;
import mathray.eval.java.ClassGenerator.MethodGenerator;
import mathray.util.MathEx;
import mathray.util.Vector;
import mathray.visitor.EvaluatingVisitor;

import static mathray.Functions.*;

public class JavaCompiler extends FunctionRegistrar<JavaImpl> {
  
  {

    register(ADD, binOp(Opcodes.DADD));
    register(SUB, binOp(Opcodes.DSUB));
    register(MUL, binOp(Opcodes.DMUL));
    register(DIV, binOp(Opcodes.DDIV));
    register(SQRT, staticCall1("java/lang/Math", "sqrt"));
    register(SIN, staticCall1("java/lang/Math", "sin"));
    register(SINH, staticCall1("java/lang/Math", "sinh"));
    register(ASIN, staticCall1("java/lang/Math", "asin"));
    register(COS, staticCall1("java/lang/Math", "cos"));
    register(COSH, staticCall1("java/lang/Math", "cosh"));
    register(ACOS, staticCall1("java/lang/Math", "acos"));
    register(TAN, staticCall1("java/lang/Math", "tan"));
    register(TANH, staticCall1( "java/lang/Math", "tanh"));
    register(ATAN, staticCall1("java/lang/Math", "atan"));
    register(LOG, staticCall1("java/lang/Math", "log"));
    register(POW, staticCall2("java/lang/Math", "pow"));
    register(MIN, staticCall2("java/lang/Math", "min"));
    register(MAX, staticCall2("java/lang/Math", "max"));
    register(UP, staticCall1("java/lang/Math", "nextUp"));
    register(DOWN, new JavaImpl() {
      @Override
      public JavaValue call(MethodGenerator mgen, Vector<JavaValue> args) {
        return mgen.unaryOp(Opcodes.DNEG, mgen.callStatic("java/lang/Math", "nextUp", "(D)D", mgen.unaryOp(Opcodes.DNEG, args.get(0))));
      }
    });
    register(SELECT_SIGN, new JavaImpl() {
      @Override
      public JavaValue call(MethodGenerator mgen, Vector<JavaValue> args) {
        return mgen.callStatic(MathEx.class.getName().replace('.', '/'), "selectSign", "(DDDD)D", args.get(0), args.get(1), args.get(2), args.get(3));
      }
    });
  }
  
  public static final FunctionGenerator<FunctionTypes.D> FUNCTION_D = new FunctionGenerator<FunctionTypes.D>() {
    public Wrapper<FunctionTypes.D> begin(Computation comp) {
      final ClassGenerator gen = new ClassGenerator(JavaArityGenerator.CLASS_NAME, new String[] {FunctionTypes.D.class.getName().replace('.', '/')});
      final MethodGenerator mgen = gen.method(false, "call", "([D[D)V");
      return new Wrapper<FunctionTypes.D>() {
        
        @Override
        public MethodGenerator getMethodGenerator() {
          return mgen;
        }
        
        @Override
        public JavaValue arg(int index) {
          return mgen.aload(mgen.arg(0), index);
        }
        
        @Override
        public void ret(int index, JavaValue value) {
          mgen.astore(mgen.arg(1), index, value);
        }
        
        @Override
        public FunctionTypes.D end() {
          mgen.ret();
          mgen.end();
          gen.end();
          return load(gen);
        }
      };
    }
  };
  
  public static final FunctionGenerator<FunctionTypes.ZeroOnRayD3> MAYBE_ZERO_ON_RAY3 = new FunctionGenerator<FunctionTypes.ZeroOnRayD3>() {
    final ClassGenerator gen = new ClassGenerator(JavaArityGenerator.CLASS_NAME, new String[] {FunctionTypes.ZeroOnRayD3.class.getName().replace('.', '/')});
    final MethodGenerator mgen = gen.method(false, "maybeHasZeroOn", "(L" + VectorD3.class.getName().replace('.', '/') + ";)Z");
    final ComputedValue aVar = mgen.allocateLocal(Type.DOUBLE_TYPE);
    final ComputedValue bVar = mgen.allocateLocal(Type.DOUBLE_TYPE);
    public Wrapper<FunctionTypes.ZeroOnRayD3> begin(Computation comp) {
      return new Wrapper<FunctionTypes.ZeroOnRayD3>() {
        
        @Override
        public MethodGenerator getMethodGenerator() {
          return mgen;
        }
        
        private JavaValue field(MethodGenerator mgen, String name) {
          return mgen.loadField(mgen.arg(1), VectorD3.class.getName(), name, "D");
        }
        
        @Override
        public JavaValue arg(int index) {
          switch(index) {
          case 0:
            return field(mgen, "x");
          case 1:
            return field(mgen, "y");
          case 2:
            return field(mgen, "z");
          case 3:
            return mgen.binOp(Opcodes.DADD, field(mgen, "x"), field(mgen, "dx"));
          case 4:
            return mgen.binOp(Opcodes.DADD, field(mgen, "y"), field(mgen, "dy"));
          case 5:
            return mgen.binOp(Opcodes.DADD, field(mgen, "z"), field(mgen, "dz"));
          default:
            throw new RuntimeException("shouldn't happen");
          }
        }
        
        @Override
        public void ret(int index, JavaValue value) {
          switch(index) {
          case 0:
            mgen.store(value, aVar);
          case 1:
            mgen.store(value, bVar);
          default:
            throw new RuntimeException("shouldn't happen");
          }
        }
        
        @Override
        public FunctionTypes.ZeroOnRayD3 end() {
          mgen.ret(mgen.callStatic(MathEx.class.getName().replace('.', '/'), "containsZero", "(DD)Z", aVar, bVar));
          mgen.end();
          gen.end();
          return load(gen);
        }
      };
    }
  };
  
  private static <T> FunctionGenerator<T> Dn_1(final int n) {
    return new FunctionGenerator<T>() {
      final ClassGenerator gen = new ClassGenerator(JavaArityGenerator.CLASS_NAME, new String[] {FunctionTypes.class.getName().replace('.', '/') + "$D" + n});
      final MethodGenerator mgen = gen.method(false, "call", JavaArityGenerator.getArityDesc(n));
      public Wrapper<T> begin(Computation comp) {
        return new Wrapper<T>() {
          @Override
          public MethodGenerator getMethodGenerator() {
            return mgen;
          }
          
          @Override
          public JavaValue arg(int index) {
            if(index < n) {
              return mgen.arg(index * 2);
            } else {
              throw new RuntimeException("shouldn't happen");
            }
          }
          
          @Override
          public void ret(int index, JavaValue value) {
            if(index == 0) {
              mgen.ret(value);
            } else {
              throw new RuntimeException("shouldn't happen");
            }
          }
          
          @Override
          public T end() {
            mgen.end();
            gen.end();
            return load(gen);
          }
        };
      }
    };
  }
  
  public static final FunctionGenerator<FunctionTypes.D1_1> D1_1 = Dn_1(1); 
  public static final FunctionGenerator<FunctionTypes.D2_1> D2_1 = Dn_1(2); 
  public static final FunctionGenerator<FunctionTypes.D3_1> D3_1 = Dn_1(3); 
  
  private static JavaImpl staticCall1(final String className, final String name) {
    return new JavaImpl() {
      @Override
      public JavaValue call(MethodGenerator mgen, Vector<JavaValue> args) {
        return mgen.callStatic(className, name, JavaArityGenerator.getArityDesc(1), args.get(0));
      }
    };
  }
  
  private static JavaImpl staticCall2(final String className, final String name) {
    return new JavaImpl() {
      @Override
      public JavaValue call(MethodGenerator mgen, Vector<JavaValue> args) {
        return mgen.callStatic(className, name, JavaArityGenerator.getArityDesc(2), args.get(0), args.get(1));
      }
    };
  }
  
  private static JavaImpl binOp(final int op) {
    return new JavaImpl() {
      @Override
      public JavaValue call(MethodGenerator mgen, Vector<JavaValue> args) {
        return mgen.binOp(op, args.get(0), args.get(1));
      }
    };
  }
  
  private static final JavaCompiler INSTANCE = new JavaCompiler();
  
  public static FunctionTypes.D compile(final Computation comp) {
    return INSTANCE.transform(FUNCTION_D, comp);
  }
  
  public static <T> T compile(FunctionGenerator<T> ctx, final Computation comp) {
    return INSTANCE.transform(ctx, comp);
  }
  
  @SuppressWarnings("unchecked")
  private static <T> T load(ClassGenerator gen) {
    try {
      Class<?> cls = gen.load();
      return (T)cls.newInstance();
    } catch (InstantiationException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
  
  public <T> T transform(FunctionGenerator<T> ctx, final Computation comp) {
    Wrapper<T> wrap = ctx.begin(comp);
    
    final MethodGenerator mgen = wrap.getMethodGenerator();
    final JavaValue[] args = new JavaValue[comp.args.size()];
    for(int i = 0; i < args.length; i++) {
      args[i] = wrap.arg(i);
    }
    final Usage usage = Usage.generate(comp);
    EvaluatingVisitor<JavaValue> v = new EvaluatingVisitor<JavaValue>() {
      @Override
      public JavaValue call(Call call, Vector<JavaValue> args) {
        JavaValue ret = lookup(call.func).call(mgen, args);
        if(usage.get(call) > 1) {
          mgen.store(ret);
        }
        return ret;
      }

      @Override
      public JavaValue symbol(Symbol sym) {
        return args[comp.args.getIndex(sym)];
      }

      @Override
      public JavaValue constant(Rational r) {
        return mgen.doubleConstant(r.toDouble());
      }

    };
    int i = 0;
    for(JavaValue val : comp.accept(v)) {
      wrap.ret(i++, val);
    }
    return wrap.end();
  }

}
