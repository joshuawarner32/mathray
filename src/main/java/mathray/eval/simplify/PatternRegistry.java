package mathray.eval.simplify;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import mathray.Call;
import mathray.Function;
import mathray.Value;

public class PatternRegistry {
  
  private Map<Function, Set<Pattern>> patterns = new HashMap<Function, Set<Pattern>>();
  
  private Set<Pattern> general = new HashSet<Pattern>();
  
  public void register(Pattern pattern) {
    
    if(pattern.match instanceof Call) {
      Call c = (Call)pattern.match;
      Set<Pattern> pats = patterns.get(c.func);
      if(pats == null) {
        patterns.put(c.func, pats = new HashSet<Pattern>());
      }
      pats.add(pattern);
    } else {
      general.add(pattern);
    }
    
  }
  
  public Value applyPatterns(Value value) {
    Value old;
    outer: do {
      old = value;
      if(value instanceof Call) {
        Call c = (Call)value;
        Set<Pattern> pats = patterns.get(c.func);
        if(pats == null) {
          break;
        } else {
          for(Pattern pat : pats) {
            value = pat.process(value);
            if(!value.equals(old)) {
              continue outer;
            }
          }
        }
      } else {
        for(Pattern pat : general) {
          value = pat.process(value);
          if(!value.equals(old)) {
            continue outer;
          }
        }
      }
    } while(!old.equals(value));
    return value;
  }

}
