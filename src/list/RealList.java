package list;

import util.Num;

/** Односвязный список действительных чисел. */
public class RealList {

    /** Узел списка. */
    private static class Node {
        double data;
        Node next;
        Node(double d) { data = d; }
    }

    private Node head, tail;
    private int size;

    public boolean isEmpty() { return head == null; }
    public int size() { return size; }

    public void addLast(double value) {
        Node n = new Node(value);
        if (head == null) head = tail = n;
        else { tail.next = n; tail = n; }
        size++;
    }

    /** Продублировать все положительные числа. */
    public void duplicatePositive() {
        Node cur = head;
        while (cur != null) {
            if (cur.data > 0) {
                Node copy = new Node(cur.data);
                copy.next = cur.next;
                cur.next = copy;
                if (copy.next == null) tail = copy;
                size++;
                cur = copy.next;
            } else cur = cur.next;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node cur = head;
        boolean first = true;
        while (cur != null) {
            if (!first) sb.append(", ");
            sb.append(Num.format(cur.data));
            first = false;
            cur = cur.next;
        }
        return sb.append("]").toString();
    }
}