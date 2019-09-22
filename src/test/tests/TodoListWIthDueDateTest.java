package tests;

import model.exceptions.ItemIdxNotFoundException;
import model.item.ImportantItemWithDueDate;
import model.item.Item;
import model.item.ItemWithDueDate;
import model.locationmap.LocationMap;
import model.todolist.TodoListWithDueDate;
import model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TodoListWIthDueDateTest extends TodoListTest {

    TodoListWithDueDate todoList;

    @BeforeEach
    void setUp(){
        user = new User("TestLoad");
        LocationMap locationMap = new LocationMap(user);
        testList = new TodoListWithDueDate(user);
        todoList = (TodoListWithDueDate) testList;
    }


    @Test
    void testNewTodo(){
        ItemWithDueDate item = new ItemWithDueDate("Homework", "", false, 0);
        assertTrue(todoList.length() == 0);
        todoList.addTodo(item);
        assertTrue(todoList.length()==1);
    }


    @Test
    void testCrossOffNoException(){
        ItemWithDueDate item = new ItemWithDueDate("Homework", "", false, 0);
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
    void testCrossOffItemIdxNotFoundException(){
        ItemWithDueDate item = new ItemWithDueDate("Homework", "", false, 0);
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
    void testGetItemReturnsNull() {
        assertEquals(null, todoList.getItem(-1));
    }

    @Test
    void testGetExpiredItems(){
        assertTrue(todoList.length() == 0);
        // populate the todoList with 10 not expired items, 10 expired items, and 10 not expired items.
        for(int i=0; i<10; i++) {
            ItemWithDueDate regularItem = new ItemWithDueDate("Sample#"+i, "2025-07-20", false, i);
            todoList.addTodo(regularItem);
        }
        for(int i=10; i<20; i++) {
            ItemWithDueDate regularItem = new ItemWithDueDate("Sample#"+i, "2019-06-20", false, i);
            todoList.addTodo(regularItem);
        }
        for(int i=20; i<30; i++) {
            ItemWithDueDate regularItem = new ItemWithDueDate("Sample#"+i, "2025-06-20", false, i);
            todoList.addTodo(regularItem);
        }

        assertTrue(todoList.length() == 30);
        List<ItemWithDueDate> expiredItems = todoList.getExpiredItems();
        assertTrue(expiredItems.size() == 10);
        for(int i=0; i<expiredItems.size();i++) {
            assertEquals("Sample#"+(i+10), expiredItems.get(i).getTaskName());
        }
        for(int i=0; i<expiredItems.size();i++) {
            assertEquals(i+10, expiredItems.get(i).getId());
        }
        for(int i=0; i<expiredItems.size();i++) {
            assertEquals("2019-06-20-Thu", expiredItems.get(i).getDueDateInString());
        }
        for(int i=0; i<expiredItems.size();i++) {
            assertEquals(false, expiredItems.get(i).getIsDone());
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

        // Meet up my friend 	false	2019-08-19-Mon	0	true	JAPAN
        //Visit grand parents	false	2019-08-29-Thu	2	false	Ishikawa
        // expected items from the file
        User testUser = new User("TestLoad");
        LocationMap locationMap = new LocationMap(testUser);
        TodoListWithDueDate listExpected = new TodoListWithDueDate(testUser);

        ItemWithDueDate importantExpected = new ImportantItemWithDueDate("Meet up my friend ",
                "2019-08-19", false, 0);
        ItemWithDueDate regularExpected = new ItemWithDueDate("Visit grand parents",
                "2019-08-29", false, 1);
        listExpected.addTodo(importantExpected);
        listExpected.addTodo(regularExpected);

        importantExpected.setLocation("JAPAN");
        regularExpected.setLocation("Ishikawa");

        // actual items loaded through load()
        ItemWithDueDate importantActual = todoList.getItem(0);
        ItemWithDueDate regularActual = todoList.getItem(1);

        // compare all the fields
        assertEquals(importantActual.getTaskName(), importantExpected.getTaskName());
        assertEquals(importantActual.getIsDone(), importantExpected.getIsDone());
        assertEquals(importantActual.getId(), importantExpected.getId());
        assertEquals(importantActual.getDueDateInDate(), importantExpected.getDueDateInDate());
        assertEquals(importantActual.getIsImportant(), importantExpected.getIsImportant());

        assertEquals(regularActual.getTaskName(), regularExpected.getTaskName());
        assertEquals(regularActual.getIsDone(), regularExpected.getIsDone());
        assertEquals(regularActual.getId(), regularExpected.getId());
        assertEquals(regularActual.getDueDateInDate(), regularExpected.getDueDateInDate());
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
    void testSave() {
        String fileName = "TestSaveTodoList.txt";
        // populate the list with 10 todos
        for(int i=0; i<10; i++) {
            ItemWithDueDate regularItem = new ItemWithDueDate("Sample#"+i,
                    "2019-07-20", false, i);
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
        TodoListWithDueDate fromFile = new TodoListWithDueDate(newUser);
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
            ItemWithDueDate thisTodo = todoList.getItem(i);
            ItemWithDueDate todoFromFile = fromFile.getItem(i);
            assertEquals(thisTodo.getTaskName(), todoFromFile.getTaskName());
            assertEquals(thisTodo.getIsDone(), todoFromFile.getIsDone());
            assertEquals(thisTodo.getId(), todoFromFile.getId());
            assertEquals(thisTodo.getDueDateInDate(), todoFromFile.getDueDateInDate());
        }
    }


    @Test
    void testIdAvailable() {
        ItemWithDueDate item1 = new ItemWithDueDate("item1", "", false, 1);
        ItemWithDueDate item2 = new ItemWithDueDate("item2", "", false, 3);
        ItemWithDueDate item3 = new ItemWithDueDate("item3", "", false, 5);
        ItemWithDueDate item4 = new ItemWithDueDate("item4", "", false, 7);
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
    void testShowItems() {
        ItemWithDueDate item1 = new ItemWithDueDate("item1", "", false, 1);
        ItemWithDueDate item2 = new ItemWithDueDate("item2", "", false, 3);
        ItemWithDueDate item3 = new ItemWithDueDate("item3", "", false, 5);
        ItemWithDueDate item4 = new ItemWithDueDate("item4", "", false, 7);
        todoList.addTodo(item1);
        todoList.addTodo(item2);
        todoList.addTodo(item3);
        todoList.addTodo(item4);
        String actual = todoList.showItems();
        String expected = "Item with due date: - \n" +
                "    ☐ item1\tItem with due date: - \n" +
                "    ☐ item2\tItem with due date: - \n" +
                "    ☐ item3\tItem with due date: - \n" +
                "    ☐ item4\t";
        assertEquals(expected, actual);
    }

    @Test
    void testShowItemsEmpty() {
        String expected = "";
        String actual = todoList.showItems();
        assertEquals(expected, actual);
    }

    @Test
    void testEditTaskName() {
        ItemWithDueDate item1 = new ItemWithDueDate("item1", "", false, 1);
        todoList.addTodo(item1);
        item1.setLocation("Test Site1");
        String newEntry = "New Name";
        assertEquals("item1", item1.getTaskName());
        todoList.editTodo(1, newEntry, item1.getDueDateInString(), "");
        assertEquals("New Name", item1.getTaskName());
    }

    @Test
    void testEditLocation() {
        ItemWithDueDate item1 = new ItemWithDueDate("item1", "", false, 1);
        todoList.addTodo(item1);
        item1.setLocation("Test Site1");
        String newEntry = "New Name";
        assertEquals("Test Site1", item1.getLocation());
        todoList.editTodo(1, item1.getTaskName(), item1.getDueDateInString(), newEntry);
        assertEquals("New Name", item1.getLocation());
    }

    @Test
    void testEditDueDate() {
        ItemWithDueDate item1 = new ItemWithDueDate("item1", "", false, 1);
        todoList.addTodo(item1);
        String newEntry = "2019-09-09";
        assertEquals("", item1.getDueDateInString());
        todoList.editTodo(1, item1.getTaskName(), newEntry, "");
        assertEquals("2019-09-09-Mon", item1.getDueDateInString());
    }

    @Test
    void testRemoveTodo() {
        ItemWithDueDate item1 = new ItemWithDueDate("item1", "", false, 1);
        ItemWithDueDate item2 = new ItemWithDueDate("item2", "", false, 3);
        ItemWithDueDate item3 = new ItemWithDueDate("item3", "", false, 5);
        ItemWithDueDate item4 = new ItemWithDueDate("item4", "", false, 7);
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
        ItemWithDueDate item1 = new ItemWithDueDate("item1", "", false, 1);
        ItemWithDueDate item2 = new ItemWithDueDate("item2", "", false, 3);
        ItemWithDueDate item3 = new ItemWithDueDate("item3", "", false, 5);
        ItemWithDueDate item4 = new ItemWithDueDate("item4", "", false, 7);
        todoList.addTodo(item1);
        todoList.addTodo(item2);
        todoList.addTodo(item3);
        todoList.addTodo(item4);
        assertEquals(4, todoList.length());
        todoList.removeTodo(2);
        assertEquals(4, todoList.length());
    }

    @Test
    void testSortByDate() {
        ItemWithDueDate item1 = new ItemWithDueDate("item1", "2020-01-01", false, 1);
        ItemWithDueDate item2 = new ItemWithDueDate("item2", "2021-01-02", false, 3);
        ItemWithDueDate item3 = new ItemWithDueDate("item3", "2024-01-01", false, 5);
        ItemWithDueDate item4 = new ItemWithDueDate("item4", "2021-01-01", false, 7);
        todoList.addTodo(item1);
        todoList.addTodo(item2);
        todoList.addTodo(item3);
        todoList.addTodo(item4);
        String beforeExpected = item1.showItems() + item2.showItems() + item3.showItems() + item4.showItems();
        String beforeActual = "";
        for (Item item :todoList) {
            beforeActual += item.showItems();
        }
        todoList.sortByDate();
        String afterExpected = item1.showItems() + item4.showItems() + item2.showItems() + item3.showItems();

        String afterActual = "";
        for (Item item :todoList) {
            afterActual += item.showItems();
        }

        assertEquals(beforeExpected, beforeActual);
        assertEquals(afterExpected, afterActual);
    }

    @Test
    void testSortByDateItemWithNoDueDate() {
        ItemWithDueDate item1 = new ItemWithDueDate("item1", "", false, 1);
        ItemWithDueDate item2 = new ItemWithDueDate("item2", "2021-01-02", false, 3);
        ItemWithDueDate item3 = new ItemWithDueDate("item3", "", false, 5);
        ItemWithDueDate item4 = new ItemWithDueDate("item4", "2021-01-01", false, 7);
        todoList.addTodo(item1);
        todoList.addTodo(item2);
        todoList.addTodo(item3);
        todoList.addTodo(item4);
        String beforeExpected = item1.showItems() + item2.showItems() + item3.showItems() + item4.showItems();
        String beforeActual = "";
        for (Item item :todoList) {
            beforeActual += item.showItems();
        }
        todoList.sortByDate();
        String afterExpected = item2.showItems() + item4.showItems() + item3.showItems() + item1.showItems();

        String afterActual = "";
        for (Item item :todoList) {
            afterActual += item.showItems();
        }

        assertEquals(beforeExpected, beforeActual);
        assertEquals(afterExpected, afterActual);
    }


    //String taskName, String dateInput, boolean isDone, int id,
    //                                          boolean isImportant
    @Test
    void testInstantiateItemImportantItem() {
        String taskName = "Test";
        String dateInput = "";
        boolean isDone = false;
        int id = 2;
        boolean isImportant = true;

        Item item = todoList.instantiateItem(taskName, dateInput, isDone, id, isImportant);
        assertTrue(item instanceof ImportantItemWithDueDate);
    }
    @Test
    void testInstantiateItem() {
        String taskName = "Test";
        String dateInput = "";
        boolean isDone = false;
        int id = 2;
        boolean isImportant = false;

        Item item = todoList.instantiateItem(taskName, dateInput, isDone, id, isImportant);
        assertTrue(item instanceof ItemWithDueDate);
    }
}