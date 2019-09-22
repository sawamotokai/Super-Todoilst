package model.todolist;

import java.io.IOException;

public interface Loadable {
    void load(String fileName) throws IOException;
}
