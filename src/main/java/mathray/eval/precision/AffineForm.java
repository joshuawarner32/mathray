package mathray.eval.precision;

import java.util.HashMap;
import java.util.Map;

import mathray.Definition;
import mathray.Function;
import mathray.SelectFunction;
import mathray.Value;
import mathray.eval.precision.AffineContext.AffineTerm;

public class AffineForm {
  
  private final Value center;
  
  private final Map<AffineTerm, Value> coeffs;
  
  AffineForm(Value center, Map<AffineTerm, Value> coeffs) {
    this.center = center;
    this.coeffs = new HashMap<AffineContext.AffineTerm, Value>(coeffs);
  }
  
  // TODO: some kind of SelectDefinition or generic Select<Definition>
  public AffineForm pairwise(Definition func, Definition id1, Definition id2, AffineForm other) {
    // TODO: use id1
    AffineForm ret = new AffineForm(func.call(center, other.center).get(0), coeffs);
    for(Map.Entry<AffineTerm, Value> coeff : other.coeffs.entrySet()) {
      Value first = ret.coeffs.get(coeff.getKey());
      if(first == null) {
        ret.coeffs.put(coeff.getKey(), id2.call(coeff.getValue()).get(0));
      } else {
        ret.coeffs.put(coeff.getKey(), func.call(first, coeff.getValue()).get(0));
      }
    }
    return ret;
  }

}
