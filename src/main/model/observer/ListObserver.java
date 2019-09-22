package model.observer;

import model.item.Item;

public interface ListObserver {

    String updateItemAdded(Item item);

    String updateItemRemoved(Item item);

    String updateItemEdited(Item item);
}
