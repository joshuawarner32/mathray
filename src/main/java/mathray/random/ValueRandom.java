package mathray.random;

import java.util.Random;

import mathray.Args;
import mathray.Definition;
import mathray.Function;
import mathray.Generator;
import mathray.Rational;
import mathray.Value;
import mathray.Symbol;
import mathray.Vector;

import static mathray.Expressions.*;

public final class ValueRandom {
  
  private Random random;
  
  private Vector<Symbol> namedConstants;
  private Vector<Function> functions;
  
  public ValueRandom(Vector<Symbol> namedConstants, Vector<Function> functions) {
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
  
  public Symbol randomNamedConstant() {
    return namedConstants.get(random.nextInt(namedConstants.size()));
  }
  
  public Function randomFunction() {
    return functions.get(random.nextInt(functions.size())); 
  }
  
  public Value randomValue(final Vector<Symbol> args, final double recurseProb, final double decay, final double rationalProb) {
    if(random.nextDouble() < recurseProb) {
      Function function = randomFunction();
      return function.call(Vector.<Value>generate(function.arity, new Generator<Value>() {
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
  
  public Definition randomDefinition(final Args args, final double recurseProb, final double decay, final double rationalProb) {
    return def(args, randomValue(args.toVector(), recurseProb, decay, rationalProb));
  }
  

}
