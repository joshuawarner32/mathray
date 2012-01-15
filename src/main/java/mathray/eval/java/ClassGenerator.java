package mathray.eval.java;


import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
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
  

  
  public MethodGenerator method(boolean static_, String name, String desc) {
    MethodGenerator ret = new MethodGenerator(static_, name, desc, classVisitor);
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
