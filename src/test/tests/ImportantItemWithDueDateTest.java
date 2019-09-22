package tests;

import model.item.ImportantItemWithDueDate;
import model.item.ItemWithDueDate;
import model.locationmap.LocationMap;
import model.todolist.TodoListWithDueDate;
import model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ImportantItemWithDueDateTest extends ItemWithDueDateTest {

    @BeforeEach
    public void setUp(){
        user = new User("Test");
        testMap = new LocationMap(user);
        testList = new TodoListWithDueDate(user);
        this.item = new ImportantItemWithDueDate("homework", "2030-07-20", false, 1);
        itemWithDueDate = (ItemWithDueDate) item;
        testList.addTodo(itemWithDueDate);
        itemWithDueDate.setLocation("Test Site");
    }

    @Test
    void testIsImportant() {
        assertEquals(true, itemWithDueDate.getIsImportant());
    }

    @Test
    void testGetReprWithDueDate(){
        assertFalse(itemWithDueDate.getIsDone());
        String msgExpected1 = "#1 ☐ homework !!IMPORTANT!! \n" +
                "    - Due: 2030-07-20-Sat - Location: Test Site";
        assertEquals(msgExpected1, itemWithDueDate.getRepr());
    }

    @Test
    void testGetReprWithDueDateItemDone() {
        ImportantItemWithDueDate testItem = new ImportantItemWithDueDate("test", "", true, 2);

        assertTrue(testItem.getIsDone());
        String msgExpected1 = "#2 ✓ test !!IMPORTANT!! \n" +
                "    - Due:  - Location: null";
        assertEquals(msgExpected1, testItem.getRepr());
    }


    @Test
    void testGetReprWithoutDueDate(){
        itemWithDueDate.setDueDate("");
        assertEquals(itemWithDueDate.getDueDateInString(), "");
        assertFalse(itemWithDueDate.getIsDone());
        String msgExpected1 = "#1 ☐ homework !!IMPORTANT!! \n" +
                "    - Due:  - Location: Test Site";
        assertEquals(msgExpected1, itemWithDueDate.getRepr());
    }

    @Test
    void testConstructorWithWrongDateFormat() {
        ItemWithDueDate testItem = new ImportantItemWithDueDate("Test", "wrong", false, 1);
        assertEquals("", testItem.getDueDateInString());
    }


    @Test
    void testConstructorWhenDateInputIsEmpty() {
        ItemWithDueDate testItem = new ImportantItemWithDueDate("Test", "", false, 1);
        assertEquals("", testItem.getDueDateInString());
        assertEquals(null, testItem.getDueDateInDate());
    }

    @Test
    void testShowItems() {
        assertEquals("Important Item with due date: -2030-07-20-Sat" + "\n    ☐ homework",this.itemWithDueDate.showItems());
    }


    @Test
    void testShowItemsItemDone() {
        ItemWithDueDate testItem = new ImportantItemWithDueDate("Test", "", true, 1);
        assertEquals("Important Item with due date: -\n" +
                "    ✓ Test",testItem.showItems());
    }


    @Test
    void testShowItemsSuper() {
        String expected = "Item with due date: - 2030-07-20-Sat\n    ☐ homework";
        ItemWithDueDate item = new ItemWithDueDate("homework", "2030-07-20", false, 1);
        assertEquals(expected, item.showItems());
    }
}
