package mathray.eval.linear;

import static mathray.Expressions.*;

import java.util.HashMap;
import java.util.Map;

import mathray.Symbol;
import mathray.Value;

public class LinearTerm {
  public final Value lowOffset;
  public final Value highOffset;
  
  final Map<Symbol, Value> linearCoefficients;
  
  LinearTerm(Value lowOffset, Value highOffset, Map<Symbol, Value> linearCoefficients) {
    this.lowOffset = lowOffset;
    this.highOffset = highOffset;
    this.linearCoefficients = linearCoefficients;
  }
  
  public LinearTerm(Symbol symbol) {
    lowOffset = highOffset = num(0);
    linearCoefficients = new HashMap<Symbol, Value>();
    linearCoefficients.put(symbol, symbol);
  }
  
}
