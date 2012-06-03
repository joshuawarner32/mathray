import static org.junit.Assert.*;

import mathray.Lambda;
import mathray.Definition;
import mathray.Multidef;
import mathray.Symbol;
import mathray.device.FunctionTypes;
import mathray.eval.machine.VisitorDevice;
import mathray.eval.simplify.Simplifications;
import mathray.eval.transform.Project;
import static mathray.Expressions.*;

import org.junit.Ignore;
import org.junit.Test;


public class TestProject {
  
  Symbol x = sym("x");
  Symbol y = sym("y");
  
  private double closedProjectedEval(Definition def, double[] cameraArgs, double[] args) {
    Lambda<Multidef> projected = Project.project(def.toMultidef(), def.args);
    FunctionTypes.ClosureD<FunctionTypes.D> res = VisitorDevice.closure(projected);
    double[] r = new double[1];
    res.close(cameraArgs).call(args, r);
    return r[0];
  }
  
  @Test
  public void test1DProjectIdentity() {
    Definition def = def(args(x), x);
    Lambda<Multidef> projected = Project.project(def.toMultidef(), def.args);
    Multidef simplified = Simplifications.simplify(projected.call(num(0), num(1)));
    assertEquals(simplified, multidef(args(x), x));
  }
  
  @Test
  public void test1DProject() {
    Definition def = def(args(x), sin(x));
    double res = closedProjectedEval(def, new double[] {1, -1}, new double[] {1});
    assertEquals(0.0, res, 0.000001);
  }
  
  @Test
  @Ignore
  public void test2DProjectIdentity() {
    Definition def = def(args(x, y), max(x, y));
    double[] id = new double[] {0, 0, 1, 0, 0, 1};
//    assertEquals(0.0, closedProjectedEval(def, id, new double[] {0, 0}), 0.000001);
//    assertEquals(1.0, closedProjectedEval(def, id, new double[] {0, 1}), 0.000001);
//    assertEquals(2.0, closedProjectedEval(def, id, new double[] {2, 1}), 0.000001);
    assertEquals(-1.0, closedProjectedEval(def, id, new double[] {-1, -2}), 0.000001);
  }
  
  @Test
  @Ignore
  public void test2DProject() {
    Definition def = def(args(x, y), add(x, pow(y, 2)));
    double res = closedProjectedEval(def, new double[] {}, new double[] {});
    // TODO: use MachineEvaluator to test result
  }

}
