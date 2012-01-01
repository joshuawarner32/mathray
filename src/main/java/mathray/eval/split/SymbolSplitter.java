package mathray.eval.split;

import mathray.Symbol;
import mathray.util.Vector;

public interface SymbolSplitter {
  
  Vector<Symbol> split(Symbol symbol);

}
