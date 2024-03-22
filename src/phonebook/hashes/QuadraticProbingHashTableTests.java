package phonebook.hashes;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import phonebook.utils.KVPair;

public class QuadraticProbingHashTableTests {

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

    QuadraticProbingHashTable qp;
    String result;
    boolean b;

    private String put(KVPair pair) {
        return qp.put(pair.getKey(), pair.getValue());
    }

    private String remove(KVPair pair) {
        return qp.remove(pair.getKey());
    }

    @Test
    public void constructorTest1() {
        qp = new QuadraticProbingHashTable(true);
        assertEquals(0, qp.size());
        assertEquals(7, qp.capacity());
    }

    @Test
    public void soft_putTest1() {
        qp = new QuadraticProbingHashTable(true);
        result = put(TEST);
        assertEquals(result, TEST.getValue());
        assertEquals(TEST, qp.get(1));
        assertEquals(1, qp.size());

        result = put(CC);
        assertEquals(result, CC.getValue());
        assertEquals(TEST, qp.get(1));
        assertEquals(CC, qp.get(3));
        assertEquals(2, qp.size());

        result = put(EE);
        assertEquals(result, EE.getValue());
        assertEquals(TEST, qp.get(1));
        assertEquals(CC, qp.get(3));
        assertEquals(EE, qp.get(5));
        assertEquals(3, qp.size());

    }

    @Test
    public void soft_removeTest1() {
        soft_putTest1();
        // missing
        result = remove(AA);
        assertEquals(null, result);
        // hit
        result = remove(TEST);
        assertEquals(result, TEST.getValue());
        assertEquals(OpenAddressingHashTable.TOMBSTONE, qp.get(1));
        assertEquals(CC, qp.get(3));
        assertEquals(EE, qp.get(5));
        assertEquals(2, qp.size());

    }

    @Test
    public void soft_putTest2() {
        soft_removeTest1();
        result = put(JJ);
        assertEquals(result, JJ.getValue());
        assertEquals(JJ, qp.get(0));
        assertEquals(OpenAddressingHashTable.TOMBSTONE, qp.get(1));
        assertEquals(CC, qp.get(3));
        assertEquals(EE, qp.get(5));
        assertEquals(3, qp.size());

    }

    @Test
    public void soft_getTest1() {
        soft_putTest2();
        // miss
        result = qp.get(AA.getKey());
        assertEquals(null, result);
        // hits
        result = qp.get(JJ.getKey());
        assertEquals(result, JJ.getValue());
        result = qp.get(CC.getKey());
        assertEquals(result, CC.getValue());
        result = qp.get(EE.getKey());
        assertEquals(result, EE.getValue());
    }

    @Test
    public void hard_putTest1() {
        qp = new QuadraticProbingHashTable(false);
        result = put(TEST);
        assertEquals(result, TEST.getValue());
        assertEquals(TEST, qp.get(1));
        assertEquals(1, qp.size());

        result = put(CC);
        assertEquals(result, CC.getValue());
        assertEquals(TEST, qp.get(1));
        assertEquals(CC, qp.get(3));
        assertEquals(2, qp.size());

        result = put(EE);
        assertEquals(result, EE.getValue());
        assertEquals(TEST, qp.get(1));
        assertEquals(CC, qp.get(3));
        assertEquals(EE, qp.get(5));
        assertEquals(3, qp.size());

        result = put(DD);
        assertEquals(result, DD.getValue());
        assertEquals(TEST, qp.get(1));
        assertEquals(DD, qp.get(2));
        assertEquals(CC, qp.get(3));
        assertEquals(EE, qp.get(5));
        assertEquals(4, qp.size());
    }

    @Test
    public void hard_removeTest1() {
        hard_putTest1();
        // missing
        result = remove(AA);
        assertEquals(null, result);
        // hit
        result = remove(TEST);
        assertEquals(result, TEST.getValue());
        assertEquals(CC, qp.get(1));
        assertEquals(DD, qp.get(2));
        assertEquals(EE, qp.get(3));
        assertEquals(3, qp.size());
    }

    @Test
    public void hard_putTest2() {
        hard_removeTest1();
        result = put(JJ);
        assertEquals(result, JJ.getValue());
        assertEquals(JJ, qp.get(0));
        assertEquals(CC, qp.get(1));
        assertEquals(DD, qp.get(2));
        assertEquals(EE, qp.get(3));
        assertEquals(4, qp.size());

    }

    @Test
    public void hard_getTest1() {
        hard_putTest2();
        // miss
        result = qp.get(AA.getKey());
        assertEquals(null, result);
        // hits
        result = qp.get(JJ.getKey());
        assertEquals(result, JJ.getValue());
        result = qp.get(CC.getKey());
        assertEquals(result, CC.getValue());
        result = qp.get(DD.getKey());
        assertEquals(result, DD.getValue());
        result = qp.get(EE.getKey());
        assertEquals(result, EE.getValue());
    }
}
