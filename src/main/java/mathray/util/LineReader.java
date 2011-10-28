package mathray.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LineReader {
  
  private BufferedReader reader;
  
  public LineReader(InputStream stream) {
    reader = new BufferedReader(new InputStreamReader(stream));
  }
  
  public String readLine() {
    try {
      return reader.readLine();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
