package mathray.cli;

import java.io.File;
import java.io.IOException;

import mathray.plot.Format;
import mathray.plot.Output;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class PlotOptions {
  
  private static Options options = new Options();
  
  static {
    options.addOption(opt("help", "print this message"));
    options.addOption(arg("plot", "equation", "function or equation to plot"));
    options.addOption(arg("w", "width", "width", "width of output in pixels"));
    options.addOption(arg("h", "height", "height", "height of output in pixels"));
    options.addOption(arg("xa", "value", "lowest (left-most) x value visible in output"));
    options.addOption(arg("xb", "value", "highest (right-most) x value visible in output"));
    options.addOption(arg("ya", "value", "lowest y value visible in output (near bottom)"));
    options.addOption(arg("yb", "value", "highest y value visible in output (near top)"));
    
    options.addOption(arg("output", "file", "file to write result to"));
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
  private Double xa;
  private Double xb;
  private Double ya;
  private Double yb;

  private File output;
  
  public PlotOptions(String[] args) {
    
    try {
      CommandLine line = new GnuParser().parse(options, args);
      
      if(line.hasOption("help")) {
        printHelp = true;
        return;
      }
      
      if(!line.hasOption("plot")) {
        System.err.println("specify an equation or function to plot");
        printHelp = true;
        return;
      } else {
        plot = line.getOptionValue("plot");
      }
      
      if(line.hasOption("xa")) {
        xa = Double.parseDouble(line.getOptionValue("xa"));
      }
      if(line.hasOption("xb")) {
        xb = Double.parseDouble(line.getOptionValue("xb"));
      }
      if(line.hasOption("ya")) {
        ya = Double.parseDouble(line.getOptionValue("ya"));
      }
      if(line.hasOption("yb")) {
        yb = Double.parseDouble(line.getOptionValue("yb"));
      }
      
      if(line.hasOption("width")) {
        format.width = Integer.parseInt(line.getOptionValue("width"));
      }
      if(line.hasOption("height")) {
        format.height = Integer.parseInt(line.getOptionValue("height"));
      }
      
      if(line.hasOption("output")) {
        output = new File(line.getOptionValue("output"));
      }
    } catch(ParseException e) {
      System.err.println(e.getMessage());
      valid = false;
    }
  }
  
  public void start() {
    if(!valid || printHelp) {
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp("mathray --plot \"<equation or function>\"", options);
      return;
    }
    if(!valid) {
      System.exit(1);
    }
    
    try {
      PlotCommand cmd = new PlotCommand(plot);
      Output out = cmd.plot(format);
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
