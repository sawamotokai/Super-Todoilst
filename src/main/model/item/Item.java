package model.item;

import model.composition.ItemComponent;
import model.locationmap.LocationMap;
import model.todolist.TodoList;
import model.user.User;

import java.text.SimpleDateFormat;

public abstract class Item  implements ItemComponent {
    protected String taskName;
    protected boolean isDone;
    protected int id;
    protected final String pattern = "yyyy-MM-dd";
    protected final SimpleDateFormat format = new SimpleDateFormat(pattern);
    protected final SimpleDateFormat dayOfWeek = new SimpleDateFormat("E");
    protected boolean isImportant;
    protected LocationMap locationMap;
    protected String location;
    protected TodoList todoList;
    protected User user;

    @Override
    public abstract String showItems();

    public boolean getIsImportant() {
        return this.isImportant;
    }

    public String getTaskName() {
        return this.taskName;
    }

    public int getId() {
        return this.id;
    }

    public boolean getIsDone() {
        return this.isDone;
    }

    public String getLocation() {
        return this.location;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    // MODIFIES: this
    // EFFECTS: set this.todoList to be todoList. at the same time it sets locationmap related to todoList.
    public void setTodoList(TodoList todoList) {
        this.todoList = todoList;
        this.locationMap = user.getLocationMap();
    }


    // MODIFIES: this
    // EFFECTS: sets user of this
    public void setUser(User user) {
        this.user = user;
    }

    // MODIFIES: this, locationmap
    // EFFECTS: sets the location name of this. sets the item on the location map.
    public void setLocation(String locationName) {
        if (this.location == null) {
            this.location = locationName;
            this.locationMap.addItem(locationName, this);
        }
    }

    // MODIFIES: this, locationmap
    // EFFECTS: sets the location name of this. sets the item on the location map.
    public void editLocation(String locationName) {
        this.locationMap.removeItem(this);
        this.location = locationName;
        this.locationMap.addItem(locationName, this);
    }

    // EFFECTS: returns string representation of the item.
    public abstract String getRepr();

    // MODIFIES: this
    // EFFECTS: turn the status of the item
    public void crossOff() {
        this.isDone = !this.isDone;
    }
}
