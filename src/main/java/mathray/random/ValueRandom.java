package mathray.random;

import java.util.Random;

import mathray.Args;
import mathray.Definition;
import mathray.Function;
import mathray.Generator;
import mathray.Rational;
import mathray.SelectFunction;
import mathray.Value;
import mathray.Variable;
import mathray.Vector;

import static mathray.Expressions.*;

public final class ValueRandom {
  
  private Random random;
  
  private Vector<Variable> namedConstants;
  private Vector<Function> functions;
  
  public ValueRandom(Vector<Variable> namedConstants, Vector<Function> functions) {
    this.namedConstants = namedConstants;
    this.functions = functions;
    random = new Random();
  }
  
  public Rational randomRational() {
    // TODO: improve
    long a = random.nextInt(11) - 6;
    long b = random.nextInt(4) + 1;
    return num(a, b);
  }
  
  public Variable randomNamedConstant() {
    return namedConstants.get(random.nextInt(namedConstants.size()));
  }
  
  public Function randomFunction() {
    return functions.get(random.nextInt(functions.size())); 
  }
  
  public SelectFunction randomSelectFunction() {
    Function function = randomFunction();
    return function.select(random.nextInt(function.outputArity));
  }
  
  public Value randomValue(final Vector<Variable> args, final double recurseProb, final double decay, final double rationalProb) {
    if(random.nextDouble() < recurseProb) {
      SelectFunction function = randomSelectFunction();
      return function.call(Vector.<Value>generate(function.func.inputArity, new Generator<Value>() {
        @Override
        public Value generate(int index) {
          return randomValue(args, recurseProb * decay, decay, rationalProb);
        }
      }));
    } else if(random.nextDouble() < rationalProb) {
      return randomRational();
    } else {
      int r = random.nextInt(namedConstants.size() + args.size());
      if(r < namedConstants.size()) {
        return namedConstants.get(r);
      } else {
        return args.get(r - namedConstants.size());
      }
    }
  }
  
  public Definition randomDefinition(final Args args, int outputArity, final double recurseProb, final double decay, final double rationalProb) {
    return new Definition(args, Vector.<Value>generate(outputArity, new Generator<Value>() {
      @Override
      public Value generate(int index) {
        return randomValue(args.toVector(), recurseProb, decay, rationalProb);
      }
    }));
  }
  

}
