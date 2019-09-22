package model.item;

public class ImportantItemWithDueDate extends ItemWithDueDate {

    public ImportantItemWithDueDate(String taskName, String dateInput, boolean isDone, int number) {
        super(taskName, dateInput, isDone, number);
        isImportant = true;
    }

    @Override
    public String showItems() {
        return "Important Item with due date: -" + getDueDateInString() + "\n    " + (isDone ? "✓ " : "☐ ")
                + taskName;
    }

    @Override
    // EFFECTS: returns the string representation of the item, depending on whether it has due date.
    public String getRepr() {
        String msg;
        msg = "#" + getId() + " " + (isDone ? "✓" : "☐") + " " + getTaskName() + " !!IMPORTANT!! \n"
                + "    - Due: " + getDueDateInString() + " - Location: " + getLocation();
        return msg;
    }
}
