package model.todolist;

import model.exceptions.ItemIdxNotFoundException;
import model.item.Item;
import model.item.RecurringItem;
import model.user.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RecurringTodoList extends TodoList implements Iterable<RecurringItem> {

    private List<RecurringItem> items = new ArrayList<>();

    public RecurringTodoList(User user) {
        super(user);
        this.fileName = user.getUsername() + "recurringlist.txt";
    }


    @Override
    public String showItems() {
        String itemReprs = "";
        if (items.isEmpty()) {
            System.out.println("No Recurring Todos exist");
        } else {
            for (RecurringItem item: items) {
                itemReprs += item.showItems();
                itemReprs += "\t";
            }
        }
        return itemReprs;
    }



    // MODIFIES: item with id
    // EFFECTS: edit the item based off of user input
    public void editTodo(int id, String taskName, String interval, String location) {
        RecurringItem item = getItem(id);
        item.setTaskName(taskName);
        item.setInterval(interval);
        item.editLocation(location);
        notifyObserverItemEdited(item);
    }


    // MODIFIES: this
    // EFFECTS: removes item with id
    public void removeTodo(int id) {
        for (Item item: items) {
            if (item.getId() == id) {
                items.remove(item);
                notifyObserverItemRemoved(item);
                return;
            }
        }
    }


    // EFFECTS: returns an item at idx. if not found returns null.
    public RecurringItem getItem(int id) {
        for (RecurringItem item : items) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }


    // MODIFIES: this, item
    // EFFECTS: add the input item to the list. Throws TooManyItemsNotDoneException
    // if the list contains more than 100 items thats not done.
    public void addTodo(RecurringItem item) {
        this.items.add(item);
        item.setUser(this.getUser());
        item.setTodoList(this);
//        if (item.getLocation() == null) {
//            item.setLocation("");
//        }
        notifyObserverItemAdded(item); // notify the map of adding the item
    }

    // EFFECTS: returns true if the id is already taken by item in the list.
    public boolean isIdAvailable(int id) {
        for (Item item: items) {
            if (item.getId() == id) {
                return false;
            }
        }
        return true;
    }


    // MODIFIES: this
    // EFFECTS: take in the id of the item user want to cross off
    // and turns the status of selected item in todoList to done
    public void crossOff(int id) throws ItemIdxNotFoundException {
        // find the item that matches the id by looping.
        for (Item item: items) {
            if (item.getId() == id) {
                item.crossOff();
                return;
            }
        }
        throw new ItemIdxNotFoundException();
    }

    // EFFECTS: returns the size of the regularItems
    public int length() {
        return this.items.size();
    }

    // String taskName, String interval, boolean isDone, int id
    // EFFECTS: save the current list to memory.
    //          opens a file, loop through this list and write each regularItems
    //          throws an exception when it fails to open the file;
    @Override
    public void save(String fileName) throws IOException {
        PrintWriter writer;
        writer = new PrintWriter(fileName, "UTF-8");
        final String SPLIT_CHAR = "\t";

        for (RecurringItem item : items) {
            // For each regularItem in our list, write the regularItem's details in the following form:
            //     TaskName<TAB>isDone<TAB>dueDate<TAB>id<TAB>isImportant
            writer.println(item.getTaskName() + SPLIT_CHAR + item.getIsDone() + SPLIT_CHAR + item.getInterval()
                    + SPLIT_CHAR + item.getId() + SPLIT_CHAR + item.getIsImportant() + SPLIT_CHAR
                    + item.getLocation() + SPLIT_CHAR + item.getLastUpdatedInString());
        }
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: load each line of memory and instantiate each item, and add them to this list
    @Override
    public void load(String filename) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filename));
        for (String line: lines) {
            // For each line, split the line into the different parts
            // We used the format:
            // TaskName<TAB>IsDone<TAB>dueDate<TAB>id<TAB>isImportant
            // As long as the setup of your reader and writer match, your code should be happy!
            String[] parts = split(line);

            // Finally, we can use the split parts of the line to create a new Book object
            String taskName = parts[0];
            boolean isDone = Boolean.parseBoolean(parts[1]);
            String interval = parts[2];
            int id = Integer.parseInt(parts[3]);
            String location = parts[5];
            String dateInput = parts[6];

            RecurringItem item = new RecurringItem(taskName, interval, isDone, id, dateInput);
            item.renewStatus();
            addTodo(item);
            item.setLocation(location);
        }
    }


    @Override
    public Iterator<RecurringItem> iterator() {
        return items.iterator();
    }
}
