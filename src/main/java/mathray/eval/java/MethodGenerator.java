package mathray.eval.java;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class MethodGenerator {
  
  final MethodVisitor methodVisitor;
  private final int[] argIndices;
  private final Type[] argTypes;
  
  int localVarIndex = 0;
  
  private State state = new State();
  
  MethodGenerator(boolean static_, String name, String desc, ClassVisitor classVisitor) {
    this.methodVisitor = classVisitor.visitMethod(Opcodes.ACC_PUBLIC | (static_ ? Opcodes.ACC_STATIC : 0), name, desc, null, null);
    methodVisitor.visitCode();
    argTypes = Type.getArgumentTypes(desc);
    argIndices = new int[argTypes.length];
    if(!static_) {
      localVarIndex++;
    }
    for(int i = 0; i < argTypes.length; i++) {
      argIndices[i] = localVarIndex;
      localVarIndex += argTypes[i].getSize();
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
      val.load(methodVisitor);
    }
  }
  
  private void store() {
    state.stack.pop().store(this);
  }
  
  public JavaValue binOp(int opcode, JavaValue a, JavaValue b) {
    load(a, b);
    methodVisitor.visitInsn(opcode);
    return state.stack.push(new ComputedValue(-1, a.getType()));
  }

  public JavaValue unaryOp(int opcode, JavaValue a) {
    load(a);
    methodVisitor.visitInsn(opcode);
    return state.stack.push(new ComputedValue(-1, a.getType()));
  }
  
  public JavaValue arg(int index) {
    return new ComputedValue(argIndices[index], argTypes[index]);
  }
  
  public JavaValue aload(JavaValue array, int index) {
    load(array, intConstant(index));
    methodVisitor.visitInsn(Opcodes.DALOAD);
    return state.stack.push(new ComputedValue(-1, Type.DOUBLE_TYPE));
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
    methodVisitor.visitMaxs(100, 100);
    methodVisitor.visitEnd();
  }

  public JavaValue callStatic(String className, String name, String desc, JavaValue... args) {
    load(args);
    methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, className, name, desc);
    return state.stack.push(new ComputedValue(-1, Type.getReturnType(desc)));
  }

  public JavaValue loadField(JavaValue obj, String class_, String name, String desc) {
    load(obj);
    methodVisitor.visitFieldInsn(Opcodes.GETFIELD, class_, name, desc);
    return state.stack.push(new ComputedValue(-1, Type.getType(desc)));
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
  
}