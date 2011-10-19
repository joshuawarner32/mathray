package mathray.eval.java;

import mathray.Definition;
import mathray.FunctionD;
import mathray.eval.Visitor;
import mathray.eval.Visitors;
import mathray.eval.java.ClassGenerator.MethodGenerator;

import static mathray.Expressions.*;

public class JavaCompiler {
  
  public static FunctionD compile(Definition def) {
    
    ClassGenerator gen = new ClassGenerator(JavaArityGenerator.CLASS_NAME, new String[] {FunctionD.class.getName().replace('.', '/')});
    MethodGenerator mgen = gen.method(false, "call", "([D[D)V");
    JavaValue[] args = new JavaValue[def.args.size()];
    for(int i = 0; i < args.length; i++) {
      args[i] = mgen.aload(mgen.arg(0), i);
    }
    Visitor<JavaValue> v = Visitors.bind(new JavaVisitor(mgen), def.args, vector(args));
    for(int i = 0; i < def.values.size(); i++) {
      mgen.astore(mgen.arg(1), i, def.values.get(i).accept(v));
    }
    mgen.ret();
    mgen.end();
    gen.end();
    
    try {
      return (FunctionD)gen.load().newInstance();
    } catch (InstantiationException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

}
