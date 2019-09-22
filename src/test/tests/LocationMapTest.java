package tests;

import model.item.Item;
import model.item.ItemWithDueDate;
import model.locationmap.LocationMap;
import model.todolist.TodoListWithDueDate;
import model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LocationMapTest {
    LocationMap locationMap;
    User user;
    ItemWithDueDate item1;
    TodoListWithDueDate todoList;

    @BeforeEach
    void setUp() {
        user = new User("Test");
        locationMap = user.getLocationMap();
        todoList = user.getTodoListWithDueDate();
        item1  = new ItemWithDueDate("item1", "", false, 1);
        todoList.addTodo(item1);
        item1.setLocation("UBC");
    }

    @Test
    void testGetLocations() {
        Set<String> expected = new TreeSet<>();
        expected.add("UBC");
        assertEquals(expected, locationMap.getLocations());
    }

    @Test
    void testAddItemToExistingPlace() {
        ItemWithDueDate item2 = new ItemWithDueDate("item2", "", false, 2);
        todoList.addTodo(item2);
        locationMap.addItem("UBC", item2);
        assertEquals(2, locationMap.getItemsAtLocation("UBC").size());
        assertEquals(item2, locationMap.getItemsAtLocation("UBC").get(1));
    }

    @Test
    void testRemoveItem() {
        ItemWithDueDate item2 = new ItemWithDueDate("item2", "", false, 2);
        todoList.addTodo(item2);
        locationMap.addItem("UBC", item2);
        assertEquals(item2, locationMap.getItemsAtLocation("UBC").get(1));
        assertEquals(item1, locationMap.getItemsAtLocation("UBC").get(0));
        locationMap.removeItem(item2);
        assertEquals(item1, locationMap.getItemsAtLocation("UBC").get(0));
        assertEquals(1, locationMap.getItemsAtLocation("UBC").size());
    }

    @Test
    void testRemoveItemListGetsEmptyAfter() {
        ItemWithDueDate item2 = new ItemWithDueDate("item2", "", false, 2);
        todoList.addTodo(item2);
        locationMap.addItem("Test Site", item2);
        assertEquals(item2, locationMap.getItemsAtLocation("Test Site").get(0));
        locationMap.removeItem(item2);
        assertEquals(new ArrayList<Item>(), locationMap.getItemsAtLocation("Test Site"));
    }



    @Test
    void testGetItemAtLocationWrongKey() {
        ArrayList<Item> list = locationMap.getItemsAtLocation("WRONG");
        assertEquals(new ArrayList<Item>(), list);
    }
}
