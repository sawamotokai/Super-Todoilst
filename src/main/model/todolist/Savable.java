package model.todolist;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface Savable {
    void save(String fileName) throws FileNotFoundException, UnsupportedEncodingException, IOException;
}
