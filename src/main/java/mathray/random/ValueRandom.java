package mathray.random;

import java.util.Random;

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
  
  public ValueRandom(Vector<Variable> namedConstants) {
    random = new Random();
  }
  
  public Rational randomRational() {
    // TODO: improve
    long a = random.nextLong();
    long b = random.nextLong();
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
  
  public Value randomValue(final double recurseProb, final double decay, final double rationalProb) {
    // TODO: arguments
    if(random.nextDouble() < recurseProb) {
      SelectFunction function = randomSelectFunction();
      return function.call(Vector.<Value>generate(function.func.inputArity, new Generator<Value>() {
        @Override
        public Value generate(int index) {
          return randomValue(recurseProb * decay, decay, rationalProb);
        }
      }));
    } else if(random.nextDouble() < rationalProb) {
      return randomRational();
    } else {
      return randomNamedConstant();
    }
  }
  
  public Definition randomDefinition(int inputArity, int outputArity, final double recurseProb, final double decay, final double rationalProb) {
    // TODO: arguments
    return new Definition(args(), Vector.<Value>generate(outputArity, new Generator<Value>() {
      @Override
      public Value generate(int index) {
        return randomValue(recurseProb, decay, rationalProb);
      }
    }));
  }
  

}
