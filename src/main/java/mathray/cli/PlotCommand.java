package mathray.cli;

import static mathray.Expressions.*;
import static mathray.Functions.*;
import static mathray.NamedConstants.*;

import java.util.Set;

import mathray.Args;
import mathray.Definition;
import mathray.Symbol;
import mathray.Value;
import mathray.eval.text.DefaultPrinter;
import mathray.eval.text.ParseInfo;
import mathray.plot.BasicFunctionPlotter;
import mathray.plot.Format;
import mathray.plot.Output;
import mathray.plot.Plotter;
import mathray.visitor.Visitors;

public class PlotCommand {
  
  public static final Symbol x = sym("x");
  public static final Symbol y = sym("y");
  public static final Symbol z = sym("z");
  
  public static final Symbol r = sym("r");
  public static final Symbol theta = sym("theta");
  public static final  Symbol phi = sym("phi");
  
  public static final Symbol t = sym("t");
  
  public static final ParseInfo COORDINATE_PARSER = DefaultPrinter.BASIC_FUNCTIONS.toBuilder()
    .values(E, PI, TAU)
    .value("infinity", POS_INF)
    .values(x, y, z)
    .values(r, theta, phi)
    .values(t)
    .build();

  private Definition def;
  private Plotter plotter;
  
  private static void ensureNone(Set<Symbol> symbols, Symbol... blacklist) throws InputException {
    for(Symbol s : blacklist) {
      if(symbols.contains(s)) {
        throw new InputException("not expecting variable " + s);
      }
    }
  }
  
  private Args detectArgs(Value... values) throws InputException {
    Set<Symbol> symbols = Visitors.getSymbols(values);
    
    if(symbols.contains(r) || symbols.contains(theta) || symbols.contains(phi)) {
      ensureNone(symbols, x, y);
      if(symbols.contains(phi)) {
        ensureNone(symbols, z);
        return args(r, theta, phi);
      }
      if(symbols.contains(z)) {
        return args(r, theta, z);
      } else {
        if(symbols.contains(r)) {
          return args(r, theta);
        } else {
          return args(theta);
        }
      }
    } else {
      if(symbols.contains(z)) {
        return args(x, y, z);
      } else if(symbols.contains(y)) {
        return args(x, y);
      } else {
        plotter = new BasicFunctionPlotter();
        return args(x);
      }
    }
  }
  
  public PlotCommand(String plot) throws InputException {
    String[] parts = plot.split("=");
    if(parts.length != 1 && parts.length != 2) {
      throw new InputException("wrong number of equals ('=') signs");
    }
    Value[] values = new Value[parts.length];
    for(int i = 0; i < parts.length; i++) {
      values[i] = COORDINATE_PARSER.parse(parts[i]);
    }
    Args args = detectArgs(values);
    if(plotter == null) {
      throw new InputException("unsupported coordinate system " + args);
    }

    def = def(args, fold(SUB, values));
  }

  public Output plot(Format format) {
    return plotter.plot(def, format);
  }
  
}
