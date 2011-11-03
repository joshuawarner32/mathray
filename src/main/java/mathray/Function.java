package mathray;

import static mathray.Expressions.*;

public class Function implements Comparable<Function> {
  
  public final String name;
  
  public final int inputArity;
  
  public final int outputArity;
  
  public final Vector<String> individualNames;
  
  private final SelectFunction[] selectFunctions;
  
  private Definition definition; // created lazily
  
  public Function(String name, int inputArity, int outputArity) {
    this(name, inputArity, outputArity, defaultNames(name, outputArity));
  }
  
  public Function(String name, int inputArity, int outputArity, Vector<String> individualNames) {
    this.name = name;
    this.inputArity = inputArity;
    this.outputArity = outputArity;
    this.individualNames = individualNames;
    selectFunctions = new SelectFunction[outputArity];
    for(int i = 0; i < outputArity; i++) {
      selectFunctions[i] = new SelectFunction(this, i);
    }
  }
  
  private static Vector<String> defaultNames(String name, int count) {
    if(count == 1) {
      return new Vector<String>(name);
    } else {
      String[] names = new String[count];
      for(int i = 0; i < count; i++) {
        names[i] = name + "_" + count;
      }
      return new Vector<String>(names);
    }
  }

  public String fullName() {
    return name + "/" + inputArity + "-" + outputArity;
  }
  
  public Call call(Vector<Value> vector) {
    return new Call(this, vector);
  }

  public Call call(Value... values) {
    return call(Expressions.vector(values));
  }

  public int compareTo(Function func) {
    int d = inputArity - func.inputArity;
    if(d != 0) {
      return d;
    }
    d = outputArity - func.outputArity;
    if(d != 0) {
      return d;
    }
    d = name.compareTo(func.name);
    if(d != 0) {
      return d;
    }
    return hashCode() - func.hashCode();
  }

  public SelectFunction select(int i) {
    return selectFunctions[i];
  }

  public Definition define() {
    if(definition == null) {
      Args args = args(inputArity);
      final Vector<Value> vargs = args.toValueVector();
      definition = new Definition(args, Vector.<Value>generate(outputArity, new Generator() {
        @Override
        public Object generate(int index) {
          return call(vargs);
        }
      }));
    }
    return definition;
  }

  // Purposefully not overriding hashCode and equals - we want identity comparisons.
  
}
