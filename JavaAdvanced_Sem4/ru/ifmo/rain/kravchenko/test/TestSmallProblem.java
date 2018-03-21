package ru.ifmo.rain.kravchenko.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Alexander on 10.03.2018.
 */
public class TestSmallProblem {
    public static void main(String[] args) {
        ArrayList<Integer> a = new ArrayList<>();
        a.add(1);
        a.add(2);
        a.add(77777);
        ArrayList<Integer> b = new ArrayList<>();
        b.add(4);
        b.add(5);
        b.add(6);
        ArrayList<Integer> c = new ArrayList<>();
        c.add(7);
        c.add(8);
        c.add(9);
        List<Iterator<Integer>> itr = new ArrayList<>(3);
        itr.add(a.iterator());
        itr.add(b.iterator());
        itr.add(c.iterator());
        Iterator<Integer> as = new SmallProblem<Integer>().getCommonIterator(itr);
        while (as.hasNext()) {
            System.out.println(as.next());
        }

    }
}
