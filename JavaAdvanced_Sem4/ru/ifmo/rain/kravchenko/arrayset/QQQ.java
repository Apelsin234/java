package ru.ifmo.rain.kravchenko.arrayset;

/**
 * Created by Alexander on 21.02.2018.
 */
public class QQQ {
    public static void main(String[] args) {
        int[] a = {1, 2, 3, 4, 5};
        int[] b = new int[3];
        System.arraycopy(a, 0, b, 0, 3);
        b[0] = 0;
        System.out.println(a[0]);
        System.out.println(b[0]);
    }
}
