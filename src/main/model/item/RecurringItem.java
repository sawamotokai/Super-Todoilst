package model.item;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class RecurringItem extends Item {

    private Date lastUpdated;
    protected String interval;

    // constructor for new items.
    public RecurringItem(String taskName, String interval, boolean isDone, int id) {
        this.interval = interval;
        this.lastUpdated = new Date();
        this.taskName = taskName;
        this.id = id;
        this.isDone = isDone;
    }

    // constructor for items from file that has been instantiated before.
    public RecurringItem(String taskName, String interval, boolean isDone, int id, String dateInput) {
        this.interval = interval;
        try {
            this.lastUpdated = format.parse(dateInput);
        } catch (ParseException e) {
            System.out.println("Date format is wrong. The item now has no due date.");
            this.lastUpdated = new Date();
        }
        this.taskName = taskName;
        this.id = id;
        this.isDone = isDone;
    }


    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }


    public Date getLastUpdated() {
        return this.lastUpdated;
    }


    public String getLastUpdatedInString() {
        return format.format(lastUpdated);
    }

    // MODIFIES: this
    // EFFECTS: if the item wasn't undone in a day, undo it.
    public void updateDailyTodo() {
        if (this.getIsDone()) {
            Calendar c = Calendar.getInstance();
            c.setTime(this.lastUpdated);
            // Reference: https://stackoverflow.com/questions/428918/how-can-i-increment-a-date-by-one-day-in-java
            c.add(Calendar.DATE, 1);
            Date lastUpdatePlusADay = c.getTime();
            Date now = new Date();
            if (lastUpdatePlusADay.before(now)) {
                this.undoTodo();
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: if the item wasn't undone in a week, undo it.
    // I could make it so that it requires dayOfWeekInput and make it repeat every week on that day.
    public void updateWeeklyTodo() {
        if (this.getIsDone()) {
            Calendar c = Calendar.getInstance();
            c.setTime(this.lastUpdated);
            c.add(Calendar.DATE, 7);
            Date lastUpdatePlusADay = c.getTime();
            Date now = new Date();
            if (lastUpdatePlusADay.before(now)) {
                this.undoTodo();
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: if the item wasn't undone in a month, undo it.
    public void updateMonthlyTodo() {
        if (this.getIsDone()) {
            Calendar c = Calendar.getInstance();
            c.setTime(this.lastUpdated);
            c.add(Calendar.MONTH, 1);
            Date lastUpdatePlusADay = c.getTime();
            Date now = new Date();
            if (lastUpdatePlusADay.before(now)) {
                this.undoTodo();
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: if the item wasn't undone in a year, undo it.
    public void updateYearlyTodo() {
        if (this.getIsDone()) {
            Calendar c = Calendar.getInstance();
            c.setTime(this.lastUpdated);
            c.add(Calendar.YEAR, 1);
            Date lastUpdatePlusADay = c.getTime();
            Date now = new Date();
            if (lastUpdatePlusADay.before(now)) {
                this.undoTodo();
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: undo item
    private void undoTodo() {
        this.isDone = false;
        this.lastUpdated = new Date();
    }

    public void renewStatus() {
        switch (this.getInterval()) {
            case "daily":
                updateDailyTodo();
                break;
            case "weekly":
                updateWeeklyTodo();
                break;
            case "monthly":
                updateMonthlyTodo();
                break;
            case "yearly":
                updateYearlyTodo();
                break;
            default:
                break;
        }
    }

    public String getInterval() {
        return this.interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    @Override
    public String showItems() {
        return "Recurring Item: - " + interval + "\n    " + (isDone ? "✓ " : "☐ ") + taskName;
    }


    @Override
    public String getRepr() {
        String msg;
        msg = "#" + id + " " + (isDone ? "✓" : "☐")  + " " + taskName + "\n"
                  + "      Interval: " + interval + " - Location: " + location;
        return msg;
    }

    // MODIFIES: this
    // EFFECTS: turn the status of the item
    @Override
    public void crossOff() {
        this.isDone = !this.isDone;
        // if it's daily, set the lastUpdate because the next time it will be undone has to be always the day after,
        // not based on the creation date
        if (this.interval.equals("daily")) {
            this.lastUpdated = new Date();
        }
    }

}
