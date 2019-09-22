package model.locationmap;

import model.item.Item;
import model.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class LocationMap {

    private HashMap<String, ArrayList<Item>> map = new HashMap<>();
    private User user;

    public LocationMap(User user) {
        this.user = user;
    }

    public Set<String> getLocations() {
        return map.keySet();
    }


    public void removeItem(Item item) {
        ArrayList<Item> list = map.get(item.getLocation());
        if (list == null) {
            return;
        }
        list.remove(item);
        if (list.isEmpty()) {
            map.remove(item.getLocation());
        }
    }


    public void addItem(String locationName, Item item) {
        if (locationExists(locationName)) {
            ArrayList<Item> list = map.get(locationName);
            if (!list.contains(item)) {
                list.add(item);
            }
        } else {
            ArrayList<Item> list = new ArrayList<>();
            list.add(item);
            map.put(locationName,list);
        }
        item.setLocation(locationName);
    }

    public boolean locationExists(String locationName) {
        return map.containsKey(locationName);
    }

    public ArrayList<Item> getItemsAtLocation(String locationName) {
        ArrayList<Item> ret = new ArrayList<>();
        if (map.get(locationName) != null) {
            ret = map.get(locationName);
        }
        return ret;
    }
}
