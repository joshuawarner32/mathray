package mathray.eval.java;


import org.objectweb.asm.Opcodes;

import org.objectweb.asm.Type;

import mathray.Call;
import mathray.Closure;
import mathray.Multidef;
import mathray.FunctionSymbolRegistrar;
import mathray.NamedConstants;
import mathray.Rational;
import mathray.Symbol;
import mathray.concrete.BlockD3;
import mathray.concrete.RayD3;
import mathray.device.FunctionTypes;
import mathray.util.MathEx;
import mathray.util.Vector;
import mathray.visitor.Processor;

import static mathray.Functions.*;

public class JavaDevice extends FunctionSymbolRegistrar<JavaImpl, Double> {
  
  {
    
    register(NamedConstants.TAU, MathEx.TAU);
    register(NamedConstants.PI, Math.PI);
    register(NamedConstants.E, Math.E);
    register(NamedConstants.NEG_INF, Double.NEGATIVE_INFINITY);
    register(NamedConstants.POS_INF, Double.POSITIVE_INFINITY);
    register(NamedConstants.UNDEF, Double.NaN);

    register(ADD, binOp(Opcodes.DADD));
    register(SUB, binOp(Opcodes.DSUB));
    register(MUL, binOp(Opcodes.DMUL));
    register(DIV, binOp(Opcodes.DDIV));
    register(NEG, unaryOp(Opcodes.DNEG));
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
        return mgen.callStatic(MathEx.class.getName().replace('.', '/'), "selectSign", "(DDD)D", args.get(0), args.get(1), args.get(2));
      }
    });
    register(SELECT_EQUAL, new JavaImpl() {
      @Override
      public JavaValue call(MethodGenerator mgen, Vector<JavaValue> args) {
        return mgen.callStatic(MathEx.class.getName().replace('.', '/'), "selectEqual", "(DDDD)D", args.get(0), args.get(1), args.get(2), args.get(3));
      }
    });
    register(SELECT_INTEGER, new JavaImpl() {
      @Override
      public JavaValue call(MethodGenerator mgen, Vector<JavaValue> args) {
        return mgen.callStatic(MathEx.class.getName().replace('.', '/'), "selectInteger", "(DDDD)D", args.get(0), args.get(1), args.get(2), args.get(3));
      }
    });
  }
  
  private static String name(Class<?> cls) {
    return Type.getType(cls).getInternalName();
  }
  
  private static void addArityMethods(ClassGenerator cgen, int inputArity, int outputArity) {
    MethodGenerator inp = cgen.method(false, "getInputArity", "()I");
    inp.ret(inp.intConstant(inputArity));
    inp.end();
    
    MethodGenerator out = cgen.method(false, "getOutputArity", "()I");
    out.ret(out.intConstant(outputArity));
    out.end();
  }
  
  public static final FunctionGenerator<Multidef, FunctionTypes.D> FUNCTION_D = new FunctionGenerator<Multidef, FunctionTypes.D>() {
    public Wrapper<FunctionTypes.D> begin(final Multidef def) {
      ClassGenerator cgen = new ClassGenerator(JavaArityGenerator.CLASS_NAME, new String[] {FunctionTypes.D.class.getName().replace('.', '/')});
      addArityMethods(cgen, def.args.size(), def.value.size());
      MethodGenerator mgen = cgen.method(false, "call", "([D[D)V");
      return new Wrapper<FunctionTypes.D>(cgen, mgen) {
        
        @Override
        public JavaValue symbol(Symbol sym) {
          int index = def.args.indexOf(sym);
          if(index >= 0) {
            return mgen.aload(mgen.arg(0), index);
          } else {
            return null;
          }
        }
        
        @Override
        public void ret(int index, JavaValue value) {
          mgen.astore(mgen.arg(1), index, value);
        }
        
        @Override
        public FunctionTypes.D end() {
          mgen.ret();
          mgen.end();
          cgen.end();
          return load(cgen);
        }
      };
    }
  };
  
  public static final FunctionGenerator<Multidef, FunctionTypes.ZeroOnRayD3> MAYBE_ZERO_ON_RAY3 = new FunctionGenerator<Multidef, FunctionTypes.ZeroOnRayD3>() {
    public Wrapper<FunctionTypes.ZeroOnRayD3> begin(final Multidef def) {
      final ClassGenerator gen = new ClassGenerator(JavaArityGenerator.CLASS_NAME, new String[] {name(FunctionTypes.ZeroOnRayD3.class)});
      final MethodGenerator mgen = gen.method(false, "maybeHasZeroOn", "(L" + name(RayD3.class) + ";)Z");
      return new Wrapper<FunctionTypes.ZeroOnRayD3>(gen, mgen) {
        
        private JavaValue aVar;
        private JavaValue bVar;
        
        private JavaValue field(String name) {
          return mgen.loadField(mgen.arg(0), new JavaField(name(RayD3.class), name, "D"));
        }
        
        private JavaValue mm(String name, String method) {
          JavaValue x = field(name);
          JavaValue y = mgen.binOp(Opcodes.DADD, field(name), field("d" + name));
          return mgen.callStatic("java/lang/Math", method, "(DD)D", x, y);
        }
        
        @Override
        public JavaValue symbol(Symbol sym) {
          int index = def.args.indexOf(sym);
          switch(index) {
          case -1:
            return null;
          case 0:
            return mm("x", "min");
          case 1:
            return mm("x", "max");
          case 2:
            return mm("y", "min");
          case 3:
            return mm("y", "max");
          case 4:
            return mm("z", "min");
          case 5:
            return mm("z", "max");
          default:
            throw new RuntimeException("shouldn't happen");
          }
        }
        
        @Override
        public void ret(int index, JavaValue value) {
          switch(index) {
          case 0:
            aVar = value;
            break;
          case 1:
            bVar = value;
            break;
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
  
  public static final FunctionGenerator<Multidef, FunctionTypes.ZeroInBlockD3> MAYBE_ZERO_IN_BLOCKD3 = new FunctionGenerator<Multidef, FunctionTypes.ZeroInBlockD3>() {
    public Wrapper<FunctionTypes.ZeroInBlockD3> begin(final Multidef def) {
      ClassGenerator cgen = new ClassGenerator(JavaArityGenerator.CLASS_NAME, new String[] {name(FunctionTypes.ZeroInBlockD3.class)});
      MethodGenerator mgen = cgen.method(false, "maybeHasZeroIn", "(L" + name(BlockD3.class) + ";)Z");
      return new Wrapper<FunctionTypes.ZeroInBlockD3>(cgen, mgen) {
        
        private JavaValue aVar;
        private JavaValue bVar;
        
        private JavaValue field(String name) {
          return mgen.loadField(mgen.arg(0), new JavaField(name(BlockD3.class), name, "D"));
        }
        
        @Override
        public JavaValue symbol(Symbol sym) {
          int index = def.args.indexOf(sym);
          switch(index) {
          case -1:
            return null;
          case 0:
            return field("x0");
          case 1:
            return field("x1");
          case 2:
            return field("y0");
          case 3:
            return field("y1");
          case 4:
            return field("z0");
          case 5:
            return field("z1");
          default:
            throw new RuntimeException("shouldn't happen");
          }
        }
        
        @Override
        public void ret(int index, JavaValue value) {
          switch(index) {
          case 0:
            aVar = value;
            break;
          case 1:
            bVar = value;
            break;
          default:
            throw new RuntimeException("shouldn't happen");
          }
        }
        
        @Override
        public FunctionTypes.ZeroInBlockD3 end() {
          mgen.ret(mgen.callStatic(MathEx.class.getName().replace('.', '/'), "containsZero", "(DD)Z", aVar, bVar));
          mgen.end();
          cgen.end();
          return load(cgen);
        }
      };
    }
  };
  
  private static <T> FunctionGenerator<Multidef, T> Dn_1(final int n) {
    return new FunctionGenerator<Multidef, T>() {
      public Wrapper<T> begin(final Multidef def) {
        ClassGenerator cgen = new ClassGenerator(JavaArityGenerator.CLASS_NAME, new String[] {name(FunctionTypes.class) + "$D" + n + "_1"});
        MethodGenerator mgen = cgen.method(false, "call", JavaArityGenerator.getArityDesc(n));
        return new Wrapper<T>(cgen, mgen) {
          @Override
          public JavaValue symbol(Symbol sym) {
            int index = def.args.indexOf(sym);
            if(index >= 0) {
              if(index < n) {
                return mgen.arg(index * 2);
              } else {
                throw new RuntimeException("shouldn't happen");
              }
            } else {
              return null;
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
            cgen.end();
            return load(cgen);
          }
        };
      }
    };
  }
  
  public static final FunctionGenerator<Multidef, FunctionTypes.D1_1> D1_1 = Dn_1(1); 
  public static final FunctionGenerator<Multidef, FunctionTypes.D2_1> D2_1 = Dn_1(2); 
  public static final FunctionGenerator<Multidef, FunctionTypes.D3_1> D3_1 = Dn_1(3);

  public static <T> FunctionGenerator<Closure<Multidef>, FunctionTypes.ClosureD<T>> closureD(final FunctionGenerator<Multidef, T> gen) {
    return new FunctionGenerator<Closure<Multidef>, FunctionTypes.ClosureD<T>>() {
      @Override
      public Wrapper<FunctionTypes.ClosureD<T>> begin(final Closure<Multidef> def) {
        final Wrapper<T> wrap = gen.begin(def.value);
        MethodGenerator closeMethod = wrap.cgen.method(false, "close", "(D])" + name(FunctionTypes.ClosureD.class));
        JavaValue arr = closeMethod.arg(0);
        final JavaField[] args = new JavaField[def.args.size()];
        for(int i = 0; i < args.length; i++) {
          String name = "closed_" + def.args.get(i).name;
          JavaField field = wrap.cgen.field(false, name, "D");
          closeMethod.storeField(closeMethod.getThis(), field, closeMethod.aload(arr, i));
        }
        return new Wrapper<FunctionTypes.ClosureD<T>>(wrap.cgen, wrap.mgen) {
          @Override
          public JavaValue symbol(Symbol sym) {
            JavaValue ret = wrap.symbol(sym);
            if(ret == null) {
              int index = def.args.indexOf(sym);
              if(index >= 0) {
                ret = mgen.loadField(mgen.getThis(), args[index]);
              }
            }
            return ret;
          }

          @Override
          public void ret(int index, JavaValue value) {
            wrap.ret(index, value);
          }

          @SuppressWarnings("unchecked")
          @Override
          public FunctionTypes.ClosureD<T> end() {
            return (FunctionTypes.ClosureD<T>)wrap.end();
          }
        };
      }
    };
  }
  
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
  
  private static JavaImpl unaryOp(final int op) {
    return new JavaImpl() {
      @Override
      public JavaValue call(MethodGenerator mgen, Vector<JavaValue> args) {
        return mgen.unaryOp(op, args.get(0));
      }
    };
  }
  
  private static final JavaDevice INSTANCE = new JavaDevice();
  
  public static FunctionTypes.D compile(final Multidef def) {
    return INSTANCE.transform(FUNCTION_D, def);
  }
  
  public static <T, Clos extends Closure<?>> T compile(FunctionGenerator<Clos, T> ctx, final Clos def) {
    return INSTANCE.transform(ctx, def);
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
  
  public <T, Clos extends Closure<?>> T transform(FunctionGenerator<Clos, T> ctx, final Clos def) {
    final Wrapper<T> wrap = ctx.begin(def);
    
    final MethodGenerator mgen = wrap.mgen;
    final Usage usage = Usage.generate(def);
    Processor<JavaValue> v = new Processor<JavaValue>() {
      @Override
      public JavaValue process(Call call, Vector<JavaValue> args) {
        JavaValue ret = lookup(call.func).call(mgen, args);
        if(usage.get(call) > 1) {
          mgen.store(ret);
        }
        return ret;
      }

      @Override
      public JavaValue process(Symbol sym) {
        JavaValue ret = wrap.symbol(sym);
        if(ret == null) {
          ret = mgen.doubleConstant(lookup(sym));
        }
        if(usage.get(sym) > 1) {
          mgen.store(ret);
        }
        return ret;
      }

      @Override
      public JavaValue process(Rational r) {
        JavaValue ret = mgen.doubleConstant(r.toDouble());
        if(usage.get(r) > 1) {
          mgen.store(ret);
        }
        return ret;
      }

    };
    int i = 0;
    for(JavaValue val : def.acceptVector(v)) {
      wrap.ret(i++, val);
    }
    return wrap.end();
  }

}
