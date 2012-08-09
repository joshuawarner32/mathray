package mathray.plot;

import java.awt.image.BufferedImage;

import mathray.Definition;
import mathray.concrete.CameraD3;
import mathray.concrete.VectorD3;

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
    
    CameraD3 cam = CameraD3.lookAt(new VectorD3(3, 3, 3), new VectorD3(0, 0, 0), 1, format.width / (double)format.height, new VectorD3(0, 0, 1));

    BufferedImage image = Plot3D.plotBlockDefault(def, cam, format.width, format.height, 0.001, 100);
    return new ImageOutput(def.toString() + " = 0", image);
  }

}
