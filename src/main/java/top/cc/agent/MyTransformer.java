package top.cc.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import org.objectweb.asm.*;

public class MyTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if(!className.startsWith("java/lang")){
            return classfileBuffer;
        }
        try {
            ClassReader classReader = new ClassReader(classfileBuffer);
            ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
            ClassVisitor myClassVisitor = new MyClassVisitor(classWriter);
            classReader.accept(myClassVisitor,ClassReader.EXPAND_FRAMES);
            return classWriter.toByteArray();

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    private static class MyClassVisitor extends ClassVisitor {

        public MyClassVisitor(ClassWriter classWriter) {
            super(Opcodes.ASM7,classWriter);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
            if(name.equals("toString")){
                return new MyMethodVisitor(methodVisitor);
            }
            return methodVisitor;
        }
    }

    private static class MyMethodVisitor extends MethodVisitor {

        public MyMethodVisitor(MethodVisitor methodVisitor) {
            super(Opcodes.ASM7,methodVisitor);
        }

        @Override
        public void visitCode() {
                super.visitFieldInsn(Opcodes.GETSTATIC,"java/lang/System","out","Ljava/io/PrintStream;");
                super.visitLdcInsn("");
                super.visitMethodInsn(Opcodes.INVOKEVIRTUAL,"Ljava/io/PrintStream","println","(Ljava/lang/String;)V",false);
                super.visitCode();
        }
    }
}
