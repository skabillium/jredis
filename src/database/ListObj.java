package database;

public class ListObj extends Obj {
    DoublyLinkedList list = new DoublyLinkedList();

    ListObj(long expiresAt) {
        this.expiresAt = expiresAt;
    }
}
