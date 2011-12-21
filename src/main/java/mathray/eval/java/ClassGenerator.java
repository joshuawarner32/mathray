package mathray.eval.java;


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
    
    private final MethodVisitor methodVisitor;
    private final int[] argIndices;
    
    private int localVarIndex = 0;
    
    private MethodGenerator(boolean static_, String name, String desc) {
      this.methodVisitor = classVisitor.visitMethod(Opcodes.ACC_PUBLIC | (static_ ? Opcodes.ACC_STATIC : 0), name, desc, null, null);
      methodVisitor.visitCode();
      Type[] types = Type.getArgumentTypes(desc);
      argIndices = new int[types.length];
      if(!static_) {
        localVarIndex++;
      }
      for(int i = 0; i < types.length; i++) {
        argIndices[i] = localVarIndex;
        localVarIndex += types[i].getSize();
      }
    }
    
    private void loadD(JavaValue value) {
      methodVisitor.visitVarInsn(Opcodes.DLOAD, value.localVarIndex);
    }
    
    private void loadA(JavaValue value) {
      methodVisitor.visitVarInsn(Opcodes.ALOAD, value.localVarIndex);
    }
    
    private void loadInt(int value) {
      methodVisitor.visitLdcInsn(value);
    }
    
    private JavaValue store() {
      int index = localVarIndex;
      localVarIndex += 2;
      methodVisitor.visitVarInsn(Opcodes.DSTORE, index);
      return new JavaValue(index);
    }
    
    public JavaValue binOp(int opcode, JavaValue a, JavaValue b) {
      loadD(a);
      loadD(b);
      methodVisitor.visitInsn(opcode);
      return store();
    }

    public JavaValue unaryOp(int opcode, JavaValue a) {
      loadD(a);
      methodVisitor.visitInsn(opcode);
      return store();
    }
    
    public JavaValue arg(int index) {
      return new JavaValue(argIndices[index]);
    }
    
    public JavaValue aload(JavaValue array, int index) {
      loadA(array);
      loadInt(index);
      methodVisitor.visitInsn(Opcodes.DALOAD);
      return store();
    }
    
    public void astore(JavaValue array, int index, JavaValue value) {
      loadA(array);
      loadInt(index);
      loadD(value);
      methodVisitor.visitInsn(Opcodes.DASTORE);
    }

    public JavaValue cst(double value) {
      methodVisitor.visitLdcInsn(value);
      return store();
    }
    
    public void ret() {
      methodVisitor.visitInsn(Opcodes.RETURN);
    }
    
    public void end() {
      methodVisitor.visitMaxs(100, 100);
      methodVisitor.visitEnd();
    }

    public JavaValue callStatic(String className, String name, String desc, JavaValue... args) {
      for(JavaValue value : args) {
        loadD(value);
      }
      methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, className, name, desc);
      return store();
    }

    public JavaValue loadField(JavaValue obj, String class_, String name, String desc) {
      methodVisitor.visitFieldInsn(Opcodes.GETFIELD, class_, name, desc);
      return store();
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
