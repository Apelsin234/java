package ru.ifmo.rain.kravchenko.implementor;

import info.kgeorgiy.java.advanced.implementor.Impler;
import info.kgeorgiy.java.advanced.implementor.ImplerException;
import info.kgeorgiy.java.advanced.implementor.JarImpler;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;


import static java.lang.Class.forName;
import static java.lang.String.format;
import static java.lang.System.getProperty;
import static java.lang.reflect.Modifier.*;
import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.newBufferedWriter;
import static java.nio.file.Paths.get;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

/**
 * Provides implementation for interfaces {@link Impler} and {@link JarImpler}.
 */
public class Implementor implements JarImpler {

    private static final String INDENT = "    ";

    public static void main(String[] args) {
        Implementor i = new Implementor();
        try {
            if (args[0].equals("-jar")) {
                i.implementJar(forName(args[1]), get(args[2]));
            } else {
                i.implement(forName(args[0]), get(args[1]));
            }
        } catch (ImplerException e) {
            throw new RuntimeException("ImplerException was occurred", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Class not found", e);
        }
    }

    /**
     * Produces code implementing class or interface specified by provided <tt>token</tt>.
     * <p>
     * Generated class full name should be same as full name of the type token with <tt>Impl</tt> suffix
     * added. Generated source code should be placed in the correct subdirectory of the specified
     * <tt>root</tt> directory and have correct file name. For example, the implementation of the
     * interface {@link java.util.List} should go to <tt>$root/java/util/ListImpl.java</tt>
     *
     *
     * @param token type token to create implementation for.
     * @param root root directory.
     * @throws info.kgeorgiy.java.advanced.implementor.ImplerException when implementation cannot be
     * generated.
     */
    @Override
    public void implement(Class<?> token, Path root) throws ImplerException {

        if (token == null || root == null) {
            throw new NullPointerException("token or root is null");
        }
        if (!token.isInterface()) {
            throw new ImplerException("It isn't Interface.");
        }

        Path folderPath = root.resolve(getPackagePath(token));
        String pack = token.getPackage().getName();
        Path filePath = folderPath.resolve(token.getSimpleName() + "Impl.java");

        try {
            createDirectories(folderPath);
        } catch (IOException e) {
            throw new ImplerException("Can't create file.");
        }


        try (BufferedWriter writer = newBufferedWriter(filePath)) {
            writePackage(writer, pack);
            writeTitle(writer, token);
            writeMethods(writer, token.getMethods());
            writer.write("}");
        } catch (IOException e) {
            throw new ImplerException(e);
        }

    }

    /**
     * Write to <tt>writer</tt> package string <tt>packageStr</tt> if it's length equals 0
     * then write empty string.
     *
     * @param writer object writer to buffer.
     * @param packageStr package string.
     * @throws IOException when <tt>writer</tt> can't write.
     */
    private void writePackage(BufferedWriter writer, String packageStr) throws IOException {
        String out = packageStr.length() > 0 ? format("package %s;", packageStr) : "";
        writer.write(format("%s\n\n", out));
    }

    /**
     * Write to <tt>writer</tt> class <tt>token</tt>  name with modifiers.
     *
     * @param writer object writer to buffer.
     * @param token class that need to implement.
     * @throws IOException when <tt>writer</tt> can't write.
     */
    private void writeTitle(BufferedWriter writer, Class<?> token) throws IOException {
        String interfaceName = token.getSimpleName();
        String className = interfaceName + "Impl";
        int mod = token.getModifiers();
        if (isAbstract(mod)) {
            mod -= ABSTRACT;
        }
        if (isInterface(mod)) {
            mod -= INTERFACE;
        }
        String out = format("%s class %s implements %s {\n", Modifier.toString(mod), className, interfaceName);
        writer.write(out);
    }

    /**
     * Write to <tt>writer</tt> methods <tt>methods</tt> .
     *
     * @param writer object writer to buffer.
     * @param methods methods that need to implement.
     * @throws IOException when <tt>writer</tt> can't write.
     */
    private void writeMethods(BufferedWriter writer, Method[] methods) throws IOException {
        for (Method md : methods) {
            if (md.isDefault()) {
                continue;
            }
            writeAnnotations(writer, md.getAnnotations());
            int mod = md.getModifiers();
            if (isAbstract(mod)) {
                mod -= ABSTRACT;
            }
            if (isTransient(mod)) {
                mod -= TRANSIENT;
            }

            String out = format("\n%s%s %s %s", INDENT, Modifier.toString(mod), md.getReturnType().getCanonicalName(), md.getName());
            writer.write(out);

            writeArgs(writer, md.getParameters());
            writeExceptions(writer, md.getExceptionTypes());
            writeBody(writer, md.getReturnType());

        }
    }

    /**
     * Write to <tt>writer</tt> annotations <tt>parameters</tt>  .
     *
     * @param writer object writer to buffer.
     * @param parameters annotations method or class that need to implement.
     * @throws IOException when <tt>writer</tt> can't write.
     */
    private void writeAnnotations(BufferedWriter writer, Annotation[] parameters) throws IOException {

        writer.write(stream(parameters).map(par -> "@" + par.annotationType().getCanonicalName())
                .collect(joining(System.lineSeparator())) + System.lineSeparator());

    }

    /**
     * Write to <tt>writer</tt> arguments methods <tt>parameters</tt>  .
     *
     * @param writer object writer to buffer.
     * @param parameters arguments method that need to implement.
     * @throws IOException when <tt>writer</tt> can't write.
     */
    private void writeArgs(BufferedWriter writer, Parameter[] parameters) throws IOException {
        writer.write("(");
        writer.write(stream(parameters).map(par -> par.getType().getCanonicalName() + " " + par.getName())
                .collect(joining(", ")));
        writer.write(")");
    }

    /**
     * Write to <tt>writer</tt> exceptions <tt>parameters</tt>  .
     *
     * @param writer object writer to buffer.
     * @param parameters exceptions method that need to implement.
     * @throws IOException when <tt>writer</tt> can't write.
     */
    private void writeExceptions(BufferedWriter writer, Class<?>[] parameters) throws IOException {
        if (parameters.length == 0) {
            return;
        }
        writer.write(" throws ");
        writer.write(stream(parameters).map(Class::getCanonicalName).collect(joining(", ")));
    }

    /**
     * Write to <tt>writer</tt> body methods <tt>parameters</tt>  .
     *
     * @param writer object writer to buffer.
     * @param returnType type that return method where this run.
     * @throws IOException when <tt>writer</tt> can't write.
     */
    private void writeBody(BufferedWriter writer, Class<?> returnType) throws IOException {
        writer.write(" {\n");
        if (returnType != void.class) {
            String retType;
            if (returnType == boolean.class) {
                retType = "false";
            } else if (returnType.isPrimitive()) {
                retType = "0";
            } else {
                retType = "null";
            }
            String out = format("%s%sreturn %s;", INDENT, INDENT, retType);
            writer.write(out);
        }
        writer.write(format("\n%s}\n", INDENT));

    }


    /**
     * Generates string representation of relative path to folder where class source should be placed
     * based on it's package.
     * <p>
     * For instance, {@code getPackagePath(java.util.List.class)} will produce "java/util".
     * @param token type token to find location for.
     * @return determined relative path.
     */
    private String getPackagePath(Class<?> token) {
        return token.getPackage() != null ? token.getPackage().getName().replace('.', '/') : "";
    }


    /**
     * Produces <tt>.jar</tt> file implementing class or interface specified by provided <tt>token</tt>.
     * <p>
     * Generated class full name should be same as full name of the type token with <tt>Impl</tt> suffix
     * added.
     *
     * @param token type token to create implementation for.
     * @param jarFile target <tt>.jar</tt> file.
     * @throws ImplerException when implementation cannot be generated.
     */
    @Override
    public void implementJar(Class<?> token, Path jarFile) throws ImplerException {
        Path tmp = get(getProperty("user.home")).resolve(".implementor");

        implement(token, tmp);

        Path folder = tmp.resolve(getPackagePath(token));
        Path sourcePath = folder.resolve(token.getSimpleName() + "Impl.java");
        Path classPath = folder.resolve(token.getSimpleName() + "Impl.class");

        compile(sourcePath.toString());

        Manifest manifest = new Manifest();
        manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
        try (JarOutputStream jarOutputStream = new JarOutputStream(Files.newOutputStream(jarFile), manifest);
             FileInputStream inputStream = new FileInputStream(classPath.toString())) {

            String classZipPath = getPackagePath(token) + '/' + token.getSimpleName() + "Impl.class";

            jarOutputStream.putNextEntry(new JarEntry(classZipPath));

            byte[] buffer = new byte[1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                jarOutputStream.write(buffer, 0, read);
            }

            jarOutputStream.closeEntry();
        } catch (IOException e) {
            throw new ImplerException(e);
        } finally {
            clean(tmp);
        }

    }

    /**
     * Clean derictores that situated to path <tt>tmp</tt> .
     *
     * @param tmp path where need clean derictory.
     */
    private void clean(Path tmp) {
        try {
            Files.walkFileTree(tmp, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Compile class file from java file that sit on path <tt>sourcePath</tt>.
     *
     * @param sourcePath java file that need compile.
     * @throws ImplerException if when compile error was occurred .
     */
    private void compile(String sourcePath) throws ImplerException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        int exitCode = compiler.run(null, null, null, sourcePath);
        if (exitCode != 0) {
            throw new ImplerException("Error compile with exitCode is " + exitCode);
        }
    }

}
