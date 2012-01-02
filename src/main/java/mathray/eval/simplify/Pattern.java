package mathray.eval.simplify;

import java.util.Arrays;

import mathray.Args;
import mathray.Call;
import mathray.Definition;
import mathray.Rational;
import mathray.Symbol;
import mathray.Value;
import mathray.util.Vector;

public class Pattern {
  
  public final Args args;
  
  public final Value match;
  
  public final Value replace;
  
  public final Vector<Test> tests;
  
  public Pattern(Args args, Value match, Value replace, Vector<Test> tests) {
    this.args = args;
    this.match = match;
    this.replace = replace;
    this.tests = tests;
  }
  
  public interface Test {
    public boolean check(Args args, Value[] matches);
  }

  private boolean match(Value[] matches, Value match, Value value) {
    if(match instanceof Symbol) {
      Integer index = args.indexOf((Symbol)match);
      if(index != null) {
        if(matches[index] == null) {
          matches[index] = value;
          return true;
        }
        return matches[index].equals(value);
      } else {
        return match.equals(value);
      }
    } else if(match instanceof Rational) {
      return match.equals(value);
    } else { // match instanceof Call
      Call mc = (Call)match;
      if(value instanceof Call) {
        Call vc = (Call)value;
        if(!mc.func.equals(vc.func)) {
          return false;
        }
        Value[] tmpMatches = Arrays.copyOf(matches, matches.length);
        for(int i = 0; i < mc.args.size(); i++) {
          if(!match(tmpMatches, mc.args.get(i), vc.args.get(i))) {
            return false;
          }
        }
        for(int i = 0; i < matches.length; i++) {
          matches[i] = tmpMatches[i];
        }
        return true;
      } else {
        return false;
      }
    }
  }
  
  public Value process(Value value) {
    Value[] matches = new Value[args.size()];
    if(match(matches, match, value)) {
      for(Test test : tests) {
        if(!test.check(args, matches)) {
          return value;
        }
      }
      return new Definition(args, replace).call(matches);
    }
    return value;
  }
  
  public static Pattern pattern(Args args, Value match, Value replace, Test... tests) {
    return new Pattern(args, match, replace, new Vector<Test>(tests));
  }
  
  public static Test isInteger(final Symbol x) {
    return new Test() {
      public boolean check(Args args, Value[] matches) {
        Integer i = args.indexOf(x);
        if(i == null) {
          return false;
        }
        return matches[i] instanceof Rational && ((Rational)matches[i]).isInteger();
      }
    };
  }

}
