package model.item;

import java.text.ParseException;
import java.util.Date;

public class ItemWithDueDate extends Item {

    protected Date dueDate;

    // REQUIRES: dateInput has to be in format of yyyy-MM-dd. id is the index of the item in the list
    //           isDone should be false in default
    public ItemWithDueDate(String taskName, String dateInput, boolean isDone, int number) {
        isImportant = false;
        this.taskName = taskName;
        this.id = number;
        this.isDone = isDone;
        if (dateInput.equals("")) {
            this.dueDate = null;
        } else {
            try {
                this.dueDate = format.parse(dateInput);
            } catch (ParseException e) {
                System.out.println("The date format was wrong. A todo with no due date was created instead.");
                this.dueDate = null;
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: Reset the due date of this todo item
    public void setDueDate(String dateInput) {
        if (dateInput.equals("")) {
            this.dueDate = null;
        } else {
            try {
                this.dueDate = format.parse(dateInput);
            } catch (ParseException e) {
                System.out.println("Date format is wrong. The item now has no due date.");
                this.dueDate = null;
            }
        }

    }


    // EFFECTS: returns the due date in string of the format of yyyy-MM-dd
    public String getDueDateInString() {
        if (dueDate != null) {
            return format.format(this.dueDate) + "-" + dayOfWeek.format(this.dueDate);
        }
        return "";
    }

    // EFFECTS: returns the due date as a Date object
    public Date getDueDateInDate() {
        if (dueDate != null) {
            return this.dueDate;
        }
        return null;
    }



    // I referred to stack overflow for the usage of Date lib
    // EFFECTS: returns true if the current date is past dueDate of this item and the items is not done.
    //          returns false if the item has no due date.
    public boolean isExpired() {
        Date now = new Date();

        if (this.getDueDateInString().equals("")) {
            return false;
        }
        return !now.before(this.getDueDateInDate()); // if now is before dueDate -> false (not expired yet)
    }


    @Override
    public String showItems() {
        return "Item with due date: - " + getDueDateInString()
                + "\n    "  + (isDone ? "✓ " : "☐ ") + taskName;
    }

    @Override
    // EFFECTS: returns a string representing the item
    public String getRepr() {
        String msg;
        msg = "#" + getId() + " " + (isDone ? "✓" : "☐") + " " + getTaskName() + " \n"
                + "    - Due: " + getDueDateInString() + " - Location: " + getLocation();

        return msg;
    }
}


