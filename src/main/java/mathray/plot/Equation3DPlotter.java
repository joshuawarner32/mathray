package mathray.plot;

import java.awt.image.BufferedImage;

import mathray.Definition;
import mathray.concrete.CameraD3;
import mathray.concrete.VectorD3;

public class Equation3DPlotter implements Plotter {
  
  private CameraD3 pickFormat(Format format) {
    if(format.width == null) {
      format.width = 512;
    }
    if(format.height == null) {
      format.height = 512;
    }
    
    if(format.cameraPosition == null) {
      if(format.cameraLookAt == null) {
        format.cameraPosition = new VectorD3(3, 3, 3);
        format.cameraLookAt = new VectorD3(0, 0, 0);
      } else {
        format.cameraPosition = VectorD3.add(format.cameraLookAt, new VectorD3(3, 3, 3));
      }
    } else {
      if(format.cameraLookAt == null) {
        format.cameraLookAt = VectorD3.sub(format.cameraLookAt, new VectorD3(3, 3, 3));
      }
    }
    
    if(format.cameraUp == null) {
      format.cameraUp = new VectorD3(0, 0, 1);
    }
    
    if(format.cameraFieldOfView == null) {
      format.cameraFieldOfView = 1d;
    }
    
    return CameraD3.lookAt(format.cameraPosition, format.cameraLookAt, 1, format.width / (double)format.height, format.cameraUp);
  }

  @Override
  public Output plot(Definition def, Format format) {
    CameraD3 cam = pickFormat(format);
    
    BufferedImage image = Plot3D.plotBlockDefault(def, cam, format.width, format.height, 0.001, 100);
    return new ImageOutput(def.toString() + " = 0", image);
  }

}
