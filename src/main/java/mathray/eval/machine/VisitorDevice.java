package mathray.eval.machine;

import java.util.Arrays;

import mathray.Call;
import mathray.Lambda;
import mathray.Multidef;
import mathray.Definition;
import mathray.FunctionSymbolRegistrar;
import mathray.NamedConstants;
import mathray.Rational;
import mathray.Symbol;
import mathray.Value;
import mathray.device.Device;
import mathray.device.FunctionType;
import mathray.device.FunctionTypes;
import mathray.eval.Impl;
import mathray.util.Generator;
import mathray.util.MathEx;
import mathray.util.Vector;
import mathray.visitor.Processor;

import static mathray.Functions.*;

public class VisitorDevice extends FunctionSymbolRegistrar<Impl<Double>, Double> implements Device {
  
  private static final VisitorDevice INSTANCE = new VisitorDevice();
  
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
    if(def.args.size() != params.size()) {
      throw new IllegalArgumentException();
    }
    
    final Processor<Double> v = new Processor<Double>() {
      
      public Double check(Double d, Value value) {
        if(Double.isNaN(d)) {
          throw new VisitorArithmeticException(d, value); 
        }
        return d;
      }
      
      @Override
      public Double process(Call call, Vector<Double> args) {
        return check(lookup(call.func).call(args), call);
      }

      @Override
      public Double process(Symbol sym) {
        int s = def.args.indexOf(sym);
        if(s != -1) {
          return check(params.get(s), sym);
        } else {
          return check(lookup(sym), sym);
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
  
  // TODO: this could be a lot prettier...
  public static void eval(Multidef def, final double[] args, double[] res) {
    Vector<Double> vArgs = Vector.generate(args.length, new Generator<Double>() {
      @Override
      public Double generate(int index) {
        return args[index];
      }
    });
    Vector<Double> r = eval(def, vArgs);
    for(int i = 0; i < r.size(); i++) {
      res[i] = r.get(i);
    }
  }
  
  public static FunctionTypes.ClosureD<FunctionTypes.D> closure(final Lambda<Multidef> def) {
    final Multidef complete = new Multidef(def.args.concat(def.value.args), def.value.value);
    return new FunctionTypes.ClosureD<FunctionTypes.D>() {
      @Override
      public FunctionTypes.D close(final double... closedArgs) {
        if(closedArgs.length != def.args.size()) {
          throw new IllegalArgumentException("expected " + def.args.size() + " but got " + closedArgs.length);
        }
        return new FunctionTypes.D() {
          @Override
          public int getOutputArity() {
            return complete.value.size();
          }
          
          @Override
          public int getInputArity() {
            return complete.args.size();
          }
          
          @Override
          public void call(double[] args, double[] res) {
            if(args.length != def.value.args.size()) {
              throw new IllegalArgumentException("expected " + def.value.args.size() + " but got " + args.length);
            }
            double[] all = Arrays.copyOf(closedArgs, complete.args.size());
            for(int i = 0; i < args.length; i++) {
              all[i + closedArgs.length] = args[i];
            }
            eval(complete, all, res);
          }
        };
      }
    };
  }

  @Override
  public <T, Clos> T compile(FunctionType<T, Clos> type, Clos def) {
    if(type != FunctionTypes.FUNCTION_D) {
      throw new IllegalArgumentException();
    }
    return (T)new FunctionTypes.D() {

      @Override
      public int getInputArity() {
        return 0;
      }

      @Override
      public int getOutputArity() {
        // TODO Auto-generated method stub
        return 0;
      }

      @Override
      public void call(double[] args, double[] res) {
        // TODO Auto-generated method stub
        
      }
      
    };
  }

  @Override
  public int cost(FunctionType<?, ?> type) {
    if(type == FunctionTypes.FUNCTION_D) {
      return 10;
    } else {
      return Device.NO_SUPPORT;
    }
  }

  @Override
  public int compileCost(FunctionType<?, ?> type) {
    if(type == FunctionTypes.FUNCTION_D) {
      return 0;
    } else {
      return Device.NO_SUPPORT;
    }
  }

}
