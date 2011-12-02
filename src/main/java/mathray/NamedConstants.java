package mathray;

import static mathray.Expressions.*;

public final class NamedConstants {
  
  private NamedConstants() {}
  
  // tau == 2 * pi, the REAL circle constant (see tauday.com)
  public static final Symbol TAU = sym("tau");
  
  public static final Symbol PI = sym("pi");
  
  public static final Symbol E = sym("e");
  
  public static final Symbol POS_INF = sym("pos_inf");
  
  public static final Symbol NEG_INF = sym("neg_inf");
  
  public static final Vector<Symbol> ALL = vector(TAU, E);

}
