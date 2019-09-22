package tests;

import model.item.Item;
import model.item.ItemWithDueDate;
import model.item.RecurringItem;
import model.locationmap.LocationMap;
import model.todolist.RecurringTodoList;
import model.todolist.TodoListWithDueDate;
import model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class UserTest {
    User user;
    RecurringTodoList recurringTodoList;
    TodoListWithDueDate todoListWithDueDate;
    LocationMap locationMap;

    @BeforeEach
    void setUp() {
        user = new User("Test");
        todoListWithDueDate = user.getTodoListWithDueDate();
        recurringTodoList = user.getRecurringTodoList();
        locationMap = user.getLocationMap();
    }

    @Test
    void testGetUsername() {
        assertEquals("Test", user.getUsername());
    }

    @Test
    void testGetNumOfItems() {
        assertEquals(0, user.getNumOfItems());
        ItemWithDueDate item1 = new ItemWithDueDate("item1", "", false, 1);
        todoListWithDueDate.addTodo(item1);
        assertEquals(1, user.getNumOfItems());
        RecurringItem item2 = new RecurringItem("item2", "daily", false, 2);
        recurringTodoList.addTodo(item2);
        assertEquals(2, user.getNumOfItems());
    }


    @Test
    void testShowAllItems() {
        assertEquals(0, user.getNumOfItems());
        ItemWithDueDate item1 = new ItemWithDueDate("item1", "", false, 1);
        todoListWithDueDate.addTodo(item1);
        assertEquals(1, user.getNumOfItems());
        RecurringItem item2 = new RecurringItem("item2", "daily", false, 2);
        recurringTodoList.addTodo(item2);
        assertEquals(2, user.getNumOfItems());
        assertEquals("Item with due date: - \n" +
                "    ☐ item1\tRecurring Item: - daily\n" +
                "    ☐ item2\t", user.showAllItems());
    }

    @Test
    void testGetAvailableId() {
        assertEquals(1, user.getAvailableId());
        assertEquals(0, user.getNumOfItems());
        ItemWithDueDate item1 = new ItemWithDueDate("item1", "", false, 1);
        todoListWithDueDate.addTodo(item1);
        assertEquals(1, user.getNumOfItems());
        RecurringItem item2 = new RecurringItem("item2", "daily", false, 2);
        recurringTodoList.addTodo(item2);
        assertEquals(3, user.getAvailableId());
        todoListWithDueDate.removeTodo(1);
        assertEquals(1, user.getAvailableId());
        ItemWithDueDate item3 = new ItemWithDueDate("item3", "", false, 1);
        todoListWithDueDate.addTodo(item1);
        recurringTodoList.removeTodo(2);
        assertEquals(2, user.getAvailableId());
    }

    @Test
    void testUpdateItemEdited() {
        Item item1 = new ItemWithDueDate("item1", "", false, 1);
        assertEquals("#1 ☐ item1 \n" +
                "    - Due:  - Location: null", user.updateItemEdited(item1));
    }

}
