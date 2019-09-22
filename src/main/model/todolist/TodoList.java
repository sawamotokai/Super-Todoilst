package model.todolist;


import model.composition.ItemComponent;
import model.exceptions.ItemIdxNotFoundException;
import model.observer.ListAsSubject;
import model.user.User;

import java.io.IOException;

public abstract class TodoList extends ListAsSubject implements Loadable, Savable, ItemComponent {
    private User user;
    protected String fileName;

    public TodoList(User user) {
        // constructor
        this.user = user;
        addObserver(user);
    }

    public abstract void editTodo(int id, String taskName, String location, String dueDateOrInterval);


    public abstract boolean isIdAvailable(int id);

    @Override
    public abstract String showItems();


    public String getFileName() {
        return this.fileName;
    }

    public User getUser() {
        return user;
    }

    // MODIFIES: this
    // EFFECTS: take in the id of the item user want to cross off
    // and turns the status of selected item in todoList to done
    public abstract void crossOff(int idToCrossOff) throws ItemIdxNotFoundException;


    // EFFECTS: returns the size of the regularItems
    public abstract int length();


    // Helper method
    // EFFECTS: turn and return the line into an array of strings, splitting it by tab.
    public String[] split(String line) {
        return line.split("\t");
    }


    // EFFECTS: save the current list to memory.
    //          opens a file, loop through this list and write each regularItems
    //          throws an exception when it fails to open the file;
    @Override
    public abstract void save(String fileName) throws IOException;


    // REQUIRES: File has regularItems in the specified format
    // MODIFIES: this
    // EFFECTS: load each line of memory and instantiate each item, and add them to this list
    @Override
    public abstract void load(String filename) throws IOException;
}
