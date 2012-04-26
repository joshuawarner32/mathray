package mathray.plot;

import java.awt.image.BufferedImage;

import mathray.Closure;
import mathray.Definition;
import mathray.Multidef;
import mathray.concrete.CameraD3;
import mathray.concrete.VectorD3;
import mathray.device.FunctionTypes;
import mathray.eval.java.JavaDevice;
import mathray.eval.simplify.Simplifications;
import mathray.eval.split.IntervalTransform;
import mathray.eval.transform.Project;

public class Equation3DPlotter implements Plotter {
  
  private void pickFormat(Format format) {
    if(format.width == null) {
      format.width = 512;
    }
    if(format.height == null) {
      format.height = 512;
    }
  }

  @Override
  public Output plot(Definition def, Format format) {
    pickFormat(format);
    Closure<Multidef> proj = Project.project(def.toMultidef(), def.args); 
    Closure<Multidef> inter = proj.close(Simplifications.simplify(IntervalTransform.intervalize(proj.value, proj.value.args)));
    CameraD3 cam = CameraD3.lookAt(new VectorD3(3, 3, 3), new VectorD3(0, 0, 0), 1, format.width / (double)format.height, new VectorD3(0, 0, 1));
    FunctionTypes.ClosureD<FunctionTypes.ZeroInBlockD3> func = JavaDevice.compile(JavaDevice.closureD(JavaDevice.MAYBE_ZERO_IN_BLOCKD3), inter);
    BufferedImage image = Plot3D.plotBlockDepth(func.close(cam.args()), format.width, format.height, 0.001, 100);
    return new ImageOutput(def.toString() + " = 0", image);
  }

}
