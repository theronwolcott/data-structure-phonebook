package phonebook.hashes;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import phonebook.utils.KVPair;

public class OrderedLinearProbingHashTableTests {

    KVPair TEST = new KVPair("test", "testy"); // 1
    KVPair AA = new KVPair("a", "aaaa"); // 6
    KVPair BB = new KVPair("b", "bbbb"); // 0
    KVPair CC = new KVPair("c", "cccc"); // 1
    KVPair DD = new KVPair("d", "dddd"); // 2
    KVPair EE = new KVPair("e", "eeee"); // 3
    KVPair FF = new KVPair("f", "ffff"); // 4
    KVPair GG = new KVPair("g", "gggg"); // 5
    KVPair HH = new KVPair("h", "hhhh"); // 6
    KVPair II = new KVPair("i", "iiii"); // 0
    KVPair JJ = new KVPair("j", "jjjj"); // 1

    OrderedLinearProbingHashTable ol;
    String result;
    boolean b;

    private String put(KVPair pair) {
        return ol.put(pair.getKey(), pair.getValue());
    }

    @Test
    public void constructorTest1() {
        ol = new OrderedLinearProbingHashTable(false);
        assertEquals(0, ol.size());
        assertEquals(7, ol.capacity());
    }

    @Test
    public void hard_putTest1() {
        ol = new OrderedLinearProbingHashTable(false);
        result = put(TEST);
        assertEquals(result, TEST.getValue());
        assertEquals(TEST, ol.get(1));
        assertEquals(1, ol.size());
    }

    @Test
    public void hard_putTest2() {
        hard_putTest1();
        result = put(CC);
        assertEquals(result, CC.getValue());
        assertEquals(CC, ol.get(1));
        assertEquals(TEST, ol.get(2));
        assertEquals(2, ol.size());
    }

    @Test
    public void hard_putTest3() {
        hard_putTest2();
        result = put(EE);
        assertEquals(result, EE.getValue());
        assertEquals(CC, ol.get(1));
        assertEquals(TEST, ol.get(2));
        assertEquals(EE, ol.get(3));
        assertEquals(3, ol.size());
    }

    @Test
    public void putTest4() {
        hard_putTest3();
        assertEquals(CC, ol.get(1));
        result = put(DD);
        assertEquals(result, DD.getValue());
        assertEquals(CC, ol.get(1));
        assertEquals(DD, ol.get(2));
        assertEquals(EE, ol.get(3));
        assertEquals(TEST, ol.get(4));
        assertEquals(4, ol.size());
    }

    @Test
    public void soft_putTest1() {
        ol = new OrderedLinearProbingHashTable(true);
        result = put(TEST);
        assertEquals(result, TEST.getValue());
        assertEquals(TEST, ol.get(1));
        assertEquals(1, ol.size());
    }

    public void soft_putTest2() {
        soft_putTest1();
        result = put(CC);
        assertEquals(result, CC.getValue());
        assertEquals(CC, ol.get(1));
        assertEquals(TEST, ol.get(2));
        assertEquals(2, ol.size());
    }

    @Test
    public void soft_putTest3() {
        soft_putTest2();
        result = put(EE);
        assertEquals(result, EE.getValue());
        assertEquals(CC, ol.get(1));
        assertEquals(TEST, ol.get(2));
        assertEquals(EE, ol.get(3));
        assertEquals(3, ol.size());
    }

    @Test
    public void hard_removeTest1() {
        hard_putTest1();
        // missing
        result = ol.remove(AA.getKey());
        assertEquals(result, null);
        assertEquals(1, ol.size());

        // hit
        result = ol.remove(TEST.getKey());
        assertEquals(result, TEST.getValue());
        assertEquals(0, ol.size());
        assertEquals(null, ol.get(1));

    }

    @Test
    public void hard_removeTest2() {
        hard_putTest2();
        result = ol.remove(TEST.getKey());
        assertEquals(result, TEST.getValue());
        assertEquals(1, ol.size());
        assertEquals(CC, ol.get(1));
        assertEquals(null, ol.get(2));
    }

    @Test
    public void hard_removeTest3() {
        putTest4();
        result = ol.remove(EE.getKey());
        assertEquals(result, EE.getValue());
        assertEquals(3, ol.size());
        assertEquals(CC, ol.get(1));
        assertEquals(DD, ol.get(2));
        assertEquals(TEST, ol.get(3));
        assertEquals(null, ol.get(4));
        assertEquals(3, ol.size());

    }

    @Test
    public void hard_putTest4() {
        hard_removeTest3();
        result = put(JJ);
        assertEquals(result, JJ.getValue());
        assertEquals(CC, ol.get(1));
        assertEquals(DD, ol.get(2));
        assertEquals(JJ, ol.get(3));
        assertEquals(TEST, ol.get(4));
        assertEquals(4, ol.size());

    }

    @Test
    public void soft_removeTest1() {
        soft_putTest1();
        // missing
        result = ol.remove(AA.getKey());
        assertEquals(result, null);
        assertEquals(1, ol.size());

        // hit
        result = ol.remove(TEST.getKey());
        assertEquals(result, TEST.getValue());
        assertEquals(0, ol.size());
        assertEquals(OpenAddressingHashTable.TOMBSTONE, ol.get(1));
    }

    @Test
    public void soft_removeTest2() {
        soft_putTest3();
        result = ol.remove(TEST.getKey());
        assertEquals(result, TEST.getValue());
        assertEquals(2, ol.size());
        assertEquals(CC, ol.get(1));
        assertEquals(OpenAddressingHashTable.TOMBSTONE, ol.get(2));
        assertEquals(EE, ol.get(3));
    }

    @Test
    public void soft_putTest4() {
        soft_removeTest2();
        result = put(JJ);
        assertEquals(result, JJ.getValue());
        assertEquals(CC, ol.get(1));
        assertEquals(OpenAddressingHashTable.TOMBSTONE, ol.get(2));
        assertEquals(EE, ol.get(3));
        assertEquals(JJ, ol.get(4));
        assertEquals(3, ol.size());
    }

    @Test
    public void soft_putTest5() {
        soft_removeTest2();
        result = put(DD);
        assertEquals(7, ol.capacity());
        assertEquals(3, ol.size());

        assertEquals(result, DD.getValue());
        assertEquals(CC, ol.get(1));
        assertEquals(OpenAddressingHashTable.TOMBSTONE, ol.get(2));
        assertEquals(DD, ol.get(3));
        assertEquals(EE, ol.get(4));
    }

    @Test
    public void getTest1() {
        soft_putTest1();
        // missing
        result = ol.get(AA.getKey());
        assertEquals(result, null);
        // hit
        result = ol.get(TEST.getKey());
        assertEquals(result, TEST.getValue());
    }

    @Test
    public void getTest2() {
        soft_putTest5();
        result = ol.get(DD.getKey());
        assertEquals(result, DD.getValue());
    }

    @Test
    public void getTest3() {
        hard_putTest4();
        result = ol.get(JJ.getKey());
        assertEquals(result, JJ.getValue());
    }

    @Test
    public void containsKeyTest1() {
        soft_putTest1();
        // missing
        b = ol.containsKey(AA.getKey());
        assertEquals(b, false);
        // hit
        b = ol.containsKey(TEST.getKey());
        assertEquals(b, true);
    }

    @Test
    public void containsKeyTest2() {
        soft_putTest5();
        b = ol.containsKey(DD.getKey());
        assertEquals(b, true);
    }

    @Test
    public void containsKeyTest3() {
        hard_putTest4();
        b = ol.containsKey(JJ.getKey());
        assertEquals(b, true);
    }

    @Test
    public void containsValueTest1() {
        soft_putTest5();
        // missing
        b = ol.containsValue(AA.getValue());
        assertEquals(b, false);

        b = ol.containsValue(CC.getValue());
        assertEquals(b, true);
        b = ol.containsValue(DD.getValue());
        assertEquals(b, true);
        b = ol.containsValue(EE.getValue());
        assertEquals(b, true);

    }

    @Test
    public void containsValueTest2() {
        hard_putTest4();
        // missing
        b = ol.containsValue(AA.getValue());
        assertEquals(b, false);

        b = ol.containsValue(CC.getValue());
        assertEquals(b, true);
        b = ol.containsValue(DD.getValue());
        assertEquals(b, true);
        b = ol.containsValue(JJ.getValue());
        assertEquals(b, true);
        b = ol.containsValue(TEST.getValue());
        assertEquals(b, true);

    }
}
