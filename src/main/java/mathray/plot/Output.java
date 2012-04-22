package mathray.plot;

import java.io.File;
import java.io.IOException;

public interface Output {
  
  public void save(File f) throws IOException;
  
  public void show();

}
