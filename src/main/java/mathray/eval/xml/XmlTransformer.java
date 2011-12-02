package mathray.eval.xml;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import mathray.Call;
import mathray.Rational;
import mathray.Value;
import mathray.Symbol;
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
    public Element call(Call call) {
      Element el = doc.createElement("call");
      el.setAttribute("name", call.func.fullName());
      for(Element arg : call.visitArgs(this)) {
        el.appendChild(arg);
      }
      return el;
    }

    @Override
    public Element symbol(Symbol var) {
      Element el = doc.createElement("var");
      el.setAttribute("name", var.name);
      return el;
    }

    @Override
    public Element constant(Rational r) {
      Element el = doc.createElement("rational");
      el.setAttribute("value", r.toString());
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
