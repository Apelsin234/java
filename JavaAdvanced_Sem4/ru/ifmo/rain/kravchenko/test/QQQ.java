package ru.ifmo.rain.kravchenko.test;

import java.lang.reflect.Array;

/**
 * Created by Alexander on 21.02.2018.
 */
public class QQQ<T> {
    private T[] array ;
    private T[] copyOf(T[] a, int lenNew) {
        Class cl = a.getClass();
        if (!cl.isArray()) return null;
        Class type = cl.getComponentType();

        int lenOld = Array.getLength(a);
        @SuppressWarnings("unchecked")
        T[] newA = (T[])Array.newInstance(type, lenNew);
        if(lenOld != 0)
        System.arraycopy(a, 0, newA, 0, Math.min(lenNew, lenOld));
        return newA;
    }
    void foo(int n){
       array = copyOf(array, 0);
       array = copyOf(array, n);
    }


}
