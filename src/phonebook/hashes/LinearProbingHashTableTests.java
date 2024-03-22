package phonebook.hashes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import phonebook.utils.Probes;

public class LinearProbingHashTableTests {
    LinearProbingHashTable lp;
    String result;

    @Test
    public void constructorTest1() {
        lp = new LinearProbingHashTable(false); // hard deletes
        assertEquals(7, lp.capacity());
        assertEquals(0, lp.size());
    }

    // @Test
    // public void putTest1() {
    // // check exceptions
    // lp = new LinearProbingHashTable(false); // hard deletes
    // try {
    // lp.put(null, "test");
    // assertFalse(true); // shouldn't get here
    // } catch (Exception e) {
    // assertTrue(true); // should get here
    // }
    // try {
    // lp.put("test", null);
    // assertFalse(true); // shouldn't get here
    // } catch (IllegalArgumentException e) {
    // assertTrue(true); // should get here
    // }
    // }

    @Test
    public void putTest2() {
        // check single put
        lp = new LinearProbingHashTable(false); // hard deletes
        result = lp.put("test", "testy"); // hash of 1
        assertEquals(1, lp.size());
    }

    @Test
    public void putTest3() {
        // collision with test
        putTest2();
        result = lp.put("c", "cat"); // also hash of 1
        assertEquals(2, lp.size());
    }

    @Test
    public void putTest4() {
        // collision with c
        putTest3();
        result = lp.put("d", "dog"); // hash of 2
        assertEquals(3, lp.size());
    }

    @Test
    public void putTest5() {
        // no collision
        putTest4();
        result = lp.put("f", "fox"); // hash of 4
        assertEquals(4, lp.size());
    }

    @Test
    public void containsKeyTest1() {
        // single key
        lp = new LinearProbingHashTable(false); // hard deletes
        assertFalse(lp.containsKey("test"));
        result = lp.put("test", "testy");
        assertTrue(lp.containsKey("test"));
    }

    @Test
    public void containsKeyTest2() {
        // collision with test
        containsKeyTest1();
        assertFalse(lp.containsKey("c"));
        result = lp.put("c", "cat"); // also hash of 1
        assertTrue(lp.containsKey("c"));
    }

    @Test
    public void containsKeyTest3() {
        // no collision
        containsKeyTest2();
        assertFalse(lp.containsKey("f"));
        result = lp.put("f", "fox"); // hash of 4
        assertTrue(lp.containsKey("f"));
    }

    @Test
    public void containsValueTest1() {
        // single
        lp = new LinearProbingHashTable(false); // hard deletes
        assertFalse(lp.containsValue("testy"));
        result = lp.put("test", "testy");
        assertTrue(lp.containsValue("testy"));
    }

    @Test
    public void containsValueTest2() {
        // collision
        containsValueTest1();
        assertFalse(lp.containsValue("cat"));
        result = lp.put("c", "cat");
        assertTrue(lp.containsValue("cat"));
    }

    @Test
    public void containsValueTest3() {
        // collision
        containsValueTest1();
        assertFalse(lp.containsValue("fox"));
        result = lp.put("f", "fox"); // hash of 4
        assertTrue(lp.containsValue("fox"));
    }

    @Test
    public void getTest1() {
        // single
        lp = new LinearProbingHashTable(false); // hard deletes
        // missing
        result = lp.get("test");
        assertEquals(null, result);
        // found
        result = lp.put("test", "testy"); // hash of 1
        result = lp.get("test");
        assertEquals("testy", result);
    }

    @Test
    public void getTest2() {
        // collision
        getTest1();
        // missing
        result = lp.get("c"); // also hash of 1
        assertEquals(null, result);
        // found
        result = lp.put("c", "cat");
        result = lp.get("c");
        assertEquals("cat", result);
    }

    @Test
    public void getTest3() {
        // no collision
        getTest2();
        // missing
        result = lp.get("f"); // hash of 2
        assertEquals(null, result);
        // found
        result = lp.put("f", "fox");
        result = lp.get("f");
        assertEquals("fox", result);
    }

    @Test
    public void removeTest1() {
        // single
        lp = new LinearProbingHashTable(false); // hard deletes
        // missing
        result = lp.remove("test");
        assertEquals(0, lp.size());
        assertEquals(null, result);
        // found
        result = lp.put("test", "testy"); // hash of 1
        assertEquals(1, lp.size());
        result = lp.remove("test");
        assertEquals("testy", result);
        assertEquals(0, lp.size());
    }

    @Test
    public void removeTest2() {
        // collision
        lp = new LinearProbingHashTable(false); // hard deletes
        result = lp.put("test", "testy"); // hash of 1
        assertEquals(1, lp.size());
        // missing
        result = lp.remove("c");
        assertEquals(1, lp.size());
        assertEquals(null, result);
        // found
        result = lp.put("c", "cat");
        assertEquals(2, lp.size());
        result = lp.remove("c");
        assertEquals("cat", result);
        assertEquals(1, lp.size());
    }

    @Test
    public void removeTest3() {
        // no collision
        lp = new LinearProbingHashTable(false); // hard deletes
        result = lp.put("test", "testy"); // hash of 1
        result = lp.put("c", "cat"); // hash of 1
        result = lp.put("f", "fox"); // hash of 4
        assertEquals(3, lp.size());
        result = lp.remove("f");
        assertEquals("fox", result);
        assertEquals(2, lp.size());
    }

    // soft delete remove tests
    @Test
    public void soft_removeTest1() {
        // single
        lp = new LinearProbingHashTable(true); // soft deletes
        // missing
        result = lp.remove("test");
        assertEquals(0, lp.size());
        assertEquals(null, result);
        // found
        result = lp.put("test", "testy"); // hash of 1
        assertEquals(1, lp.size());
        result = lp.remove("test");
        assertEquals("testy", result);
        assertEquals(0, lp.size());
    }

    @Test
    public void soft_removeTest2() {
        // collision
        lp = new LinearProbingHashTable(true); // soft deletes
        result = lp.put("test", "testy"); // hash of 1
        // missing
        result = lp.remove("c");
        assertEquals(1, lp.size());
        assertEquals(null, result);
        // found
        result = lp.put("c", "cat");
        assertEquals(2, lp.size());
        result = lp.remove("c");
        assertEquals("cat", result);
        assertEquals(1, lp.size());
    }

    @Test
    public void soft_removeTest3() {
        // no collision
        lp = new LinearProbingHashTable(true); // soft deletes
        result = lp.put("test", "testy"); // hash of 1
        result = lp.put("c", "cat"); // hash of 1
        result = lp.remove("c");
        result = lp.put("j", "jaguar"); // hash of 1
        assertEquals("jaguar", result);
        assertEquals(2, lp.size());
    }

    @Test
    public void enlargeTest1() {
        lp = new LinearProbingHashTable(false); // hard deletes
        result = lp.put("test", "testy"); // hash of 1
        result = lp.put("c", "cat"); // hash of 1
        result = lp.put("e", "elephant"); // hash of 3
        result = lp.put("g", "gorilla"); // hash of 5
        // 5th put causes enlarge
        result = lp.put("h", "hippo"); // hash of 6
        assertEquals(13, lp.capacity());
        assertEquals(5, lp.size());
        assertTrue(lp.containsKey("test"));
        assertTrue(lp.containsKey("c"));
        assertTrue(lp.containsKey("e"));
        assertTrue(lp.containsKey("g"));
        assertTrue(lp.containsKey("h"));
        assertEquals("hippo", result);
    }
}
