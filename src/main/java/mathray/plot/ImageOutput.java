package mathray.plot;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import mathray.util.UI;

public class ImageOutput implements Output {
  
  private final String name;
  private final BufferedImage image;

  public ImageOutput(String name, BufferedImage image) {
    this.name = name;
    this.image = image;
  }

  @Override
  public void save(File f) throws IOException {
    ImageIO.write(image, "png", f);
  }

  @Override
  public void show() {
    UI.show(name, image);
  }

}
