package gui;

import model.exceptions.ItemIdxNotFoundException;
import model.todolist.TodoList;

import java.io.IOException;

public class Util {
    public static void trySaving(TodoList todoList) {
        try {
            todoList.save(todoList.getFileName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void tryLoad(TodoList todoList) {
        try {
            todoList.load(todoList.getFileName());
            System.out.println("Hello " + todoList.getUser().getUsername() + ". Your data was loaded!");
        } catch (Exception e) {
            System.out.println("Hello " + todoList.getUser().getUsername() + ". Your Todo List list was created");
        }
        System.out.println("");
    }

    // helper
    public static void tryCrossOff(TodoList todoList, int idToCrossOff) {
        try {
            todoList.crossOff(idToCrossOff);
        } catch (ItemIdxNotFoundException e) {
            System.out.println("No item with the id you entered was found.");
        }
    }


    // Helper method
    // EFFECTS: turn and return the line into an array of strings, splitting it by tab.
    public static String[] split(String line) {
        return line.split("\t");
    }
}
