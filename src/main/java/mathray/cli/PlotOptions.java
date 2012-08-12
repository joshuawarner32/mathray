package mathray.cli;

import java.io.File;
import java.io.IOException;

import mathray.concrete.VectorD3;
import mathray.plot.Format;
import mathray.plot.Output;
import mathray.plot.Range;
import mathray.plot.Resolution;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.google.common.base.Stopwatch;

public class PlotOptions {
  
  private static Options options = new Options();
  
  static {
    options.addOption(opt("help", "print this message"));
    options.addOption(arg("s", "size", "width>x<height", "size of output in pixels"));
    options.addOption(arg("r", "range", "xmin>:<xmax>,<ymin>:<ymax", "range of visible coordinates"));
    options.addOption(arg("c", "camera", "x>,<y>,<z", "camera position"));
    options.addOption(arg("l", "lookat", "x>,<y>,<z", "look-at position"));
    options.addOption(arg("u", "up", "x>,<y>,<z", "up vector"));
    options.addOption(arg("f", "fov", "value", "field of view"));
    
    options.addOption(arg("o", "output", "file", "file to write result to"));
  }
  
  private static Option arg(String name, String argName, String desc) {
    Option ret = new Option(null, true, desc);
    ret.setArgName(argName);
    ret.setLongOpt(name);
    return ret;
  }
  
  private static Option arg(String sht, String lng, String argName, String desc) {
    Option ret = new Option(sht, lng, true, desc);
    ret.setArgName(argName);
    return ret;
  }
  
  private static Option opt(String name, String desc) {
    Option ret = new Option(null, desc);
    ret.setLongOpt(name);
    return ret;
  }
  
  private boolean valid = true;
  private boolean printHelp = false;
  private String plot;
  private Format format = new Format();

  private File output;
  
  private static Range parseRange(String s) throws InputException {
    String[] p = s.split(":");
    if(p.length != 2) {
      throw new InputException("specify range as <min>:<max>, got " + s);
    }
    return new Range(Double.parseDouble(p[0]), Double.parseDouble(p[1]));
  }
  
  private static VectorD3 parseVector3(String s) throws InputException {
    String[] p = s.split(",");
    if(p.length != 3) {
      throw new InputException("specify vector as <x>,<y>,<z>, got " + s);
    }
    return new VectorD3(
        Double.parseDouble(p[0]),
        Double.parseDouble(p[1]),
        Double.parseDouble(p[2]));
  }
  
  public PlotOptions(String[] args) {
    
    try {
      CommandLine line = new GnuParser().parse(options, args);
      
      if(line.hasOption("help")) {
        printHelp = true;
        return;
      }
      
      String[] plots = line.getArgs();
      if(plots.length < 1) {
        System.err.println("specify an equation or function to plot");
        printHelp = true;
        return;
      } else {
        plot = plots[0];
      }
      
      if(line.hasOption("size")) {
        String[] s = line.getOptionValue("size").split("x");
        if(s.length != 2) {
          throw new InputException("specify size as <width>x<height>, got " + line.getOptionValue("size"));
        }
        format.resolution = new Resolution(
            Integer.parseInt(s[0]),
            Integer.parseInt(s[1]));
      }
      
      if(line.hasOption("range")) {
        String[] s = line.getOptionValue("range").split(",");
        if(s.length < 1 || s.length > 2) {
          throw new InputException("specify range as <xmin>-<xmax>[,<ymin>-<ymax>], got " + line.getOptionValue("range"));
        }
        format.xRange = parseRange(s[0]);
        if(s.length == 2) {
          format.yRange = parseRange(s[1]);
        }
      }
      
      if(line.hasOption("camera")) {
        format.cameraPosition = parseVector3(line.getOptionValue("camera"));
      }
      
      if(line.hasOption("lookat")) {
        format.cameraLookAt = parseVector3(line.getOptionValue("lookat"));
      }
      
      if(line.hasOption("up")) {
        format.cameraUp = parseVector3(line.getOptionValue("up"));
      }
      
      if(line.hasOption("fov")) {
        format.cameraFieldOfView = Double.parseDouble(line.getOptionValue("fov"));
      }
      
      if(line.hasOption("output")) {
        output = new File(line.getOptionValue("output"));
      }
    } catch(ParseException e) {
      System.err.println(e.getMessage());
      valid = false;
    } catch(InputException e) {
      System.err.println(e.getMessage());
      valid = false;
    }
  }
  
  public void start() {
    if(!valid || printHelp) {
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp("mathray \"<equation or function>\"", options);
      return;
    }
    if(!valid) {
      System.exit(1);
    }
    
    try {
      PlotCommand cmd = new PlotCommand(plot);
      Stopwatch watch = new Stopwatch();
      watch.start();
      Output out = cmd.plot(format);
      watch.stop();
      System.out.println("time to plot: " + watch.elapsedMillis() / 1000.0 + " seconds");
      if(output != null) {
        out.save(output);
      } else {
        out.show();
      }
    } catch (InputException e) {
      System.err.println(e.getMessage());
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
    
  }

}
