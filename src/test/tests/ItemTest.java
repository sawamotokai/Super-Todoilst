package tests;

import model.item.Item;
import model.locationmap.LocationMap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

abstract class ItemTest {

    protected Item item;
    protected LocationMap testMap;

    @Test
    void testSimpleCrossOff(){
        assertTrue(!item.getIsDone());
        item.crossOff();
        assertTrue(item.getIsDone());
    }

    @Test
    void testTwiceCrossOff(){
        assertTrue(!item.getIsDone());
        item.crossOff();
        assertTrue(item.getIsDone());
        item.crossOff();
        assertTrue(!item.getIsDone());
    }

    @Test
    void testGetIsDone(){
        assertEquals(false, item.getIsDone());
    }



    @Test
    abstract void testConstructorWhenDateInputIsEmpty();

    @Test
    void testSetTaskName() {
        assertEquals("homework", item.getTaskName());
        item.setTaskName("Dish");
        assertEquals("Dish", item.getTaskName());
    }


}
