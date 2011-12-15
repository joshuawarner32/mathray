package mathray.eval.set;

import mathray.Computation;
import mathray.Function;

public interface MultidimSet {

  MultidimSet apply(Function func);

  MultidimSet split(Computation comp);

}
