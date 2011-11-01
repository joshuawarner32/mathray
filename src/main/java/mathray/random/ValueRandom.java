package mathray.random;

import java.util.Random;

import mathray.Definition;
import mathray.Rational;
import mathray.Value;

public final class ValueRandom {
  
  private Random random;
  
  public ValueRandom() {
    random = new Random();
  }
  
  public ValueRandom(long seed) {
    random = new Random(seed);
  }
  
  public Rational randomRational() {
    return null;
  }
  
  public Value randomValue() {
    return null;
  }
  
  public Definition randomDefinition() {
    return null;
  }

}
