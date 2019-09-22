package tests;

import model.todolist.TodoList;
import model.todolist.TodoListWithDueDate;
import model.user.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;



class TodoListTest {

    TodoList testList;
    User user;

    @Test
    void testGetUser() {
        assertEquals(user, testList.getUser());
    }

    @Test
    void getFileName() {
        if (testList instanceof TodoListWithDueDate) {
            assertEquals(user.getUsername() + "TodoList.txt", testList.getFileName());
        } else {
            assertEquals(user.getUsername() + "recurringlist.txt", testList.getFileName());
        }
    }

    @Test
    void testSplit() {
        String str = "This\tis\ta\ttest\tstring";
        String[] splited = testList.split(str);
        assertEquals("This", splited[0]);
        assertEquals("is", splited[1]);
        assertEquals("a", splited[2]);
        assertEquals("test", splited[3]);
        assertEquals("string", splited[4]);
    }
}
