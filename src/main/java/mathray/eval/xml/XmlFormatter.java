package mathray.eval.xml;

import java.io.StringWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

public class XmlFormatter {

  private XmlFormatter() {}

  public static String prettyPrint(Document doc) {
    Transformer transformer;
    try {
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      transformer = transformerFactory.newTransformer();
      transformerFactory.setAttribute("indent-number", 2);
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      //initialize StreamResult with File object to save to file
      StreamResult result = new StreamResult(new StringWriter());
      DOMSource source = new DOMSource(doc);
      transformer.transform(source, result);
    return result.getWriter().toString();
    } catch (TransformerConfigurationException e) {
      throw new RuntimeException(e);
    } catch (TransformerFactoryConfigurationError e) {
      throw new RuntimeException(e);
    } catch (TransformerException e) {
      throw new RuntimeException(e);
    }
  }

}