package tests;

import model.item.Item;
import model.item.ItemWithDueDate;
import model.observer.ListAsSubject;
import model.observer.ListObserver;
import model.todolist.TodoListWithDueDate;
import model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListAsSubjectTest {
    ListAsSubject list;
    User user;

    @BeforeEach
    void setUp() {
        user = new User("Test");
        list = new TodoListWithDueDate(user);
    }

    @Test
    void testAddObserver() {
        assertEquals(1, list.getObservers().size());
        ListObserver o = new User("observer");
        list.addObserver(o);
        assertEquals(2, list.getObservers().size());
        assertEquals(user, list.getObservers().get(0));
        assertEquals(o, list.getObservers().get(1));
    }

    @Test
    void testRemoveObserver() {
        assertEquals(1, list.getObservers().size());
        ListObserver o = new User("observer");
        list.addObserver(o);
        assertEquals(2, list.getObservers().size());
        assertEquals(user, list.getObservers().get(0));
        assertEquals(o, list.getObservers().get(1));
        list.removeObserver(o);
        assertEquals(1, list.getObservers().size());
        assertEquals(user, list.getObservers().get(0));
        list.removeObserver(user);
        assertEquals(0, list.getObservers().size());
    }

    @Test
    void testNotifyObserverItemAdded() {
        Item item = new ItemWithDueDate("Test", "", true, 1);
        assertEquals(0, user.getNumOfItems());
        String msg = list.notifyObserverItemAdded(item);
        assertEquals(1, user.getNumOfItems());
        assertEquals(item.getRepr()+"\t", msg);
    }

    @Test
    void testNotifyObserverItemRemoved() {
        Item item = new ItemWithDueDate("Test", "", true, 1);
        assertEquals(0, user.getNumOfItems());
        String msg = list.notifyObserverItemRemoved(item);
        assertEquals(-1, user.getNumOfItems());
        assertEquals(item.getRepr()+"\t", msg);
    }

    @Test
    void testNotifyObserverItemEdited() {
        Item item = new ItemWithDueDate("Test", "", true, 1);
        assertEquals(0, user.getNumOfItems());
        String msg = list.notifyObserverItemEdited(item);
        assertEquals(0, user.getNumOfItems());
        assertEquals(item.getRepr()+"\t", msg);
    }

}
