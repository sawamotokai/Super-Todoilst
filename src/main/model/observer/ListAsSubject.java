package model.observer;

import model.item.Item;

import java.util.ArrayList;
import java.util.List;

public class ListAsSubject {
    private List<ListObserver> observers = new ArrayList<>();

    public void addObserver(ListObserver o) {
        observers.add(o);
    }

    public void removeObserver(ListObserver o) {
        observers.remove(o);
    }

    public List<ListObserver> getObservers() {
        return observers;
    }

    public String notifyObserverItemAdded(Item item) {
        String msgs = "";
        for (ListObserver o : observers) {
            msgs += o.updateItemAdded(item);
            msgs += "\t";
        }
        return msgs;
    }

    public String notifyObserverItemEdited(Item item) {
        String msgs = "";
        for (ListObserver o: observers) {
            msgs += o.updateItemEdited(item);
            msgs += "\t";
        }
        return msgs;
    }

    public String notifyObserverItemRemoved(Item item) {
        String msgs = "";
        for (ListObserver o: observers) {
            msgs += o.updateItemRemoved(item);
            msgs += "\t";
        }
        return msgs;
    }

}
