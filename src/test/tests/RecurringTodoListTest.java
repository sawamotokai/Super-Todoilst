package tests;
import model.exceptions.ItemIdxNotFoundException;
import model.item.Item;

import model.item.RecurringItem;
import model.locationmap.LocationMap;
import model.todolist.RecurringTodoList;
import model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class RecurringTodoListTest extends TodoListTest {

    RecurringTodoList todoList;

    @BeforeEach
    void setUp(){
        user = new User("TestLoad");
        model.locationmap.LocationMap locationMap = new LocationMap(user);
        testList = new RecurringTodoList(user);
        todoList = (RecurringTodoList) testList;
    }

    @Test
    void testNewTodo(){
        RecurringItem item = new RecurringItem("Homework", "daily", false, 0);
        assertTrue(todoList.length() == 0);
        todoList.addTodo(item);
        assertTrue(todoList.length() == 1);
    }

    @Test
    void testRemoveTodo() {
        RecurringItem item1 = new RecurringItem("item1", "", false, 1);
        RecurringItem item2 = new RecurringItem("item2", "", false, 3);
        RecurringItem item3 = new RecurringItem("item3", "", false, 5);
        RecurringItem item4 = new RecurringItem("item4", "", false, 7);
        todoList.addTodo(item1);
        todoList.addTodo(item2);
        todoList.addTodo(item3);
        todoList.addTodo(item4);
        assertEquals(4, todoList.length());
        todoList.removeTodo(3);
        assertEquals(3, todoList.length());
        assertEquals(item1, todoList.getItem(1));
        assertEquals(null, todoList.getItem(3));
    }

    @Test
    void testRemoveTodoNotFound() {
        RecurringItem item1 = new RecurringItem("item1", "", false, 1);
        RecurringItem item2 = new RecurringItem("item2", "", false, 3);
        RecurringItem item3 = new RecurringItem("item3", "", false, 5);
        RecurringItem item4 = new RecurringItem("item4", "", false, 7);

        todoList.addTodo(item1);
        todoList.addTodo(item2);
        todoList.addTodo(item3);
        todoList.addTodo(item4);
        assertEquals(4, todoList.length());
        todoList.removeTodo(2);
        assertEquals(4, todoList.length());
    }

    @Test
    void testShowItemsEmpty() {
        String expected = "";
        String actual = todoList.showItems();
        assertEquals(expected, actual);
    }

    @Test
    void testEditTaskName() {
        RecurringItem item1 = new RecurringItem("item1", "", false, 1);
        todoList.addTodo(item1);
        item1.setLocation("Test Site1");
        String newEntry = "New Name";
        assertEquals("item1", item1.getTaskName());
        todoList.editTodo(1, newEntry, item1.getInterval(), "");
        assertEquals("New Name", item1.getTaskName());
    }

    @Test
    void testEditLocation() {
        RecurringItem item1 = new RecurringItem("item1", "", false, 1);
        todoList.addTodo(item1);
        item1.setLocation("Test Site1");
        String newEntry = "New Name";
        assertEquals("Test Site1", item1.getLocation());
        todoList.editTodo(1, item1.getTaskName(), item1.getInterval(), newEntry);
        assertEquals("New Name", item1.getLocation());
    }

    @Test
    void testEditInterval() {
        RecurringItem item1 = new RecurringItem("item1", "daily", false, 1);
        todoList.addTodo(item1);
        item1.setLocation("Test Site1");
        String newEntry = "weekly";
        assertEquals("daily", item1.getInterval());
        todoList.editTodo(1, item1.getTaskName(), newEntry, item1.getLocation());
        assertEquals("weekly", item1.getInterval());
    }

    @Test
    void testShowItems() {
        RecurringItem item1 = new RecurringItem("item1", "daily", false, 1);
        RecurringItem item2 = new RecurringItem("item2", "daily", false, 3);
        RecurringItem item3 = new RecurringItem("item3", "daily", false, 5);
        RecurringItem item4 = new RecurringItem("item4", "daily", false, 7);
        todoList.addTodo(item1);
        todoList.addTodo(item2);
        todoList.addTodo(item3);
        todoList.addTodo(item4);
        String actual = todoList.showItems();
        String expected = "Recurring Item: - daily\n" +
                "    ☐ item1\tRecurring Item: - daily\n" +
                "    ☐ item2\tRecurring Item: - daily\n" +
                "    ☐ item3\tRecurring Item: - daily\n" +
                "    ☐ item4\t";
        assertEquals(expected, actual);
    }

    @Test
    void testIdAvailable() {
        RecurringItem item1 = new RecurringItem("item1", "", false, 1);
        RecurringItem item2 = new RecurringItem("item2", "", false, 3);
        RecurringItem item3 = new RecurringItem("item3", "", false, 5);
        RecurringItem item4 = new RecurringItem("item4", "", false, 7);
        todoList.addTodo(item1);
        todoList.addTodo(item2);
        todoList.addTodo(item3);
        todoList.addTodo(item4);
        assertTrue(todoList.isIdAvailable(2));
        assertFalse(todoList.isIdAvailable(3));
        assertFalse(todoList.isIdAvailable(5));
        assertTrue(todoList.isIdAvailable(6));
    }

    @Test
    void testSave() {
        String fileName = "TestSaveRecurringList.txt";
        // populate the list with 10 todos
        for(int i=0; i<10; i++) {
            RecurringItem regularItem = new RecurringItem("Sample#"+i,
                    "daily", false, i);
            todoList.addTodo(regularItem);
        }
        try {
            todoList.save(fileName);
        } catch (IOException e) {
            System.out.println("Failed to update the file.");
            e.printStackTrace();
        }

        User newUser = new User("TestSave");
        LocationMap locationMap = new LocationMap(newUser);
        RecurringTodoList fromFile = new RecurringTodoList(newUser);
        try {
            fromFile.load(fileName);
        } catch (IOException e) {
            fail("Exception not expected");
            e.printStackTrace();
        }

        // both list are the same length?
        assertTrue(todoList.length() == fromFile.length());

        // both list have the same items? Compare all the fields
        for (int i = 0; i<todoList.length(); i++){
            RecurringItem thisTodo = todoList.getItem(i);
            RecurringItem todoFromFile = fromFile.getItem(i);
            assertEquals(thisTodo.getTaskName(), todoFromFile.getTaskName());
            assertEquals(thisTodo.getIsDone(), todoFromFile.getIsDone());
            assertEquals(thisTodo.getId(), todoFromFile.getId());
            assertEquals(thisTodo.getInterval(), todoFromFile.getInterval());
        }
    }

    @Test
    void testLoad() {
        assertTrue(todoList.length() == 0);
        try {
            todoList.load(todoList.getFileName());
        } catch (IOException e) {
            fail("Exception not expected");
            e.printStackTrace();
        }
        assertTrue(todoList.length()==2);

        // expected items from the file
        User testUser = new User("TestLoad");
        LocationMap locationMap = new LocationMap(testUser);
        RecurringTodoList listExpected = new RecurringTodoList(testUser);

        RecurringItem importantExpected = new RecurringItem("Sample#0",
                "daily", false, 0);
        RecurringItem regularExpected = new RecurringItem("Sample#1",
                "daily", false, 1);
        listExpected.addTodo(importantExpected);
        listExpected.addTodo(regularExpected);

        // actual items loaded through load()
        RecurringItem importantActual = todoList.getItem(0);
        RecurringItem regularActual = todoList.getItem(1);

        // compare all the fields
        assertEquals(importantActual.getTaskName(), importantExpected.getTaskName());
        assertEquals(importantActual.getIsDone(), importantExpected.getIsDone());
        assertEquals(importantActual.getId(), importantExpected.getId());
        assertEquals(importantActual.getInterval(), importantExpected.getInterval());
        assertEquals(importantActual.getIsImportant(), importantExpected.getIsImportant());

        assertEquals(regularActual.getTaskName(), regularExpected.getTaskName());
        assertEquals(regularActual.getIsDone(), regularExpected.getIsDone());
        assertEquals(regularActual.getId(), regularExpected.getId());
        assertEquals(regularActual.getInterval(), regularExpected.getInterval());
        assertEquals(regularActual.getIsImportant(), regularExpected.getIsImportant());
    }

    @Test
    void testLoadWithException(){
        assertTrue(todoList.length() == 0);
        try {
            todoList.load("WrongFileName.txt");
            fail("File loading exception expected.");
        } catch (IOException e) {
            // pass
        }
        assertTrue(todoList.length()==0);
    }

    @Test
    void testGetItemReturnsNull() {
        assertEquals(null, todoList.getItem(-1));
    }

    @Test
    void testCrossOffItemIdxNotFoundException(){
        RecurringItem item = new RecurringItem("Homework", "", false, 0);
        assertTrue(todoList.length() == 0);
        todoList.addTodo(item);
        assertTrue(todoList.length()==1);
        Item i = todoList.getItem(0);
        assertTrue(!i.getIsDone());
        try {
            todoList.crossOff(1);
            fail("Should throw exception");
        } catch (ItemIdxNotFoundException e) {
            // pass
        }
        assertFalse(i.getIsDone());
    }
    @Test
    void testCrossOffNoException(){
        RecurringItem item = new RecurringItem("Homework", "", false, 0);
        assertTrue(todoList.length() == 0);
        todoList.addTodo(item);
        assertTrue(todoList.length()==1);
        Item i = todoList.getItem(0);
        assertTrue(!i.getIsDone());
        try {
            todoList.crossOff(0);
        } catch (ItemIdxNotFoundException e) {
            fail("No exception expected.");
        }
        assertTrue(i.getIsDone());
    }

    @Test
    void testIterator() {
        RecurringItem item2 = new RecurringItem("Homework", "", false, 2);
        todoList.addTodo(item2);
        RecurringItem item3 = new RecurringItem("Homework", "", false, 3);
        todoList.addTodo(item3);
        int num = 0;
        for (RecurringItem i:todoList) {
            num++;
            assertEquals("Homework", i.getTaskName());
        }
        assertEquals(2, num);
    }

}
