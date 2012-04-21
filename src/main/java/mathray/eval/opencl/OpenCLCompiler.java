package mathray.eval.opencl;

import static mathray.Functions.*;
import mathray.FunctionSymbolRegistrar;
import mathray.NamedConstants;
import mathray.util.MathEx;

public class OpenCLCompiler extends FunctionSymbolRegistrar<OpenCLImpl, Double>{

  {
    
    register(NamedConstants.TAU, MathEx.TAU);
    register(NamedConstants.PI, Math.PI);
    register(NamedConstants.E, Math.E);
    register(NamedConstants.NEG_INF, Double.NEGATIVE_INFINITY);
    register(NamedConstants.POS_INF, Double.POSITIVE_INFINITY);
    register(NamedConstants.UNDEF, Double.NaN);
    
    register(ADD, binop("+"));
    register(SUB, binop("-"));
    register(MUL, binop("*"));
    register(DIV, binop("/"));
  }
  
  private OpenCLImpl binop(String op) {
    return new OpenCLImpl();
  }
}
