package ui;

import model.exceptions.ItemIdxNotFoundException;
import model.item.ImportantItemWithDueDate;
import model.item.Item;
import model.item.ItemWithDueDate;
import model.item.RecurringItem;
import model.todolist.RecurringTodoList;
import model.todolist.TodoList;
import model.todolist.TodoListWithDueDate;
import model.user.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class TodoRunner {
//    private LocationMap locationMap;
//    private TodoListWithDueDate todoListWithDueDate;
//    private RecurringTodoList recurringTodoList;
    private User user;

    public static void main(String[] args) {
        TodoRunner app = new TodoRunner();
        app.run();
    }


    // EFFECTS: runs the entire program and prompts user for inputs
    private void run() {
        // load user's Lists
        try {
            apiTest();
        } catch (IOException e) {
            e.printStackTrace();
        }
        init();
        topMenu();
    }


    // EFFECTS: Ask username and load the appropriate file and return todoListWithDueDate loaded.
    //          If user is not found, return a brand new todoListWithDueDate
    private void init() {
        System.out.print("Enter your name: ");
        String username = getUserResponse(); // Scans the next token of the input as an int.
        System.out.println("");

        // instantiate map and lists from username
        this.user = new User(username);

        tryLoad(user.getTodoListWithDueDate());
        tryLoad(user.getRecurringTodoList());
    }


    private void tryLoad(TodoList todoList) {
        try {
            todoList.load(todoList.getFileName());
            System.out.println("Hello " + user.getUsername() + ". Your data was loaded!");
        } catch (Exception e) {
            System.out.println("Hello " + user.getUsername() + ". Your Todo List list was created");
        }
        System.out.println("");
    }


    private void topMenu() {
        while (true) {
            System.out.println("\nWhich List Are You Interested in? \n1 - One-off TodoList \n2 - Recurring TodoList");
            System.out.println("3 - See your Todos by Location\n4 - See all Items\n0 - Quit");
            String userInstruction = getUserResponse();
            executeMethodTop(userInstruction);
        }
    }

    // helper
    private void executeMethodTop(String userInstruction) {
        switch (userInstruction) {
            case "1":
                showTodoListMenu(user.getTodoListWithDueDate());
                break;
            case "2":
                showRecurringMenu(user.getRecurringTodoList());
                break;
            case "3":
                showItemsAtLocationsUI();
                break;
            case "4":
                showAllItems();
                break;
            case "0":
                System.exit(0);
                break;
            default:
        }
    }

    // helper
    private void showMenu(TodoList todoList) {
        if (todoList instanceof RecurringTodoList) {
            System.out.println("- Enter 1 to Create New Todo");
            System.out.println("- Enter 2 to See your Todos by Interval");
            System.out.println("- Enter 3 to Tick Todo");
            System.out.println("- Enter 0 to Go Back to Top Menu");
            System.out.println("");
            System.out.print("Your Input: ");
        } else {
            System.out.println("- Enter 1 to Create New Todo");
            System.out.println("- Enter 2 to See your All Todos in the List");
            System.out.println("- Enter 3 to Tick Todo");
            System.out.println("- Enter 4 to See your Expired Todos");
            System.out.println("- Enter 0 to Go Back to Top Menu");
            System.out.println("");
            System.out.print("Your Input: ");
        }
    }

    private void showRecurringMenu(TodoList todoList) {
        String userInstruction;
        do {
            showMenu(todoList);
            userInstruction = getUserResponse();
            executeMethodRecurring(userInstruction);
        } while (!userInstruction.equals("0"));
    }

    private void showTodoListMenu(TodoListWithDueDate todoList) {
        String userInstruction;
        do {
            showMenu(todoList);
            userInstruction = getUserResponse();
            executeMethodTodo(userInstruction);
        } while (!userInstruction.equals("0"));
    }

    // helper
    private void executeMethodRecurring(String userInstruction) {
        switch (userInstruction) {
            case "1":
                newRecurringTodoUI();
                break;
            case "2":
                showRecurringTodoList();
                break;
            case "3":
                crossOffItemUI(user.getRecurringTodoList());
                break;
            case "0":
                break;
            default:
                System.out.println("Give me a correct input!");
        }
    }

    // helper
    private void executeMethodTodo(String userInstruction) {
        switch (userInstruction) {
            case "1":
                newTodoUI();
                break;
            case "2":
                showTodoList();
                break;
            case "3":
                crossOffItemUI(user.getTodoListWithDueDate());
                break;
            case "4":
                showExpiredItems();
                break;
            default:
        }
    }

    private void newTodoUI() {
        System.out.print("Enter name of todo to add to list: ");
        String taskName = getUserResponse();
        System.out.print("Enter the due date in format (yyyy-MM-dd) or leave it black if no due date: ");
        String dateInput = getUserResponse();
        System.out.print("Enter \"y\" if this todo is especially important or type anything else otherwise: ");
        String isImportant = getUserResponse();
        System.out.println("Enter the LOCATION or LEAVE IT BLANK if no location: ");
        String location = getUserResponse();
        ItemWithDueDate item;
        if (isImportant.equals("y")) {
            item = new ImportantItemWithDueDate(taskName, dateInput, false, user.getAvailableId());
        } else {
            item = new ItemWithDueDate(taskName, dateInput, false, user.getAvailableId());
        }
        user.getTodoListWithDueDate().addTodo(item);
        item.setLocation(location);
        trySaving(user.getTodoListWithDueDate());
    }

    private void newRecurringTodoUI() {
        System.out.print("Enter name of todo to add to list: ");
        String taskName = getUserResponse();
        System.out.println("Enter the interval of the recursion: ");
        String interval;
        interval = userSelectsInterval();

        System.out.println("Enter the LOCATION or LEAVE IT BLANK if no location: ");
        String location = getUserResponse();

        RecurringItem item = new RecurringItem(taskName, interval, false, user.getAvailableId());
        user.getRecurringTodoList().addTodo(item);
        item.setLocation(location);
        trySaving(user.getRecurringTodoList());
    }


    // helper
    private void trySaving(TodoList todoList) {
        try {
            todoList.save(todoList.getFileName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // EFFECTS: prints out the id, taskName, status and due date of all the items in the list if it's not expired.
    private void showTodoList() {
        TodoListWithDueDate todoListWithDueDate = user.getTodoListWithDueDate();
        if (todoListWithDueDate.length() == 0) {
            System.out.println("There is no todo yet.");
            return;
        }
        todoListWithDueDate.sortByDate();
        int count = 0;
        int idPossible = 1;
        while (count < todoListWithDueDate.length()) {
            ItemWithDueDate item = todoListWithDueDate.getItem(idPossible);
            if (item != null) {
                if (!item.isExpired()) {
                    System.out.println(item.getRepr());
                }
                count++;
            }
            idPossible++;
        }
    }

    // EFFECTS: prints out string representations of items by each interval
    private void showRecurringTodoList() {
        if (user.getRecurringTodoList().length() == 0) {
            System.out.println("There is no todo yet.");
            return;
        }
        System.out.println("Enter the interval of the items you want to see: ");
        String interval = userSelectsInterval();
        int count = 0;
        int idPossible = 1;
        while (count < user.getRecurringTodoList().length()) {
            RecurringItem item = user.getRecurringTodoList().getItem(idPossible);
            if (item != null) {
                if (item.getInterval().equals(interval)) {
                    System.out.println(item.getRepr());
                }
                count++;
            }
            idPossible++;
        }
    }

    // helper
    private String userSelectsInterval() {
        System.out.println("1 - daily");
        System.out.println("2 - weekly");
        System.out.println("3 - monthly");
        System.out.println("4 - yearly");
        System.out.println("Your Input: ");
        System.out.println("");
        String intervalInput = getUserResponse();
        String interval;
        interval = convertIntUserResponseToIntervalString(intervalInput);
        return interval;
    }

    // helper
    private String convertIntUserResponseToIntervalString(String intervalInput) {
        switch (intervalInput) {
            case "1":
                return "daily";
            case "2":
                return "weekly";
            case "3":
                return "monthly";
            case "4":
                return "yearly";
            default:
                System.out.println("Wrong input. Interval was set to daily");
                return "daily";
        }
    }

    // EFFECTS: show user all the items whose status is "not done yet" and prompts user for id of item to cross off.
    private void crossOffItemUI(TodoList todoList) {
        if (todoList.length() == 0) {
            System.out.println("There is no todo yet.");
            return;
        }
        // prints all the items which is "Not done yet"
        if (todoList instanceof RecurringTodoList) {
            showRecurringTodoList();
        } else {
            showTodoList();
        }
        // get id to cross off from user
        System.out.print("Which item do you want to cross off? Type the id here: ");
        int idToCrossOff;
        // set idToCrossOff
        try {
            idToCrossOff = Integer.parseInt(getUserResponse());
        } catch (Exception e) {
            idToCrossOff = -1;
            // if user input cannot be parsed to int, set it to -1 so it never matches with id of any item.
        }
        // turn isDone prop if id matches
        tryCrossOff(todoList, idToCrossOff);
        trySaving(todoList);
    }

    // helper
    private void tryCrossOff(TodoList todoList, int idToCrossOff) {
        try {
            todoList.crossOff(idToCrossOff);
        } catch (ItemIdxNotFoundException e) {
            System.out.println("No item with the id you entered was found.");
        }
    }

    private void showExpiredItems() {
        if (user.getTodoListWithDueDate().length() == 0) {
            System.out.println("There is no todo yet.");
            return;
        }
        List<ItemWithDueDate> items = user.getTodoListWithDueDate().getExpiredItems();
        if (items.size() > 0) {
            System.out.println("You have expired todos!");
            for (Item item : items) {
                System.out.println(item.getRepr());
            }
        } else {
            System.out.println("You have no expired todo!");
        }
        System.out.println("");
        System.out.println("_____________________________________");
        System.out.println("");
    }

    private void showAllItems() {
        user.showAllItems();
    }


    private void apiTest()throws MalformedURLException, IOException {
        BufferedReader br = null;

        try {
            String theUrl = "https://www.ugrad.cs.ubc.ca/~cs210/2018w1/welcomemsg.html"; //this can point to any URL
            URL url = new URL(theUrl);
            br = new BufferedReader(new InputStreamReader(url.openStream()));

            String line;

            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {

                sb.append(line);
                sb.append(System.lineSeparator());
            }

            System.out.println(sb);
        } finally {

            if (br != null) {
                br.close();
            }
        }
    }


    private void showItemsAtLocationsUI() {
        System.out.println("");
        System.out.println("Which location do you like to look at?");
        Set<String> locations = user.getLocationMap().getLocations();
        for (String location: locations) {
            if (!location.equals("")) {
                System.out.println("- " + location);
            }
        }
        System.out.println("");
        System.out.println("Enter the exact name of the location you'd like to look at. Leave it blank to see items "
                + "without a location: ");
        String location = getUserResponse();

        ArrayList<Item> items = user.getLocationMap().getItemsAtLocation(location);

        for (Item item:items) {
            System.out.println(item.getRepr());
        }
        System.out.println("");
    }

    // EFFECTS: reads a line of text from standard input and returns it
    private String getUserResponse() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}

