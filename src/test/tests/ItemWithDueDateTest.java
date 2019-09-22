package tests;

import model.item.Item;
import model.item.ItemWithDueDate;
import model.todolist.TodoListWithDueDate;
import model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class ItemWithDueDateTest extends ItemTest {

    protected TodoListWithDueDate testList;
    protected User user;
    protected ItemWithDueDate itemWithDueDate;
    
    @BeforeEach
    public void setUp(){
        user = new User("Test");
        testMap = user.getLocationMap();
        testList = user.getTodoListWithDueDate();
        this.item = new ItemWithDueDate("homework", "2030-07-20", false, 1);
        itemWithDueDate = (ItemWithDueDate) this.item;
        testList.addTodo(itemWithDueDate);
        itemWithDueDate.setLocation("Test Site");
    }


    @Test
    void testGetDueDateInString(){
        assertEquals("2030-07-20-Sat", itemWithDueDate.getDueDateInString());
    }

    @Test
    void testGetDueDateInDate(){
        final String PATTERN = "yyyy-MM-dd";
        final SimpleDateFormat FORMAT = new SimpleDateFormat(PATTERN);
        try {
            Date dueDateExpected = FORMAT.parse("2030-07-20");
            assertEquals(dueDateExpected, itemWithDueDate.getDueDateInDate());
        } catch (ParseException e) {
            fail("No exception expected");
        }
    }


    @Test
    void testSetDueDate(){
        assertFalse(itemWithDueDate.getDueDateInString().equals("2019-05-05"));
        itemWithDueDate.setDueDate("2019-05-05");
        assertEquals("2019-05-05-Sun", itemWithDueDate.getDueDateInString());
    }

    @Test
    void testSetDueDateToNull(){
        assertFalse(itemWithDueDate.getDueDateInString().equals(""));
        itemWithDueDate.setDueDate("");
        System.out.println(itemWithDueDate.getDueDateInString());
        assertTrue(itemWithDueDate.getDueDateInString().equals(""));
        assertTrue(itemWithDueDate.getDueDateInDate() == null);
    }

    @Test
    void testSetDueDateWithWrongDateFormat(){
        assertFalse(itemWithDueDate.getDueDateInString().equals(""));
        itemWithDueDate.setDueDate("wrong");
        System.out.println(itemWithDueDate.getDueDateInString());
        assertTrue(itemWithDueDate.getDueDateInString().equals(""));
        assertTrue(itemWithDueDate.getDueDateInDate() == null);
    }

    @Test
    void testGetReprWithDueDate(){
        assertFalse(itemWithDueDate.getIsDone());
        String msgExpected1 = "#1 ☐ homework \n    - Due: 2030-07-20-Sat - Location: Test Site";
        assertEquals(msgExpected1, itemWithDueDate.getRepr());
        itemWithDueDate.crossOff();
        assertTrue(itemWithDueDate.getIsDone());
        String msgExpected2 = "#1 ✓ homework \n    - Due: 2030-07-20-Sat - Location: Test Site";
        assertEquals(msgExpected2, itemWithDueDate.getRepr());
    }

    @Test
    void testIsImportant() {
        assertEquals(false, itemWithDueDate.getIsImportant());
    }

    @Test
    void testGetReprWithoutDueDate(){
        itemWithDueDate.setDueDate("");
        assertEquals(itemWithDueDate.getDueDateInString(), "");
        assertFalse(itemWithDueDate.getIsDone());
        String msgExpected1 = "#1 ☐ homework \n    - Due:  - Location: Test Site";
        assertEquals(msgExpected1, itemWithDueDate.getRepr());
    }

    @Test
    void testConstructorWhenDateInputIsEmpty() {
        ItemWithDueDate testItem = new ItemWithDueDate("Test", "", false, 1);
        testList.addTodo(testItem);
        testItem.setLocation("Test");
        assertEquals("", testItem.getDueDateInString());
        assertEquals(null, testItem.getDueDateInDate());
    }

    @Test
    void testConstructorWithWrongDateFormat() {
        ItemWithDueDate testItem = new ItemWithDueDate("Test", "wrong", false, 1);
        assertEquals("", testItem.getDueDateInString());

    }

    @Test
    void testEditLocation() {
        User testUser = new User("Test");
        model.locationmap.LocationMap testMap2 = testUser.getLocationMap();
        TodoListWithDueDate testList2 = testUser.getTodoListWithDueDate();
        ItemWithDueDate testItem = new ItemWithDueDate("homework", "2030-07-20", false, 1);
        testList2.addTodo(testItem);
        testItem.setLocation("Test Site2");
        assertEquals("Test Site2", testItem.getLocation());
        ArrayList<Item> list = testMap2.getItemsAtLocation("Test Site2");
        assertEquals(1, list.size());
        assertEquals(testItem, list.get(0));

        testItem.editLocation("New Site");

        assertEquals(testItem, testMap2.getItemsAtLocation("New Site").get(0));
        assertEquals(new ArrayList<>(), testMap2.getItemsAtLocation("Test Site2"));
    }


    @Test
    void testShowItems() {
        String expected = "Item with due date: - 2030-07-20-Sat\n    ☐ homework";
        assertEquals(expected, itemWithDueDate.showItems());
    }

    @Test
    void testShowItemsItemDone() {
        ItemWithDueDate testItem = new ItemWithDueDate("test", "", true, 2);
        String expected = "Item with due date: - \n" +
                "    ✓ test";
        assertEquals(expected, testItem.showItems());
    }

    @Test
    void testIsExpired(){
        assertFalse(itemWithDueDate.isExpired());
        itemWithDueDate.setDueDate("2016-03-03");
        assertTrue(itemWithDueDate.isExpired());
    }

    @Test
    void testIsExpiredWhenDateIsNull(){
        itemWithDueDate.setDueDate("");
        assertFalse(itemWithDueDate.isExpired());
    }


}
