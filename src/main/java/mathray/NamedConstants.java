package mathray;

import static mathray.Expressions.*;

public final class NamedConstants {
  
  private NamedConstants() {}
  
  // two pi, the REAL circle constant (see tauday.com)
  public static final Variable TAU = var("tau");
  
  public static final Value PI = div(TAU, num(2));
  
  public static final Variable E = var("e");
  
  public static final Variable POS_INF = var("pos_inf");
  
  public static final Variable NEG_INF = var("neg_inf");
  
  public static final Vector<Variable> ALL = vector(TAU, E);

}
