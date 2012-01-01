package mathray.eval.set;

import mathray.Multidef;
import mathray.Function;

public interface MultidimSet {

  MultidimSet apply(Function func);

  MultidimSet split(Multidef def);

}
