package tests;

import model.item.Item;
import model.item.ItemWithDueDate;
import model.item.RecurringItem;
import model.todolist.RecurringTodoList;
import model.todolist.TodoListWithDueDate;
import model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

public class RecurringItemTest extends ItemTest {

    User user;
    RecurringTodoList testList;
    RecurringItem recurringItem;

    @BeforeEach
    void setUp() {
        user = new User("Test");
        testMap = user.getLocationMap();
        testList = user.getRecurringTodoList();
        item = new RecurringItem("homework", "daily", false, 1);
        recurringItem = (RecurringItem) item;
        testList.addTodo(recurringItem);
        recurringItem.setLocation("Test Site");
    }


    @Test
    void testIsImportant() {
        assertEquals(false, recurringItem.getIsImportant());
    }

    @Test
    void testEditLocation() {
        User testUser = new User("Test");
        model.locationmap.LocationMap testMap2 = testUser.getLocationMap();
        TodoListWithDueDate testList2 = testUser.getTodoListWithDueDate();
        Item testItem = new ItemWithDueDate("homework", "2030-07-20", false, 1);
        testList2.addTodo((ItemWithDueDate) testItem);
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
    void testWeeklyCrossOff() {
        RecurringItem test = new RecurringItem("test", "weekly", false, 2);
        Date beforeUpdate = test.getLastUpdated();

        assertTrue(!test.getIsDone());
        test.crossOff();
        assertTrue(test.getIsDone());
        test.crossOff();
        assertTrue(!test.getIsDone());
        assertEquals("weekly", test.getInterval());
        assertEquals(beforeUpdate, test.getLastUpdated());
    }

    @Test
    void testCrossDailyItem() {
        Date beforeUpdate = recurringItem.getLastUpdated();
        assertEquals("daily", recurringItem.getInterval());
        assertTrue(!recurringItem.getIsDone());
        recurringItem.crossOff();
        assertTrue(recurringItem.getIsDone());

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        recurringItem.crossOff();
        assertTrue(!recurringItem.getIsDone());
        assertNotEquals(beforeUpdate, recurringItem.getLastUpdated());
        assertTrue(beforeUpdate.before(recurringItem.getLastUpdated()));
    }


    @Test
    // String taskName, String interval, boolean isDone, int id, String dateInput
    void testConstructorWithStringInput() {
        String taskName = "TestItem";
        String interval = "daily";
        boolean isDone = false;
        int id = 2;
        String dateInput = "2019-09-09";
        RecurringItem testItem2 = new RecurringItem(taskName, interval, isDone, id, dateInput);
        assertEquals(taskName, testItem2.getTaskName());
        assertEquals(interval, testItem2.getInterval());
        assertEquals(isDone, testItem2.getIsDone());
        assertEquals(id, testItem2.getId());
        assertEquals(dateInput, testItem2.getLastUpdatedInString());
    }

    @Test
    void testConstructorWithWringStringInput() {
        String taskName = "TestItem";
        String interval = "daily";
        boolean isDone = false;
        int id = 2;
        String dateInput = "Wrong input";
        RecurringItem testItem2 = new RecurringItem(taskName, interval, isDone, id, dateInput);
        assertEquals(taskName, testItem2.getTaskName());
        assertEquals(interval, testItem2.getInterval());
        assertEquals(isDone, testItem2.getIsDone());
        assertEquals(id, testItem2.getId());
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        assertEquals(format.format(today), testItem2.getLastUpdatedInString());
    }

    @Test
    void testSuperCrossOff(){
        Item testItem = new ItemWithDueDate("test", "", false, 3);
        assertTrue(!testItem.getIsDone());
        testItem.crossOff();
        assertTrue(testItem.getIsDone());
    }

    @Override
    void testConstructorWhenDateInputIsEmpty() {

    }

    @Test
    void testSetInterval() {
        assertEquals("daily", recurringItem.getInterval());
        recurringItem.setInterval("weekly");
        assertEquals("weekly", recurringItem.getInterval());
    }


    @Test
    void testSetLastUpdated() {
        Date before = recurringItem.getLastUpdated();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Date now = new Date();
        recurringItem.setLastUpdated(now);
        assertTrue(before.before(now));
        assertEquals(now, recurringItem.getLastUpdated());
        assertNotEquals(before, recurringItem.getLastUpdated());

    }

    @Test
    void testUpdateDailyTodo() {
        RecurringItem testItem = new RecurringItem("testItem", "daily", true, 2);
        assertTrue(testItem.getIsDone());
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date yesterday = cal.getTime();
        testItem.setLastUpdated(yesterday);
        assertEquals(yesterday, testItem.getLastUpdated());
        Date now = new Date();
        assertTrue(yesterday.before(now));
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        testItem.renewStatus();
        System.out.println(testItem.getIsDone());
        assertNotEquals(yesterday, testItem.getLastUpdated());
        assertTrue(yesterday.before(testItem.getLastUpdated()));
        assertTrue(!testItem.getIsDone());
    }

    @Test
    void testUpdateDailyTodoNotUpdated() {
        RecurringItem testItem = new RecurringItem("testItem", "daily", true, 2);
        assertTrue(testItem.getIsDone());
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        Date tomorrow = cal.getTime();
        testItem.setLastUpdated(tomorrow);
        assertEquals(tomorrow, testItem.getLastUpdated());
        Date now = new Date();
        assertFalse(tomorrow.before(now));
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        testItem.renewStatus();
        assertEquals(tomorrow, testItem.getLastUpdated());
        assertTrue(testItem.getIsDone());
    }

    @Test
    void testUpdateWeeklyTodo() {
        RecurringItem testItem = new RecurringItem("testItem", "weekly", true, 2);
        assertTrue(testItem.getIsDone());
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        Date lastWeek = cal.getTime();
        testItem.setLastUpdated(lastWeek);
        assertEquals(lastWeek, testItem.getLastUpdated());
        Date now = new Date();
        assertTrue(lastWeek.before(now));
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        testItem.renewStatus();
        assertFalse(testItem.getIsDone());
        assertNotEquals(lastWeek, testItem.getLastUpdated());
        assertTrue(lastWeek.before(testItem.getLastUpdated()));
    }

    @Test
    void testUpdateWeeklyTodoNotUpdated() {
        RecurringItem testItem = new RecurringItem("testItem", "weekly", true, 2);
        assertTrue(testItem.getIsDone());
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -3);
        Date lastWeek = cal.getTime();
        testItem.setLastUpdated(lastWeek);
        assertEquals(lastWeek, testItem.getLastUpdated());
        Date now = new Date();
        assertTrue(lastWeek.before(now));
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        testItem.renewStatus();
        assertTrue(testItem.getIsDone());
        assertEquals(lastWeek, testItem.getLastUpdated());
    }

    @Test
    void testUpdateWeeklyTodoItemNotDone() {
        RecurringItem testItem = new RecurringItem("testItem", "weekly", false, 2);
        assertFalse(testItem.getIsDone());
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        Date lastWeek = cal.getTime();
        testItem.setLastUpdated(lastWeek);
        assertEquals(lastWeek, testItem.getLastUpdated());
        Date now = new Date();
        assertTrue(lastWeek.before(now));
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        testItem.renewStatus();
        assertFalse(testItem.getIsDone());
        assertEquals(lastWeek, testItem.getLastUpdated());
    }

    @Test
    void testUpdateMonthlyTodo() {
        RecurringItem testItem = new RecurringItem("testItem", "monthly", true, 2);
        assertTrue(testItem.getIsDone());
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        Date lastWeek = cal.getTime();
        testItem.setLastUpdated(lastWeek);
        assertEquals(lastWeek, testItem.getLastUpdated());
        Date now = new Date();
        assertTrue(lastWeek.before(now));
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        testItem.renewStatus();
        assertFalse(testItem.getIsDone());
        assertNotEquals(lastWeek, testItem.getLastUpdated());
        assertTrue(lastWeek.before(testItem.getLastUpdated()));
    }

    @Test
    void testUpdateMonthlyTodoNotUpdated() {
        RecurringItem testItem = new RecurringItem("testItem", "monthly", true, 2);
        assertTrue(testItem.getIsDone());
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date lastWeek = cal.getTime();
        testItem.setLastUpdated(lastWeek);
        assertEquals(lastWeek, testItem.getLastUpdated());
        Date now = new Date();
        assertTrue(lastWeek.before(now));
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        testItem.renewStatus();
        assertTrue(testItem.getIsDone());
        assertEquals(lastWeek, testItem.getLastUpdated());
    }

    @Test
    void testUpdateMonthlyTodoItemNotDone() {
        RecurringItem testItem = new RecurringItem("testItem", "monthly", false, 2);
        assertFalse(testItem.getIsDone());
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        Date lastWeek = cal.getTime();
        testItem.setLastUpdated(lastWeek);
        assertEquals(lastWeek, testItem.getLastUpdated());
        Date now = new Date();
        assertTrue(lastWeek.before(now));
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        testItem.renewStatus();
        assertFalse(testItem.getIsDone());
        assertEquals(lastWeek, testItem.getLastUpdated());
    }

    @Test
    void testUpdateYearlyTodo() {
        RecurringItem testItem = new RecurringItem("testItem", "yearly", true, 2);
        assertTrue(testItem.getIsDone());
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);
        Date lastWeek = cal.getTime();
        testItem.setLastUpdated(lastWeek);
        assertEquals(lastWeek, testItem.getLastUpdated());
        Date now = new Date();
        assertTrue(lastWeek.before(now));
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        testItem.renewStatus();
        assertFalse(testItem.getIsDone());
        assertNotEquals(lastWeek, testItem.getLastUpdated());
        assertTrue(lastWeek.before(testItem.getLastUpdated()));
    }
    @Test
    void testUpdateYearlyTodoItemNotDone() {
        RecurringItem testItem = new RecurringItem("testItem", "yearly", false, 2);
        assertFalse(testItem.getIsDone());
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);
        Date lastWeek = cal.getTime();
        testItem.setLastUpdated(lastWeek);
        assertEquals(lastWeek, testItem.getLastUpdated());
        Date now = new Date();
        assertTrue(lastWeek.before(now));
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        testItem.renewStatus();
        assertFalse(testItem.getIsDone());
        assertEquals(lastWeek, testItem.getLastUpdated());
    }

    @Test
    void testUpdateYearlyTodoNotUpdated() {
        RecurringItem testItem = new RecurringItem("testItem", "yearly", true, 2);
        assertTrue(testItem.getIsDone());
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date lastWeek = cal.getTime();
        testItem.setLastUpdated(lastWeek);
        assertEquals(lastWeek, testItem.getLastUpdated());
        Date now = new Date();
        assertTrue(lastWeek.before(now));
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        testItem.renewStatus();
        assertTrue(testItem.getIsDone());
        assertEquals(lastWeek, testItem.getLastUpdated());
    }

    @Test
    void testRenewStatusDefault() {
        RecurringItem testItem = new RecurringItem("testItem", "wrong", true, 2);
        testItem.renewStatus();
        assertTrue(testItem.getIsDone());
    }


    @Test
    void testShowItems() {
        String msg = "Recurring Item: - daily\n    ☐ homework";
        assertEquals(msg, recurringItem.showItems());
    }

    @Test
    void testShowItemsWhenDone() {
        RecurringItem testItem = new RecurringItem("testItem", "daily", true, 2);
        String msg = "Recurring Item: - daily\n    ✓ testItem";
        assertEquals(msg, testItem.showItems());
    }

    @Test
    void testGetReprNotDone() {
        String msg = "#1 ☐ homework\n" +
                "      Interval: daily - Location: Test Site";
        assertEquals(msg, recurringItem.getRepr());
    }

    @Test
    void testGetReprItemDone() {
        RecurringItem testItem = new RecurringItem("testItem", "daily", true, 2);
        String msg = "#2 ✓ testItem\n" +
                "      Interval: daily - Location: null";
        assertEquals(msg, testItem.getRepr());
    }
}
