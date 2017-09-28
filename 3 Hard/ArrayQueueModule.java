

public class ArrayQueueModule {
	//inv:  size >= 0
    //      for i = 1..size : a[i] != null

    private static int size = 0;
    private static int first = 0;
    private static int last = 0;
    private static Object[] elements = new Object[10];

    //pre: e != null
    //post: size' = size + 1, for i = 1..size : a'[i] = a[i], a[size'] = e
    public static void enqueue(Object e) {
        assert e != null;
        ensureCapacity(size + 1);
        elements[last] = e;
        last = (last + 1) % elements.length;
        size++;
    }

    private static void ensureCapacity(int s) {
        if (s <= elements.length) {
            return;
        }
        Object[] es = new Object[s * 2];
        if (first + size > elements.length) {
            System.arraycopy(elements, first, es, 0, size - last);
            System.arraycopy(elements, 0, es, size - last, last);
        } else {
            System.arraycopy(elements, first, es, 0, size);
        }
        elements = es;
        first = 0;
        last = size;
    }

    //pre: size > 0
    //post: R = a[1], immutable
    public static Object element() {
        assert size > 0;
        return elements[first];
    }

    // pre: size > 0
    //post: R = a[1], size' = size - 1, for i = 1..size' : a'[i] = a[i + 1]
    public static Object dequeue() {
        assert size > 0;

        Object e = elements[first];
        elements[first++] = null;
        first %= elements.length;
        size--;
        return e;
    }

    //post: R = size, immutable
    public static int size() {
        return size;
    }

    //post: R = (size == 0), immutable
    public static boolean isEmpty() {
        return size == 0;
    }

    //post: size' = 0
    public static void clear() {
        elements = new Object[10];
        size = 0;
        first = 0;
        last = 0;
    }

    // pre: size > 0
    //post: R = [a[1],a[2]..a[size]], immutable
    public static Object[] toArray() {

        Object[] es = new Object[size];
        if (first + size > elements.length) {
            System.arraycopy(elements, first, es, 0, size - last);
            System.arraycopy(elements, 0, es, size - last, last);
        } else {
            System.arraycopy(elements, first, es, 0, size);
        }

        return es;
    }
	
	//pre: e != null
    //post: size' = size + 1, for i = 2..size : a'[i] = a[i - 1], a[0] = e
    public static void push(Object e) {
        assert e != null;
        ensureCapacity(size + 1);
        if (first == 0) {
            first = elements.length - 1;
        } else {
            first--;
        }
        elements[first] = e;
        size++;
    }

	//pre: size > 0
    //post: R = a[size], immutable
    public static Object peek() {
        assert size > 0;
        return last > 0 ? elements[last - 1] : elements[elements.length - 1];
    }

	// pre: size > 0
    //post: R = a[size], size' = size - 1, for i = 1..size' : a'[i] = a[i]
    public static Object remove() {
        assert size > 0;
        last = last > 0 ? last - 1 : elements.length - 1;
        Object e = elements[last];
        elements[last] = null;
        size--;
        return e;
    }
}
