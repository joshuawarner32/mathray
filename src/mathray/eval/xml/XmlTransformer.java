package mathray.eval.xml;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import mathray.Call;
import mathray.Constant;
import mathray.Value;
import mathray.Variable;
import mathray.Vector;
import mathray.eval.Visitor;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XmlTransformer {
  
  private static class XmlVisitor implements Visitor<Element> {
    private final Document doc;
    
    XmlVisitor(Document doc) {
      this.doc = doc;
    }

    @Override
    public Vector<Element> call(Visitor<Element> v, Call call) {
      Element el = doc.createElement("call");
      el.setAttribute("name", call.func.fullName());
      for(Element arg : call.visitArgs(v)) {
        el.appendChild(arg);
      }
      return new Vector<Element>(el);
    }

    @Override
    public Element variable(Variable var) {
      Element el = doc.createElement("var");
      el.setAttribute("name", var.name);
      return el;
    }

    @Override
    public Element constant(Constant cst) {
      Element el = doc.createElement("cst");
      el.setAttribute("value", String.valueOf(cst.value));
      return el;
    }
    
  }

  public static Element toXml(Document doc, Value value) {
    return value.accept(new XmlVisitor(doc));
  }
  
  public static String toXmlString(Value value) { 
    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder;
    try {
      docBuilder = docFactory.newDocumentBuilder();
    } catch (ParserConfigurationException e) {
      throw new RuntimeException(e);
    }
    Document doc = docBuilder.newDocument();
    doc.appendChild(toXml(doc, value));
    return XmlFormatter.prettyPrint(doc);
  }
}
