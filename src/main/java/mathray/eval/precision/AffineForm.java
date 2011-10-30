package mathray.eval.precision;

import java.util.HashMap;
import java.util.Map;

import mathray.Value;
import mathray.eval.precision.AffineContext.AffineTerm;

public class AffineForm {
  
  private final Value center;
  
  private final Map<AffineTerm, Value> coeffs = new HashMap<AffineContext.AffineTerm, Value>();
  
  public AffineForm(Value center) {
    this.center = center;
  }

}
