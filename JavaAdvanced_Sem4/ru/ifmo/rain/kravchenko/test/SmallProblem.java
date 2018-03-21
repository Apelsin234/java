package ru.ifmo.rain.kravchenko.test;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Alexander on 07.03.2018.
 */
class SmallProblem<E> {

    public Iterator<E> getCommonIterator(List<Iterator<E>> a) {
        return new CustomIterator<>(a);
    }

    private class CustomIterator<T> implements Iterator<T> {

        List<Iterator<T>> obj;
        int index;

        public CustomIterator(List<Iterator<T>> a) {
            obj = a;
            index = 0;
        }

        @Override
        public boolean hasNext() {
            while (index != obj.size() && !obj.get(index).hasNext()) {
                index++;
            }
            return index != obj.size();
        }

        @Override
        public T next() {
            return obj.get(index).next();
        }
    }
}
