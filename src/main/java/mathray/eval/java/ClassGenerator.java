package mathray.eval.java;


import java.util.Stack;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.util.CheckClassAdapter;

class ClassGenerator {
  private ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
  
  private ClassVisitor classVisitor = new CheckClassAdapter(cw);
  //private ClassVisitor classVisitor = new org.objectweb.asm.util.TraceClassVisitor(new java.io.PrintWriter(System.out));
  private String name;
  
  public ClassGenerator(String name, String[] interfaces) {

    classVisitor.visit(Opcodes.V1_6,
        Opcodes.ACC_PUBLIC,
        name,
        null,
        "java/lang/Object",
        interfaces);
    
    this.name = name;
    
    generateClinit();
    generateInit();
  }
  
  private void generateClinit() {

    MethodVisitor methodVisitor = classVisitor.visitMethod(Opcodes.ACC_PUBLIC, "<clinit>", "()V", null, null);
    methodVisitor.visitCode();
    methodVisitor.visitInsn(Opcodes.RETURN);
    methodVisitor.visitMaxs(1, 1);
    methodVisitor.visitEnd();
    
  }
  
  private void generateInit() {
    
    MethodVisitor methodVisitor = classVisitor.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
    methodVisitor.visitCode();
    methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
    methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
    methodVisitor.visitInsn(Opcodes.RETURN);
    methodVisitor.visitMaxs(1, 1);
    methodVisitor.visitEnd();
    
  }
  
  public class MethodGenerator {
    
    final MethodVisitor methodVisitor;
    private final int[] argIndices;
    private final Type[] argTypes;
    
    int localVarIndex = 0;
    
    private Stack<JavaValue> stack = new Stack<JavaValue>();
    
    private MethodGenerator(boolean static_, String name, String desc) {
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
        indices[i] = stack.indexOf(values[i]);
        if(indices[i] >= 0 && indices[i] < min) {
          min = indices[i];
        }
        if(i == run + 1 && indices[i] == indices[i - 1] + 1) {
          run++;
        }
      }
      // TODO: check if we can swap
      while(stack.size() > min) {
        store();
      }
      for(JavaValue val : values) {
        stack.push(val);
        val.load(methodVisitor);
      }
    }
    
    private void store() {
      stack.pop().store(this);
    }
    
    public JavaValue binOp(int opcode, JavaValue a, JavaValue b) {
      load(a, b);
      methodVisitor.visitInsn(opcode);
      return stack.push(new ComputedValue(-1, a.getType()));
    }

    public JavaValue unaryOp(int opcode, JavaValue a) {
      load(a);
      methodVisitor.visitInsn(opcode);
      return stack.push(new ComputedValue(-1, a.getType()));
    }
    
    public JavaValue arg(int index) {
      return new ComputedValue(argIndices[index], argTypes[index]);
    }
    
    public JavaValue aload(JavaValue array, int index) {
      load(array, intConstant(index));
      methodVisitor.visitInsn(Opcodes.DALOAD);
      return stack.push(new ComputedValue(-1, Type.DOUBLE_TYPE));
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
      methodVisitor.visitInsn(Opcodes.DRETURN);
    }
    
    public void end() {
      methodVisitor.visitMaxs(100, 100);
      methodVisitor.visitEnd();
    }

    public JavaValue callStatic(String className, String name, String desc, JavaValue... args) {
      load(args);
      methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, className, name, desc);
      return stack.push(new ComputedValue(-1, Type.getReturnType(desc)));
    }

    public JavaValue loadField(JavaValue obj, String class_, String name, String desc) {
      methodVisitor.visitFieldInsn(Opcodes.GETFIELD, class_, name, desc);
      return stack.push(new ComputedValue(-1, Type.getType(desc)));
    }

    public void store(JavaValue value) {
      int index = stack.indexOf(value);
      if(index >= 0) {
        while(stack.size() >= index) {
          store();
        }
      }
    }

    public ComputedValue allocateLocal(Type type) {
      int ret = localVarIndex;
      localVarIndex += type.getSize();
      return new ComputedValue(ret, type);
    }

    public void store(JavaValue value, ComputedValue slot) {
      value.load(methodVisitor);
      value.forceStore(methodVisitor, slot.localVarIndex);
    }
    
    private class State {
      private Stack<JavaValue> stack = new Stack<JavaValue>();
      
      {
        for(JavaValue v : MethodGenerator.this.stack) {
          stack.add(v);
        }
      }

      private void restore() {
        MethodGenerator.this.stack = stack;
      }
    }
    
    private State save() {
      return new State();
    }
    
  }
  
  public MethodGenerator method(boolean static_, String name, String desc) {
    MethodGenerator ret = new MethodGenerator(static_, name, desc);
    return ret;
  }
  
  public void end() {
    classVisitor.visitEnd();
  }

  public Class<?> load() {
    try {
      Class<?> c = new ClassLoader() {
        public Class<?> loadClass(String name) throws ClassNotFoundException {
          byte[] arr = cw.toByteArray();
          if(name.equals(ClassGenerator.this.name.replace('/', '.'))) {
            return defineClass(name, arr, 0, arr.length);
          }
          return super.loadClass(name);
        }
      }.loadClass(name.replace('/', '.'));
      
      return c;
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }
}
