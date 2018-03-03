package ru.ifmo.rain.kravchenko.arrayset;

import java.util.*;

/**
 * Created by Alexander on 18.02.2018.
 */
public class ArraySet2<E> extends AbstractSet<E> implements SortedSet<E> {
    private E[] container;
    private Comparator<? super E> comparator;
    private int from, to;

    public ArraySet2() {
        this((Comparator<E>) null);
    }

    @SuppressWarnings("unchecked")
    public ArraySet2(Comparator<? super E> cmp) {
        container = (E[]) new Object[0];
        from = to = 0;
    }

    public ArraySet2(Collection<? extends E> container) {
        this(container, null);
    }

    @SuppressWarnings("unchecked")
    public ArraySet2(Collection<? extends E> container, Comparator<? super E> cmp) {
        this();
        comparator = cmp;
        if (container.size() == 0) {
            return;
        }
        from = 0;
        to = 1;

        E[] buffer = (E[]) new Object[container.size()];
        container.toArray(buffer);
        Arrays.sort(buffer, cmp);
        for (int i = 1; i < container.size(); i++) {
            if (buffer[i] == null) {
                throw new NullPointerException();
            }
            if (!ek(buffer[i], buffer[i - 1])) {
                buffer[to++] = buffer[i];
            }
        }
        this.container = (E[]) new Object[to];
        System.arraycopy(buffer, 0, this.container, 0, to);

    }


    private ArraySet2(ArraySet2<E> parent, int from, int to) {
        this.from = from;
        this.to = to;
        this.container = parent.container;
        this.comparator = parent.comparator;
    }

    //AbstractCollection


    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
        return binarySearch((E) o) >= 0;
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }


    // AbstractSet


    @Override
    public Iterator<E> iterator() {
        return new MyIterator(container, from, to);
    }

    @Override
    public int size() {
        return to - from;
    }

    //SortedSet

    @Override
    public Comparator<? super E> comparator() {
        return comparator;
    }

    @Override
    public SortedSet<E> subSet(E fromElement, E toElement) {

        if (toElement == null || fromElement == null) {
            throw new NullPointerException();
        }
        int indFirst = findFromInd(fromElement);
        int indSecond = findToInd(toElement) + 1;
        if (indFirst > indSecond) {
            indSecond = indFirst;
        }
        return new ArraySet2<>(this, indFirst, indSecond);
    }

    @Override
    public SortedSet<E> headSet(E toElement) {
        if (toElement == null) {
            throw new NullPointerException();
        }
        return new ArraySet2<>(this, from, findToInd(toElement) + 1);
    }

    @Override
    public SortedSet<E> tailSet(E fromElement) {

        if (fromElement == null) {
            throw new NullPointerException();
        }
        return new ArraySet2<>(this, findFromInd(fromElement), to);
    }


    @Override
    public E first() {
        if (size() == 0) {
            throw new NoSuchElementException();
        }
        return container[from];
    }

    @Override
    public E last() {
        if (size() == 0) {
            throw new NoSuchElementException();
        }
        return container[to - 1];
    }

    private int findFromInd(E element) {
        int ind = binarySearch(element);
        if (ind < 0) {
            ind = ~ind;
        }
        return ind;
    }

    private int findToInd(E element) {
        int ind = binarySearch(element);
        if (ind < 0) {
            ind = ~ind - 1;
        } else {
            --ind;
        }
        return ind;
    }

    private int binarySearch(E element) {
        return Arrays.binarySearch(container, from, to, element, comparator);
    }

    private boolean ek(E a, E b) {
        if (comparator == null) {
            return a.equals(b);
        } else {
            return comparator.compare(a, b) == 0;
        }
    }

    //MyIterator

    private class MyIterator implements Iterator<E> {
        private E[] item;
        private int from, to;

        public MyIterator(E[] item, int from, int to) {
            this.item = item;
            this.from = from;
            this.to = to;
        }


        @Override
        public boolean hasNext() {
            return from < to;
        }

        @Override
        public E next() {
            return item[from++];
        }
    }

}
