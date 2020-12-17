package clojure_starter;

import org.objectweb.asm.*;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SuppressWarnings("unused")
public class Tryasm {

    /**
     * This static method allows you to print the bytecode of a .class given a path
     *
     * @param path Path to a file
     * @throws IOException If opening will be wrong
     */
    @SuppressWarnings("unused")
    public static void printBytecode(Path path) throws IOException {

        class MyTraceMethodVisitor extends MethodVisitor {

            public MyTraceMethodVisitor(MethodVisitor mv) {
                super(Opcodes.ASM7, mv);
            }

            @Override
            public void visitMaxs(int maxStack, int maxLocals) {
            }
        }

        class MyClassVisitor extends ClassVisitor {
            public MyClassVisitor(ClassVisitor cv) {
                super(Opcodes.ASM7, cv);
            }

            @Override
            public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
                return null;
            }

            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                return new MyTraceMethodVisitor(super.visitMethod(access, name, desc, signature, exceptions));
            }
        }

        try (var input = Files.newInputStream(path)) {
            ClassReader classReader = new ClassReader(input);
            PrintWriter printWriter = new PrintWriter(System.out);
            TraceClassVisitor traceClassVisitor = new TraceClassVisitor(printWriter);
            MyClassVisitor myClassVisitor = new MyClassVisitor(traceClassVisitor);
            classReader.accept(myClassVisitor, ClassReader.SKIP_DEBUG);
        } catch (Exception e) {
            throw new IOException();
        }
    }

    //package org.objectweb.asm.util;

    /**
     * This method generates a test classfile for training purpose
     */
    @SuppressWarnings("unused")
    public static void generateHelloWorld() throws IOException {

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC,
                "AsmHelloWorld", null, "java/lang/Object", null);

        MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC
                + Opcodes.ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);

        mv.visitCode();

        mv.visitFieldInsn(Opcodes.GETSTATIC,
                "java/lang/System", "out", "Ljava/io/PrintStream;");

        mv.visitLdcInsn("Hello world!");

        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();

        /////////////////Constant stuff//////////////////

        MethodVisitor mv2 = cw.visitMethod(Opcodes.ACC_PUBLIC
                + Opcodes.ACC_STATIC, "test", "()I", null, null);

        mv2.visitCode();

        mv2.visitVarInsn(Opcodes.BIPUSH, 42);
        mv2.visitVarInsn(Opcodes.ISTORE, 0);
        mv2.visitVarInsn(Opcodes.ILOAD, 0);
        mv2.visitInsn(Opcodes.IRETURN);
        mv2.visitMaxs(1, 1);
        mv2.visitEnd();

        cw.visitEnd();

        byte[] bytes = cw.toByteArray();
        Path path = Paths.get("out/AsmHelloWorld.class");
        try (var stream = Files.newOutputStream(path)) {
            stream.write(bytes);
        } catch (Exception e) {
            throw new IOException();
        }
    }
}
