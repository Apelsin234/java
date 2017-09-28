
public class ArrayQueueADT {
	//inv:  size >= 0
    //      for i = 1..size : a[i] != null

    private int size = 0;
    private int first = 0;
    private int last = 0;
    private Object[] elements = new Object[10];

    //pre: e != null
	//     queue != null
    //post: size' = size + 1, for i = 1..size : a'[i] = a[i], a[size'] = e
    public static void enqueue(ArrayQueueADT queue, Object e) {
        assert e != null;
        ensureCapacity(queue, queue.size + 1);
        queue.elements[queue.last] = e;
        queue.last = (queue.last + 1) % queue.elements.length;
        queue.size++;

    }

    private static void ensureCapacity(ArrayQueueADT queue, int s) {
        if (s <= queue.elements.length) {
            return;
        }
        Object[] es = new Object[s * 2];
        if (queue.first + queue.size > queue.elements.length) {
            System.arraycopy(queue.elements, queue.first, es, 0, queue.size - queue.last);
            System.arraycopy(queue.elements, 0, es, queue.size - queue.last, queue.last);
        } else {
            System.arraycopy(queue.elements, queue.first, es, 0, queue.size);
        }
        queue.elements = es;
        queue.first = 0;
        queue.last = queue.size;
    }

    //pre: size > 0
    //post: R = a[1], immutable
    public static Object element(ArrayQueueADT queue) {
        assert queue.size > 0;
        return queue.elements[queue.first];
    }

    // pre: size > 0
    //post: R = a[1], size' = size - 1, for i = 1..size' : a'[i] = a[i + 1]
    public static Object dequeue(ArrayQueueADT queue) {
        assert queue.size > 0;

        Object e = queue.elements[queue.first];
        queue.elements[queue.first++] = null;
        queue.first %= queue.elements.length;
        queue.size--;
        return e;
    }

    //pre: queue != null
    //post: R = size, immutable
    public static int size(ArrayQueueADT queue) {
        return queue.size;
    }

    //pre: queue != null
    //post: R = (size == 0), immutable
    public static boolean isEmpty(ArrayQueueADT queue) {
        return queue.size == 0;
    }

    //post: size' = 0
    public static void clear(ArrayQueueADT queue) {
        queue.elements = new Object[10];
        queue.size = 0;
        queue.first = 0;
        queue.last = 0;
    }

    // pre: size > 0
    //post: R = [a[1],a[2]..a[size]], immutable
    public static Object[] toArray(ArrayQueueADT queue) {

        Object[] es = new Object[queue.size];
        if (queue.first + queue.size > queue.elements.length) {
            System.arraycopy(queue.elements, queue.first, es, 0, queue.size - queue.last);
            System.arraycopy(queue.elements, 0, es, queue.size - queue.last, queue.last);
        } else {
            System.arraycopy(queue.elements, queue.first, es, 0, queue.size);
        }

        return es;
    }
	
	//pre: e != null
	//     queue != null
    //post: size' = size + 1, for i = 2..size : a'[i] = a[i - 1], a[0] = e
    public static void push(ArrayQueueADT queue, Object e) {
        assert e != null;
        ensureCapacity(queue, queue.size + 1);
        if (queue.first == 0) {
            queue.first = queue.elements.length - 1;
        } else {
            queue.first--;
        }
        queue.elements[queue.first] = e;
        queue.size++;
    }

	//pre: size > 0
	//     queue != null
    //post: R = a[size], immutable
    public static Object peek(ArrayQueueADT queue) {
        assert queue.size > 0;
        return queue.last > 0 ? queue.elements[queue.last - 1] : queue.elements[queue.elements.length - 1];
    }

	// pre: size > 0
	//      queue != null
    //post: R = a[size], size' = size - 1, for i = 1..size' : a'[i] = a[i]
    public static Object remove(ArrayQueueADT queue) {
        assert queue.size > 0;
        queue.last = queue.last > 0 ? queue.last - 1 : queue.elements.length - 1;
        Object e = queue.elements[queue.last];
        queue.elements[queue.last] = null;
        queue.size--;
        return e;
    }
}
