package model.user;

import model.composition.ItemComponent;
import model.item.Item;
import model.locationmap.LocationMap;
import model.observer.ListObserver;
import model.todolist.RecurringTodoList;
import model.todolist.TodoList;
import model.todolist.TodoListWithDueDate;

import java.util.ArrayList;
import java.util.List;

public class User implements ListObserver {

    private LocationMap locationMap;
    private List<TodoList> todoLists = new ArrayList<>();
    private String username;
    private int numOfItems;
    private TodoListWithDueDate todoListWithDueDate;
    private RecurringTodoList recurringTodoList;

    public User(String username) {
        this.username = username;
        this.locationMap = new LocationMap(this);
        this.todoListWithDueDate = new TodoListWithDueDate(this);
        todoLists.add(todoListWithDueDate);
        this.recurringTodoList = new RecurringTodoList(this);
        todoLists.add(recurringTodoList);
    }

    public TodoListWithDueDate getTodoListWithDueDate() {
        return this.todoListWithDueDate;
    }

    public RecurringTodoList getRecurringTodoList() {
        return recurringTodoList;
    }

    public LocationMap getLocationMap() {
        return this.locationMap;
    }

    public String getUsername() {
        return this.username;
    }

    public int getNumOfItems() {
        return numOfItems;
    }


    // Client of the composition design
    public String showAllItems() {
        String itemReprs = "";
        for (ItemComponent todoList: todoLists) {
            itemReprs += todoList.showItems();
        }
        return itemReprs;
    }


    public int getAvailableId() {
        int idPotential = 1;
        while (true) {
            int count = 0;
            for (TodoList list : todoLists) {
                if (list.isIdAvailable(idPotential)) {
                    count++;
                }
            }
            if (count == 2) {
                return idPotential;
            }
            idPotential++;
        }
    }

    @Override
    public String updateItemAdded(Item item) {
        this.numOfItems++;
        return item.getRepr();
    }

    @Override
    public String updateItemRemoved(Item item) {
        this.numOfItems--;
        return item.getRepr();
    }

    @Override
    public String updateItemEdited(Item item) {
        return item.getRepr();
    }


}
