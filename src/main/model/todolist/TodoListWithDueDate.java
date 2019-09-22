package model.todolist;

import model.exceptions.ItemIdxNotFoundException;
import model.item.ImportantItemWithDueDate;
import model.item.Item;
import model.item.ItemWithDueDate;
import model.user.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class TodoListWithDueDate extends TodoList implements ListWithDatedObj, Iterable<ItemWithDueDate> {
    protected List<ItemWithDueDate> items  = new ArrayList<>();


    public TodoListWithDueDate(User user) {
        super(user);
        this.fileName = user.getUsername() + "TodoList.txt";
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

    @Override
    public String showItems() {
        String itemReprs = "";
        if (items.isEmpty()) {
            System.out.println("No Todos With Due Date Exist");
        } else {
            for (ItemWithDueDate item: items) {
                itemReprs += item.showItems();
                itemReprs += "\t";
            }
        }
        return itemReprs;
    }


    // EFFECTS: returns an item with id. if not found returns null.
    public ItemWithDueDate getItem(int id) {
        for (ItemWithDueDate item: items) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }


    // EFFECTS: returns expired regularItems
    public List<ItemWithDueDate> getExpiredItems() {
        List<ItemWithDueDate> expiredItems = new ArrayList<>();
        for (ItemWithDueDate item : items) {
            if (item.isExpired()) {
                expiredItems.add(item);
            }
        }
        return expiredItems;
    }

    // MODIFIES: this, item
    // EFFECTS: add the input item to the list.
    public void addTodo(ItemWithDueDate item) {
        this.items.add(item);
        item.setUser(this.getUser());
        item.setTodoList(this);
//        if (item.getLocation() == null) {
//            item.setLocation("");
//        }
        notifyObserverItemAdded(item); // notify the map of adding the item
    }


    // MODIFIES: item with id
    // EFFECTS: edit the item based off of user input
    public void editTodo(int id, String taskName, String dueDate, String location) {
        ItemWithDueDate item = getItem(id);
        item.setTaskName(taskName);
        item.setDueDate(dueDate);
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

    // MODIFIES: this
    // EFFECTS: sort the items in this list by date in ascending order (Selection sort)
    // and items without due date are placed at the end in the order of creation
    @Override
    public void sortByDate() {
        for (int i = 0; i < this.length(); i++) {
            int j = i;
            // initialize latestSoFar to be the current item
            ItemWithDueDate latestSoFar = items.get(i);

            // reinitialize latestSoFar to be the first item with non-null date
            latestSoFar = setLatestSoFar(i, latestSoFar);
            // at this point latestSoFar is not null unless all the items after the current item
            // in the list has non-null date
            // therefore, if latestSoFar is still null, it can finish the iteration.
            if (latestSoFar.getDueDateInDate() == null) {
                break;
            }

            for (; j < this.length(); j++) {
                if (items.get(j).getDueDateInDate() == null) {
                    break;
                } else if (items.get(j).getDueDateInDate().before(latestSoFar.getDueDateInDate())) {
                    latestSoFar = items.get(j);
                }
            }

            Collections.swap(items, i, items.indexOf(latestSoFar));
        }
    }

    // EFFECTS: save the current list to memory.
    //          opens a file, loop through this list and write each regularItems
    //          throws an exception when it fails to open the file;
    @Override
    public void save(String fileName) throws IOException {
        PrintWriter writer;
        writer = new PrintWriter(fileName, "UTF-8");
        final String SPLIT_CHAR = "\t";

        for (ItemWithDueDate item : items) {
            // For each regularItem in our list, write the regularItem's details in the following form:
            //     TaskName<TAB>isDone<TAB>dueDate<TAB>id<TAB>isImportant
            writer.println(item.getTaskName() + SPLIT_CHAR + item.getLocation() + SPLIT_CHAR + item.getDueDateInString()
                    + SPLIT_CHAR + item.getId()
                    + SPLIT_CHAR + item.getIsImportant() + SPLIT_CHAR + item.getIsDone());
        }
        writer.close();
    }

    // REQUIRES: File has regularItems in the specified format
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
            String location = parts[1];
            String dateInput = parts[2];
            int id = Integer.parseInt(parts[3]);
            boolean isImportant = Boolean.parseBoolean(parts[4]);
            boolean isDone = Boolean.parseBoolean(parts[5]);
            ItemWithDueDate item = instantiateItem(taskName, dateInput, isDone, id, isImportant);
            addTodo(item);
            item.setLocation(location);
        }
    }


    // helper
    // EFFECTS: set latestSoFar to be the first item with non-null date and returns the latestSoFar
    private ItemWithDueDate setLatestSoFar(int i, ItemWithDueDate latestSoFar) {
        int k = i;
        while (latestSoFar.getDueDateInDate() == null && k < items.size()) {
            ItemWithDueDate nextItem = items.get(k);
            if (nextItem.getDueDateInDate() != null) {
                latestSoFar = nextItem;
            }
            k++;
        }
        return latestSoFar;
    }

    // Helper method
    // EFFECTS: instantiate an appropriate item depending on the importance of it
    public ItemWithDueDate instantiateItem(String taskName, String dateInput, boolean isDone, int id,
                                          boolean isImportant) {
        ItemWithDueDate item;
        if (isImportant) {
            item = new ImportantItemWithDueDate(taskName, dateInput, isDone, id);
        } else {
            item = new ItemWithDueDate(taskName, dateInput, isDone, id);
        }
        return item;
    }

    @Override
    public Iterator iterator() {
        return items.iterator();
    }
}
