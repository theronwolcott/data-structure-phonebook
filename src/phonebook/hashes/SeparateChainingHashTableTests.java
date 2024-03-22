package phonebook.hashes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SeparateChainingHashTableTests {
    SeparateChainingHashTable sc;
    String result;

    @Test
    public void constructorTest1() {
        sc = new SeparateChainingHashTable();
        assertEquals(7, sc.capacity());
        assertEquals(0, sc.size());
    }

    // @Test
    // public void putTest1() {
    // // check exceptions
    // sc = new SeparateChainingHashTable();
    // try {
    // sc.put(null, "test");
    // assertFalse(true); // shouldn't get here
    // } catch (Exception e) {
    // assertTrue(true); // should get here
    // }
    // try {
    // sc.put("test", null);
    // assertFalse(true); // shouldn't get here
    // } catch (IllegalArgumentException e) {
    // assertTrue(true); // should get here
    // }
    // }

    @Test
    public void putTest2() {
        // check single put
        sc = new SeparateChainingHashTable();
        result = sc.put("test", "testy"); // hash of 1
        assertEquals(1, sc.size());
    }

    @Test
    public void putTest3() {
        // check collision
        putTest2();
        result = sc.put("c", "cat"); // also hash of 1
        assertEquals(2, sc.size());
    }

    @Test
    public void putTest4() {
        // no collision
        putTest3();
        result = sc.put("d", "dog"); // hash of 2
        assertEquals(3, sc.size());
    }

    @Test
    public void containsKeyTest1() {
        // single key
        sc = new SeparateChainingHashTable();
        assertFalse(sc.containsKey("test"));
        result = sc.put("test", "testy");
        assertTrue(sc.containsKey("test"));
    }

    @Test
    public void containsKeyTest2() {
        // collision
        containsKeyTest1();
        assertFalse(sc.containsKey("c"));
        result = sc.put("c", "cat"); // also hash of 1
        assertTrue(sc.containsKey("c"));
    }

    @Test
    public void containsKeyTest3() {
        // no collision
        containsKeyTest2();
        assertFalse(sc.containsKey("d"));
        result = sc.put("d", "dog"); // hash of 2
        assertTrue(sc.containsKey("d"));
    }

    @Test
    public void containsValueTest1() {
        // single
        sc = new SeparateChainingHashTable();
        assertFalse(sc.containsValue("testy"));
        result = sc.put("test", "testy");
        assertTrue(sc.containsValue("testy"));
    }

    @Test
    public void containsValueTest2() {
        // collision
        containsValueTest1();
        assertFalse(sc.containsValue("cat"));
        result = sc.put("c", "cat");
        assertTrue(sc.containsValue("cat"));
    }

    @Test
    public void containsValueTest3() {
        // no collision
        containsValueTest1();
        assertFalse(sc.containsValue("dog"));
        result = sc.put("d", "dog"); // hash of 2
        assertTrue(sc.containsValue("dog"));
    }

    @Test
    public void getTest1() {
        // single
        sc = new SeparateChainingHashTable();
        // missing
        result = sc.get("test");
        assertEquals(null, result);
        // found
        result = sc.put("test", "testy"); // hash of 1
        result = sc.get("test");
        assertEquals("testy", result);
    }

    @Test
    public void getTest2() {
        // collision
        getTest1();
        // missing
        result = sc.get("c"); // also hash of 1
        assertEquals(null, result);
        // found
        result = sc.put("c", "cat"); // put doesn't have extra probes
        result = sc.get("c"); // get DOES have extra probes
        assertEquals("cat", result);
    }

    @Test
    public void getTest3() {
        // no collision
        getTest2();
        // missing
        result = sc.get("d"); // hash of 2
        assertEquals(null, result);
        // found
        result = sc.put("d", "dog");
        result = sc.get("d");
        assertEquals("dog", result);
    }

    @Test
    public void removeTest1() {
        // single
        sc = new SeparateChainingHashTable();
        // missing
        result = sc.remove("test");
        assertEquals(0, sc.size());
        assertEquals(null, result);
        // found
        result = sc.put("test", "testy"); // hash of 1
        assertEquals(1, sc.size());
        result = sc.remove("test");
        assertEquals("testy", result);
        assertEquals(0, sc.size());
    }

    @Test
    public void removeTest2() {
        // collision
        sc = new SeparateChainingHashTable();
        assertEquals(0, sc.size());
        result = sc.put("test", "testy"); // hash of 1
        assertEquals(1, sc.size());
        // missing
        result = sc.remove("c");
        assertEquals(1, sc.size());
        assertEquals(null, result);
        // found
        result = sc.put("c", "cat");
        assertEquals(2, sc.size());
        result = sc.remove("c");
        assertEquals("cat", result);
        assertEquals(1, sc.size());
    }

    @Test
    public void removeTest3() {
        // no collision
        sc = new SeparateChainingHashTable();
        result = sc.put("test", "testy"); // hash of 1
        result = sc.put("c", "cat"); // hash of 1
        result = sc.put("d", "dog"); // hash of 2
        assertEquals(3, sc.size());
        result = sc.remove("d");
        assertEquals("dog", result);
        assertEquals(2, sc.size());
    }

    @Test
    public void enlargeTest1() {
        sc = new SeparateChainingHashTable();
        sc.enlarge();
        assertEquals(13, sc.capacity());
        assertEquals(0, sc.size());

    }

    @Test
    public void enlargeTest2() {
        sc = new SeparateChainingHashTable();
        result = sc.put("test", "testy"); // hash of 1
        result = sc.put("c", "cat"); // hash of 1
        result = sc.put("d", "dog"); // hash of 2
        sc.enlarge();
        assertEquals(13, sc.capacity());
        assertTrue(sc.containsKey("test"));
        assertTrue(sc.containsKey("c"));
        assertTrue(sc.containsKey("d"));
        assertEquals(3, sc.size());

    }

    @Test
    public void shrinkTest1() {
        enlargeTest1();
        sc.shrink();
        assertEquals(7, sc.capacity());
        assertEquals(0, sc.size());
    }

    @Test
    public void shrinkTest2() {
        enlargeTest2();
        sc.shrink();
        assertEquals(7, sc.capacity());
        assertTrue(sc.containsKey("test"));
        assertTrue(sc.containsKey("c"));
        assertTrue(sc.containsKey("d"));
        assertEquals(3, sc.size());
    }
}
