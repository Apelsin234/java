package ru.ifmo.rain.kravchenko.walk;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.nio.file.Paths.get;

public class Walk {

    public static void main(String[] args) {
        Walk walk = new Walk();
        walk.start(args);
    }

    private void start(String[] args) {
        if (args == null || args[0] == null || args.length != 2 || args[1] == null) {
            System.err.println("Invalid args");
            return;
        }

        try (BufferedWriter writer = Files.newBufferedWriter(get(args[1]))) {
            try (BufferedReader reader = Files.newBufferedReader(get(args[0]))) {
                String s;
                while ((s = reader.readLine()) != null) {
                    execute(s, writer);
                }

            } catch (IOException e) {
                System.err.println("I/O error occurs opening the input file " + e.getMessage());
            } catch (SecurityException e) {
                System.err.println("Haven't access to read to input file" + e.getMessage());
            } catch (InvalidPathException e) {
                System.err.println("Path string input file cannot be converted to a Path " + e.getMessage());
            }
        } catch (IOException e) {
            System.err.println("I/O error occurs opening the output file " + e.getMessage());
        } catch (InvalidPathException e) {
            System.err.println("Path string output file cannot be converted to a Path " + e.getMessage());
        } catch (UnsupportedOperationException e) {
            System.err.println("An unsupported option is specified " + e.getMessage());
        } catch (SecurityException e) {
            System.err.println("Haven't access to write to output file" + e.getMessage());
        }
    }

    private void execute(String str, BufferedWriter writer) {
        try (Stream<Path> st = Files.walk(get(str))) {
            st.forEach(e -> writeFile(e, writer));
        } catch (IOException q) {
            String s = "I/O error is thrown when accessing the starting file " + q.getMessage();
            wasError(s, writer, str);
        } catch (InvalidPathException e) {
            String s = "Path string directory file cannot be converted to a Path " + e.getMessage();
            wasError(s, writer, str);
        } catch (SecurityException e) {
            System.err.println("Haven't access to read to the directory " + e.getMessage());
        }
    }

    private void writeFile(Path in, BufferedWriter writer) {
        try {
            if (!in.toFile().isDirectory()) {
                writer.write(getHash(in));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getHash(Path in) {
        int hash = 0x811c9dc5;
        boolean passed = false;
        int size = 1024;
        try (FileInputStream reader = new FileInputStream(in.toFile())) {
            byte[] buffer = new byte[size];
            int countRead;
            while ((countRead = reader.read(buffer)) != -1) {
                hash = hash(buffer, countRead, hash);
            }
            passed = true;
        } catch (FileNotFoundException e) {
            System.err.println("File with data not found " + e.getMessage());
        } catch (SecurityException e) {
            System.err.println("Haven't access to read to the file with data " + e.getMessage());
        } catch (UnsupportedOperationException e) {
            System.err.println("This Path is not associated with the default provider " + e.getMessage());
        } catch (IOException e) {
            System.err.println(" I/O error occurs " + e.getMessage());
        }
        return format("%08x %s\n", passed ? hash : 0, in.toString());
    }

    private int hash(final byte[] bytes, int countRead, int hash) {
        for (int i = 0; i < countRead; i++) {
            hash = (hash * 0x01000193) ^ (bytes[i] & 0xff);
        }
        return hash;
    }

    private void wasError(String s, BufferedWriter bf, String nameFile) {
        System.err.println(s);
        try {
            bf.write(format("%08x %s\n", 0, nameFile));
        } catch (IOException q) {
            System.err.println("I/O error occurs, can't write to the output file " + q.getMessage());
        }
    }
}
