package ru.ifmo.rain.kravchenko.arrayset;

import java.util.*;

public class ArraySet3<E> extends AbstractSet<E> implements NavigableSet<E> {
    private E[] container;
    private Comparator<? super E> comparator;
    private int from, to;

    public ArraySet3() {
        this((Comparator<E>) null);
    }

    @SuppressWarnings("unchecked")
    public ArraySet3(Comparator<? super E> cmp) {
        container = (E[]) new Object[0];
        from = to = 0;
    }

    public ArraySet3(Collection<? extends E> container) {
        this(container, null);
    }

    @SuppressWarnings("unchecked")
    public ArraySet3(Collection<? extends E> container, Comparator<? super E> cmp) {
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

    private ArraySet3(ArraySet3<E> parent, int from, int to) {
        this.from = from;
        this.to = to;
        this.container = parent.container;
        this.comparator = parent.comparator;
    }

    private ArraySet3(E[] es, Comparator<? super E> cmp) {
        container = es;
        comparator = cmp;
        from = 0;
        to = es.length;
    }

    //AbstractCollection

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object o) {
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

        return subSet(fromElement, true, toElement, false);
    }

    @Override
    public SortedSet<E> headSet(E toElement) {
        return headSet(toElement, false);
    }

    @Override
    public SortedSet<E> tailSet(E fromElement) {
        return tailSet(fromElement, true);
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

    //NavigableSet

    @Override
    public E lower(E e) {
        int ind = findToInd(e, false);
        return ind >= from && ind < to ? container[ind] : null;
    }

    @Override
    public E floor(E e) {
        int ind = findToInd(e, true);
        return ind >= from && ind < to ? container[ind] : null;
    }

    @Override
    public E ceiling(E e) {
        int ind = findFromInd(e, true);
        return ind >= from && ind < to ? container[ind] : null;
    }

    @Override
    public E higher(E e) {
        int ind = findFromInd(e, false);
        return ind >= from && ind < to ? container[ind] : null;
    }

    @Override
    public E pollFirst() {
        throw new UnsupportedOperationException();

    }

    @Override
    public E pollLast() {
        throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("unchecked")
    public NavigableSet<E> descendingSet() {
        return new ArraySet3<>(copyOfAndReverse(container), Collections.reverseOrder(comparator));
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new MyIterator(container, from, to, true);
    }

    @Override
    public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
        int indFirst = findFromInd(fromElement, fromInclusive);
        int indSecond = findToInd(toElement, toInclusive) + 1;
        if (indFirst > indSecond) {
            indSecond = indFirst;
        }
        return new ArraySet3<>(this, indFirst, indSecond);
    }

    @Override
    public NavigableSet<E> headSet(E toElement, boolean inclusive) {
        return new ArraySet3<>(this, from, findToInd(toElement, inclusive) + 1);
    }

    @Override
    public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
        return new ArraySet3<>(this, findFromInd(fromElement, inclusive), to);
    }

    //MyIterator

    private class MyIterator implements Iterator<E> {
        private E[] item;
        private int from, to;
        private boolean reverse;

        public MyIterator(E[] item, int from, int to) {
            this(item, from, to, false);
        }

        public MyIterator(E[] item, int from, int to, boolean reverse) {
            this.item = item;
            this.from = from;
            this.to = to;
            this.reverse = reverse;
        }


        @Override
        public boolean hasNext() {
            return from < to;
        }

        @Override
        public E next() {
            return reverse ? item[--to] : item[from++];
        }
    }

    private int findFromInd(E element, boolean inclusive) {
        int ind = binarySearch(element);
        if (ind < 0) {
            ind = ~ind;
        } else if (!inclusive) {
            ++ind;
        }
        return ind;
    }

    private int findToInd(E element, boolean inclusive) {
        int ind = binarySearch(element);
        if (ind < 0) {
            ind = ~ind - 1;
        } else if (!inclusive) {
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

    @SuppressWarnings("unchecked")
    private E[] copyOfAndReverse(E[] a) {
        E[] reverseA = (E[]) new Object[size()];
        for (int i = 0; i < to; i++) {
            reverseA[i] = a[to - i - 1];
        }
        return reverseA;
    }

}
