import static org.junit.Assert.*;

import mathray.Closure;
import mathray.Definition;
import mathray.Multidef;
import mathray.Symbol;
import mathray.device.FunctionTypes;
import mathray.eval.machine.VisitorDevice;
import mathray.eval.transform.Project;
import static mathray.Expressions.*;

import org.junit.Test;


public class TestProject {
  
  Symbol x = sym("x");
  Symbol y = sym("y");
  
  private double closedProjectedEval(Definition def, double[] cameraArgs, double[] args) {
    Closure<Multidef> projected = Project.project(def.toMultidef(), args(x));
    FunctionTypes.ClosureD<FunctionTypes.D> res = VisitorDevice.closure(projected);
    double[] r = new double[1];
    res.close(cameraArgs).call(args, r);
    return r[0];
  }
  
  @Test
  public void test1DProject() {
    Definition def = def(args(x), sin(x));
    double res = closedProjectedEval(def, new double[] {1, -1}, new double[] {1});
    assertEquals(0.0, res, 0.000001);
  }
  
  
  @Test
  public void test2DProject() {
    Definition def = def(args(x, y), max(x, y));
    Closure<Multidef> projected = Project.project(def.toMultidef(), args(x, y));
    System.out.println(projected);
    // TODO: use MachineEvaluator to test result
  }

}
