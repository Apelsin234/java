package ru.ifmo.rain.kravchenko.implementor;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Created by Alexander on 11.03.2018.
 */
public interface Test {

    boolean a(String[] o, String p) throws IOException, RuntimeException;

    int b(String[] o, String p);

    long bd(Long o, long p);

    String[] c(String[] o, String p);

    Double[] s(String[] o, String p);

    Method testMethod(Integer o, int p, Character q, char qw);
}
