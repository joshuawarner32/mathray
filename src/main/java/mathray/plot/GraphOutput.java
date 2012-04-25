package mathray.plot;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import mathray.util.UI;

public class GraphOutput implements Output {
  
  private final Graph2D graph;
  private final Format format;
  private final String name;
  
  private BufferedImage image;
  
  public GraphOutput(String name, Graph2D graph, Format format) {
    this.graph = graph;
    this.format = format;
    this.name = name;
  }
  
  void draw() {
    if(image == null) {
      image = new BufferedImage(format.width, format.height, BufferedImage.TYPE_INT_ARGB);
      Graphics2D g = image.createGraphics();
      g.setBackground(Color.WHITE);
      g.clearRect(0, 0, image.getWidth(), image.getHeight());
      g.setPaint(Color.BLACK);
      graph.draw(g, 0, 0, image.getWidth(), image.getHeight());
    }
  }

  @Override
  public void save(File f) throws IOException {
    draw();
    ImageIO.write(image, "png", f);
  }

  @Override
  public void show() {
    draw();
    UI.show(name, image);
  }

}
