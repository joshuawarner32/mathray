import mathray.Closure;
import mathray.Definition;
import mathray.Multidef;
import mathray.Symbol;
import mathray.eval.machine.MachineEvaluator;
import mathray.eval.transform.Project;
import static mathray.Expressions.*;

import org.junit.Test;


public class TestProject {
  
  Symbol x = sym("x");
  Symbol y = sym("y");
  
  @Test
  public void test1DProject() {
    Definition def = def(args(x), sin(x));
    Closure<Multidef> projected = Project.project(def.toMultidef(), args(x));
    System.out.println(projected);
//    MachineEvaluator.close(projected, vector(1.0, -1.0));
    // TODO: use MachineEvaluator to test result
  }
  
  
  @Test
  public void test2DProject() {
    Definition def = def(args(x, y), max(x, y));
    Closure<Multidef> projected = Project.project(def.toMultidef(), args(x, y));
    System.out.println(projected);
    // TODO: use MachineEvaluator to test result
  }

}
