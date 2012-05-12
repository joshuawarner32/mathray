package mathray.cli;

import mathray.eval.text.ParseException;

@SuppressWarnings("serial")
public class InputException extends Exception {

  public InputException(String message) {
    super(message);
  }

  public InputException(Throwable cause) {
    super(cause);
  }
}
