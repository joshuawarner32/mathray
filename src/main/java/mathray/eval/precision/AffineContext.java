package mathray.eval.precision;

import java.util.HashSet;
import java.util.Set;

public class AffineContext {
  public class AffineTerm {}
  
  private Set<AffineTerm> terms = new HashSet<AffineTerm>();
  
  public AffineTerm newTerm() {
    AffineTerm term = new AffineTerm();
    terms.add(term);
    return term;
  }
}
