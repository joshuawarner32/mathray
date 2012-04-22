package mathray.cli;

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
  
  public PlotOptions(String[] args) {
    
    try {
      CommandLine line = new GnuParser().parse(options, args);
      
      if(line.hasOption("help")) {
        printHelp = true;
        return;
      }
    } catch(ParseException e) {
      System.err.println(e.getMessage());
      valid = false;
    }
  }
  
  public void start() {
    if(printHelp) {
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp("mathray --plot \"<equation or function>\"", options);
      return;
    }
    if(!valid) {
      System.exit(1);
    }
    
  }

}
