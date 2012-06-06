import static org.junit.Assert.*;

import mathray.Lambda;
import mathray.Definition;
import mathray.Multidef;
import mathray.Symbol;
import mathray.Value;
import mathray.concrete.CameraD3;
import mathray.concrete.VectorD3;
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
  Symbol z = sym("z");
  
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
    assertEquals(multidef(args(x), x), simplified);
  }
  
  @Test
  public void test1DProject() {
    Definition def = def(args(x), sin(x));
    double res = closedProjectedEval(def, new double[] {1, -1}, new double[] {1});
    assertEquals(0.0, res, 0.000001);
  }
  
  private void checkCoordinates(CameraD3 cam, FunctionTypes.ClosureD<FunctionTypes.D> f, VectorD3 vec) {
    VectorD3 expected = cam.transform(vec);
    double[] res = new double[3];
    f.close(cam.args()).call(vec.toArray(), res);
    VectorD3 actual = VectorD3.fromArray(res);
    assertEquals(expected, actual);
  }
  
  @Test
  @Ignore
  public void test3DProject() {
    Multidef def = multidef(args(x, y, z), x, y, z);
    Lambda<Multidef> projected = Project.project(def, def.args);
    Lambda<Multidef> simplified = projected.close(Simplifications.simplify(projected.value));
    FunctionTypes.ClosureD<FunctionTypes.D> f = VisitorDevice.closure(simplified);
    System.out.println(simplified);
    CameraD3 cam = CameraD3.lookAt(new VectorD3(1, 1, 1), new VectorD3(3, 3, 3), 1, 1, new VectorD3(0, 0, 1));
    checkCoordinates(cam, f, new VectorD3(0, 0, 0));
    checkCoordinates(cam, f, new VectorD3(1, 2, 3));
    checkCoordinates(cam, f, new VectorD3(4, 1, 2));
    checkCoordinates(cam, f, new VectorD3(1.2, 4, -1));
  }
  
  @Test
  @Ignore
  public void test2DProjectIdentity() {
    Multidef def = multidef(args(x, y), x, y);
    Lambda<Multidef> projected = Project.project(def, def.args);
    System.out.println(projected);
    Value[] id = new Value[] {
      num(0), num(0),
      num(1), num(-1),
      num(0), num(1)
    };
    Multidef simplified = Simplifications.simplify(projected.call(id));
    assertEquals(multidef(args(x, y), x, y), simplified);
  }
  
  @Test
  @Ignore
  public void test2DProject() {
    Definition def = def(args(x, y), add(x, pow(y, 2)));
    double res = closedProjectedEval(def, new double[] {}, new double[] {});
    // TODO: use MachineEvaluator to test result
  }

}
