package mathray.eval.java;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class MethodGenerator {
  
  final MethodVisitor methodVisitor;
  private final MethodArg[] args;
  private final MethodArg thisRef;
  
  private int maxStack = 0;
  
  private State state = new State();
  
  MethodGenerator(boolean static_, String name, String desc, ClassGenerator cgen) {
    this.methodVisitor = cgen.classVisitor.visitMethod(Opcodes.ACC_PUBLIC | (static_ ? Opcodes.ACC_STATIC : 0), name, desc, null, null);
    methodVisitor.visitCode();
    Type[] argTypes = Type.getArgumentTypes(desc);
    args = new MethodArg[argTypes.length];
    if(static_) {
      thisRef = null;
    } else {
      state.locals.add(null);
      thisRef = new MethodArg(Type.getObjectType(cgen.name));
    }
    for(int i = 0; i < argTypes.length; i++) {
      findOrAllocLocalIndex(args[i] = new MethodArg(argTypes[i]));
    }
  }
  
  private void load(JavaValue... values) {
    int[] indices = new int[values.length];
    int min = Integer.MAX_VALUE;
    int run = 0;
    for(int i = 0; i < values.length; i++) {
      indices[i] = state.stack.lastIndexOf(values[i]);
      if(indices[i] >= 0 && indices[i] < min) {
        min = indices[i];
      }
      if(i == run + 1 && indices[i] == indices[i - 1] + 1) {
        run++;
      }
    }
    // TODO: check if we can swap
    if(min == indices[0]) {
      while(state.stack.size() > indices[0] + run + 1) {
        store();
      }
      for(int i = 0; i < run + 1; i++) {
        state.stack.pop();
      }
    } else {
      while(state.stack.size() > min) {
        store();
      }
      run = -1;
    }
    for(int i = run + 1; i < values.length; i++) {
      JavaValue val = values[i];
      val.load(this);
    }
  }
  
  private void store() {
    state.stack.pop().store(this);
  }
  
  JavaValue push(JavaValue val) {
    state.stack.push(val);
    if(state.stack.size() > maxStack) {
      maxStack = state.stack.size();
    }
    return val;
  }
  
  public JavaValue binOp(int opcode, JavaValue a, JavaValue b) {
    load(a, b);
    methodVisitor.visitInsn(opcode);
    return push(new ComputedValue(a.getType()));
  }

  public JavaValue unaryOp(int opcode, JavaValue a) {
    load(a);
    methodVisitor.visitInsn(opcode);
    return push(new ComputedValue(a.getType()));
  }
  
  public JavaValue arg(int index) {
    return args[index];
  }
  
  public JavaValue aload(JavaValue array, int index) {
    load(array, intConstant(index));
    methodVisitor.visitInsn(Opcodes.DALOAD);
    return push(new ComputedValue(Type.DOUBLE_TYPE));
  }
  
  public void astore(JavaValue array, int index, JavaValue value) {
    load(array, intConstant(index), value);
    methodVisitor.visitInsn(Opcodes.DASTORE);
  }
  
  public JavaValue intConstant(int value) {
    return new IntConstant(value);
  }
  
  public JavaValue doubleConstant(double value) {
    return new DoubleConstant(value);
  }

  public void ret() {
    methodVisitor.visitInsn(Opcodes.RETURN);
  }
  
  public void ret(JavaValue value) {
    load(value);
    methodVisitor.visitInsn(value.getType().getOpcode(Opcodes.IRETURN));
  }
  
  public void end() {
    methodVisitor.visitMaxs(maxStack + 10, state.locals.size());
    methodVisitor.visitEnd();
  }

  public JavaValue callStatic(String className, String name, String desc, JavaValue... args) {
    load(args);
    methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, className, name, desc);
    return push(new ComputedValue(Type.getReturnType(desc)));
  }
  
  public JavaValue getThis() {
    if(thisRef != null) {
      return thisRef;
    } else {
      throw new IllegalStateException("accessing this from static method");
    }
  }

  public void store(JavaValue value) {
    int index = state.stack.lastIndexOf(value);
    if(value.needsStore() && index >= 0) {
      while(state.stack.size() > index) {
        store();
      }
      
    }
  }
  
  public class State {
    private Stack<JavaValue> stack = new Stack<JavaValue>();
    private List<JavaValue> locals = new ArrayList<JavaValue>();
    
    public State() {}
    
    public State(State old) {
      stack.addAll(old.stack);
      locals.addAll(old.locals);
    }
  }
  
  public State save() {
    return new State(state);
  }
  
  public void restore(State state) {
    this.state = state;
  }

  public int getLocalIndex(JavaValue value) {
    return state.locals.indexOf(value);
  }

  public int findOrAllocLocalIndex(JavaValue value) {
    int index = state.locals.indexOf(value);
    if(index == -1) {
      index = state.locals.size();
      state.locals.add(value);
      if(value.getType().getSize() == 2) {
        state.locals.add(null);
      }
    }
    return index;
  }

  public void storeField(JavaValue obj, JavaField field, JavaValue value) {
    load(obj, value);
    field.store(this);
  }

  public JavaValue loadField(JavaValue obj, JavaField field) {
    load(obj);
    return field.load(this);
  }
  
}