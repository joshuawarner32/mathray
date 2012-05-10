package mathray.cli;

public class Main {
  
  public static void main(String[] args) {
    
    new PlotOptions(args).start();
    
    // Exit immediately, without (possibly) waiting for AWT to get around to closing its thread
    System.exit(0);
  }

}
