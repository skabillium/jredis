package database;

import java.util.ArrayList;

public class DoublyLinkedList {
    class ListNode {
        String value;
        ListNode next;
        ListNode prev;

        ListNode(String value) {
            this.value = value;
        }
    }

    public int length = 0;
    ListNode head;
    ListNode tail;

    public void prepend(String value) {
        var node = new ListNode(value);
        length++;
        if (length == 1) {
            head = node;
            tail = node;
            return;
        }

        head.prev = node;
        node.next = head;
        head = node;
    }

    public void append(String value) {
        if (length == 0) {
            prepend(value);
            return;
        }

        var node = new ListNode(value);
        length++;
        tail.next = node;
        node.prev = tail;
        tail = node;
    }

    public String popHead() {
        if (length == 0) {
            return null;
        }
        length--;
        var node = head;
        if (length == 0) {
            head = null;
            tail = null;
            return node.value;
        }

        head = node.next;
        return node.value;
    }

    public ArrayList<String> popHeadN(int n) {
        var nodes = new ArrayList<String>();
        var total = n < length ? n : length;
        for (int i = 0; i < total; i++) {
            nodes.add(popHead());
        }
        return nodes;
    }

    public String popTail() {
        if (length == 0) {
            return null;
        }
        length--;
        var node = tail;
        if (length == 0) {
            head = null;
            tail = null;
            return node.value;
        }

        tail = node.prev;
        return node.value;
    }

    public ArrayList<String> popTailN(int n) {
        var nodes = new ArrayList<String>();
        var total = n < length ? n : length;
        for (int i = 0; i < total; i++) {
            nodes.add(popTail());
        }
        return nodes;
    }

    public String peekHead() {
        if (length == 0) {
            return null;
        }
        return head.value;
    }

    public String peekTail() {
        if (length == 0) {
            return null;
        }
        return tail.value;
    }
}
